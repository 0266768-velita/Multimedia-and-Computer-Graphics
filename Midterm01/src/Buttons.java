import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Buttons extends JPanel {

    public Buttons(ImagePanel imagePanel, ImageMenu m) {

        setLayout(new FlowLayout(FlowLayout.CENTER, 8, 6));
        setBackground(new Color(45, 45, 45));

        JButton load      = new JButton(" Load Image");
        JButton setRegion = new JButton(" Set Region");
        JButton cut       = new JButton(" Cut");
        JButton invert    = new JButton("Invert");
        JButton rotate    = new JButton(" Rotate");
        JButton save      = new JButton("Save");

        JButton[] editButtons = {setRegion, cut, invert, rotate, save};

        styleButton(load, new Color(70, 130, 180));
        for (JButton btn : editButtons) {
            styleButton(btn, new Color(80, 80, 80));
            btn.setVisible(false);
        }

        add(load);
        for (JButton btn : editButtons) add(btn);


        // LOAD IMAGE

        load.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(imagePanel) == JFileChooser.APPROVE_OPTION) {
                try {
                    BufferedImage img = ImageIO.read(chooser.getSelectedFile());
                    if (img != null) {
                        m.setImage(img);
                        imagePanel.setImage(img);
                        imagePanel.clearCrop();
                        rotate.setText(" Rotate ");
                        setRegion.setBackground(new Color(80, 80, 80));
                        cut.setBackground(new Color(80, 80, 80));
                        rotate.setBackground(new Color(80, 80, 80));
                        for (JButton btn : editButtons) btn.setVisible(true);
                        revalidate();
                        repaint();
                    } else {
                        JOptionPane.showMessageDialog(imagePanel, "Could not read the image.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(imagePanel, "Error: " + ex.getMessage());
                }
            }
        });


        // SET REGION

        setRegion.addActionListener(e -> {
            try {
                int x = Integer.parseInt(JOptionPane.showInputDialog("X:"));
                int y = Integer.parseInt(JOptionPane.showInputDialog("Y:"));
                int w = Integer.parseInt(JOptionPane.showInputDialog("Width:"));
                int h = Integer.parseInt(JOptionPane.showInputDialog("Height:"));
                m.setRegion(x, y, w, h);
                imagePanel.setCrop(x, y, w, h);
                rotate.setText("Rotate");
                setRegion.setBackground(new Color(70, 160, 255));
                cut.setBackground(new Color(70, 160, 255));
                rotate.setBackground(new Color(70, 160, 255));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(imagePanel, "Please enter valid numbers.");
            }
        });

        cut.addActionListener(e -> {
            if (!m.hasRegion()) {
                JOptionPane.showMessageDialog(imagePanel, "Please set a region first.");
                return;
            }
            m.cut();
            imagePanel.setImage(m.getImage());
            imagePanel.clearCrop();
            setRegion.setBackground(new Color(80, 80, 80));
            cut.setBackground(new Color(80, 80, 80));
            rotate.setBackground(new Color(80, 80, 80));
            rotate.setText(" Rotate ");
        });


        // INVERT

        invert.addActionListener(e -> {
            m.invertColors();
            imagePanel.setImage(m.getImage());
        });


        // ROTATE
        rotate.addActionListener(e -> {
            m.rotate();
            imagePanel.setImage(m.getImage());
            if (m.hasRegion()) {
                imagePanel.setCrop(m.getRegionX(), m.getRegionY(), m.getRegionW(), m.getRegionH());
            }
            rotate.setText(" Rotate (" + m.getTotalRotation() + "°)");
        });


        // SAVE
        save.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showSaveDialog(imagePanel) == JFileChooser.APPROVE_OPTION) {
                try {
                    ImageIO.write(m.getImage(), "png", chooser.getSelectedFile());
                    JOptionPane.showMessageDialog(imagePanel, "Image saved!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(imagePanel, "Error saving: " + ex.getMessage());
                }
            }
        });
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}