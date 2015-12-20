package battle.landscape;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

import battle.Destroyable;
import battle.Drawable;

public abstract class AbstractObstacle   implements Drawable,Destroyable{
	
	final static int OBSTACLE_SIZE = 64;
	protected int x;
	protected int y;
	protected Color obstacleColor;
	protected Image image;
	private boolean destroyed;
	
	public AbstractObstacle() {
		destroyed = false;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void draw(Graphics g) {
		if(!this.isDestroyed()) {
			if(image != null) {
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
			} else {
				g.setColor(obstacleColor);
				g.fillRect(x, y, OBSTACLE_SIZE, OBSTACLE_SIZE);
			}
		}
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}

	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}

	public void destroy() {
		x = -100;
		y = -100;
		destroyed = true;
	}
}
