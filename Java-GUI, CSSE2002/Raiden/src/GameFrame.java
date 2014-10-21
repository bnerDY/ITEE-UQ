import java.awt.BorderLayout;
import javax.swing.JFrame;


@SuppressWarnings("serial")
public class GameFrame extends JFrame{
	
	private GamePanel gamePanel = new GamePanel();
	public GameFrame(){
		
		this.setTitle("Raiden");
		this.setSize(Global.FRAME_WIDTH + 10, Global.FRAME_HEIGHT + 35);
		this.setLocationRelativeTo(null);
		gamePanel.setSize(Global.FRAME_WIDTH, Global.FRAME_HEIGHT);
		this.add(gamePanel,BorderLayout.CENTER);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		gamePanel.requestFocus();
	}
	public static void main(String[] args) {
		new GameFrame();
	}
}
