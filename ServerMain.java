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
public class ServerMain implements Runnable, ServerMainInterface {

    /** Port number the server listens on. */
    public static final int PORT = 500;

    private final int port;
    private final ReservationServer server;

    /**
     * Constructs a server that listens on the given port.
     */
    public ServerMain(int port) {
        this.port = port;
        this.server = new ReservationServer();
    }

    /**
     * Convenience method required by ServerMainInterface.
     * Starts the server loop on a new thread.
     */
    @Override
    public void startServer() {
        Thread t = new Thread(this);
        t.start();
    }

    /**
     * Runnable entry point: opens the ServerSocket and accepts clients.
     */
    @Override
    public void run() {
        System.out.println("Reservation server starting on port " + port + "...");

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Reservation server is now listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getRemoteSocketAddress());

                // Spawn a handler for each client
                Thread handler = new Thread(() -> handleClient(clientSocket, server));
                handler.start();
            }
        } catch (IOException e) {
            System.err.println("Fatal server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Classic static main so you can still run the server directly.
     */
    public static void main(String[] args) {
        ServerMain mainServer = new ServerMain(PORT);
        // For the normal program, we just run synchronously:
        mainServer.run();
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
