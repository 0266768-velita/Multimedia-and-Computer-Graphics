import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;

public class Main {

    static int W = 800;
    static int H = 800;

    public static void main(String[] args) throws Exception {

        BufferedImage img = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);

        int xA = 100, yA = 700; // Red
        int xC = 400, yC = 100; // Blue
        int xB = 700, yB = 700; // Green


        for (int x = 0; x < W; x++) {
            for (int y = 0; y < H; y++) {

                double a1 =
                        (double) ((yB - yC) * (x - xC) + (xC - xB) * (y - yC)) /
                                ((yB - yC) * (xA - xC) + (xC - xB) * (yA - yC));

                double a2 =
                        (double) ((yC - yA) * (x - xC) + (xA - xC) * (y - yC)) /
                                ((yB - yC) * (xA - xC) + (xC - xB) * (yA - yC));

                double a3 = 1 - a1 - a2;

                if (a1 >= 0 && a2 >= 0 && a3 >= 0) {

                    int r = (int) (a1 * 255);
                    int g = (int) (a2 * 255);
                    int b = (int) (a3 * 255);

                    img.setRGB(x, y, new Color(r, g, b).getRGB());
                } else {
                    img.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }

        ImageIO.write(img, "png", new File("trian.png"));
    }
}