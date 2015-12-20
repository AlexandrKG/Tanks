package battle.landscape;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import javax.swing.*;

import battle.Destroyable;

public class Eagle extends AbstractObstacle implements Destroyable {
	private int v;
	private int h;

	public Eagle() {
		obstacleColor = new Color(217, 39, 196);
		setImages();

	}

	private void setImages() {
		try {
			image = new ImageIcon(getClass().getResource("/resources/pictures/staff.png")).getImage();
		} catch (Exception e) {
			throw new IllegalStateException("Can't find staff images.");
		}
	}

	public int getV() {
		return v;
	}

	public void setV(int v) {
		this.v = v;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}
	
	@Override
	public void draw(Graphics g) {
		if(!this.isDestroyed()) {
			if(image != null) {
				g.drawImage(image, this.getX(), this.getY(), new ImageObserver() {					
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
}
