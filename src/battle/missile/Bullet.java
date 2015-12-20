package battle.missile;

import java.awt.Color;
import java.awt.Graphics;

import battle.BattleField;
import battle.tanks.AbstractTank;
import battle.Destroyable;
import battle.Direction;
import battle.Drawable;

public class Bullet  implements Drawable,Destroyable{
	
	private Direction direction;
	private int x;
	private int y;
	private int speed = 5;
	public final static int BULLET_SIZE = 6;
	public final static int RECT_SIZE = BattleField.RECT_SIZE;
	protected Color bulletColor;
	private boolean destroyed;

	public Bullet(AbstractTank tank) {
		x = tank.getX();
		y = tank.getY();
		updateX((RECT_SIZE - BULLET_SIZE) / 2);
		updateY((RECT_SIZE - BULLET_SIZE) / 2);
		direction = tank.getDirection();
		bulletColor = new Color(255,255,0);	
		destroyed = false;
	}

	public Direction getDirection() {
		return direction;
	}
	public int getSpeed() {
		return speed;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}

	public void updateX(int step) {
		x += step;
	}
	
	public void updateY(int step) {
		y += step;
	}
	
	public void draw(Graphics g) {
		
		g.setColor(bulletColor);
		g.fillRect(x, y, BULLET_SIZE, BULLET_SIZE);

	}
	
	public boolean isDestroyed() {
		return destroyed;
	}
	
	public void destroy() {
		x = -100;
		y = -100;
		destroyed = true;
		
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

}
