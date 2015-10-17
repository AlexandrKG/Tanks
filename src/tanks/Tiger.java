package tanks;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import utl.BattleFile;
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
		speed = 15;
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
			images[0] = ImageIO.read(new File("pictures/tiger-top.PNG").getAbsoluteFile());
			images[1] = ImageIO.read(new File("pictures/tiger-bottom.PNG").getAbsoluteFile());
			images[2] = ImageIO.read(new File("pictures/tiger-left.PNG").getAbsoluteFile());
			images[3] = ImageIO.read(new File("pictures/tiger-right.PNG").getAbsoluteFile());
		} catch(IOException e) {
			throw new IllegalStateException("Can't find tank images.");
		}
	}

}
