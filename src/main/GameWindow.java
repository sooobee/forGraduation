// 게임 창
package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

public class GameWindow  {
	private JFrame jframe;
	
	public GameWindow(GamePanel gamePanel) {
		
		jframe = new JFrame();
		
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창을 닫으면 완벽하게 프로그램 종료
		jframe.add(gamePanel);
		jframe.setResizable(false); // 창 크기 조절을 원하지 않음
		jframe.pack();
		jframe.setLocationRelativeTo(null); // 화면 중앙에 창을 생성
		jframe.setVisible(true); // 화면 출력
		// 윈도우가 포커스를 얻거나 잃는 경우에 발생한
		jframe.addWindowFocusListener(new WindowFocusListener() {

			@Override
			public void windowLostFocus(WindowEvent e) {
				gamePanel.getGame().windowFocusLost();
			}

			@Override
			public void windowGainedFocus(WindowEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}
}
