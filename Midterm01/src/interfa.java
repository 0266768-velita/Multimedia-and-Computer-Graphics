import javax.swing.*;
import java.awt.*;

public class interfa extends JFrame {

    public interfa() {
        setTitle("Image Editor");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        ImagePanel imagePanel = new ImagePanel();
        ImageMenu m = new ImageMenu();
        Buttons buttons = new Buttons(imagePanel, m);

        add(imagePanel, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        setVisible(true);
    }
}