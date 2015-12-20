package battle.tanks;

import java.awt.Color;
import java.awt.Image;
import javax.swing.*;

import battle.Direction;
import battle.BattleField;

public class T34 extends AbstractTank {

	public T34() {
		tankColor = new Color(255, 0, 0);
		towerColor = new Color(0, 255, 0);
		setImages();
		speed = 15;
		destroyableObst.add("B");
		destroyableObst.add("E");
		underwater = false;
	}
	
	public T34(BattleField bf, int x, int y, Direction direction) {
		this();
		this.installTank(bf,x,y,direction);
	}
	
	private void setImages() {
		images = new Image[4];
		try{
			images[0] = new ImageIcon(getClass().getResource("/resources/pictures/t34top.PNG")).getImage();
			images[1] = new ImageIcon(getClass().getResource("/resources/pictures/t34bottom.PNG")).getImage();
			images[2] = new ImageIcon(getClass().getResource("/resources/pictures/t34left.PNG")).getImage();
			images[3] = new ImageIcon(getClass().getResource("/resources/pictures/t34right.PNG")).getImage();
		} catch(Exception e) {
			throw new IllegalStateException("Can't find t34 images.");
		}
	}

}
