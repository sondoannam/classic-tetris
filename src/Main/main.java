package Main;

import javax.swing.JFrame;

public class main {
	public static void main(String[] args) {
		JFrame window = new JFrame("Tetris");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		
		//add gamePanel to window
		GamePanel gp = new GamePanel();
		window.add(gp);
		window.pack();
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		gp.lauchGame();
	}
}
