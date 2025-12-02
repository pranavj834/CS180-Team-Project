import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.GenericArrayType;

/**
 * Provides the GUI.
 * NOTE: Screen is currently non-functional and is not
 *       meant to be run or tested. It, along with this
 *       class, are placeholders at the time of writing.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author chan531, lab sec L23
 * @version November 10, 2025
 */

public class Screen extends JPanel implements ActionListener, ScreenInterface {
    private Color background;
    private Color text;
    private JButton testButton;
    private JTextField testField;
    private int currentScreen; // 0 - login, 1 - logged in

    public Screen(Client client) {
        background = new Color(35, 37, 40);
        text = new Color(255, 255, 255);
        currentScreen = 0;

        testButton = new JButton("Test");
        testButton.setBounds(50, 100, 100, 30);
        add(testButton);
        testButton.addActionListener(this);

        testField = new JTextField();
        testField.setBounds(50, 165, 100, 30);
        add(testField);
        testField.addActionListener(this);

        setLayout(null);
        setFocusable(true);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (currentScreen == 0) { // logged out
            setBounds(0, 0, 725, 500);
            setSize(725, 500);
            g.setColor(background);
            g.fillRect(0, 0, 1366, 768);

            g.setFont(new Font("Uni Sans", Font.BOLD, 36));
            g.setColor(text);
            g.drawString("Restaurant Reservation Manager", 50, 75);
            g.setFont(new Font("Uni Sans", Font.BOLD, 25));
            g.drawString("Welcome!", 50, 120);
            g.setFont(new Font("Uni Sans", Font.BOLD, 20));
            g.drawString("Please login or register.", 50, 155);
        } else if (currentScreen == 1) { // logged in
            // TODO: implement GUI components to handle methods in Client.java
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == testButton) {
            testField.setText("");
        }
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(725, 500);
    }
}