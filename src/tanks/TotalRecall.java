package tanks;

public class TotalRecall  implements Role{
	private AbstractTank tank;
	public TotalRecall(AbstractTank tank) {
		this.tank = tank;
	}

	public void script() throws Exception {
		tank.acts.add(Action.NONE);
	}
	
	public void installData() {
		
	}


}
