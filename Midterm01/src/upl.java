import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class upl {

    public BufferedImage openImage() {
        try {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                return ImageIO.read(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}