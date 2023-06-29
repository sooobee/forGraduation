package utilz;

import static utilz.Constants.EnemyConstants.GHOST;
import static utilz.Constants.ObjectConstants.*;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Ghost;
import main.Game;
import objects.Coin;
import objects.Trap;

public class AddMethod {
	

	// 타일인지 여부 확인, 위치가 게임 창 내부에 있는지
	private static boolean IsSolid(float x, float y, int[][] lvlData) {
		int maxWidth = lvlData[0].length * Game.TILES_SIZE; // 맵의 최대 너비
		if (x < 0 || x >= maxWidth)
			return true;
		if (y < 0 || y >= Game.GAME_HEIGHT)
			return true;

		float xIndex = x / Game.TILES_SIZE;
		float yIndex = y / Game.TILES_SIZE;

		// outside_sprites 크기. value == 12는 투명함. 그래서 11까지만 범위 설정.
		return IsTileSolid((int) xIndex, (int) yIndex, lvlData);
	}

	// 타일을 밟을 수 있는지
	public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData) {
		int value = lvlData[yTile][xTile];

		if (value >= 48 || value < 0 || value != 11)
			return true;
		return false;
	}
	
	// 움직일 수 있는 영역
		public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {

			// 타일이 없는지 확인
			if (!IsSolid(x, y, lvlData))
				if (!IsSolid(x + width, y + height, lvlData))
					if (!IsSolid(x + width, y, lvlData))
						if (!IsSolid(x, y + height, lvlData))
							return true;
			return false;
		}
		
		// 플레이어의 오른쪽 하단 모서리를 확인하여 타일이 있는지 확인
		public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
			// Check the pixel below bottomleft and bottomright
			if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
				if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
					return false;

			return true;

		}
		
		// 물체의 아래에 바닥이 있는지 체크
		public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
			if (xSpeed > 0)
				return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
			else
				return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
		}

		public static boolean IsTileWalk(int xStart, int xEnd, int y, int[][] lvlData) {
			for (int i = 0; i < xEnd - xStart; i++) {
				if (IsTileSolid(xStart + i, y, lvlData))
					return false;
				// Solid가 더이상 없을 경우 돌아간다
				if (!IsTileSolid(xStart + i, y + 1, lvlData))
					return false;
			}

			return true;
		}

		public static boolean IsClearSight(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox,
				int yTile) {
			int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
			int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);

			if (firstXTile > secondXTile)
				return IsTileWalk(secondXTile, firstXTile, yTile, lvlData);
			else
				return IsTileWalk(firstXTile, secondXTile, yTile, lvlData);

		}

	// 좌 우 장애물 충돌 설정
	public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
		int currentTile = (int) (hitbox.x / Game.TILES_SIZE);
		if (xSpeed > 0) {
			// Right
			int tileXPos = currentTile * Game.TILES_SIZE;
			int xOffset = (int) (Game.TILES_SIZE - hitbox.width);
			return tileXPos + xOffset - 1;
		} else
			// Left
			return currentTile * Game.TILES_SIZE;
	}

	// 상 하(천장 & 바닥) 충돌 설정
	public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
		int currentTile = (int) (hitbox.y / Game.TILES_SIZE);
		if (airSpeed > 0) {
			// Falling - touching floor
			int tileYPos = currentTile * Game.TILES_SIZE;
			int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
			return tileYPos + yOffset - 1;
		} else
			// Jumping
			return currentTile * Game.TILES_SIZE;

	}
	


	// 타일 세팅
	public static int[][] GetLevelData(BufferedImage img) {
		int[][] lvlData = new int[img.getHeight()][img.getWidth()];
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getRed();
				if (value >= 48)
					value = 0;
				lvlData[j][i] = value;
			}
		return lvlData;
	}


// 캐릭터 스폰지점
	public static Point GetPlayerSpawn(BufferedImage img) {
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getGreen();
				if (value == 100)
					return new Point(i * Game.TILES_SIZE, j * Game.TILES_SIZE);
			}
		return new Point(1 * Game.TILES_SIZE, 1 * Game.TILES_SIZE);
	}
// 코인 생성
	public static ArrayList<Coin> GetCoins(BufferedImage img) {
		ArrayList<Coin> list = new ArrayList<>();
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getBlue();
				if (value == COIN_1 || value == COIN_2)
					list.add(new Coin(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));
			}

		return list;
	}


// 함정 생성
	public static ArrayList<Trap> GetTraps(BufferedImage img) {
		ArrayList<Trap> list = new ArrayList<>();

		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getBlue();
				if (value == TRAP) //
					list.add(new Trap(i * Game.TILES_SIZE, j * Game.TILES_SIZE, TRAP));
			}

		return list;
	}
	
	// 맵 픽셀의 색깔에 따라 캐릭터를 출현시키는 위치 결정 메서드
	public static ArrayList<Ghost> GetGhosts(BufferedImage img) {
		ArrayList<Ghost> list = new ArrayList<>();
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getGreen();
				if (value == GHOST) // 초록색값에 픽셀을 생성하겠다
					list.add(new Ghost(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
			}
		return list;
	}

}