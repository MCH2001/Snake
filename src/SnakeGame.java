import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;

public class SnakeGame extends JPanel implements ActionListener {
    private static final int BOARD_WIDTH = 400;
    private static final int BOARD_HEIGHT = 400;
    private static final int UNIT_SIZE = 20;
    private static final int GAME_UNITS = (BOARD_WIDTH * BOARD_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private static final int DELAY = 75;
    public static int score;

    private final int[] snakeX = new int[GAME_UNITS];
    private final int[] snakeY = new int[GAME_UNITS];
    private int bodyParts = 6;
    private int applesEaten = 0;

    private JButton close;

    private int appleX;
    private int appleY;
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;

    public SnakeGame() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {

            // Draw the grid
            for (int i = 0; i < BOARD_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, BOARD_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, BOARD_WIDTH, i * UNIT_SIZE);
            }

            // Draw the apple
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // Draw the snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(new Color(45, 180, 0));
                }
                g.fillRect(snakeX[i], snakeY[i], UNIT_SIZE, UNIT_SIZE);
            }

            // Display the score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (BOARD_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
                    g.getFont().getSize());

        } else {

            gameOver(g);

        }
    }

    public void newApple() {
        appleX = (int) (Math.random() * (BOARD_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = (int) (Math.random() * (BOARD_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }
        switch (direction) {
            case 'U':
                snakeY[0] = snakeY[0] - UNIT_SIZE;
                break;
            case 'D':
                snakeY[0] = snakeY[0] + UNIT_SIZE;
                break;
            case 'L':
                snakeX[0] = snakeX[0] - UNIT_SIZE;
                break;
            case 'R':
                snakeX[0] = snakeX[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if (snakeX[0] == appleX && snakeY[0] == appleY) {
            bodyParts++;
            applesEaten++;
            score++;
            newApple();
        }
    }

    public void checkCollisions() {
        // Check if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if (snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
                running = false;
                break;
            }
        }

        // Check if head touches border
        if (snakeX[0] < 0 || snakeY[0] < 0 || snakeX[0] >= BOARD_WIDTH || snakeY[0] >= BOARD_HEIGHT) {
            running = false;
        }

        // Stop the timer if the game is over
        if (!running) {
            timer.stop();

        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (BOARD_WIDTH - metrics.stringWidth("Game Over")) / 2, BOARD_HEIGHT / 2);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + applesEaten, (BOARD_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
                BOARD_HEIGHT / 2 + g.getFont().getSize());

        // create a button to close the window of the game
        close = new JButton("Close");
        close.setBounds(BOARD_WIDTH / 2 - 50, BOARD_HEIGHT / 2 + 50, 100, 50);
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame gameFrame = (JFrame) SwingUtilities.getWindowAncestor(close);
                gameFrame.dispose();
            }
        });
        add(close);

        // create a text entry to save the score and the username on score.txt
        JTextField username = new JTextField("Enter your username");
        username.setBounds(BOARD_WIDTH / 2 - 100, BOARD_HEIGHT / 2 + 100, 200, 50);
        add(username);
        // make the username text entry be saved when the user press enter on score.txt
        // with the score
        username.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    FileWriter fw = new FileWriter("score.txt", true);
                    fw.write(score + " " + username.getText() + "\n");
                    fw.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Snake Game");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setResizable(false);
                frame.add(new SnakeGame(), BorderLayout.CENTER);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                // Set the icon for the frame
                ImageIcon icon = new ImageIcon("icon0.png");
                frame.setIconImage(icon.getImage());
            }
        });
    }
}
