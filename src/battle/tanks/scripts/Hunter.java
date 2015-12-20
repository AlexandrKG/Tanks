package battle.tanks.scripts;


import battle.tanks.AbstractTank;
import battle.tanks.Action;
import battle.Direction;

public class Hunter implements Script {
    private AbstractTank tank;
    private BuilderPath bp;
    private boolean startPoint;
    private  int stepRecount;

    public Hunter(AbstractTank tank) {

        this.tank = tank;
        bp = new BuilderPath();
        startPoint = true;
        stepRecount = 3;
    }

    @Override
    public void script() throws Exception {
        tank.setScriptFlag(false);
        if(startPoint) {
            if(bp.setPath(tank.getY()/tank.TANK_SIZE, tank.getX()/tank.TANK_SIZE,
                    tank.getTarget().getY()/tank.TANK_SIZE, tank.getTarget().getX()/tank.TANK_SIZE)) {
                startPoint = false;
            } else {
                tank.stopFlag = true;
            }
        }
        radarTarget();
        int length = bp.getShortPathSize() - 1;
        if (length > 0) {
            Direction dir = bp.poopShortPathDirection();
            tank.cleanDirection(dir);
            tank.move();
            tank.getActs().add(Action.START_SCRIPT);
            stepRecount--;
            if(stepRecount<=0) {
                resetScript();
            }
        }
    }

    @Override
    public void installData() {
        bp.installBuilderPath(tank);
    }

    @Override
    public void resetScript() {
        startPoint = true;
        stepRecount = 3;
        bp.resetBuilderPath();
    }

    private void radarTarget() throws Exception {
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
