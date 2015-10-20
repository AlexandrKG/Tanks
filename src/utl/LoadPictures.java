package utl;

import java.awt.Image;
import javax.swing.*;

public class LoadPictures {

	private Image waterImage;
	private Image brickImage;
	private Image rockImage;

	public LoadPictures() {
		
		try {
			waterImage = new ImageIcon(getClass().getResource("/resources/pictures/water.PNG")).getImage();
			brickImage = new ImageIcon(getClass().getResource("/resources/pictures/bricks.PNG")).getImage();
			rockImage = new ImageIcon(getClass().getResource("/resources/pictures/stone.PNG")).getImage();
		} catch (Exception e) {
			System.err.println("Can't find landscape image");
		}

	}

	public Image getBrickImage() {
		return brickImage;
	}

	public Image getRockImage() {
		return rockImage;
	}

	public Image getWaterImage() {
		return waterImage;
	}

}
