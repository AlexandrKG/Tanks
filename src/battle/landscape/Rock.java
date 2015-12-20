package battle.landscape;

import java.awt.Color;
import java.awt.Image;

import battle.Destroyable;
public class Rock extends AbstractObstacle implements Destroyable{
	
	public Rock(Image image) {
		obstacleColor = new Color(127,127,127);
		this.image = image;
	}
	

}
