package entities;

import static utilz.Constants.PlayerConstants.*;
import static utilz.AddMethod.*;
import static utilz.Constants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

public class Player extends Entity {
	private BufferedImage[][] animations;
	private boolean moving = false, attacking = false;
	private boolean left, right, jump;
	private int[][] lvlData;
	private float xDrawOffset = 21 * Game.SCALE;
	private float yDrawOffset = 4 * Game.SCALE;

	// 뛰기랑 중력가동
	private float jumpSpeed = -2.32f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;

//	 StatusBarUI
	private BufferedImage statusBarImg;

	private int statusBarWidth = (int) (193 * Game.SCALE);
	private int statusBarHeight = (int) (59 * Game.SCALE);
	private int statusBarX = (int) (10 * Game.SCALE);
	private int statusBarY = (int) (10 * Game.SCALE);

	// 체력 크기 변화
	private int coinBarXstart = (int) (34 * Game.SCALE);
	private int coinBarYstart = (int) (14 * Game.SCALE);
	private int coinBarWidth = (int) (150 * Game.SCALE);
	private int coinBarHeight = (int) (4 * Game.SCALE);
	private int coinWidth = coinBarWidth;

	// 캐릭터 좌우 모션 변화 변수
	private int flipX = 0;
	private int flipW = 1;

	private void initAttackBox() { //어택박스 초기화
		attackBox = new Rectangle2D.Float(x, y, (int) (20 * Game.SCALE), (int) (20 * Game.SCALE));
	}
	
	private boolean attackChecked;
	private Playing playing;

	
	public Player(float x, float y, int width, int height, Playing playing) {
		super(x, y, width, height);
		this.playing = playing;
		this.state = IDLE;
		this.maxHealth = 100;
		this.currentHealth = maxHealth;
		this.walkSpeed = Game.SCALE * 1.0f;
		loadAnimations();
		initHitbox(20, 27);
		initAttackBox();
	}

	public void setSpawn(Point spawn) {
		this.x = spawn.x;
		this.y = spawn.y;
		hitbox.x = x;
		hitbox.y = y;
	}


	public void update() {
		updateCoinBar();

		if (currentHealth <= 0) {
			if (state != DEAD) {
				state = DEAD;
				aniTick = 0;
				aniIndex = 0; // 이미지 열 주소
				playing.setPlayerDying(true);
			} else if (aniIndex == GetSpriteAmount(DEAD) - 1 && aniTick >= ANI_SPEED - 1) {
				playing.setGameOver(true);
				playing.getGame().getAudioPlayer().stopSong();
				playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
			} else
				updateAnimationTick();

			return;
		}

		updateAttackBox();

		updatePos();
		if (moving) {
			checkCointouch();
			checkTraptouch();
		}
		if (attacking)
			checkAttack();

		updateAnimationTick();
		setAnimation();
	}
	
	private void checkAttack() {
		if (attackChecked || aniIndex != 1)
			return;
		attackChecked = true;
		playing.checkEnemyHit(attackBox);
		playing.getGame().getAudioPlayer().playAttackSound();
	}

	private void updateAttackBox() {
		if (right)
			attackBox.x = hitbox.x + hitbox.width + (int) (Game.SCALE * 10);
		else if (left)
			attackBox.x = hitbox.x - hitbox.width - (int) (Game.SCALE * 10);

		attackBox.y = hitbox.y + (Game.SCALE * 10);
	}
	
	private void checkTraptouch() {
		playing.checkTraptouch(this);

	}
	
	private void checkCointouch() {
		playing.checkCointouch(hitbox);
	}


	

	private void updateCoinBar() {
		coinWidth = (int) ((currentHealth / (float) maxHealth) * coinBarWidth);
	}

	// 체력 바 표시 메서드
		private void drawUI(Graphics g) {
			g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
			g.setColor(Color.yellow);
			g.fillRect(coinBarXstart + statusBarX, coinBarYstart + statusBarY, coinWidth, coinBarHeight);
		}
	
	// 캐릭터 크기 조정
	public void render(Graphics g, int lvlOffset) {
		g.drawImage(animations[state][aniIndex], (int) (hitbox.x - xDrawOffset) - lvlOffset + flipX,
				(int) (hitbox.y - yDrawOffset), width * flipW, height, null);
		drawUI(g);
	}

	

	// 캐릭터 애니메이션(동작) 메서드
	private void updateAnimationTick() {
		aniTick++;
		if (aniTick >= ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(state)) {
				aniIndex = 0;
				attacking = false;
				attackChecked = false;
			}

		}

	}

	private void setAnimation() {
		int startAni = state;

		if (moving)
			state = RUNNING;
		else
			state = IDLE;

		if (inAir) {
			if (airSpeed < 0)
				state = JUMP;
			else
				state = FALLING;
		}

		if (attacking) {
			state = ATTACK;
			if (startAni != ATTACK) {
				aniIndex = 1;
				aniTick = 0;
				return;
			}
		}
		if (startAni != state)
			resetAniTick();
	}

	// 움직임이 깔끔해지는 메서드
	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}

	// 캐릭터 위치 이동 메서드
	private void updatePos() {
		moving = false;

		if (jump)
			jump();

		// 좌우키 중복 시, 동작 애니메이션 오류 방지. 아래 코드가 없다면 불안정한 애니메이션 효과
		if (!inAir)
			if ((!left && !right) || (right && left))
				return;

		float xSpeed = 0;

		if (left) {
			xSpeed -= walkSpeed;
			flipX = width;
			flipW = -1;
		}
		if (right) {
			xSpeed += walkSpeed;
			flipX = 0;
			flipW = 1;
		}

		// 바닥에 있지 않은 경우
		if (!inAir)
			if (!IsEntityOnFloor(hitbox, lvlData))
				inAir = true;
		// 점프 중(공중에 있는지 여부)
		if (inAir) {
			if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
				hitbox.y += airSpeed;
				airSpeed += GRAVITY;
				updateXpos(xSpeed);
			} else {
				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
				if (airSpeed > 0)
					resetInAir();
				else
					airSpeed = fallSpeedAfterCollision;
				updateXpos(xSpeed);
			}

		} else
			updateXpos(xSpeed);
		moving = true;
	}

	private void jump() {
		if (inAir) // 공중에 있다면
			return;
		playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
		inAir = true;
		airSpeed = jumpSpeed;
	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;
	}

	private void updateXpos(float xSpeed) {
		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
			hitbox.x += xSpeed;
		else {
			hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
		}
	}

	// 체력 변화
	public void changeHealth(int value) {
		currentHealth += value;

		if (currentHealth <= 0)
			currentHealth = 0;
		else if (currentHealth >= maxHealth)
			currentHealth = maxHealth;
	}

	// 플레이어 즉사 메서드
	public void kill() {
		currentHealth = 0;
	}

	// 캐릭터 파일 로드 (프로젝트 버전 변경 필요!!)
	private void loadAnimations() {
		BufferedImage img = LoadSave.GetSpritePng(LoadSave.PLAYER); // JACK(LoadSave에 원본)
		// (매우 중요!)이미지 파일 안 캐릭터 동작 선택!!, 행렬 개념.
		animations = new BufferedImage[7][8];
		for (int j = 0; j < animations.length; j++)
			for (int i = 0; i < animations[j].length; i++)
				animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
	}

	public void loadLvlData(int[][] lvlData) {
		this.lvlData = lvlData;
		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;

	}

	public void resetDirBooleans() {
		left = false;
		right = false;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}
	
	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isRight() {
		return right;
	}

	

	public void setJump(boolean jump) {
		this.jump = jump;
	}

	public void resetAll() {
		resetDirBooleans();
		inAir = false;
		attacking = false;
		moving = false;
		state = IDLE;
		currentHealth = maxHealth;

		hitbox.x = x;
		hitbox.y = y;

		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
	}

}