import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.imageio.ImageIO;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyGUI {
    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // Set the background image
        BufferedImage backgroundImage = null;
        try {
            backgroundImage = ImageIO.read(new File("snake.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (backgroundImage != null) {
            Image scaledImage = backgroundImage.getScaledInstance(frame.getWidth(), frame.getHeight(),
                    Image.SCALE_SMOOTH);
            ImageIcon backgroundIcon = new ImageIcon(scaledImage);
            JPanel backgroundPanel = new JPanel(new BorderLayout());
            backgroundPanel.add(new JLabel(backgroundIcon), BorderLayout.CENTER);

            // Create a panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
            buttonPanel.setOpaque(false);

            // Create the Start button
            JButton startButton = new JButton("Start");
            startButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Create a new frame for the SnakeGame
                    JFrame snakeFrame = new JFrame("Snake Game");

                    snakeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                    snakeFrame.add(new SnakeGame());
                    snakeFrame.pack();
                    snakeFrame.setLocationRelativeTo(null);
                    snakeFrame.setVisible(true);
                }
            });
            buttonPanel.add(startButton);

            // Create the Scores button
            JButton scoresButton = new JButton("Scores");
            scoresButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Read scores from the file
                    String scoresFilePath = "score.txt";
                    List<ScoreEntry> scoreEntries = new ArrayList<>();
                    try (BufferedReader br = new BufferedReader(new FileReader(scoresFilePath))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            String[] parts = line.split(" ");
                            if (parts.length == 2) {
                                int score = Integer.parseInt(parts[0]);
                                String entry = parts[1];
                                scoreEntries.add(new ScoreEntry(score, entry));
                            }
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    // Sort the scores in descending order
                    Collections.sort(scoreEntries, Comparator.reverseOrder());

                    // Create the scores GUI
                    JFrame scoresFrame = new JFrame("Scores");
                    scoresFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    scoresFrame.setSize(400, 300);

                    // Create a JTextArea to display the scores
                    JTextArea scoresTextArea = new JTextArea();
                    scoresTextArea.setEditable(false);
                    for (ScoreEntry entry : scoreEntries) {
                        scoresTextArea.append(entry.getFormattedEntry() + "\n");
                    }

                    // Add the scores text area to a scroll pane
                    JScrollPane scrollPane = new JScrollPane(scoresTextArea);

                    // Add the scroll pane to the scores frame
                    scoresFrame.getContentPane().add(scrollPane);

                    // Make the scores frame visible
                    scoresFrame.setVisible(true);
                }
            });
            buttonPanel.add(scoresButton);

            // Create the Exit button
            JButton exitButton = new JButton("Exit");
            exitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            buttonPanel.add(exitButton);

            // Set the icon for the frame
            ImageIcon icon = new ImageIcon("icon0.png");
            frame.setIconImage(icon.getImage());

            // Add the button panel to the background panel
            backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

            // Add the background panel to the frame
            frame.setContentPane(backgroundPanel);

            // Make the frame visible
            frame.setVisible(true);
        }
    }

    static class ScoreEntry implements Comparable<ScoreEntry> {
        private int score;
        private String entry;

        public ScoreEntry(int score, String entry) {
            this.score = score;
            this.entry = entry;
        }

        public int getScore() {
            return score;
        }

        public String getEntry() {
            return entry;
        }

        public String getFormattedEntry() {
            return score + " " + entry;
        }

        @Override
        public int compareTo(ScoreEntry other) {
            return Integer.compare(this.score, other.score);
        }
    }
}
