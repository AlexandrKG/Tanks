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

	public BattleField() {
		loadPictures = new LoadPictures();
		battleField = new String[RECT_NUMBER_Y][RECT_NUMBER_X];
		obst = new ArrayList<>();
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
		initStaff();
	}

	public void setObstacles() {
		for (int v = 0; v < RECT_NUMBER_Y; v++) {
			for (int h = 0; h < RECT_NUMBER_X; h++) {
				initObstacles(battleField[v][h], v, h);
			}
		}
	}

	private String bfRandom() {
		Random r = new Random();
		int i = r.nextInt(4);
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
		Random r = new Random();
		int h = r.nextInt(RECT_NUMBER_X);
		int v = r.nextInt(RECT_NUMBER_Y);
		cleanQuadrant(v, h);
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

}
