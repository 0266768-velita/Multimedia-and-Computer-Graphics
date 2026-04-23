import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::buildUI);
    }
//GUI for visual terminal project
    private static void buildUI() {

        JFrame frame = new JFrame("AI Video Creator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(660, 620);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(28, 28, 28));
        frame.setContentPane(root);

        //  TITLE
        JLabel title = new JLabel("AI Video Creator", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(15, 10, 10, 10));
        root.add(title, BorderLayout.NORTH);

        //  CENTER style
        JTextArea logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(new Color(15, 15, 15));
        logArea.setForeground(Color.LIGHT_GRAY);

        JScrollPane scroll = new JScrollPane(logArea);
        root.add(scroll, BorderLayout.CENTER);

        //  PROGRESS
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setString("Ready");

        root.add(progressBar, BorderLayout.SOUTH);

        // INPUT
        Input input = new Input();

        List<File> files = new ArrayList<>();

        //  BUTTON PANEL
        JPanel panel = new JPanel();
        panel.setBackground(new Color(28, 28, 28));

        JButton addBtn = new JButton("Add Files");
        JButton clearBtn = new JButton("Clear");
        JButton startBtn = new JButton("Generate Video");

        panel.add(addBtn);
        panel.add(clearBtn);
        panel.add(startBtn);

        root.add(panel, BorderLayout.NORTH);

        //  ACTIONS

        addBtn.addActionListener(e -> {
            input.openFileChooser(frame);
            files.clear();
            files.addAll(input.getSelectedFiles());

            logArea.append("Files added: " + files.size() + "\n");
        });

        clearBtn.addActionListener(e -> {
            input.clearFiles();
            files.clear();
            logArea.setText("");
            progressBar.setValue(0);
            progressBar.setString("Ready");
        });

        startBtn.addActionListener(e -> {

            if (files.isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Add files first",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            addBtn.setEnabled(false);
            clearBtn.setEnabled(false);
            startBtn.setEnabled(false);

            progressBar.setString("Processing...");
            logArea.setText("");

            new Thread(() -> {

                try {
                    Controller controller = new Controller(new ArrayList<>(files));
                    controller.setProgressBar(progressBar);
                    controller.run(frame, logArea);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                SwingUtilities.invokeLater(() -> {
                    progressBar.setString("Done");
                    addBtn.setEnabled(true);
                    clearBtn.setEnabled(true);
                    startBtn.setEnabled(true);
                });

            }).start();
        });

        frame.setVisible(true);
    }
}