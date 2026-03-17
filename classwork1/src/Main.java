import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Main {

  public static void main(String[] args) throws Exception {

    int W = 400;
    int H = 300;

    BufferedImage img = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);

    // background
    for (int y = 0; y < H; y++) {
      for (int x = 0; x < W; x++) {
        img.setRGB(x, y, Color.WHITE.getRGB());
      }
    }

    // sun
    int sunX = 85;
    int sunY = 70;
    int r = 38;

    for (int y = -r; y <= r; y++) {
      for (int x = -r; x <= r; x++) {
        if (x * x + y * y <= r * r) {
          img.setRGB(sunX + x, sunY + y, Color.YELLOW.getRGB());
        }
      }
    }

    // ray
    Color ray = Color.RED;

    for (int i = 45; i <= 70; i++) {
      img.setRGB(sunX, sunY - i, ray.getRGB()); // up
      img.setRGB(sunX, sunY + i, ray.getRGB()); // down
      img.setRGB(sunX - i, sunY, ray.getRGB()); // left
      img.setRGB(sunX + i, sunY, ray.getRGB()); // ri
    }

    for (int i = 32; i <= 55; i++) {
      img.setRGB(sunX + i, sunY + i, ray.getRGB());
      img.setRGB(sunX - i, sunY + i, ray.getRGB());
      img.setRGB(sunX + i, sunY - i, ray.getRGB());
      img.setRGB(sunX - i, sunY - i, ray.getRGB());
    }

    // green
    for (int x = 0; x < W; x++) {
      int wave = (int)(10 * Math.sin(x * 0.12));

      for (int y = 185 + wave; y < H; y++) {
        img.setRGB(x, y, Color.GREEN.getRGB());
      }
    }

    ImageIO.write(img, "png", new File("classwork01.png"));
  }
}
