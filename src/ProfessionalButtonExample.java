import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProfessionalButtonExample {

    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Professional Button");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new FlowLayout());

        // Create the button
        JButton button = new JButton("Click Me");
        button.setBackground(new Color(52, 152, 219)); // Background color
        button.setForeground(Color.WHITE); // Text color
        button.setFocusPainted(false); // Remove focus border
        button.setFont(new Font("Arial", Font.BOLD, 14)); // Font style

        // Custom hover effect using MouseListener
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(41, 128, 185)); // Hover background color
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(52, 152, 219)); // Default background color
                button.setCursor(Cursor.getDefaultCursor());
            }
        });

        // Add the button to the frame
        frame.add(button);

        // Make the frame visible
        frame.setVisible(true);
    }
}
