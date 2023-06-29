package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;

public abstract class Entity {
	
	protected Rectangle2D.Float hitbox; // 맵과 충돌 방지 변수
	protected float x, y;
	protected int width, height;
	protected int state;
	protected float airSpeed;
	protected int aniTick, aniIndex;
	protected Rectangle2D.Float attackBox;
	protected boolean inAir = false;
	protected int maxHealth;
	protected int currentHealth;
	protected float walkSpeed;
	
	public Entity(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	protected void drawAttackBox(Graphics g, int xLvlOffset) {
		g.setColor(Color.red); 
		g.drawRect((int) (attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
	}
	
	// Hitbox: 캐릭터와 맵의 경계선 코드
	protected void drawHitbox(Graphics g, int xLvlOffset) {
		//히트박스 디버깅 해보기??
		g.setColor(Color.PINK);
		g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
	}
	
	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}

	public int getState() {
		return state;
	}

	public int getAniIndex() {
		return aniIndex;
	}

	protected void initHitbox(int width, int height) {
		hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
	}
	
	

}
