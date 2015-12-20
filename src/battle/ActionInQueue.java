package battle;


import battle.tanks.AbstractTank;
import battle.tanks.Action;

public class ActionInQueue {

    private AbstractTank tank;
    private Action act;

    public ActionInQueue(AbstractTank tank, Action act) {
        this.tank = tank;
        this.act = act;
    }

    public AbstractTank getTank() {
        return tank;
    }

    public Action getAct() {
        return act;
    }

    public void setTank(AbstractTank tank) {
        this.tank = tank;
    }

    public void setAct(Action act) {
        this.act = act;
    }

}
