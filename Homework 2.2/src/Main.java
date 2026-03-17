import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Scanner;

class AspectRatioCalculator extends JFrame {

    private JTextField widthField;
    private JTextField heightField;
    private JLabel resultLabel;

    public AspectRatioCalculator() {

        setTitle("Aspect Ratio");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
  //GUI
        getContentPane().setBackground(new Color(255, 220, 235));
        setLayout(new GridLayout(4, 2, 5, 5));

        JLabel widthLabel = new JLabel("Width:");
        JLabel heightLabel = new JLabel("Height:");
        widthLabel.setForeground(new Color(150, 0, 80));
        heightLabel.setForeground(new Color(150, 0, 80));

        add(widthLabel);
        widthField = new JTextField();
        add(widthField);

        add(heightLabel);
        heightField = new JTextField();
        add(heightField);

        JButton calculateButton = new JButton("Calculate");
        calculateButton.setBackground(new Color(255, 180, 210));
        calculateButton.setForeground(Color.BLACK);
        add(calculateButton);
//FILES
        JButton fileButton = new JButton("Load from file");
        fileButton.setBackground(new Color(255, 180, 210));
        fileButton.setForeground(Color.BLACK);
        add(fileButton);
//DESING
        resultLabel = new JLabel("Aspect Ratio:");
        resultLabel.setForeground(new Color(150, 0, 80));
        add(resultLabel);

        calculateButton.addActionListener(e -> calculateAspectRatio());
        fileButton.addActionListener(e -> loadFromFile());
    }

    private void calculateAspectRatio() {
        try {
            int width = Integer.parseInt(widthField.getText());
            int height = Integer.parseInt(heightField.getText());

            int mcd = mcd(width, height);

            resultLabel.setText("Aspect Ratio: " + (width / mcd) + ":" + (height / mcd));
        } catch (Exception e) {
            resultLabel.setText("Invalid data");
        }
    }

    private void loadFromFile() {
        JFileChooser chooser = new JFileChooser();
        int option = chooser.showOpenDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            try (Scanner sc = new Scanner(file)) {
                if (sc.hasNextInt()) {
                    widthField.setText(String.valueOf(sc.nextInt()));
                }
                if (sc.hasNextInt()) {
                    heightField.setText(String.valueOf(sc.nextInt()));
                }
            } catch (Exception e) {
                resultLabel.setText("Error reading file");
            }
        }
    }

    private int mcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public static void main(String[] args) {
        new AspectRatioCalculator().setVisible(true);
    }
}
