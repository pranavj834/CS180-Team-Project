import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.io.IOException;

/**
 * Entry point for the client application.
 *
 * <p>This class is responsible for:</p>
 * <ul>
 *     <li>Creating a {@link ClientConnection} and connecting to the server.</li>
 *     <li>Creating the high-level {@link ClientAPI} wrapper around the connection.</li>
 *     <li>Launching the Swing GUI (currently the {@link Screen} panel).</li>
 * </ul>
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author zhu1220, lab sec L23
 * @version November 20, 2025
 */
public class ClientMain {

    /** Hostname or IP of the server. For local testing this is localhost. */
    private static final String HOST = "localhost";

    /** Port number the server is listening on (must match ServerMain). */
    private static final int PORT = 5000;

    /**
     * Starts the client, connects to the server, and opens the GUI.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        try {
            // 1. Set up low-level connection to the server
            ClientConnection conn = new ClientConnection();
            conn.connect(HOST, PORT);

            // 2. Create the high-level API wrapper (no ClientCache / Session anymore)
            ClientAPI api = new ClientAPI(conn);

            // 3. Launch the GUI on the Swing event dispatch thread
            SwingUtilities.invokeLater(() -> { // Swing-thread safety method.
                JFrame frame = new JFrame("Restaurant Reservation Client");

                // If when Screen needs the API, change this to new Screen(api)
                Screen screen = new Screen();
                frame.setContentPane(screen);

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null); // center on screen
                frame.setVisible(true);
            });

        } catch (IOException e) {
            System.err.println("Failed to connect to server at " + HOST + ":" + PORT);
            e.printStackTrace();
        }
    }
}
