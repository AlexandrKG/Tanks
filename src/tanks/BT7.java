package tanks;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import utl.Direction;
import battle.BattleField;

public class BT7 extends AbstractTank {
	
	public BT7() {
		speed = 10;
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
			images[0] = ImageIO.read(new File("pictures/bt7-top.PNG").getAbsoluteFile());
			images[1] = ImageIO.read(new File("pictures/bt7-bottom.PNG").getAbsoluteFile());
			images[2] = ImageIO.read(new File("pictures/bt7-left.PNG").getAbsoluteFile());
			images[3] = ImageIO.read(new File("pictures/bt7-right.PNG").getAbsoluteFile());
		} catch(IOException e) {
			throw new IllegalStateException("Can't find tank images.");
		}
	}	

}
