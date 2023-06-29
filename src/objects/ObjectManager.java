 package objects;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Player;
import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;
import static utilz.Constants.ObjectConstants.*;



public class ObjectManager {

	private Playing playing;
	private BufferedImage[][] coinImgs;
	private BufferedImage trapImag;
	private ArrayList<Coin> coins;
	private ArrayList<Trap> traps;

	public ObjectManager(Playing playing) {
		this.playing = playing;
		loadImgs();
	}

	public void checkSpikesTouched(Player p) {
		for (Trap s : traps)
			if (s.getHitbox().intersects(p.getHitbox()))
				p.kill();
	}

	public void checkObjectTouched(Rectangle2D.Float hitbox) {
		for (Coin p : coins)
			if (p.isActive()) {
				if (hitbox.intersects(p.getHitbox())) {
					p.setActive(false);
					applyEffectToPlayer(p);
				}
			}
	}
	
	public void collectCoin() {
        // Increment the numCoins variable
        Playing.numCoins++;
    }

	public void applyEffectToPlayer(Coin p) {
		if (p.getObjType() == COIN_1 || p.getObjType() == COIN_2)
		{
			playing.getPlayer().changeHealth(COIN1_VALUE);
			collectCoin();
		}
			
	}

	public void loadObjects(Level newLevel) {
		traps = newLevel.getTraps();
		coins = new ArrayList<>(newLevel.getCoins());
	}

	private void loadImgs() {
		BufferedImage coinsprites = LoadSave.GetSpritePng(LoadSave.COIN_IMG);
		coinImgs = new BufferedImage[2][7];

		for (int j = 0; j < coinImgs.length; j++)
			for (int i = 0; i < coinImgs[j].length; i++)
				coinImgs[j][i] = coinsprites.getSubimage(12 * i, 16 * j, 12, 16);
		trapImag = LoadSave.GetSpritePng(LoadSave.TRAP);
	}

	public void update(int[][] lvlData, Player player) {
		for (Coin p : coins)
			if (p.isActive())
				p.update();

	}

	public void draw(Graphics g, int xLvlOffset) {
		drawCoins(g, xLvlOffset);
		drawTraps(g, xLvlOffset);
	}

	private void drawTraps(Graphics g, int xLvlOffset) {
		for (Trap s : traps)
			g.drawImage(trapImag, (int) (s.getHitbox().x - xLvlOffset), (int) (s.getHitbox().y - s.getyDrawOffset()),
					TRAP_WIDTH, TRAP_HEIGHT, null);

	}

	private void drawCoins(Graphics g, int xLvlOffset) {
		for (Coin p : coins)
			if (p.isActive()) {
				int type = 0;
				if (p.getObjType() == COIN_1)
					type = 1;
				g.drawImage(coinImgs[type][p.getAniIndex()],
						(int) (p.getHitbox().x - p.getxDrawOffset() - xLvlOffset),
						(int) (p.getHitbox().y - p.getyDrawOffset()), COIN_WIDTH, COIN_HEIGHT, null);
			}
	}

	// 다음 레벨을 위한 object 초기화
	public void resetAllObjects() {
		loadObjects(playing.getLevelManager().getCurrentLevel());
		for (Coin p : coins)
			p.reset();
	}
}