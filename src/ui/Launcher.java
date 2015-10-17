package ui;

import utl.Sound;

import java.awt.SplashScreen;

public class Launcher {

	public static void main(String[] args) throws Exception {
		
		SplashScreen splash = SplashScreen.getSplashScreen();
		Thread.sleep(1000);
		splash.close();

		TanksUI tUI = new TanksUI();
	}

}
