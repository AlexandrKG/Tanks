package tanks;

import utl.BuilderPath;
import utl.Direction;

public class DestroyStaff  implements Role{
	

	private AbstractTank tank;
	private BuilderPath bp;
	private boolean startPoint;

	public  DestroyStaff(AbstractTank tank) {
		
		this.tank = tank;
		bp = new BuilderPath();
		startPoint = true;
	}

	public void installData() {
		bp.installBuilderPath(tank);
	}
	
	public void script() throws Exception {
		tank.setScriptFlag(false);
		if(startPoint) {
			if(bp.setPath(tank.getY()/tank.TANK_SIZE, tank.getX()/tank.TANK_SIZE,
					tank.getBf().getStaff().getV(), tank.getBf().getStaff().getH())) {
				startPoint = false;
			} else {
				tank.stopFlag = true;
			}
		}
		int length = bp.getShortPathSize() - 1;
		if (length > 0) {
			Direction dir = bp.poopShortPathDirection();
			tank.cleanDirection(dir);
			tank.move();
			tank.getActs().add(Action.START_SCRIPT);
		}

	}

}
