import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Handles communication with a single client over a socket.
 *
 * Reads CommunicationPacket requests in a loop, delegates processing to
 * ReservationServer, and sends back response packets.
 * * @author zhu1220, lab sec L23
 * * @version November 8, 2025
 */
public class ClientHandler implements Runnable {
    private final Socket socket;               // client socket
    private final ReservationServer server;    // shared server core
    private ObjectInputStream in;              // read packets from client
    private ObjectOutputStream out;            // write packets to client

    /**
     * Constructs a new handler for the given client socket and server state.
     *
     * @param socket connected client socket
     * @param server shared ReservationServer instance
     */
    public ClientHandler(Socket socket, ReservationServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            // Important: create ObjectOutputStream first, flush, then ObjectInputStream
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            // Main read/process loop
            while (true) {
                Object obj = in.readObject();
                if (!(obj instanceof CommunicationPacket)) {
                    System.err.println("Received invalid object from client: " + obj);
                    continue; // ignore or break, depending on how strict you want to be
                }

                CommunicationPacket req = (CommunicationPacket) obj;

                // Let the ReservationServer handle the business logic
                CommunicationPacket res = server.handlePacket(req);

                // Send the response back
                out.writeObject(res);
                out.flush();
            }
        } catch (IOException e) {
            System.out.println("Client disconnected: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Received unknown class from client: " + e.getMessage());
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException ignored) { }
            try {
                if (out != null) out.close();
            } catch (IOException ignored) { }
            try {
                if (socket != null && !socket.isClosed()) socket.close();
            } catch (IOException ignored) { }
        }
    }
}
