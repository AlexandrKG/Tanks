package battle.tanks.scripts;

import battle.tanks.AbstractTank;
import battle.tanks.Action;
import battle.Direction;

public class DestroyStaff  implements Script {
	

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
		radarDefender();
		radarStaff();
		int length = bp.getShortPathSize() - 1;
		if (length > 0) {
			Direction dir = bp.poopShortPathDirection();
			tank.cleanDirection(dir);
			tank.move();
			tank.getActs().add(Action.START_SCRIPT);
		}

	}

	@Override
	public void resetScript() {
		startPoint = true;
		bp.resetBuilderPath();
	}

	private void radarDefender() throws Exception {
		Direction dir = tank.getDirection();
		int f = tank.searchTank(dir);
		if (f > 0) {
			tank.turn(dir);
			for (int k = f; k > 0; k--) {
				tank.fire();
			}
			tank.getActs().add(Action.START_SCRIPT);
		}
	}

	private void radarStaff() throws Exception {
		Direction dir = tank.getDirection();
		for (int i = 0, j = dir.getId(); i < 4; i++,j++) {
			if (j > 3) {
				j = 0;
			}
			dir = Direction.values()[j];
			int f = tank.searchStaff(dir);
			if(f > 0) {
				tank.turn(dir);
				for(int k = f; k>0; k--) {
					tank.fire();
				}
				tank.getActs().add(Action.START_SCRIPT);
				break;
			}
		}
	}
}
