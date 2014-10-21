import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class EPlane extends Plane{

	private Image img;
	private JPanel jpanel;
	
	public JPanel getJpanel() {
		return jpanel;
	}

	public void setJpanel(JPanel jpanel) {
		this.jpanel = jpanel;
	}

	public EPlane(int x, int y, int width, int height) {
		super(x, y, width, height);
		img = new ImageIcon("Image/enemy.png").getImage();
		// TODO Auto-generated constructor stub
	}

	public void drawMe(Graphics g) {
		// TODO Auto-generated method stub
		
		g.drawImage(img, this.x, this.y, this.w, this.h, jpanel);
	}
	
	public void eplaneMove(){
		this.y+=3;
	}

}
