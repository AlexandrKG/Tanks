package tanks;

import java.awt.Color;
import java.awt.Image;
import javax.swing.*;

import utl.Direction;
import battle.BattleField;

public class Tiger extends AbstractTank {
	private int armor;
	
	public Tiger() {
		armor = 1;
		tankColor = new Color(0,0,0);
		towerColor  = new Color(255,0,0);	
		destroyableObst.add("B");
		destroyableObst.add("R");
		destroyableObst.add("E");	
		setImages();
		underwater = false;
		speed = 21;
	}

	public Tiger(BattleField bf, int x, int y,Direction direction) {
		this();
		this.installTank(bf,x,y,direction);
	}
	
	public int getArmor() {
		return armor;
	}

	public void updateArmor(int step) {
		armor += step;
	}
	private void setImages() {
		images = new Image[4];
		try{
			images[0] = new ImageIcon(getClass().getResource("/resources/pictures/tiger-top.PNG")).getImage();
			images[1] = new ImageIcon(getClass().getResource("/resources/pictures/tiger-bottom.PNG")).getImage();
			images[2] = new ImageIcon(getClass().getResource("/resources/pictures/tiger-left.PNG")).getImage();
			images[3] = new ImageIcon(getClass().getResource("/resources/pictures/tiger-right.PNG")).getImage();
		} catch(Exception e) {
			throw new IllegalStateException("Can't find tiger images.");
		}
	}

}
