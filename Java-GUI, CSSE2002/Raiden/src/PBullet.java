import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class PBullet extends Bullet{

	private Image img;                        
	private JPanel jpanel;
	
	
		
	public JPanel getJpanel() {
		return jpanel;
	}

	public void setJpanel(JPanel jpanel) {
		this.jpanel = jpanel;
	}

	public PBullet(int x, int y, int width, int heigth) {
		super(x, y, width, heigth);
		img = new ImageIcon("Image/fire.png").getImage();
		// TODO Auto-generated constructor stub
	}

	public void bulletMove() {
		// TODO Auto-generated method stub
		this.y-=20;              
	}

	public void drawMe(Graphics g) {
		// TODO Auto-generated method stub
		g.drawImage(img, x, y, width, heigth, jpanel);
	}

	public int isPbulletHitEplane(){		
		for(int j=0;j < Controller.eplanes.size();j++){
			Rectangle recPbullet = new Rectangle(x, y, width, heigth);
			Rectangle recEplane = new Rectangle(Controller.eplanes.elementAt(j).x,
			Controller.eplanes.elementAt(j).y,Controller.eplanes.elementAt(j).w,
			Controller.eplanes.elementAt(j).h);
			if(recPbullet.intersects(recEplane)) 
			{
				 return j;
			 }
		}
		return -1;
	}
}
