package battle.tanks.scripts;

import battle.tanks.AbstractTank;
import battle.tanks.Action;

public class TotalRecall  implements Script {
	private AbstractTank tank;
	public TotalRecall(AbstractTank tank) {
		this.tank = tank;
	}

	public void script() throws Exception {
		tank.getActs().add(Action.NONE);
	}
	
	public void installData() {
		
	}
	@Override
	public void resetScript() {

	}

}
