package utl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;

import battle.ActionInQueue;
import battle.Direction;
import battle.tanks.AbstractTank;
import battle.tanks.Action;
import battle.tanks.BT7;
import battle.tanks.T34;
import battle.tanks.Tiger;
import battle.ActionField;
import battle.BattleField;

public class BattleFile {

	private String fileName;
	private ActionField af;
	private FileOutputStream fos;
	OutputStreamWriter fileWriter;

	public BattleFile() {
		fileName = "LastBattle.txt";
//		try {
//			fos = new FileOutputStream(fileName, true);
//			fileWriter = new OutputStreamWriter(fos);
//
//		}  catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	public void write(String data) {
		try {
			fileWriter.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			fileWriter.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
//	public void write(String data) {
//		try (FileOutputStream fos = new FileOutputStream(fileName, true);
//				OutputStreamWriter fileWriter = new OutputStreamWriter(fos)) {
//					fileWriter.write(data);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	public void readBattle(ActionField af) {
		this.af = af;
		try (FileInputStream fis = new FileInputStream(fileName);
				InputStreamReader reader = new InputStreamReader(fis);
				BufferedReader bufferedReader = new BufferedReader(reader)) {

			initButtleField(bufferedReader);
			initTanks(bufferedReader);
			loadActions(bufferedReader);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initButtleField(BufferedReader bufferedReader) {
		try {
			String str = bufferedReader.readLine();
			if (str.trim().equals("#BattleField")) {
				af.getBattleField().clearButtleField();
				for (int i = 0; i < BattleField.RECT_NUMBER_Y; i++) {
					str = bufferedReader.readLine();
					str = str.substring(1, str.length() - 1);
					af.getBattleField().getBattleField()[i] = str.split(",");
					System.out.println(Arrays.toString(af.getBattleField()
							.getBattleField()[i]));
				}
				af.getBattleField().setObstacles();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initTanks(BufferedReader bufferedReader) {
		String str = null;
		try {
			str = bufferedReader.readLine();
			if (str.trim().equals("#aggressor")) {
				str = bufferedReader.readLine();
				af.setAggressor(createTank(str.trim()));
				str = bufferedReader.readLine();
				str = str.substring(1, str.length() - 1);
				String tmp = str.split(";")[0];
				af.getAggressor().setX(Integer.valueOf(tmp.trim()));
				tmp = str.split(";")[1];
				af.getAggressor().setY(Integer.valueOf(tmp.trim()));
				str = bufferedReader.readLine();
				af.getAggressor().setDirection(Direction.valueOf(str.trim()));
				af.getAggressor().setStrRole("aggressor");
				af.getAggressor().setTarget(af.getDefender());
				af.getAggressor().setBf(af.getBattleField());
				af.getAggressor().stopFlag = false;
				af.getAggressor().setDestroyed(false);
			}
			str = bufferedReader.readLine();
			if (str.trim().equals("#defender")) {
				str = bufferedReader.readLine();
//				af.setDefender(createTank(str.trim()));
				str = bufferedReader.readLine();
				str = str.substring(1, str.length() - 1);
				String tmp = str.split(";")[0];
				af.getDefender().setX(Integer.valueOf(tmp.trim()));
				tmp = str.split(";")[1];
				af.getDefender().setY(Integer.valueOf(tmp.trim()));			
				str = bufferedReader.readLine();
				af.getDefender().setDirection(Direction.valueOf(str.trim()));
				af.getDefender().setStrRole("defender");
				af.getDefender().setTarget(af.getAggressor());
				af.getDefender().setBf(af.getBattleField());
				af.getDefender().stopFlag = false;
				af.getDefender().setRobot(true);
				af.getDefender().setDestroyed(false);
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void loadActions(BufferedReader bufferedReader) {
		boolean flagArrressor = false;
		String str = null;
		try {
			while ((str = bufferedReader.readLine()) != null) {
				if(str.trim().equals("#aggressor")) {
					flagArrressor = true;
				} else if(str.trim().equals("#defender")) {
					flagArrressor = false;
				} else {
					if(flagArrressor) {
						af.getQueueActs().add(new ActionInQueue(af.getAggressor(), Action.valueOf(str.trim())));
					} else {
						af.getQueueActs().add(new ActionInQueue(af.getDefender(),Action.valueOf(str.trim())));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}			
	

	private AbstractTank createTank(String data) {
		AbstractTank tank = null;
		if(data.equals("T34")) {
			tank = new T34();
		} else if(data.equals("Tiger")) {
			tank = new Tiger();
		} else if(data.equals("BT7")) {
			tank = new BT7();
		} 
		return tank;
	}
	
	public void removeOldData() {
		File file = new File(fileName);
		if (file.exists()) {
			file.delete();
		}

		try {
			fos = new FileOutputStream(fileName, true);
			fileWriter = new OutputStreamWriter(fos);

		}  catch (IOException e) {
			e.printStackTrace();
		}

	}

}
