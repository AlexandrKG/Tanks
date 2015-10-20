package ui;

import java.awt.SplashScreen;

public class Launcher {

	public static void main(String[] args) throws Exception {
		
		SplashScreen splash = SplashScreen.getSplashScreen();
		Thread.sleep(3000);
		splash.close();

		TanksUI tUI = new TanksUI();
	}

}
