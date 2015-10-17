package utl;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class LoadPictures {

	private Image waterImage;
	private Image brickImage;
	private Image rockImage;

	public LoadPictures() {
		
		try {
			waterImage = ImageIO.read(new File("pictures/water.PNG"));
			brickImage = ImageIO.read(new File("pictures/bricks.PNG"));
			rockImage = ImageIO.read(new File("pictures/stone.PNG"));
		} catch (IOException e) {
			System.err.println("Can't find water image");
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
