import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;

/**
 * Low-level TCP client responsible for sending and receiving
 * {@link CommunicationPacket} objects to and from the server.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author zhu1220, lab sec L23
 * @version November 8, 2025
 */
public class ClientConnection implements Closeable {
    private String host;
    private int port;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public void connect(String host, int port) throws IOException {
        this.host = Objects.requireNonNull(host);
        this.port = port;
        this.socket = new Socket(host, port);
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.out.flush();
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    public CommunicationPacket send(CommunicationPacket req) throws IOException {
        if (!isConnected()) {
            throw new IOException("Not connected");
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

    @Override
    public void close() throws IOException {
        IOException err = null;
        try { if (out != null) out.close(); } catch (IOException e) { err = e; }
        try { if (in != null) in.close(); } catch (IOException e) { if (err == null) err = e; }
        try { if (socket != null) socket.close(); } catch (IOException e) { if (err == null) err = e; }
        if (err != null) throw err;
    }
}
