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
 * @author zhu1220
 * @version November 20, 2025
 */
public class ServerMain {

    /** Port number the server listens on. */
    public static final int PORT = 500;

    /**
     * Starts the reservation server, listens for incoming connections,
     * and spawns a new thread for each client.
     */
    public static void main(String[] args) {
        ReservationServer server = new ReservationServer();

        System.out.println("Reservation server starting on port " + PORT + "...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Reservation server is now listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept(); 
                System.out.println("Client connected from " + clientSocket.getRemoteSocketAddress());

                Thread handler = new Thread(() -> handleClient(clientSocket, server));
                handler.start();
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
     */
    private static void handleClient(Socket socket, ReservationServer server) {
        try (
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            out.flush();

            while (true) {
                Object obj = in.readObject();

                if (!(obj instanceof CommunicationPacket)) {
                    System.err.println("Received non-packet object: " + obj);
                    continue;
                }

                CommunicationPacket req = (CommunicationPacket) obj;
                CommunicationPacket res = server.handlePacket(req);

                out.writeObject(res);
                out.flush();
            }

        } catch (IOException e) {
            System.out.println("Client disconnected: " + e.getMessage());

        } catch (ClassNotFoundException e) {
            System.err.println("Unknown object from client: " + e.getMessage());

        } finally {
            try {
                if (socket != null && !socket.isClosed()) socket.close();
            } catch (IOException ignored) {}
        }
    }
}
