import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class EBullet extends Bullet{

	private Image img;
	private JPanel jpanel;
	
	
    public boolean isUsed = false;     
	
	
	public JPanel getJpanel() {
		return jpanel;
	}

	public void setJpanel(JPanel jpanel) {
		this.jpanel = jpanel;
	}

	public EBullet(int x, int y, int width, int heigth) {
		super(x, y, width, heigth);
		img = new ImageIcon("Image/fire.png").getImage();
		// TODO Auto-generated constructor stub
	}

	public void bulletMove() {
		// TODO Auto-generated method stub
		y+=10;
	}

	public void drawMe(Graphics g) {
		// TODO Auto-generated method stub
		if(!isUsed)
		g.drawImage(img, x, y, width, heigth, jpanel);
	}

	public boolean isEBulletHitPPlane(){
		
		int x = Controller.pplane.x; 
		int y = Controller.pplane.y;
		int w = Controller.pplane.w;
		int h = Controller.pplane.h;
		Rectangle recEbullet = new Rectangle(this.x, this.y, width, heigth);
		Rectangle recPplane = new Rectangle(x, y, w, h);
		
		return recEbullet.intersects(recPplane) && !isUsed;
	}
}
