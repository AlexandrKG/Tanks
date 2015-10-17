package battle;

import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JPanel;

import missile.*;
import tanks.*;
import ui.Result;
import utl.*;

import static java.lang.Thread.sleep;

public class ActionField extends JPanel implements Runnable {

    private BattleField battleField;
    private AbstractTank defender;
    private AbstractTank aggressor;
    private BattleFile battleFile;
    public Result result;
    public volatile boolean stopFlag = false;
    private long lastPressProcessed = 0;
    private ArrayList<ActionInQueue> QueueActs;
    private volatile boolean pauseFlag = false;
    private boolean repeatGameFlag = false;
    public Sound fireSound = new Sound("sounds/bomb.wav");


    public ActionField(AbstractTank aggressor,AbstractTank defender) throws Exception {
        this.aggressor = aggressor;
        this.defender = defender;
        battleField = new BattleField();
        battleFile = new BattleFile();
        initTanks();
        QueueActs = new ArrayList<>();
        result = new Result();
        initListner();
    }

    private void initListner() {
        if (!defender.isRobot()) {
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    try {
                        if (System.currentTimeMillis() - lastPressProcessed > 300) {
                            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                                QueueActs.add(new ActionInQueue(defender, Action.FIRE));
                            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                                QueueActs.add(new ActionInQueue(defender, Action.TURN_BOTTOM));
                                QueueActs.add(new ActionInQueue(defender, Action.MOVE));
                            } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                                QueueActs.add(new ActionInQueue(defender, Action.TURN_TOP));
                                QueueActs.add(new ActionInQueue(defender, Action.MOVE));
                            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                                QueueActs.add(new ActionInQueue(defender, Action.TURN_RIGHT));
                                QueueActs.add(new ActionInQueue(defender, Action.MOVE));
                            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                                QueueActs.add(new ActionInQueue(defender, Action.TURN_LEFT));
                                QueueActs.add(new ActionInQueue(defender, Action.MOVE));
                            } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                                if (pauseFlag) {
                                    pauseFlag = false;
                                    System.err.println("PAUSE START");
                                } else {
                                    pauseFlag = true;
                                    System.err.println("PAUSE END");
                                }
                            }
                            lastPressProcessed = System.currentTimeMillis();
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }
    }

    public void restartActionField(AbstractTank aggressor,AbstractTank defender) throws Exception {
        QueueActs.clear();
        this.aggressor = aggressor;
        this.defender = defender;
        battleField.restartButtleField();
        initTanks();
        stopFlag = false;
        repeatGameFlag = false;
        result.startNewGame();
    }

    public void repeatActionField() {
        QueueActs.clear();
        battleFile.readBattle(this);
        aggressor.setRole(new TotalRecall(aggressor));
        defender.setRole(new TotalRecall(defender));
        result.startNewGame();
        stopFlag = false;
        repeatGameFlag = true;
    }

    private void initTanks() {

        int vDefender;
        int hDefender;

        do {
            vDefender = randomInit(battleField.getDimentionY());
            hDefender = randomInit(battleField.getDimentionX());
        } while (hDefender == battleField.getStaff().getH()
                && vDefender == battleField.getStaff().getV());
        int vAggressor;
        int hAggressor;
        do {
            vAggressor = randomInit(battleField.getDimentionY());
            hAggressor = randomInit(battleField.getDimentionX());

        } while ((vAggressor == vDefender && hAggressor == hDefender)
                || (hAggressor == battleField.getStaff().getH()
                && vAggressor == battleField.getStaff().getV()));

        battleField.cleanQuadrant(vDefender, hDefender);
        battleField.cleanQuadrant(vAggressor, hAggressor);

        int x = hDefender * battleField.RECT_SIZE;
        int y = vDefender * battleField.RECT_SIZE;
        if (this.defender == null) {
            this.defender = new T34(battleField, x, y, Direction.TOP);
        } else {
            this.defender.installTank(battleField, x, y, Direction.TOP);
        }
        this.defender.setRole(new ManuaControl(this.defender));

        x = hAggressor * battleField.RECT_SIZE;
        y = vAggressor * battleField.RECT_SIZE;
        if (this.aggressor == null) {
            this.aggressor = new BT7(battleField, x, y, Direction.TOP);
            this.aggressor.setRole(new RandomTank(this.aggressor));
        } else {
            this.aggressor.installTank(battleField, x, y, Direction.TOP);
        }
        defender.setTarget(this.aggressor);
        aggressor.setTarget(defender);
        aggressor.getRole().installData();
        defender.getRole().installData();
        if (defender.getDestroyableObst().contains("E")) {
            defender.getDestroyableObst().remove("E");
        }
        aggressor.setDestroyed(false);
        defender.setDestroyed(false);
        aggressor.setStrRole("aggressor");
        defender.setStrRole("defender");
        aggressor.stopFlag = false;
        defender.stopFlag = false;
    }

    private int randomInit(int bound) {
        Random r = new Random();
        return r.nextInt(bound);
    }

    public void writeStartData() {

        battleFile.removeOldData();

        StringBuilder builder = new StringBuilder();
        builder.append("#BattleField\r\n");
        for (String[] strArr : battleField.getBattleField()) {
            builder.append(Arrays.toString(strArr));
            builder.append("\r\n");
        }

        builder.append("#aggressor\r\n");
        builder.append(aggressor.getClass().getSimpleName());
        builder.append("\r\n");
        builder.append("[" + aggressor.getX() + " ; " + aggressor.getY() + "]\r\n");
        builder.append(aggressor.getDirection().toString());
        builder.append("\r\n");

        builder.append("#defender\r\n");
        builder.append(defender.getClass().getSimpleName());
        builder.append("\r\n");
        builder.append("[" + defender.getX() + " ; " + defender.getY() + "]\r\n");
        builder.append(defender.getDirection().toString());
        builder.append("\r\n");

        battleFile.write(builder.toString());
    }

    private void startTanks() {

        new Thread(new TankMove(aggressor, this)).start();
        new Thread(new TankMove(defender, this)).start();
        new Thread(aggressor).start();
        new Thread(defender).start();

        new Thread(new BulletFly(aggressor, this)).start();
        new Thread(new BulletFly(defender, this)).start();

    }

    private ActionInQueue getQueueElement() {
        ActionInQueue result = null;
        if (QueueActs != null && !QueueActs.isEmpty()) {
            if(repeatGameFlag) {
                result = elementRepeatGame();
            } else {
                result = elementNewGame();
            }
        }
        return result;
    }

    private ActionInQueue elementNewGame() {
        ActionInQueue result = null;
        for (int i = 0; i < QueueActs.size(); i++) {
            result = QueueActs.get(i);
            if (!result.getTank().isTankMooving()) {
                QueueActs.remove(i);
                break;
            }
            result = null;
        }
        return result;
    }

    private ActionInQueue elementRepeatGame() {
        ActionInQueue result = null;
        if (!defender.isTankMooving() && !aggressor.isTankMooving()) {
            result = QueueActs.get(0);
            QueueActs.remove(0);
        }
        return result;
    }

    private void setQueueElement(AbstractTank tank) throws Exception {
        Action act = tank.setUp();
        if (act != null && act != Action.NONE) {
            QueueActs.add(new ActionInQueue(tank, act));
        }

    }

    private void updateQueue() throws Exception {
        if (QueueActs.isEmpty()) {
            setQueueElement(aggressor);
            setQueueElement(defender);
        }
    }

    private void startQueueAction() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean runFlag = true;
                ActionInQueue element = null;
                while (runFlag && !stopFlag) {
                    if (!pauseFlag) {
                        try {
                            runFlag = !aggressor.isDestroyed() && !defender.isDestroyed()
                                    && !battleField.getStaff().isDestroyed();
                            if (runFlag && !stopFlag) {
                                updateQueue();
                                element = getQueueElement();
                                if (element != null) {
                                    processAction(element.getAct(), element.getTank());
                                }
                            }
                            sleep(100);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println("ActionField stopFlag: " + stopFlag);
                System.out.println("aggressor stopFlag: " + aggressor.stopFlag);
                System.out.println("defender stopFlag: " + defender.stopFlag);
                aggressor.stopFlag = true;
                defender.stopFlag = true;
                stopFlag = true;
            }
        }).start();

    }

    public void run() {
        startQueueAction();
        startTanks();
        while (!stopFlag) {
            try {
                this.requestFocus();
                repaint();
                sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        battleFile.write(Action.STOP_GAME + "\r\n");
        battleFile.close();
        result.gameOver(defender.isDestroyed(), aggressor.isDestroyed(),
                battleField.getStaff().isDestroyed());
    }

    private AbstractTank returnOpponent(AbstractTank tank) {
        AbstractTank opponent = null;
        if (tank == aggressor) {
            opponent = defender;
        } else if (tank == defender) {
            opponent = aggressor;
        }
        return opponent;
    }

    private void processAction(Action a, AbstractTank tank) throws Exception {
        writeFile(tank, a);
        if (a == Action.MOVE) {
            processMove(tank);
        } else if (a == Action.TURN_TOP || a == Action.TURN_BOTTOM || a == Action.TURN_LEFT
                || a == Action.TURN_RIGHT) {
            processTurn(tank, a);
        } else if (a == Action.FIRE) {
            processFire(tank);
            Thread.sleep(300);
        } else if (a == Action.START_SCRIPT) {
            tank.setScriptFlag(true);
        } else if (a == Action.STOP_GAME) {
            stopFlag = true;
        }
    }

    public boolean isObstacle(AbstractTank tank, AbstractTank opponent) {
        int step = battleField.RECT_SIZE;
        int[] coord = new int[2];
        coord[0] = tank.getX() + step / 2;
        coord[1] = tank.getY() + step / 2;
        Interceptions.updateCoordinates(coord, step, tank.getDirection());
        int i = coord[1] / step;
        int j = coord[0] / step;
        int xO = opponent.getX() / step;
        int yO = opponent.getY() / step;
        if (i >= 0 && i < battleField.getDimentionY() && j >= 0
                && j < battleField.getDimentionX()) {
            String obst = battleField.scanQuadrant(i, j).trim();
            if (!obst.trim().isEmpty()) {
                if (obst.equals("W") && tank.isUnderwater()) {
                    return false;
                } else {
                    return true;
                }
            } else if ((i == yO && j == xO)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkTankLimits(AbstractTank tank, AbstractTank opponent) {
        if ((tank.getDirection() == Direction.TOP && tank.getY() <= 0)
                || (tank.getDirection() == Direction.BOTTOM
                && tank.getY() >= (battleField.getDimentionY() - 1) * battleField.RECT_SIZE)
                || (tank.getDirection() == Direction.LEFT && tank.getX() <= 0)
                || (tank.getDirection() == Direction.RIGHT
                && tank.getX() >= (battleField.getDimentionX() - 1) * battleField.RECT_SIZE)) {
            return false;
        } else if (isObstacle(tank, opponent)) {
            return false;
        }
        return true;
    }

    public void processMove(AbstractTank tank) throws Exception {
        Direction direction = tank.getDirection();
        AbstractTank opponent = returnOpponent(tank);
        if (!checkTankLimits(tank, opponent)) {
            System.out.println(tank.getStrRole() + "[illegal move] direction: " + direction
                    + " tankX: " + tank.getX() + ", tankY: " + tank.getY());
            return;
        }
        tank.setTankMooving(true);
    }

    private void processTurn(AbstractTank tank, Action act) throws Exception {
        turn(tank, act);
    }

    private void turn(AbstractTank tank, Action act) throws Exception {
        Direction dir = actionToDir(act);
        if (tank.getDirection() != dir) {
            tank.setDirection(dir);
        }
        System.out.println(tank.getStrRole() + " turn to: " + dir.toString());
    }

    private Direction actionToDir(Action act) {
        Direction dir;
        if (act == Action.TURN_TOP) {
            dir = Direction.TOP;
        } else if (act == Action.TURN_BOTTOM) {
            dir = Direction.BOTTOM;
        } else if (act == Action.TURN_LEFT) {
            dir = Direction.LEFT;
        } else {
            dir = Direction.RIGHT;
        }
        return dir;
    }

    private void writeFile(AbstractTank tank, Action act) throws Exception {
        battleFile.write("#" + tank.getStrRole() + "\r\n");
        battleFile.write(act + "\r\n");
    }

    private void processFire(AbstractTank tank) throws Exception {
        Bullet bullet = new Bullet(tank);
        if (tank == aggressor) {
            aggressor.getBullets().add(bullet);
        } else if (tank == defender) {
            defender.getBullets().add(bullet);
        }

    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        aggressor.draw(g);
        defender.draw(g);
        battleField.draw(g);
        for (Bullet b : aggressor.getBullets()) {
            b.draw(g);
        }
        for (Bullet b : defender.getBullets()) {
            b.draw(g);
        }
        aggressor.getDetonation().draw(g);
        defender.getDetonation().draw(g);
    }

    public BattleField getBattleField() {
        return battleField;
    }

    public AbstractTank getDefender() {
        return defender;
    }

    public AbstractTank getAggressor() {
        return aggressor;
    }

    public void setAggressor(AbstractTank aggressor) {
        this.aggressor = aggressor;
    }

    public ArrayList<ActionInQueue> getQueueActs() {
        return QueueActs;
    }

}
