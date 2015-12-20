package battle.tanks;

import java.awt.Color;
import java.awt.Image;
import javax.swing.*;

import battle.Direction;
import battle.BattleField;

public class BT7 extends AbstractTank {
	
	public BT7() {
		speed = 15;
		tankColor = new Color(0,255,0);
		towerColor  = new Color(255,255,0);	
		destroyableObst.add("B");
		destroyableObst.add("E");		
		setImages();
		underwater = true;
	}
	
	public BT7(BattleField bf, int x, int y,Direction direction) {
		this();
		this.installTank(bf,x,y,direction);
	}
	
	private void setImages() {
		images = new Image[4];
		try{
			images[0] = new ImageIcon(getClass().getResource("/resources/pictures/bt7-top.PNG")).getImage();
			images[1] = new ImageIcon(getClass().getResource("/resources/pictures/bt7-bottom.PNG")).getImage();
			images[2] = new ImageIcon(getClass().getResource("/resources/pictures/bt7-left.PNG")).getImage();
			images[3] = new ImageIcon(getClass().getResource("/resources/pictures/bt7-right.PNG")).getImage();
		} catch(Exception e) {
			throw new IllegalStateException("Can't find bt7 images.");
		}
	}	

}
