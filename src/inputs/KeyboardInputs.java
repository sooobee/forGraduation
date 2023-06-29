package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import gamestates.Gamestate;
import main.GamePanel;


public class KeyboardInputs implements KeyListener {

	private GamePanel gamePanel;
	// 값 변경
	public KeyboardInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

	@Override
	// 키를 놓으면 아무것도 없어야 함.
	// 100*100 화면에서 왼쪽 위 모서리는 (0,0), 오른쪽 아래 모서리는 (100,100)
	public void keyReleased(KeyEvent e) {
		switch (Gamestate.state) {
		case MENU:
			gamePanel.getGame().getMenu().keyReleased(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().keyReleased(e);
			break;
		default:
			break;

		}
	}
	
	@Override
	// 100*100 화면에서 왼쪽 위 모서리는 (0,0), 오른쪽 아래 모서리는 (100,100)
	// 키를 누를 때마다 방향을 설정
	public void keyPressed(KeyEvent e) {
		switch (Gamestate.state) {
		case MENU:
			gamePanel.getGame().getMenu().keyPressed(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().keyPressed(e);
			break;
		case OPTIONS:
			gamePanel.getGame().getGameOptions().keyPressed(e);
			break;
		default:
			break;
		}
	}
}
