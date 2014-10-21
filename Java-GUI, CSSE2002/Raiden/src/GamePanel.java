import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

import javax.swing.JPanel;


@SuppressWarnings("serial")
public class GamePanel extends JPanel{

	private Vector<Bang> bang = new Vector<Bang>();
	private Vector<EBullet> ebullet = new Vector<EBullet>();
	private Vector<PBullet> pbullet = new Vector<PBullet>();
	private Vector<EPlane> eplane = new Vector<EPlane>();
	private PPlane pplane = new PPlane(250, 400, 50, 50);
	
	public GamePanel(){
		Controller controller = 
				new Controller(bang, ebullet, pbullet, eplane, pplane, this);
		this.addKeyListener(controller);
		controller.StartRun();                
	}
	
	
	public void display(Vector<Bang> bang, Vector<EBullet> ebullet, 
			Vector<PBullet> pbullet, Vector<EPlane> eplane,PPlane pplane){
		this.bang = bang;
		this.ebullet = ebullet;
		this.pbullet = pbullet;
		this.eplane = eplane;
		this.pplane = pplane;
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		this.setBackground(Color.black);
		showScore(g);
		pplane.drawMe(g);                       
		showLife(g);
		pplane.setJpanel(this);		
		for(int i=0; i < bang.size(); i++){
			bang.elementAt(i).drawMe(g);
			bang.elementAt(i).setJpanel(this);
		}
		for(int i=0; i < ebullet.size(); i++){
			ebullet.elementAt(i).drawMe(g);
			ebullet.elementAt(i).setJpanel(this);
		}
		for(int i=0; i < pbullet.size(); i++){
			pbullet.elementAt(i).drawMe(g);
			pbullet.elementAt(i).setJpanel(this);
		}
		for(int i=0; i < eplane.size(); i++){
			eplane.elementAt(i).drawMe(g);
			eplane.elementAt(i).setJpanel(this);
		}
		
	}
	public void showScore(Graphics g){
		g.setColor(Color.green);
		g.drawString("Score: " + Controller.DestoryNum, 30, 30);
	}
	public void showLife(Graphics g){
		g.setColor(Color.red);
		g.drawString("HP: " + PPlane.life, 320, 30);
		g.fill3DRect(200, 20, PPlane.life, 20, true);
	}
}
