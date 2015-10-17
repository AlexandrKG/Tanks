package ui;

import java.util.Observable;

public class Result extends Observable {
	
	private String res = "Game is over! User interrupted game...";

	public void gameOver(boolean def,boolean agr,boolean stf) {
		
		if(def) {
			res = "Defender was destroied...";
		} else if(agr) {
			res = "Aggressoor was destroied...";
		} else if(stf) {
			res = "Staff was destroied...";
		}

		System.out.println("Game is over!");
		setChanged();
		notifyObservers();
	}
	
	public void startNewGame() {
		res = "Game is over! User interrupted game...";
		this.clearChanged();
	}

	public String getRes() {
		return res;
	}

	
}
