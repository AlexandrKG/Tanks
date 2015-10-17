package tanks;

import battle.ActionField;
import battle.BattleField;
import utl.Direction;

public class TankMove implements Runnable{
    protected AbstractTank tank;
    protected ActionField af;
    protected BattleField battleField;

    public TankMove(AbstractTank tank,ActionField af) {
        this.tank = tank;
        this.af = af;
        this.battleField = af.getBattleField();
    }
    @Override
    public void run() {
        try {
            while(!tank.stopFlag) {
                if (tank.isTankMooving()) {
                    moveOneSquare(tank);
                }
                Thread.sleep(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void moveOneSquare(AbstractTank tank) throws Exception {
        tank.setTankMooving(true);
        System.out.println(tank.getStrRole() + " MOVE to " + tank.getDirection().toString());

        int step = 1;
        int covered = 0;
        while(covered < 64 && !tank.isDestroyed()) { //BattleField.RECT_SIZE
            updateTankCoordinates(tank,1);
//            System.out.println(tank.getStrRole() + "[ move to direction: "
//                    + tank.getDirection() + " tankX: " + tank.getX()
//                    + ", tankY: " + tank.getY());
            covered += step;
            Thread.sleep(tank.getSpeed());
        }
        tank.setTankMooving(false);
    }

    private void updateTankCoordinates(AbstractTank tank, int step) {
        if (tank.getDirection() == Direction.TOP) {
            tank.updateY(-step);
        } else if (tank.getDirection() == Direction.BOTTOM) {
            tank.updateY(step);
        } else if (tank.getDirection() == Direction.LEFT) {
            tank.updateX(-step);
        } else {
            tank.updateX(step);
        }
    }

}
