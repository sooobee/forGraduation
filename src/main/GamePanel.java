// 그림
package main;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import inputs.KeyboardInputs;
import inputs.MouseInputs;
import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

public class GamePanel extends JPanel {
	
	private MouseInputs mouseInputs;
	private Game game;
	
	public GamePanel(Game game) {
		 mouseInputs = new MouseInputs(this); // 초기화 과정 필요
		 this.game = game;
		 setPanelSize();
		 this.addKeyListener(new KeyboardInputs(this));
		 this.addMouseListener(mouseInputs);
		 this.addMouseMotionListener(mouseInputs);
	}
	

	private void setPanelSize() {
		// 특정 영역의 사각형과 폭과 높이의 값을 관리할 수 있도록 도와주는 클래스
		Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
		this.setPreferredSize(size);
		
	}			
	
	
	// Graphic은 그리기 역할. JFrame에
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // 이를 호출하지 않으면 이미지 결함이 생길 수 있다.
		game.render(g);
	}
	
	public Game getGame() {
		return game;
	}
}
