import javax.swing.*;
import java.awt.*;

public class Sphere extends JPanel {

    double centerx = 100;
    double centery = 100;
    double radious = 50;

    public void circul(Graphics g){
        for (int x = 0; x < 200; x++) {
            for (int y = 0; y < 200; y++) {

                double dx = x - centerx;
                double dy = y - centery;

                if (dx*dx + dy*dy <= radious*radious) {
                    g.fillRect(x, y, 1, 1);
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        circul(g); // aquí se ejecuta tu método
    }

    public static void main(String[] args) {
        JFrame ventana = new JFrame("Prueba");
        Sphere panel = new Sphere();

        ventana.add(panel);
        ventana.setSize(300,300);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setVisible(true);
    }
}