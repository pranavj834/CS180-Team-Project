import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Screen extends JPanel implements ActionListener {
    private Color background, text;
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