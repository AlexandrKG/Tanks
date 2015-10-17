package utl;

import missile.Bullet;
import tanks.AbstractTank;

public class Interceptions {
	
	public Interceptions() {
		
	}
	private static boolean verticalRegion(Bullet bt, AbstractTank target) {
		
		if (((bt.getX() + bt.BULLET_SIZE) > target.getX())
				&& (bt.getX() < (target.getX() + target.TANK_SIZE))) {
				return true;
		}
		return false;
	}
	
	private static boolean horizontalRegion(Bullet bt, AbstractTank target) {
		if (((bt.getY() + bt.BULLET_SIZE) > target.getY())
				&& (bt.getY() < (target.getY() + target.TANK_SIZE))) {
				return true;
		}
		return false;
	}
	
	public static boolean targetExist(AbstractTank shooter, AbstractTank target) {

		Bullet bt = new Bullet(shooter);
		bt.setDirection(shooter.getDirection());
		bt.setX(0);
		bt.setY(0);
		bt.updateX(shooter.getX() + (target.TANK_SIZE - bt.BULLET_SIZE)/2);
		bt.updateY(shooter.getY() + (target.TANK_SIZE - bt.BULLET_SIZE)/2);
		
		if (bt.getDirection() == Direction.TOP) {
			if(verticalRegion(bt,target) && bt.getY() > target.getY()) {
				return true;
			}
		} else if (bt.getDirection() == Direction.BOTTOM ) {
			if(verticalRegion(bt,target)&& bt.getY() < target.getY()) {
				return true;
			}			
		} else if (bt.getDirection() == Direction.LEFT) {
			if(horizontalRegion(bt,target) && bt.getX() > target.getX()) {
				return true;
			}			
		} else {
			if(horizontalRegion(bt,target) && bt.getX() < target.getX()) {
				return true;
			}						
		}		
		return false;
	}
	
	public static boolean targetNearExist(AbstractTank shooter, AbstractTank target, Direction dir) {

		Bullet bt = new Bullet(shooter);
		bt.setDirection(dir);
		bt.setX(0);
		bt.setY(0);

		bt.updateX(shooter.getX() + (target.TANK_SIZE - bt.BULLET_SIZE)/2);
		bt.updateY(shooter.getY() + (target.TANK_SIZE - bt.BULLET_SIZE)/2);
		
		if (bt.getDirection() == Direction.TOP) {
			if(verticalRegion(bt,target) && shooter.getY() == (target.getY()+target.TANK_SIZE)) {
				return true;
			}
		} else if (bt.getDirection() == Direction.BOTTOM) {
			if(verticalRegion(bt,target)&& (shooter.getY()+ target.TANK_SIZE) == target.getY()) {
				return true;
			}			
		} else if (bt.getDirection() == Direction.LEFT) {
			if(horizontalRegion(bt,target) && shooter.getX() == (target.getX()+target.TANK_SIZE)) {
				return true;
			}			
		} else {
			if(horizontalRegion(bt,target) && (shooter.getX()+target.TANK_SIZE) == target.getX()) {
				return true;
			}						
		}		
		return false;
	}
	
	public static void updateCoordinates(int[] coord, int step, Direction dir) {
		if (dir == Direction.TOP) {
			coord[1] -= step;
		} else if (dir == Direction.BOTTOM) {
			coord[1] += step;
		} else if (dir == Direction.LEFT) {
			coord[0] -= step;
		} else {
			coord[0] += step;
		}
	}	
}
