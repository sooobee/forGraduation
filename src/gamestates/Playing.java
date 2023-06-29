package gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import objects.ObjectManager;
import ui.GameOverPage;
import ui.LevelCompletedPage;
import ui.GameCompletePage;
import ui.PausePage;
import utilz.LoadSave;

public class Playing extends State implements Statemethods {
	private Player player;
	private LevelManager levelManager;
	private EnemyManager enemyManager;
	private ObjectManager objectManager;
	private PausePage pausePage;
	private GameOverPage gameOverPage;
	private LevelCompletedPage levelCompletedPage;
	private GameCompletePage gameCompletedPage;
	private boolean paused = false;

	private int xLvlOffset;
	private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
	private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
	private int maxLvlOffsetX;
	public static int numCoins = 0;

	private BufferedImage backgroundImg;
	private Random rnd = new Random();

	private boolean gameOver;
	private boolean lvlCompleted;
	private boolean gameCompleted;
	private boolean playerDying;

	// 스테이지 시간 제한
	private int elapsedTime = 0; // 증가 시간
	private int timeLimit = 30; // 클리어 시간
	private long startTime = System.currentTimeMillis();

	public Playing(Game game) {
		super(game);
		initClasses();

		backgroundImg = LoadSave.GetSpritePng(LoadSave.PLAYING_BG_IMG);

		calcLvlOffset();
		loadStartLevel();

		gameCompletedPage = new GameCompletePage(this);
	}

	// 다음 레벨 세팅
	public void loadNextLevel() {
		resetAll();
		increaseTimeLimit();
		levelManager.loadNextLevel();
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
	}

	private void loadStartLevel() {
		enemyManager.loadEnemies(levelManager.getCurrentLevel());
		objectManager.loadObjects(levelManager.getCurrentLevel());
	}

	private void calcLvlOffset() {
		maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
	}

	private void initClasses() {
		levelManager = new LevelManager(game);
		enemyManager = new EnemyManager(this);
		objectManager = new ObjectManager(this);

		player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE), this);
		player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());

		pausePage = new PausePage(this);
		gameOverPage = new GameOverPage(this);
		levelCompletedPage = new LevelCompletedPage(this);
	}

	@Override
	public void update() {

		// 제한시간 설정
		long currentTime = System.currentTimeMillis();
		int secondsPassed = (int) ((currentTime - startTime) / 1000);
		if (secondsPassed > elapsedTime) {
			elapsedTime = secondsPassed;
		}
		
		if (elapsedTime > timeLimit) {
			player.kill();
		}

		if (paused) {
			pausePage.update();
		} else if (lvlCompleted)
			levelCompletedPage.update();
		else if (gameCompleted)
			gameCompletedPage.update();
		else if (gameOver) {
			gameOverPage.update();
		} else if (playerDying) {
			player.update();
		} else {
			levelManager.update();
			objectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
			player.update();
			enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
			checkCloseToBorder();

		}
	}

	private void checkCloseToBorder() {
		int playerX = (int) player.getHitbox().x;
		int diff = playerX - xLvlOffset;

		if (diff > rightBorder)
			xLvlOffset += diff - rightBorder;
		else if (diff < leftBorder)
			xLvlOffset += diff - leftBorder;

		if (xLvlOffset > maxLvlOffsetX)
			xLvlOffset = maxLvlOffsetX;
		else if (xLvlOffset < 0)
			xLvlOffset = 0;
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

		// 코인 획득 개수 출력
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD, 16));
		g.drawString("Coins: " + numCoins, 10, 20);

		// 제한 시간 출력
		if (timeLimit - elapsedTime > 0) {
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.BOLD, 16));
			g.drawString("Time: " + (timeLimit - elapsedTime), 1100, 20);
		}

		levelManager.draw(g, xLvlOffset);
		player.render(g, xLvlOffset);
		enemyManager.draw(g, xLvlOffset);
		objectManager.draw(g, xLvlOffset);

		if (paused) {
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
			pausePage.draw(g);
		} else if (gameOver)
			gameOverPage.draw(g);
		else if (lvlCompleted)
			levelCompletedPage.draw(g);
		else if (gameCompleted)
			gameCompletedPage.draw(g);
	}

	// 제한시간 증가 메서드
	private void increaseTimeLimit() {
		timeLimit += 5;
	}

	public void setGameCompleted() {
		gameCompleted = true;
	}

	public void setLevelCompleted(boolean levelCompleted) {
		if (levelManager.getLevelIndex() + 1 >= levelManager.getAmountOfLevels()) {
			// No more levels
			setGameCompleted();
			levelManager.setLevelIndex(0);
			levelManager.loadNextLevel();
			resetAll();
			return;
		}
		this.lvlCompleted = levelCompleted;
		if (levelCompleted)
			game.getAudioPlayer().lvlCompleted();

	}

	public void resetGameCompleted() {
		gameCompleted = false;
	}

	public void resetTime() {
		elapsedTime = 0;
		startTime = System.currentTimeMillis();
	}

	public void resetAll() {
		gameOver = false;
		paused = false;
		lvlCompleted = false;
		playerDying = false;
		player.resetAll();
		enemyManager.resetAllEnemies();
		objectManager.resetAllObjects();
		resetTime();

	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		enemyManager.checkEnemyHit(attackBox);
	}

	public void checkCointouch(Rectangle2D.Float hitbox) {
		objectManager.checkObjectTouched(hitbox);
	}

	public void checkTraptouch(Player p) {
		objectManager.checkSpikesTouched(p);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!gameOver)
			if (e.getButton() == MouseEvent.BUTTON1)
				player.setAttacking(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!gameOver && !gameCompleted && !lvlCompleted)
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				player.setLeft(true);
				break;
			case KeyEvent.VK_D:

				player.setRight(true);
				break;
			case KeyEvent.VK_SPACE:
				player.setJump(true);
				break;
			case KeyEvent.VK_ESCAPE:
				paused = !paused;
			}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!gameOver && !gameCompleted && !lvlCompleted)
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				player.setLeft(false);
				break;
			case KeyEvent.VK_D:
				player.setRight(false);
				break;
			case KeyEvent.VK_SPACE:
				player.setJump(false);
				break;
			}
	}

	public void mouseDragged(MouseEvent e) {
		if (!gameOver && !gameCompleted && !lvlCompleted)
			if (paused)
				pausePage.mouseDragged(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (gameOver)
			gameOverPage.mousePressed(e);
		else if (paused)
			pausePage.mousePressed(e);
		else if (lvlCompleted)
			levelCompletedPage.mousePressed(e);
		else if (gameCompleted)
			gameCompletedPage.mousePressed(e);

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (gameOver)
			gameOverPage.mouseReleased(e);
		else if (paused)
			pausePage.mouseReleased(e);
		else if (lvlCompleted)
			levelCompletedPage.mouseReleased(e);
		else if (gameCompleted)
			gameCompletedPage.mouseReleased(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (gameOver)
			gameOverPage.mouseMoved(e);
		else if (paused)
			pausePage.mouseMoved(e);
		else if (lvlCompleted)
			levelCompletedPage.mouseMoved(e);
		else if (gameCompleted)
			gameCompletedPage.mouseMoved(e);
	}

	public void setMaxLvlOffset(int lvlOffset) {
		this.maxLvlOffsetX = lvlOffset;
	}

	public void unpauseGame() {
		paused = false;
	}

	public void windowFocusLost() {
		player.resetDirBooleans();
	}

	public Player getPlayer() {
		return player;
	}

	public EnemyManager getEnemyManager() {
		return enemyManager;
	}

	public ObjectManager getObjectManager() {
		return objectManager;
	}

	public LevelManager getLevelManager() {
		return levelManager;
	}

	public void setPlayerDying(boolean playerDying) {
		this.playerDying = playerDying;

	}

}