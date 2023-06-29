package utilz;

import main.Game;

public class Constants {

	public static final float GRAVITY = 0.04f * Game.SCALE;
	public static final int ANI_SPEED = 25;

	public static class ObjectConstants {

		public static final int COIN_1 = 0;
		public static final int COIN_2 = 1;
		public static final int TRAP = 4;

		public static final int COIN1_VALUE = 15;
		public static final int COIN2_VALUE = 10;

		public static final int TRAP_WIDTH_DEFAULT = 32;
		public static final int TRAP_HEIGHT_DEFAULT = 32;
		public static final int TRAP_WIDTH = (int) (Game.SCALE * TRAP_WIDTH_DEFAULT);
		public static final int TRAP_HEIGHT = (int) (Game.SCALE * TRAP_HEIGHT_DEFAULT);

		public static final int COIN_WIDTH_DEFAULT = 12;
		public static final int COIN_HEIGHT_DEFAULT = 16;
		public static final int COIN_WIDTH = (int) (Game.SCALE * COIN_WIDTH_DEFAULT);
		public static final int COIN_HEIGHT = (int) (Game.SCALE * COIN_HEIGHT_DEFAULT);

		public static int GetSpriteAmount(int object_type) {
			switch (object_type) {
			case COIN_1, COIN_2:
				return 7;
			}
			return 1;
		}
	}

	public static class EnemyConstants {
		public static final int GHOST = 0;

		public static final int IDLE = 0;
		public static final int RUNNING = 1;
		public static final int ATTACK = 2;
		public static final int HIT = 3;
		public static final int DEAD = 4;

		// 캐릭터 기본 사이즈
		public static final int GHOST_WIDTH_DEFAULT = 72;
		public static final int GHOST_HEIGHT_DEFAULT = 32;

		// 게임 창 배율에 따른 캐릭터 크기 조정 변수
		public static final int GHOST_WIDTH = (int) (GHOST_WIDTH_DEFAULT * Game.SCALE);
		public static final int GHOST_HEIGHT = (int) (GHOST_HEIGHT_DEFAULT * Game.SCALE);

		public static final int GHOST_DRAWOFFSET_X = (int) (26 * Game.SCALE);
		public static final int GHOST_DRAWOFFSET_Y = (int) (9 * Game.SCALE);

		public static int GetSpriteAmount(int enemy_type, int enemy_state) {

			// 캐릭터 상태
			switch (enemy_type) {
			case GHOST:
				switch (enemy_state) {
				case IDLE:
					return 9;
				case RUNNING:
					return 6;
				case ATTACK:
					return 7;
				case HIT:
					return 4;
				case DEAD:
					return 5;
				}
			}

			return 0;

		}

		// 최대 체력 메서드
		public static int GetMaxHealth(int enemy_type) {
			switch (enemy_type) {
			case GHOST:
				return 10; // 체력 설정
			default:
				return 1;
			}
		}

		// 적이 데미지를 수치적으로 입힐 수 있게 하는 메서드
		public static int GetEnemyDmg(int enemy_type) {
			switch (enemy_type) {
			case GHOST:
				return 15; // 공격력 설정
			default:
				return 0;
			}

		}

	}

	public static class UI {

		// 메뉴 버튼
		public static class Buttons {
			public static final int B_WIDTH_DEFAULT = 140; // 메뉴 버튼 너비
			public static final int B_HEIGHT_DEFAULT = 56; // 메뉴 버튼 높이
			// 게임창이 확장되거나 축소될 경우
			public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE);
			public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);
		}

		// 일시중지 버튼
		public static class PauseButtons {
			public static final int SOUND_SIZE_DEFAULT = 42;
			public static final int SOUND_SIZE = (int) (SOUND_SIZE_DEFAULT * Game.SCALE);
		}

		// 재생, 이전, 홈 버튼
		public static class URMButtons {
			public static final int URM_DEFAULT_SIZE = 56;
			public static final int URM_SIZE = (int) (URM_DEFAULT_SIZE * Game.SCALE);

		}

		// 음량 버튼
		public static class VolumeButtons {
			public static final int VOLUME_DEFAULT_WIDTH = 28;
			public static final int VOLUME_DEFAULT_HEIGHT = 44;
			public static final int SLIDER_DEFAULT_WIDTH = 215;
			// 조정된 버전
			public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * Game.SCALE);
			public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * Game.SCALE);
			public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * Game.SCALE);
		}

	}

	public static class PlayerConstants {
		public static final int IDLE = 0;
		public static final int RUNNING = 1;
		public static final int JUMP = 2;
		public static final int FALLING = 3;
		public static final int ATTACK = 4;
		public static final int HIT = 5;
		public static final int DEAD = 6;

		public static int GetSpriteAmount(int player_action) {
			switch (player_action) {
			case DEAD:
				return 8;
			case RUNNING:
				return 6;
			case IDLE:
				return 5;
			case HIT:
				return 4;
			case JUMP:
			case ATTACK:
				return 3;
			case FALLING:
			default:
				return 1;
			}
		}
	}

	// 이동방향
	public static class Directions {
		public static final int LEFT = 0;
		public static final int UP = 1;
		public static final int RIGHT = 2;
		public static final int DOWN = 3;
	}

}
