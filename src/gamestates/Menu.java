package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.MenuButton;
import utilz.LoadSave;

public class Menu extends State implements Statemethods {

	private MenuButton[] buttons = new MenuButton[3]; // 버튼의 개수
	private BufferedImage backgroundImg, backgroundImgMaple; // 플레이 버튼 배경, 게임 첫 배경화면
	private int menuX, menuY, menuWidth, menuHeight; // 메뉴 레이아웃
	
	public Menu(Game game) {
		super(game);
		loadButtons();
		loadBackground();
		backgroundImgMaple = LoadSave.GetSpritePng(LoadSave.MENU_BACKGROUND_IMG);
	}
	
	// 메뉴 위치 지정
	private void loadBackground() {
		backgroundImg = LoadSave.GetSpritePng(LoadSave.MENU_BACKGROUND); // 배경 이미지 로드
		menuWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
		menuHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
		menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
		menuY = (int) (45 * Game.SCALE);

	}
	
	// 버튼 위치 지정(Play, Option, Quit)
	private void loadButtons() {
		buttons[0] = new MenuButton(900, (int) (150 * Game.SCALE), 0, Gamestate.PLAYING); // Play
		buttons[1] = new MenuButton(900, (int) (220 * Game.SCALE), 1, Gamestate.OPTIONS); // Options
		buttons[2] = new MenuButton(900, (int) (290 * Game.SCALE), 2, Gamestate.QUIT); // Quit
	}

	@Override
	public void update() {
		for (MenuButton mb : buttons)
			mb.update();
	}

	@Override
	// 메뉴 디자인
	public void draw(Graphics g) {

		g.drawImage(backgroundImgMaple, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);


		for (MenuButton mb : buttons)
			mb.draw(g);
	}

	

	public void mousePressed(MouseEvent e) {
		for (MenuButton mb : buttons) {
			if (isIn(e, mb)) {
				mb.setMousePressed(true);
			}
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for (MenuButton mb : buttons) {
			if (isIn(e, mb)) {
				if (mb.isMousePressed())
					mb.applyGamestate();
				if (mb.getState() == Gamestate.PLAYING)
					game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
				break;
			}
		}

		resetButtons();

	}

	private void resetButtons() {
		for (MenuButton mb : buttons)
			mb.resetBools();

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for (MenuButton mb : buttons)
			mb.setMouseOver(false);

		for (MenuButton mb : buttons)
			if (isIn(e, mb)) {
				mb.setMouseOver(true);
				break;
			}

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			Gamestate.state = Gamestate.PLAYING;

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
