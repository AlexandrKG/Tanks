package tanks;

import utl.Destroyable;
import utl.Direction;
import utl.Drawable;


public interface Tank  extends Drawable, Destroyable {
	
	public Action setUp()throws Exception;

	public void move() throws Exception;

	public void fire() throws Exception;

	public int getX();

	public int getY();
	
	public Direction getDirection();
	
	public void updateX(int x);

	public void updateY(int y);
	
	public int getSpeed();

}
