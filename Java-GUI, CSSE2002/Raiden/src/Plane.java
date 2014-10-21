import java.awt.Graphics;


abstract public class Plane {

	public int x, y;
	public int w, h;
	
	public Plane(){
		
	}
	public Plane(int x, int y, int w, int h) {
		super();
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	public abstract void drawMe(Graphics g);
}
