import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class PPlane extends Plane{

	private Image img;
	private JPanel jpanel;
	private int direction;
	public static boolean UP;
	public static boolean DOWN;
	public static boolean LEFT;
	public static boolean RIGHT;
	public static boolean isFired;
	public static int life = 100;
	
	public PPlane(){
		
	}
	public PPlane(int x, int y, int width, int height) {
		super(x, y, width, height);
		img = new ImageIcon("Image/boss1.png").getImage();
		// TODO Auto-generated constructor stub
	}
	public JPanel getJpanel() {
		return jpanel;
	}
	public void setJpanel(JPanel jpanel) {
		this.jpanel = jpanel;
	}
	
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public void drawMe(Graphics g) {
		// TODO Auto-generated method stub
		g.drawImage(img, this.x, this.y, this.w, this.h, this.jpanel);
	}

	public void changeDirection(int direction){
		this.direction = direction;
	}
	
	public void pplaneMove(){
		if (UP)
			y -= Global.SPEED;
		if (DOWN)
			y += Global.SPEED;
		if (LEFT)
			x -= Global.SPEED;
		if (RIGHT)
			x += Global.SPEED;
	}
	public boolean isAlive(){
		if(PPlane.life <= 0){
			PPlane.life = 0;
			return false;    
		} 
		else
			return true;
	}
}
