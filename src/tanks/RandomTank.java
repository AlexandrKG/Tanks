package tanks;

import java.util.Random;
import utl.Direction;

public class RandomTank implements Role{
	
	private AbstractTank tank;
	
	public  RandomTank(AbstractTank tank) {
		
		this.tank = tank;
	}

	
	public void script() throws Exception {
		tank.setScriptFlag(false);
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
}
