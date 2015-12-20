package battle.tanks.scripts;

import java.util.Random;

import battle.tanks.AbstractTank;
import battle.tanks.Action;
import battle.Direction;

public class RandomTank implements Script {
	
	private AbstractTank tank;
	
	public  RandomTank(AbstractTank tank) {
		
		this.tank = tank;
	}

	
	public void script() throws Exception {
		tank.setScriptFlag(false);
		radarDefender();
		Random r = new Random();
		int temp;
		temp = r.nextInt(5);
		if (temp == 0) {
			temp = 1;
		}
		if (temp > 0) {
			Direction dir = Direction.values()[temp - 1];
			tank.cleanDirection(dir);
			tank.move();
			tank.getActs().add(Action.START_SCRIPT);
		}
	}
	public void installData() {
		
	}
	@Override
	public void resetScript() {

	}

	private void radarDefender() throws Exception {
		Direction dir = tank.getDirection();
		for (int i = 0, j = dir.getId(); i < 4; i++,j++) {
			if (j > 3) {
				j = 0;
			}
			dir = Direction.values()[j];
			int f = tank.searchTank(dir);
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
