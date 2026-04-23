import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

//Map with coordinates of places by  images
public class generateMap {
//sizes
    private static final int W = 1080;
    private static final int H = 1920;

    public static boolean render(double lat1, double lng1,
                                 double lat2, double lng2,
                                 String phrase,
                                 String outputPath) {
        try {

            BufferedImage img = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = img.createGraphics();

            // Smooth rendering
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Background
            g.setColor(new Color(15, 15, 20));
            g.fillRect(0, 0, W, H);

            // Convert GPS to fake normalized positions (simple mapping)
            double[] p1 = normalize(lat1, lng1);
            double[] p2 = normalize(lat2, lng2);

            int x1 = (int) (p1[0] * W);
            int y1 = (int) (p1[1] * H);
            int x2 = (int) (p2[0] * W);
            int y2 = (int) (p2[1] * H);

            // Line between points
            g.setStroke(new BasicStroke(6));
            g.setColor(new Color(100, 180, 255));
            g.drawLine(x1, y1, x2, y2);

            // Start point
            drawPoint(g, x1, y1, new Color(0, 220, 120), "START");

            // End point
            drawPoint(g, x2, y2, new Color(255, 80, 80), "END");

            // Phrase
            drawText(g, phrase);

            g.dispose();

            ImageIO.write(img, "png", new File(outputPath));
            return true;

        } catch (Exception e) {
            System.out.println("[generateMap] error: " + e.getMessage());
            return false;
        }
    }

    // Converts GPS to simple normalized screen position
    private static double[] normalize(double lat, double lng) {
        double x = (lng + 180) / 360.0;
        double y = (90 - lat) / 180.0;

        return new double[]{
                Math.max(0.1, Math.min(0.9, x)),
                Math.max(0.1, Math.min(0.9, y))
        };
    }
//style
    private static void drawPoint(Graphics2D g, int x, int y, Color c, String label) {
        g.setColor(c);
        g.fillOval(x - 20, y - 20, 40, 40);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString(label, x + 25, y);
    }

    private static void drawText(Graphics2D g, String text) {
        g.setFont(new Font("Georgia", Font.ITALIC, 48));
        g.setColor(Color.WHITE);

        FontMetrics fm = g.getFontMetrics();
        int x = (W - fm.stringWidth(text)) / 2;
        int y = H - 200;

        g.drawString(text, x, y);
    }
}