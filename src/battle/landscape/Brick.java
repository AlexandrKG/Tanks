package battle.landscape;

import java.awt.Color;
import java.awt.Image;

import battle.Destroyable;

public class Brick extends AbstractObstacle implements Destroyable{
	
	public Brick(Image image) {
		obstacleColor = new Color(228,109,10);
		this.image = image;
	}
	
	
}
