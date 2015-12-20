package battle.tanks.scripts;


import battle.tanks.AbstractTank;
import battle.tanks.Action;

public class ManualControl implements Script {
    private AbstractTank tank;
    public ManualControl(AbstractTank tank) {
        this.tank = tank;
        this.tank.setRobot(false);
    }
    @Override
    public void script() throws Exception {
        tank.getActs().add(Action.NONE);
    }

    @Override
    public void installData() {

    }
    @Override
    public void resetScript() {

    }
}
