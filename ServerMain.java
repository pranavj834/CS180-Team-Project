import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Entry point for running the restaurant reservation server.
 *
 * <p>This class opens a {@link ServerSocket} on a fixed port and waits for
 * client connections. For each client that connects, it starts a new handler
 * thread which exchanges {@link CommunicationPacket} objects with the client
 * and delegates all business logic to a shared {@link ReservationServer}
 * instance.</p>
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author zhu1220, lab sec L23
 * @version November 10, 2025
 */
public class ServerMain {

    /** Port number the server listens on. The client must connect to the same port. */
    public static final int PORT = 5000;

    /**
     * Starts the reservation server, listens for incoming connections,
     * and spawns a new thread for each client.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        // Shared core server object that holds database, sessions, layout, etc.
        ReservationServer server = new ReservationServer();

        System.out.println("Reservation server starting on port " + PORT + "...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Reservation server is now listening on port " + PORT);

            // Main accept loop: runs forever, accepting new clients
            while (true) {
                Socket clientSocket = serverSocket.accept(); // wait for a client
                System.out.println("Client connected from " + clientSocket.getRemoteSocketAddress());

                // For each client, start a new handler thread
                Thread t = new Thread(() -> handleClient(clientSocket, server));
                t.start();
            }
        } catch (IOException e) {
            System.err.println("Fatal server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles communication with a single client.
     *
     * <p>Creates object streams for the socket, then repeatedly reads
     * {@link CommunicationPacket} requests, forwards them to the
     * {@link ReservationServer}, and writes back the response packets.</p>
     *
     * @param socket connected client socket
     * @param server shared ReservationServer instance
     */
    private static void handleClient(Socket socket, ReservationServer server) {
        try (
                // Important: create ObjectOutputStream first, then flush,
                // then create ObjectInputStream to avoid stream deadlocks.
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            out.flush();

            while (true) {
                // Read an object sent by the client
                Object obj = in.readObject();

                if (!(obj instanceof CommunicationPacket)) {
                    System.err.println("Received non-packet object from client: " + obj);
                    continue; // or break if you want to terminate the connection on invalid input
                }

                CommunicationPacket req = (CommunicationPacket) obj;

                // Let the core server logic handle the request
                CommunicationPacket res = server.handlePacket(req);

                // Send the response back to the client
                out.writeObject(res);
                out.flush();
            }
        } catch (IOException e) {
            System.out.println("Client disconnected: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Received unknown class from client: " + e.getMessage());
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException ignored) {
                // ignore close errors
            }
        }
    }
}
