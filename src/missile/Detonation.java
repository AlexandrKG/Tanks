package missile;

import utl.Destroyable;
import utl.Direction;
import utl.Drawable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;


public class Detonation implements Drawable, Destroyable {

    final static int DET_SIZE = 64;
    protected int x;
    protected int y;
    private int xShift = 0;
    private int yShift = 0;
    protected Image image;
    private boolean destroyed;

    public Detonation() {
        try {
            image = ImageIO.read(new File("pictures/detonation.PNG"));
            destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void initDetonation(int x, int y, Direction dir) {
        if(dir == Direction.TOP) {
            this.x = x - DET_SIZE / 2;
            this.y = y - DET_SIZE;
        } else if(dir == Direction.BOTTOM) {
            this.x = x - DET_SIZE / 2;
            this.y = y;
        } else if(dir == Direction.LEFT) {
            this.x = x - DET_SIZE;
            this.y = y - DET_SIZE / 2;
        } else {
            this.x = x;
            this.y = y - DET_SIZE / 2;
        }
        destroyed = false;
    }

    @Override
    public void destroy() throws Exception {
        x = -100;
        y = -100;
        xShift = 0;
        yShift = 0;
        destroyed = true;
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public void draw(Graphics g) {
        if (!this.isDestroyed()) {
            if (image != null) {

                if (xShift >= DET_SIZE * 8) {
                    xShift = 0;
                }

                g.drawImage(image, x, y, x + DET_SIZE, y + DET_SIZE,
                        xShift, yShift, xShift + DET_SIZE, yShift + DET_SIZE,
                        new ImageObserver() {

                            @Override
                            public boolean imageUpdate(Image img, int infoflags, int x, int y,
                                                       int width, int height) {
                                // TODO Auto-generated method stub
                                return false;
                            }
                        });
                xShift += DET_SIZE;
                if (xShift >= DET_SIZE * 8) {
                    try {
                        this.destroy();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
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
}