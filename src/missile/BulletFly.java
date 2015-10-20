package missile;


import battle.ActionField;
import battle.BattleField;
import tanks.AbstractTank;
import tanks.Tiger;
import utl.Direction;

import static java.lang.Thread.sleep;

public class BulletFly  implements Runnable{

    protected ActionField af;
    protected BattleField battleField;
    protected AbstractTank target;
    protected AbstractTank hunter;
    protected final int STEP = 1;
    protected final int SPEED = 5;
    protected int rectangleSize;

    public BulletFly(AbstractTank tank,ActionField af) {
        this.hunter = tank;
        this.af = af;
        this.battleField = af.getBattleField();
        target = returnOpponent(hunter);
        rectangleSize = battleField.RECT_SIZE;
    }


    @Override
    public void run() {
        try {
            while(true) {
                if(hunter.getBullets() != null) {
                    for (Bullet b : hunter.getBullets()) {
                        updateBulletCoordinates(b, STEP*3);
                        if (processInterception(b)) {
                            hunter.getDetonation().initDetonation(b.getX(),b.getY(),b.getDirection());
                            af.fireSound.play();
                            hunter.getBullets().remove(b);
                            b.destroy();
                        }
                    }
                    sleep(SPEED*3);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AbstractTank returnOpponent(AbstractTank tank) {
        AbstractTank opponent = null;
        if (tank == af.getAggressor()) {
            opponent = af.getDefender();
        } else if (tank == af.getDefender()) {
            opponent = af.getAggressor();
        }
        return opponent;
    }

    private void updateBulletCoordinates(Bullet bullet,int step) {

            if (bullet.getDirection() == Direction.TOP) {
                bullet.updateY(-step);
            } else if (bullet.getDirection() == Direction.BOTTOM) {
                bullet.updateY(step);
            } else if (bullet.getDirection() == Direction.LEFT) {
                bullet.updateX(-step);
            } else {
                bullet.updateX(step);
            }
    }

    private boolean processInterception(Bullet bullet){
        String coordinates = getQuadrant(bullet.getX(), bullet.getY());
        int yQ = Integer.parseInt(coordinates.split("_")[0]);
        int xQ = Integer.parseInt(coordinates.split("_")[1]);

        if (xQ >= 0 && xQ < battleField.getDimentionX() && yQ >= 0
                && yQ < battleField.getDimentionY()) {
            String obst = battleField.scanQuadrant(yQ, xQ);
            if (!obst.trim().isEmpty()) {
                if (hunter.canDestroy(obst.trim())) {
                    battleField.cleanQuadrant(yQ, xQ);
                    return true;
                } else if(obst.trim().equals("R")) {
                    return true;
                }
            } else {
                return tankDestroy(bullet, target);
            }
        }
        return false;
    }

    private String getQuadrant(int x, int y) {
        return y / rectangleSize + "_" + x / rectangleSize;
    }

    private boolean tankDestroy(Bullet bullet, AbstractTank tank){
        if (tankInterception(bullet, tank)) {
            if(tank instanceof Tiger) {
                ((Tiger) tank).updateArmor(-1);
                if(((Tiger) tank).getArmor() < 0) {
                    tank.destroy();
                }
            } else {
                tank.destroy();
            }
            return true;
        }
        return false;
    }

    private boolean tankInterception(Bullet bullet, AbstractTank tank) {
        if (tank == null) {
            return false;
        }
        Direction direction = bullet.getDirection();
        int bulletSize = bullet.BULLET_SIZE;
        int x = bullet.getX();
        int y = bullet.getY();

        if (direction == Direction.TOP) {
            if (((x + bulletSize) > tank.getX())
                    && (x < (tank.getX() + rectangleSize))) {
                if (y <= (tank.getY() + rectangleSize) && (y + bulletSize) > tank.getY()) {
                    return true;
                }
            }
        } else if (direction == Direction.BOTTOM) {
            if (((x + bulletSize) > tank.getX())
                    && (x < (tank.getX() + rectangleSize))) {
                if ((y + bulletSize) >= tank.getY() && y < (tank.getY() + rectangleSize)) {
                    return true;
                }
            }
        } else if (direction == Direction.LEFT) {
            if (((y + bulletSize) > tank.getY())
                    && (y < (tank.getY() + rectangleSize))) {
                if (x <= (tank.getX() + rectangleSize) && (x + bulletSize) >= tank.getX()) {
                    return true;
                }
            }
        } else {
            if (((y + bulletSize) > tank.getY())
                    && (y < (tank.getY() + rectangleSize))) {
                if ((x + bulletSize) >= tank.getX() && x < (tank.getX() + rectangleSize)) {
                    return true;
                }
            }
        }

        return false;
    }
}
