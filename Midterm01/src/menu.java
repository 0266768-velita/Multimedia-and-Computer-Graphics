import java.awt.*;
import java.awt.image.BufferedImage;

class ImageMenu {

    private BufferedImage currentImage;

    private int regionX, regionY, regionW, regionH;
    private boolean hasRegion = false;
    private int totalRotation = 0;


    public void setImage(BufferedImage img) {
        currentImage = img;
        hasRegion = false;
        totalRotation = 0;
    }

    public BufferedImage getImage() {
        return currentImage;
    }


    // REGION

    public void setRegion(int x, int y, int w, int h) {
        regionX = x;
        regionY = y;
        regionW = w;
        regionH = h;
        hasRegion = true;
        totalRotation = 0;
    }

    public void clearRegion() {
        hasRegion = false;
        totalRotation = 0;
    }

    public boolean hasRegion()    { return hasRegion;     }
    public int getRegionX()       { return regionX;       }
    public int getRegionY()       { return regionY;       }
    public int getRegionW()       { return regionW;       }
    public int getRegionH()       { return regionH;       }
    public int getTotalRotation() { return totalRotation; }


    // clean section

  //  public void cut() {
  //      if (currentImage == null || !hasRegion) return;
   //     hasRegion = false;
   //     totalRotation = 0;
  //  }


    public void rotate() {

        if (currentImage == null) return;

        if (hasRegion) {

            int x = regionX;
            int y = regionY;
            int w = regionW;
            int h = regionH;

            if (x < 0 || y < 0 ||
                    x + w > currentImage.getWidth() ||
                    y + h > currentImage.getHeight()) return;

            BufferedImage copy = deepCopy(currentImage);

            Graphics2D g = currentImage.createGraphics();
            g.setColor(Color.BLACK);
            g.fillRect(x, y, w, h);
            g.dispose();

            for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
                    int rgb = copy.getRGB(x + i, y + j);
                    int newX = x + (h - 1 - j);
                    int newY = y + i;
                    if (newX < currentImage.getWidth() &&
                            newY < currentImage.getHeight()) {
                        currentImage.setRGB(newX, newY, rgb);
                    }
                }
            }

            regionW = h;
            regionH = w;

        } else {

            int w = currentImage.getWidth();
            int h = currentImage.getHeight();
            BufferedImage rotated = new BufferedImage(h, w, currentImage.getType());
            for (int y = 0; y < h; y++)
                for (int x = 0; x < w; x++)
                    rotated.setRGB(h - 1 - y, x, currentImage.getRGB(x, y));
            currentImage = rotated;
        }

        totalRotation = (totalRotation + 90) % 360;
    }


    // INvert color

    public void invertColors() {

        if (currentImage == null) return;

        for (int y = 0; y < currentImage.getHeight(); y++) {
            for (int x = 0; x < currentImage.getWidth(); x++) {
                int rgb = currentImage.getRGB(x, y);
                int a =   (rgb >> 24) & 0xff;
                int r = 255 - ((rgb >> 16) & 0xff);
                int g = 255 - ((rgb >>  8) & 0xff);
                int b = 255 - ( rgb        & 0xff);
                currentImage.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }
    }


    private BufferedImage deepCopy(BufferedImage bi) {
        BufferedImage copy = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());
        Graphics g = copy.getGraphics();
        g.drawImage(bi, 0, 0, null);
        g.dispose();
        return copy;
    }
}