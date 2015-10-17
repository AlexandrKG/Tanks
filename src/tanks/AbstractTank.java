package tanks;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import missile.Bullet;
import battle.BattleField;
import missile.Detonation;
import utl.Direction;
import utl.Interceptions;



public abstract class AbstractTank  implements Tank,Runnable {
	public final static int TANK_SIZE = 64;
	public volatile boolean stopFlag = false;
	protected Direction direction;
	protected int x;
	protected int y;
	private boolean destroyed;
	protected int speed;
	protected Color tankColor;
	protected Color towerColor;
	protected BattleField bf;
	protected Queue<Action> acts;
	protected AbstractTank target;
	protected ArrayList<String> destroyableObst;
	protected Image[] images;
	protected boolean underwater;
	protected Role role;
	protected String strRole;
	ArrayBlockingQueue<Bullet> bullets;
	protected boolean robot;
	protected boolean scriptFlag;
	protected boolean tankMooving;
	private Detonation detonation;

	public AbstractTank() {
		
		this.destroyed = false;
		this.acts = new LinkedBlockingQueue<>();
		this.destroyableObst = new ArrayList<>();
		bullets = new ArrayBlockingQueue<>(100);
		stopFlag = false;
		robot = true;
		scriptFlag = true;
		tankMooving = false;
		detonation = new Detonation();
	}

	public AbstractTank(BattleField bf, int x, int y, Direction direction) {
		this();
		
		this.bf = bf;
		this.x = x;
		this.y = y;
		this.direction = direction;
	}

	public Direction getDirection() {
		return direction;
	}

	public int getSpeed() {
		return speed;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}

	public BattleField getBf() {
		return bf;
	}

	public void setBf(BattleField bf) {
		this.bf = bf;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}

	public Detonation getDetonation() {
		return detonation;
	}

	public Queue<Action> getActs() {
		return acts;
	}

	public ArrayList<String> getDestroyableObst() {
		return destroyableObst;
	}

	public Role getRole() {
		return role;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}

	public AbstractTank getTarget() {
		return target;
	}

	public void setTankMooving(boolean tankMooving) {
		this.tankMooving = tankMooving;
	}

	public boolean isRobot() {
		return robot;
	}

	public void setRobot(boolean robot) {
		this.robot = robot;
	}

	public boolean isTankMooving() {
		return tankMooving;
	}

	public boolean isUnderwater() {
		return underwater;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public void setScriptFlag(boolean flag) {
		this.scriptFlag = flag;
		System.out.println(" scriptFlag SET: " + scriptFlag);
	}

	public void setStrRole(String strRole) {
		this.strRole = strRole;
	}

	public String getStrRole() {
		return strRole;
	}

	public ArrayBlockingQueue<Bullet> getBullets() {
		return bullets;
	}

	public void installTank(BattleField bf, int x, int y,Direction direction) {
		
		this.bf = bf;
		this.x = x;
		this.y = y;
		this.direction = direction;
	}
	
	public void updateX(int x) {
		this.x += x;
	}

	public void updateY(int y) {
		this.y += y;
	}

	public void move() throws Exception {
		acts.add(Action.MOVE);
		System.out.println(strRole + " GOING TO MOVE");
	}

	public void turn(Direction direction) throws Exception {
		if(acts.add(DirToAct(direction))) {
			System.out.println(strRole + " GOING TO TURN: "+ direction.toString());
		}
	}

	public Action DirToAct(Direction direction) {
		if (direction == Direction.TOP) {
			return Action.TURN_TOP;
		} else if (direction == Direction.BOTTOM) {
			return Action.TURN_BOTTOM;
		} else if (direction == Direction.LEFT) {
			return Action.TURN_LEFT;
		} else {
			return Action.TURN_RIGHT;
		}
	}

	public void fire() {
		acts.add(Action.FIRE);
		System.out.println(strRole + " GOING TO FIRE ");
	}

	public void cleanFire(Direction dir) throws Exception {
		int numberFire = searchTankNear(dir);	
		if (searchBricks(dir)) {
			numberFire++;
		}
		while (numberFire > 0) {
			fire();
			numberFire--;
		}		
	}

	public void cleanDirection(Direction dir) throws Exception {
		    turn(dir);
			cleanFire(dir);

	}

	public boolean canDestroy(String obst) {
		for(String str:destroyableObst) {
			if(str.equals(obst)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean searchBricks(Direction dir) {
		int step = bf.RECT_SIZE;
		int[] coord = new int[2];
		coord[0] = x + step / 2;
		coord[1] = y + step / 2;

		if ((coord[0] > 0 && coord[0] < bf.BF_WIDTH)
				&& (coord[1] > 0 && coord[1] < bf.BF_HEIGHT)) {
			Interceptions.updateCoordinates(coord, step, dir);
			if (coord[1] < 0 || coord[0] < 0) {
				return false;
			}
			int i = coord[1] / step;
			int j = coord[0] / step;

			if (i >= 0 && i < bf.getDimentionY() && j >= 0
					&& j < bf.getDimentionX()) {
				String obst = bf.scanQuadrant(i, j);
				if(!obst.trim().isEmpty() &&  canDestroy(obst)) {
					return true;
				}
			}
		}
		return false;
	}

	public void updateTankCoordinates(int step) {
		if (direction == Direction.TOP) {
			updateY(-step);
		} else if (direction == Direction.BOTTOM) {
			updateY(step);
		} else if (direction == Direction.LEFT) {
			updateX(-step);
		} else {
			updateX(step);
		}
	}

	public void moveScript() throws Exception {
		role.script();
	}

	public void draw(Graphics g) {
		if (!destroyed) {
			g.drawImage(this.images[getDirection().getId()], this.getX(), this.getY(),
					new ImageObserver() {
						@Override
						public boolean imageUpdate(Image img, int infoflags, int x, int y,
												   int width, int height) {
							return false;
						}
					});
		}
	}

	public void destroy(){
		x = -100;
		y = -100;
		destroyed = true;
	}

	private int searchTank() {
		int result = 0;
		if (Interceptions.targetExist(this, target)) {
			result++;
			if(target instanceof Tiger) {
				result += ((Tiger) target).getArmor();
			}			
		}
		return result;
	}

	private int searchTankNear(Direction dir) {
		int result = 0;
		if (Interceptions.targetNearExist(this, target, dir)) {
			result++;
			if(target instanceof Tiger) {
				result += ((Tiger) target).getArmor();
			}			
		}
		return result;
	}

	public void setTarget(AbstractTank target) {
		this.target = target;
	}
	
	public Action getNextAct() {
		Action act = null;
		if (acts != null && !acts.isEmpty()) {
			act = acts.poll();
		}
		return act;
	}

	@Override
	public void run() {
		while(!destroyed && !stopFlag) {
			try {
				scriptRun();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void scriptRun()  throws Exception {
		if (scriptFlag && acts != null && acts.isEmpty()) {
				moveScript();
		}
	}

	public Action setUp()  throws Exception {
		return getNextAct();
	}

}
