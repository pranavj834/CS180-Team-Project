import java.io.*;
import java.net.Socket;
import java.util.Objects;

/**
 * Low-level TCP client responsible for sending and receiving
 * {@link CommunicationPacket} objects to and from the server.
 *
 * <p>This class manages the underlying {@link Socket} and the
 * {@link ObjectOutputStream}/{@link ObjectInputStream} pair used for
 * serialization. It is used by higher-level client APIs rather than
 * directly by the GUI.</p>
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author zhu1220, lab sec L23
 * @version November 8, 2025
 */
public class ClientConnection implements Closeable {
    private String host;                 // hostname being used
    private int port;                    // port number being used
    private Socket socket;               // TCP socket to server
    private ObjectOutputStream out;      // stream for sending objects
    private ObjectInputStream in;        // stream for receiving objects
    private String sessionId;            // session ID attached to outgoing packets

    /**
     * Establishes a connection to the given host and port and initializes
     * the object streams.
     *
     * @param host server hostname or IP address
     * @param port server TCP port
     * @throws IOException if connection or stream creation fails
     */
    public void connect(String host, int port) throws IOException {
        this.host = Objects.requireNonNull(host);
        this.port = port;
        this.socket = new Socket(host, port);
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.out.flush();
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Returns whether the socket is currently connected and open.
     *
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    /**
     * Sends a request packet to the server and blocks until the response
     * packet is received.
     *
     * @param req request packet to send
     * @return response packet from the server
     * @throws IOException if communication fails or the response type is invalid
     */
    public CommunicationPacket send(CommunicationPacket req) throws IOException {
        if (!isConnected()) {
            throw new IOException("Not connected");
        }

        // Attach session ID if logged in
        if (sessionId != null && !sessionId.isEmpty()) {
            req.setSessionId(sessionId);
        }

        synchronized (this) {
            out.writeObject(req);
            out.flush();
            try {
                Object obj = in.readObject();
                if (!(obj instanceof CommunicationPacket)) {
                    throw new IOException("Invalid response type");
                }
                return (CommunicationPacket) obj;
            } catch (ClassNotFoundException e) {
                throw new IOException("Class error reading response", e);
            }
        }
    }

    /**
     * Sets the session identifier to be attached to outgoing requests.
     *
     * @param sessionId session token
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * Returns the current session identifier.
     *
     * @return session token, or null if none
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Closes the underlying socket and associated streams.
     *
     * @throws IOException if an I/O error occurs during close
     */
    @Override
    public void close() throws IOException {
        IOException err = null;
        try { if (out != null) out.close(); } catch (IOException e) { err = e; }
        try { if (in != null) in.close(); } catch (IOException e) { if (err == null) err = e; }
        try { if (socket != null) socket.close(); } catch (IOException e) { if (err == null) err = e; }
        if (err != null) throw err;
    }
}
