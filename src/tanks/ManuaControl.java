package tanks;


public class ManuaControl implements Role {
    private AbstractTank tank;
    public ManuaControl(AbstractTank tank) {
        this.tank = tank;
        this.tank.setRobot(false);
    }
    @Override
    public void script() throws Exception {
        tank.acts.add(Action.NONE);
    }

    @Override
    public void installData() {

    }
}
