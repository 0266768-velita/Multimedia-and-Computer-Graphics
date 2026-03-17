import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {

    private BufferedImage image;
    private BufferedImage originalImage;

    private int cropX, cropY, cropW, cropH;
    private boolean hasCrop = false;

    public ImagePanel() {
        setBackground(new Color(30, 30, 30));
    }


    public void setImage(BufferedImage img) {
        if (img == null) return;
        this.originalImage = img;

        this.image = img;
        repaint();
    }

    public void setCrop(int x, int y, int w, int h) {
        this.cropX = x;
        this.cropY = y;
        this.cropW = w;
        this.cropH = h;
        this.hasCrop = true;
        repaint();
    }

    public void clearCrop() {
        hasCrop = false;
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (originalImage != null) {

            //scale
            int panelW = getWidth()  - 20;
            int panelH = getHeight() - 20;

            if (panelW <= 0 || panelH <= 0) return;

            double scale = Math.min(
                    (double) panelW / originalImage.getWidth(),
                    (double) panelH / originalImage.getHeight()
            );

            if (scale > 1.0) scale = 1.0;

            int drawW = (int) (originalImage.getWidth()  * scale);
            int drawH = (int) (originalImage.getHeight() * scale);

            int offX = (getWidth()  - drawW) / 2;
            int offY = (getHeight() - drawH) / 2;


            g.drawImage(originalImage, offX, offY, drawW, drawH, null);


            if (hasCrop) {
                int rx = offX + (int)(cropX * scale);
                int ry = offY + (int)(cropY * scale);
                int rw = (int)(cropW * scale);
                int rh = (int)(cropH * scale);

                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // shadow
                g2.setColor(new Color(0, 0, 0, 80));
                g2.setStroke(new BasicStroke(4));
                g2.drawRect(rx - 1, ry - 1, rw + 2, rh + 2);

                // edge
                g2.setColor(new Color(70, 160, 255));
                g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        0, new float[]{6, 4}, 0));
                g2.drawRect(rx, ry, rw, rh);


                g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int cs = 10;
                g2.drawLine(rx,      ry,      rx + cs,    ry);
                g2.drawLine(rx,      ry,      rx,         ry + cs);
                g2.drawLine(rx + rw, ry,      rx + rw - cs, ry);
                g2.drawLine(rx + rw, ry,      rx + rw,    ry + cs);
                g2.drawLine(rx,      ry + rh, rx + cs,    ry + rh);
                g2.drawLine(rx,      ry + rh, rx,         ry + rh - cs);
                g2.drawLine(rx + rw, ry + rh, rx + rw - cs, ry + rh);
                g2.drawLine(rx + rw, ry + rh, rx + rw,    ry + rh - cs);
            }

        } else {
            drawWelcomeScreen(g);
        }
    }


    // Welcome int
    private void drawWelcomeScreen(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        int iconSize = 90;
        int iconX = cx - iconSize / 2;
        int iconY = cy - iconSize / 2 - 50;

        g2.setColor(new Color(70, 130, 180, 50));
        g2.fillOval(iconX, iconY, iconSize, iconSize);
        g2.setColor(new Color(70, 130, 180, 120));
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(iconX, iconY, iconSize, iconSize);

        g2.setColor(new Color(100, 160, 210));
        g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int ax = cx, ayTop = iconY + 18, ayBot = iconY + 58;
        g2.drawLine(ax, ayTop, ax, ayBot);
        g2.drawLine(ax, ayTop, ax - 14, ayTop + 16);
        g2.drawLine(ax, ayTop, ax + 14, ayTop + 16);
        g2.drawLine(cx - 18, iconY + 68, cx + 18, iconY + 68);

        g2.setColor(new Color(210, 210, 210));
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        String title = "No image loaded";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(title, cx - fm.stringWidth(title) / 2, cy + 30);

        g2.setColor(new Color(140, 140, 140));
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        String sub = "Use the \"Load Image\" button to get started";
        fm = g2.getFontMetrics();
        g2.drawString(sub, cx - fm.stringWidth(sub) / 2, cy + 58);
    }
}