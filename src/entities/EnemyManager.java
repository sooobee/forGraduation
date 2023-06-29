package entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;
import static utilz.Constants.EnemyConstants.*;

// 적들이 작동, 순찰, 공격하는데 필요하는 코드 모음
public class EnemyManager {
	private Playing playing;
	private BufferedImage[][] ghostArr;
	private ArrayList<Ghost> ghosts = new ArrayList<>();
	
	// 일단 적 이미지 추출
	private void loadEnemyImgs() {
		ghostArr = new BufferedImage[5][9]; // 캐릭터 png 이미지 추출 코드, 배열을 바꾸면 추출되는 이미지도 바뀜
		BufferedImage temp = LoadSave.GetSpritePng(LoadSave.GHOST_SPRITE); // 이미지 로드
		for (int j = 0; j < ghostArr.length; j++)
			for (int i = 0; i < ghostArr[j].length; i++)
				ghostArr[j][i] = temp.getSubimage(i * GHOST_WIDTH_DEFAULT, j * GHOST_HEIGHT_DEFAULT, GHOST_WIDTH_DEFAULT, GHOST_HEIGHT_DEFAULT);
	}
	
	//적 모두 초기화 ing
	public void resetAllEnemies() {
		for (Ghost c : ghosts)
			c.resetEnemy();
	}

	public EnemyManager(Playing playing) {
		this.playing = playing;
		loadEnemyImgs();
	}
	//적 표시하기
	public void loadEnemies(Level level) {
		ghosts = level.getGhosts();
	}

	public void update(int[][] lvlData, Player player) {
		boolean isAnyActive = false; //움직이는거 아직 false 처리
		for (Ghost gg : ghosts)
			if (gg.isActive()) {
				gg.update(lvlData, player);
				isAnyActive = true;
			}
		if(!isAnyActive)
			playing.setLevelCompleted(true);
	}

	public void draw(Graphics g, int xLvlOffset) {
		DGhost(g, xLvlOffset);
	}

	private void DGhost(Graphics g, int xLvlOffset) {
		for (Ghost gg : ghosts)
			if (gg.isActive()) {
				g.drawImage(ghostArr[gg.getState()][gg.getAniIndex()], (int) gg.getHitbox().x - xLvlOffset - GHOST_DRAWOFFSET_X + gg.flipX(), (int) gg.getHitbox().y - GHOST_DRAWOFFSET_Y,
						GHOST_WIDTH * gg.flipW(), GHOST_HEIGHT, null);

			}

	}
	
	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		for (Ghost gg : ghosts)
			if (gg.isActive())
				if (attackBox.intersects(gg.getHitbox())) {
					gg.hurt(10); // 플레이어가 데미지 10을 입힘.
					return;
				}
	}


}
