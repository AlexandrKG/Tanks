package battle;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import landscape.*;
import utl.Drawable;
import utl.LoadPictures;

public class BattleField implements Drawable {

	final boolean COLORDED_MODE = false;
	public final static int RECT_SIZE = 64;
	public final static int RECT_NUMBER_X = 9;
	public final static int RECT_NUMBER_Y = 9;
	public final static int BF_WIDTH = RECT_NUMBER_X * RECT_SIZE;
	public final static int BF_HEIGHT = RECT_NUMBER_Y * RECT_SIZE;

	private String[][] battleField;
	private List<AbstractObstacle> obst;
	private Eagle staff;
	private LoadPictures loadPictures;
	private int[][] staffBlocks;
	private int[][] aggressorStartPosition;

	public BattleField() {
		loadPictures = new LoadPictures();
		battleField = new String[RECT_NUMBER_Y][RECT_NUMBER_X];
		obst = new ArrayList<>();
		staffBlocks = new int[5][2];
		aggressorStartPosition= new int[1][2];
		staff = new Eagle();
		initButtleField();
	}

	public void restartButtleField() {
		clearButtleField();
		initButtleField();
	}

	public void clearButtleField() {
		for (int v = 0; v < RECT_NUMBER_Y; v++) {
			Arrays.fill(battleField[v], " ");
		}
		obst.clear();
	}

	private void initButtleField() {
		for (int v = 0; v < RECT_NUMBER_Y; v++) {
			for (int h = 0; h < RECT_NUMBER_X; h++) {
				battleField[v][h] = bfRandom();
				initObstacles(battleField[v][h], v, h);
			}
		}
		setFreePath(randomInit(3));
		initStaff();
	}

	public void setObstacles() {
		for (int v = 0; v < RECT_NUMBER_Y; v++) {
			for (int h = 0; h < RECT_NUMBER_X; h++) {
				initObstacles(battleField[v][h], v, h);
			}
		}
	}

	private int randomInit(int bound) {
		Random r = new Random();
		return r.nextInt(bound);
	}

	private String bfRandom() {
		int i = randomInit(4);
		if(i == 0) {
			i = 3;
		}
		if (i == 0) {
			return " ";
		} else if (i == 1) {
			return "B";
		} else if (i == 2) {
			return "R";
		} else {
			return "W";
		}
	}

	private void initStaff() {
		setStaffBlocks();
		int h = RECT_NUMBER_X/2;
		int v = RECT_NUMBER_Y - 1;
		battleField[v][h] = "E";

		int y = v * RECT_SIZE;
		int x = h * RECT_SIZE;
		staff.setH(h);
		staff.setV(v);
		staff.setX(x);
		staff.setY(y);
		staff.setDestroyed(false);
		int index = v * RECT_NUMBER_X + h;
		obst.set(index, staff);
	}

	private void setStaffBlocks(){
		int i = 0;
		for (int v = RECT_NUMBER_Y-2; v < RECT_NUMBER_Y; v++) {
			for (int h = RECT_NUMBER_X/2-1; h < RECT_NUMBER_X/2+2; h++) {
				cleanQuadrant(v, h);
				if(!(v==RECT_NUMBER_Y-1 && h==RECT_NUMBER_X/2)) {
					staffBlocks[i][0] = v;
					staffBlocks[i][1] = h;
					i++;
				}
			}
		}

	}

	private void setFreePath(int pathVersion) {

		switch (pathVersion) {
			case 0:
				cleanHLine(0,RECT_NUMBER_X,0);
				cleanHLine(0,RECT_NUMBER_X,3);
				cleanHLine(0,RECT_NUMBER_X/2,6);
				cleanVLine(1, 3, RECT_NUMBER_X - 1);
				cleanVLine(4,6,0);
				aggressorStartPosition[0][0] = 0;
				aggressorStartPosition[0][1] = 0;
				break;
			case 1:
				cleanHLine(0,RECT_NUMBER_X,0);
				cleanHLine(0,RECT_NUMBER_X,3);
				cleanHLine(RECT_NUMBER_X/2 + 1,RECT_NUMBER_X,6);
				cleanVLine(1,3,0);
				cleanVLine(4,6,RECT_NUMBER_X - 1);
				aggressorStartPosition[0][0] = 0;
				aggressorStartPosition[0][1] = RECT_NUMBER_X - 1;
				break;
			case 2:
				cleanVLine(0, 4, 0);
				cleanHLine(1, 4, 3);
				cleanVLine(0, 3, 3);
				cleanHLine(4, RECT_NUMBER_X, 0);
				cleanVLine(0, 4, RECT_NUMBER_X - 1);
				cleanHLine(5, RECT_NUMBER_X - 1, 3);
				cleanVLine(4, 6, 5);
				cleanHLine(1, 5, 5);
				cleanVLine(5, RECT_NUMBER_X, 1);
				cleanHLine(2, 3, RECT_NUMBER_X - 1);
				aggressorStartPosition[0][0] = 0;
				aggressorStartPosition[0][1] = 0;
				break;
		}

	}

	private void cleanHLine(int start, int end, int line) {
		for (int h = start; h < end; h++) {
			cleanQuadrant(line, h);
		}
	}

	private void cleanVLine(int start, int end, int column) {
		for (int v = start; v < end; v++) {
			cleanQuadrant(v, column);
		}
	}

	private void initObstacles(String data, int v, int h) {
		String str = data.trim();
		int y = v * RECT_SIZE;
		int x = h * RECT_SIZE;

		if (str.equals("B")) {
			Brick br = new Brick(loadPictures.getBrickImage());
			br.setX(x);
			br.setY(y);
			obst.add(br);
		} else if (str.equals("R")) {
			Rock rc = new Rock(loadPictures.getRockImage());
			rc.setX(x);
			rc.setY(y);
			obst.add(rc);
		} else if (str.equals("W")) {
			Water wt = new Water(loadPictures.getWaterImage());
			wt.setX(x);
			wt.setY(y);
			obst.add(wt);
		} else if (str.equals("E")) {
			staff = new Eagle();
			staff.setX(x);
			staff.setY(y);
			staff.setH(h);
			staff.setV(v);
			obst.add(staff);
		} else {
			obst.add(null);
		}
	}

	public AbstractObstacle getObst(int v, int h) {
		int index = v * RECT_NUMBER_X + h;
		return obst.get(index);
	}

	public String scanQuadrant(int v, int h) {
		String data = battleField[v][h];
		return data;
	}

	public void cleanQuadrant(int v, int h) {
		battleField[v][h] = " ";
		AbstractObstacle ao = getObst(v, h);
		if (ao != null) {
			ao.destroy();
		}
	}

	public int getDimentionX() {
		return battleField[0].length;
	}

	public int getDimentionY() {
		return battleField.length;
	}

	public void draw(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		Composite org = g2D.getComposite();
		Composite translucent = AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, 0.0f);
		g2D.setComposite(translucent);

		int i = 0;
		Color cc;
		for (int v = 0; v < RECT_NUMBER_Y; v++) {
			for (int h = 0; h < RECT_NUMBER_X; h++) {
				if (COLORDED_MODE) {
					if (i % 2 == 0) {
						cc = new Color(252, 241, 177);
					} else {
						cc = new Color(233, 243, 255);
					}
				} else {
					cc = new Color(192, 192, 192); // 180
				}
				i++;
				g.setColor(cc);
				g.fillRect(h * RECT_SIZE, v * RECT_SIZE, RECT_SIZE, RECT_SIZE);
			}
		}
		g2D.setComposite(org);
		for (int j = 0; j < RECT_NUMBER_Y; j++) {
			for (int k = 0; k < RECT_NUMBER_X; k++) {
				AbstractObstacle ao = getObst(j, k);
				if (ao != null) {
					ao.draw(g);
				}
			}
		}

	}

	public Eagle getStaff() {
		return staff;
	}

	public List<AbstractObstacle> getObst() {
		return obst;
	}

	public String[][] getBattleField() {
		return battleField;
	}

	public int[][] getStaffBlocks() {
		return staffBlocks;
	}

	public int[][] getAggressorStartPosition() {
		return aggressorStartPosition;
	}
}
