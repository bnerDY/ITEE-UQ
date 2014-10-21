import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.Timer;


public class Controller extends KeyAdapter{

	public static Vector<Bang> bangs = new Vector<Bang>();
	public static Vector<EBullet> ebullets = new Vector<EBullet>();
	public static Vector<PBullet> pbullets = new Vector<PBullet>();
	public static Vector<EPlane> eplanes = new Vector<EPlane>();
	public static PPlane pplane = new PPlane();
	private GamePanel gamePanel;
	private Random random = new Random();
	public static int DestoryNum;
	
	
	public Controller(Vector<Bang> bang,Vector<EBullet> ebullet,Vector<PBullet> 
	pbullet, Vector<EPlane> eplane,PPlane pplane,GamePanel gamePanel) {
		super();
		Controller.bangs = bang;
		Controller.ebullets = ebullet;
		Controller.pbullets = pbullet;
		Controller.eplanes = eplane; 
		Controller.pplane = pplane;
		this.gamePanel = gamePanel;
			Timer timer = new Timer(1000, new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				for(int i=0;i < eplanes.size();i++){
					EBullet ebullet = new EBullet(eplanes.elementAt(i).x, 
							eplanes.elementAt(i).y,8,2);
					ebullets.add(ebullet);
				}
			}
		});
		timer.start();             
		}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch (e.getKeyCode()){
			case KeyEvent.VK_UP:
				PPlane.UP = true;			
				break;
			case KeyEvent.VK_DOWN:
				PPlane.DOWN = true;
				break;
			case KeyEvent.VK_LEFT:
				PPlane.LEFT = true;
				break;
			case KeyEvent.VK_RIGHT:
				PPlane.RIGHT = true;
				break;
			case KeyEvent.VK_X:
				PPlane.isFired = true;
				break;
		}
	}

	
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		switch (e.getKeyCode()){
		case KeyEvent.VK_UP:
			PPlane.UP = false;			
			break;
		case KeyEvent.VK_DOWN:
			PPlane.DOWN = false;
			break;
		case KeyEvent.VK_LEFT:
			PPlane.LEFT = false;
			break;
		case KeyEvent.VK_RIGHT:
			PPlane.RIGHT = false;
			break;
		case KeyEvent.VK_X:
			PPlane.isFired = false;
	}
	}

	public void StartRun(){
		new Thread(){
			public void run(){
				int count = 0;      
				while(true){
					pplane.pplaneMove();
					// add bullet.
					if(PPlane.isFired && count % 5 == 0){
						PBullet pbullet1 =
							new PBullet(pplane.x + 35, pplane.y + 50, 8, 15);
						pbullets.add(pbullet1);
						PBullet pbullet2 =
							new PBullet(pplane.x + 5, pplane.y + 50, 8, 15);
						pbullets.add(pbullet2);	
					}
					count ++;
					for(int i = 0; i < pbullets.size(); i++){
						pbullets.elementAt(i).bulletMove();
						int index = pbullets.elementAt(i).isPbulletHitEplane();
						if(index != -1)    
						{
							Bang bang = new Bang(pbullets.elementAt(i).x,
									pbullets.elementAt(i).y, 30, 30);
							bangs.add(bang);
							DestoryNum ++;
							eplanes.remove(index);			
						}
					}
					for(int i=0; i < pbullets.size(); i++){
						if(pbullets.elementAt(i).y <= 0)
						{
							pbullets.remove(i);
						}
					}
					if(eplanes.size() < Global.ENEMY_NUMBER){
						int x = random.nextInt(Global.FRAME_WIDTH);
						int y = -30;
						EPlane eplane = new EPlane(x, y, 30, 30);
						eplanes.add(eplane);
					}
					for(int i=0; i < eplanes.size(); i++){
						eplanes.elementAt(i).eplaneMove();
						if(eplanes.elementAt(i).y >= Global.FRAME_HEIGHT){
							eplanes.remove(i);
						}
					}				
				
					for(int i=0; i < ebullets.size(); i++){
						ebullets.elementAt(i).bulletMove();
						if(ebullets.elementAt(i).isEBulletHitPPlane()){
							ebullets.elementAt(i).isUsed = true;
							PPlane.life -= 2;
						}
						if(ebullets.elementAt(i).y >= Global.FRAME_HEIGHT){
							ebullets.remove(i);
						}
					}
					
					for(int i=0; i < bangs.size(); i++){
						if(bangs.elementAt(i).isBang == true){
							bangs.remove(i);
						}
					}
									
					try {
						sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					JudgeLife();
					gamePanel.display
					(bangs, ebullets, pbullets, eplanes, pplane);
					
				}
			}
		}.start();
	}
	
	public void JudgeLife(){
		if(!pplane.isAlive()){
			int result = JOptionPane.showConfirmDialog(gamePanel, 
					"Try again? :D","OK",JOptionPane.YES_OPTION);
			if(result == 0){
				newGame();
			}else{
				System.exit(0);
			}
			
		}
	}
	
	public void newGame(){
		bangs.clear();		   		
		ebullets.clear();
		pbullets.clear();
		eplanes.clear();
		pplane = new PPlane(250, 400, 100, 100);
		DestoryNum = 0;
		PPlane.life = 100;    
		PPlane.DOWN = false;  
		PPlane.UP = false;
		PPlane.LEFT = false;
		PPlane.RIGHT = false;
		PPlane.isFired = false;
	}
}
