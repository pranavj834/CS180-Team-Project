import java.awt.*;
import java.awt.event.*;

/**
 * Interface for the Screen class.
 * NOTE: Screen is currently non-functional and is not
 *       meant to be run or tested. It, along with this
 *       class, are placeholders at the time of writing.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author Ryan Chan, lab sec L23
 * @version November 10, 2025
 */

public interface ScreenInterface {
    // adjusts window size
    Dimension getPreferredSize();
    // draws GUI elements
    void paintComponent(Graphics g);
    // implements button functionality
    void actionPerformed(ActionEvent e);
}