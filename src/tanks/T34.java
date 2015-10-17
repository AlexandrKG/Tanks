package tanks;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import utl.Direction;
import battle.BattleField;

public class T34 extends AbstractTank {

	public T34() {
		tankColor = new Color(255, 0, 0);
		towerColor = new Color(0, 255, 0);
		setImages();
		speed = 7;
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
			images[0] = ImageIO.read(new File("pictures/t34top.PNG").getAbsoluteFile());
			images[1] = ImageIO.read(new File("pictures/t34bottom.PNG").getAbsoluteFile());
			images[2] = ImageIO.read(new File("pictures/t34left.PNG").getAbsoluteFile());
			images[3] = ImageIO.read(new File("pictures/t34right.PNG").getAbsoluteFile());
		} catch(IOException e) {
			throw new IllegalStateException("Can't find tank images.");
		}
	}

}
