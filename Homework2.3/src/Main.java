import javax.swing.*;
import java.awt.*;

class CoordinateConverter extends JFrame {

    private JTextField xField, yField, rField, thetaField;
    private JLabel polarResult, cartesianResult;
    private JTextField aField, kField, y0Field;
    private GraphPanel graphPanel;

    public CoordinateConverter() {

        setTitle("Coordinate Converter ");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));


        JPanel convertPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        convertPanel.setBorder(BorderFactory.createTitledBorder("Coordinate Conversion"));

        convertPanel.add(new JLabel("x:"));
        xField = new JTextField();
        convertPanel.add(xField);

        convertPanel.add(new JLabel("y:"));
        yField = new JTextField();
        convertPanel.add(yField);

        JButton toPolar = new JButton("To Polar");
        convertPanel.add(toPolar);
        polarResult = new JLabel("r = , θ = ");
        convertPanel.add(polarResult);

        convertPanel.add(new JLabel("r:"));
        rField = new JTextField();
        convertPanel.add(rField);

        convertPanel.add(new JLabel("θ (deg):"));
        thetaField = new JTextField();
        convertPanel.add(thetaField);

        JButton toCartesian = new JButton("To Cartesian");
        convertPanel.add(toCartesian);
        cartesianResult = new JLabel("x = , y = ");
        convertPanel.add(cartesianResult);

        add(convertPanel, BorderLayout.WEST);

        //gui
        JPanel tPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        tPanel.setBorder(BorderFactory.createTitledBorder("Polar  Parameters"));

        aField = new JTextField("100");
        kField = new JTextField("4");
        y0Field = new JTextField("0");

        tPanel.add(new JLabel("a:"));
        tPanel.add(aField);
        tPanel.add(new JLabel("k (int):"));
        tPanel.add(kField);
        tPanel.add(new JLabel("y0 (deg):"));
        tPanel.add(y0Field);

        JButton draw = new JButton("Draw Polar ");
        tPanel.add(draw);

        add(tPanel, BorderLayout.SOUTH);

        // gui
        graphPanel = new GraphPanel();
        add(graphPanel, BorderLayout.CENTER);

        // actions
        toPolar.addActionListener(e -> convertToPolar());
        toCartesian.addActionListener(e -> convertToCartesian());
        draw.addActionListener(e -> graphPanel.repaint());
    }

    private void convertToPolar() {
        try {
            double x = Double.parseDouble(xField.getText());
            double y = Double.parseDouble(yField.getText());

            double r = Math.hypot(x, y);
            double theta = Math.toDegrees(Math.atan2(y, x));

            polarResult.setText(String.format("r = %.2f, θ = %.2f°", r, theta));
        } catch (Exception e) {
            polarResult.setText("Invalid input");
        }
    }

    private void convertToCartesian() {
        try {
            double r = Double.parseDouble(rField.getText());
            double theta = Math.toRadians(Double.parseDouble(thetaField.getText()));

            double x = r * Math.cos(theta);
            double y = r * Math.sin(theta);

            cartesianResult.setText(String.format("x = %.2f, y = %.2f", x, y));
        } catch (Exception e) {
            cartesianResult.setText("Invalid input");
        }
    }

    class GraphPanel extends JPanel {

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.pink);

            try {
                double a = Double.parseDouble(aField.getText());
                int k = Integer.parseInt(kField.getText());
                double y0 = Math.toRadians(Double.parseDouble(y0Field.getText()));

                int w = getWidth();
                int h = getHeight();
                int cx = w / 2;
                int cy = h / 2;

                g.setColor(Color.LIGHT_GRAY);
                g.drawLine(cx, 0, cx, h);
                g.drawLine(0, cy, w, cy);

                g.setColor(new Color(255, 105, 180)); //color points


                for (double phi = 0; phi < 2 * Math.PI; phi += 0.01) {
                    double r = a * Math.cos(k * phi + y0);
                    int x = (int) (cx + r * Math.cos(phi));
                    int y = (int) (cy - r * Math.sin(phi));
                    g.fillOval(x, y, 2, 2);
                }
            } catch (Exception e) {
                //dont draw
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CoordinateConverter().setVisible(true);
        });
    }
}
