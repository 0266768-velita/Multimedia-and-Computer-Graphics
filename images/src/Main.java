import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Main {
    public static void main(String[] args) throws Exception {

        BufferedImage image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x <400; x++) {
            for (int y = 0; y <400; y++) {

               if (x >=0 && x <400 && y >=0 && y <400) {

                    if (x - 100 <= y - 100) {
                        image.setRGB(x, y, Color.red.getRGB());
                    } else {
                        image.setRGB(x, y, Color.BLUE.getRGB());
                    }

                }

            }
        }

        ImageIO.write(image, "jpg", new File("paso2.jpg"));
    }
}
