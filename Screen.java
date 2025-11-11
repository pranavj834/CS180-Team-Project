import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
    private String testText;

    public Screen() {
        background = new Color(35, 37, 40);
        text = new Color(255, 255, 255);

        testButton = new JButton("Test");
        testButton.setBounds(50, 100, 100, 30);
        add(testButton);
        testButton.addActionListener(this);

        testField = new JTextField();
        testField.setBounds(50, 50, 100, 30);
        add(testField);
        testField.addActionListener(this);

        testText = "sample text";

        setLayout(null);
        setFocusable(true);
    }

    public Dimension getPreferredSize() {
        return new Dimension(1366, 768);
    }

    public void paintComponent(Graphics g) {
        g.setColor(background);
        g.fillRect(0, 0, 1366, 768);

        g.setFont(new Font("Uni Sans", Font.BOLD, 20));
        g.setColor(text);
        g.drawString(testText, 50, 165);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == testButton) {
            testText = testField.getText();
            testField.setText("");
        }
        repaint();
    }
}