import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.io.IOException;

/**
 * Entry point for the client application.
 *
 * <p>This class is responsible for:</p>
 * <ul>
 *     <li>Connecting to the reservation server.</li>
 *     <li>Creating the high-level {@link ClientAPI} wrapper.</li>
 *     <li>Launching the GUI (the {@link Screen}).</li>
 * </ul>
 *
 * <p>Time and date are handled as simple Strings instead of LocalTime/LocalDate.</p>
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * author zhu1220
 * @version November 20, 2025
 */
public class ClientMain {

    private static final String HOST = "localhost";
    private static final int PORT = 500;

    public static void main(String[] args) {
        try {
            // 1. Open connection to server
            ClientConnection conn = new ClientConnection();
            conn.connect(HOST, PORT);

            // 2. Build cache + API wrapper
            ClientCache cache = new ClientCache();
            ClientAPI api = new ClientAPI(conn, cache);

            // 3. Launch GUI
            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("Restaurant Reservation Client");

                // TODO: update Screen constructor when you want to pass api.
                Screen screen = new Screen();

                frame.setContentPane(screen);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            });

        } catch (IOException e) {
            System.err.println("Failed to connect to server at " + HOST + ":" + PORT);
            e.printStackTrace();
        }
    }
}
