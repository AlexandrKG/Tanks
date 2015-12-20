package battle.landscape;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;

public class Water extends AbstractObstacle{
	
	public Water(Image image) {
		
		obstacleColor = new Color(93,213,255);
		this.image = image;
	}

	@Override
	public void draw(Graphics g) {
		if(!this.isDestroyed()) {
			if(image != null) {
				Graphics2D g2D = (Graphics2D)g;
				Composite org = g2D.getComposite();
				Composite translucent = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
				g2D.setComposite(translucent);
				g.drawImage(image, this.getX(), this.getY(), 
						this.getX()+OBSTACLE_SIZE, this.getY()+OBSTACLE_SIZE, 
						this.getX(),this.getY(), 
						this.getX()+OBSTACLE_SIZE,this.getY()+OBSTACLE_SIZE, 
						new ImageObserver() {
							
							@Override
							public boolean imageUpdate(Image img, int infoflags, int x, int y,
									int width, int height) {
								// TODO Auto-generated method stub
								return false;
							}
						});
				g2D.setComposite(org);
			} else {
				g.setColor(obstacleColor);
				g.fillRect(x, y, OBSTACLE_SIZE, OBSTACLE_SIZE);
			}
		}
	}
	

}
