package utilz;

import java.awt.Color;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entities.Ghost;
import main.Game;

import static utilz.Constants.EnemyConstants.GHOST;

public class LoadSave {

	public static final String PLAYER = "player_sprites.png"; // JACK(Player 파일에 이미지 추출 코드 있음)
	public static final String LEVEL_IMG = "outside_sprites.png"; // JACK(Player 파일에 이미지 추출 코드 있음)

	public static final String MENU_BUTTONS = "button.png"; // 메뉴 버튼
	public static final String MENU_BACKGROUND = "background_menu.png"; // 메뉴 배경
	public static final String PAUSE_BACKGROUND = "pause.png"; // 일지정지 메뉴
	public static final String SOUND_BUTTONS = "sound_button.png"; // 사운드 버튼
	public static final String URM_BUTTONS = "urm_buttons.png"; // resume, home, return button
	public static final String VOLUME_BUTTONS = "volume.png"; // 음량 버튼
	public static final String MENU_BACKGROUND_IMG = "background_menu.png"; // 게임 시작 배경
	public static final String PLAYING_BG_IMG = "playing.png"; // 게임 플레이 배경
	public static final String GHOST_SPRITE = "Ghost.png"; // 적
	public static final String COMPLETED_IMG = "level_complete.png"; // 레벨 통과 이미지
	public static final String COIN_IMG = "coins.png";
	public static final String PREVIEW = "preview.png";
	public static final String TRAP = "trap_atlas.png";
	public static final String DEATH_SCREEN = "death.png";
	public static final String OPTIONS_MENU = "option.png";
	public static final String GAME_COMPLETED = "game_completed.png"; 

// 이미지 추출 코드
	public static BufferedImage GetSpritePng(String fileName) {
		BufferedImage img = null;
		InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
		try {
			img = ImageIO.read(is);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return img;
	}

// 맵 가져오기
	public static BufferedImage[] GetAllLevels() {
		URL url = LoadSave.class.getResource("/lvls");
		File file = null;

		try {
			file = new File(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		File[] files = file.listFiles();
		File[] filesSorted = new File[files.length];

		for (int i = 0; i < filesSorted.length; i++)
			for (int j = 0; j < files.length; j++) {
				if (files[j].getName().equals((i + 1) + ".png"))
					filesSorted[i] = files[j];

			}

		BufferedImage[] imgs = new BufferedImage[filesSorted.length];

		for (int i = 0; i < imgs.length; i++)
			try {
				imgs[i] = ImageIO.read(filesSorted[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}

		return imgs;
	}

}
