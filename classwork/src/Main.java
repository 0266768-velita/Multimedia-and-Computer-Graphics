import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Main {

    static int W = 400;
    static int H = 400;

    public static void main(String[] args) throws Exception {

        BufferedImage img = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);

        int cx = W / 2;
        int cy = H / 2;
        int r = 150;

        // background
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                img.setRGB(x, y, Color.BLACK.getRGB());
            }
        }

        // Circ
        for (int ang = 0; ang < 360; ang++) {
            double rad = Math.toRadians(ang);
            int x = cx + (int)(Math.cos(rad) * r);
            int y = cy + (int)(Math.sin(rad) * r);
            img.setRGB(x, y, Color.LIGHT_GRAY.getRGB());
        }

        // Point hours
        for (int i = 0; i < 12; i++) {
            double rad = Math.toRadians(i * 30 - 90);
            int x = cx + (int)(Math.cos(rad) * (r - 15));
            int y = cy + (int)(Math.sin(rad) * (r - 15));

            for (int dy = -2; dy <= 2; dy++) {
                for (int dx = -2; dx <= 2; dx++) {
                    img.setRGB(x + dx, y + dy, Color.WHITE.getRGB());
                }
            }
        }



// 1 line long
        drawHand(img, cx, cy, 100, 35);

// 2 line short
        drawHand(img, cx, cy, 85, -55);


        ImageIO.write(img, "png", new File("watch.png"));
    }


    static void drawHand(BufferedImage img, int cx, int cy, int len, int deg) {
        double rad = Math.toRadians(deg - 90);
        int x2 = cx + (int)(Math.cos(rad) * len);
        int y2 = cy + (int)(Math.sin(rad) * len);

        int steps = Math.max(Math.abs(x2 - cx), Math.abs(y2 - cy));
        for (int i = 0; i <= steps; i++) {
            int x = cx + (x2 - cx) * i / steps;
            int y = cy + (y2 - cy) * i / steps;
            img.setRGB(x, y, Color.WHITE.getRGB());
        }
    }
}
