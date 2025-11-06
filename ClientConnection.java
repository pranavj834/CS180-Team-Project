/*
What it is: The socket client.

Key methods:

connect(host, port) — opens socket & object streams.

send(CommunicationPacket) — writes the packet, blocks for response, returns the response CommunicationPacket.

setSessionId() — stored so every request automatically carries your session.

Why it matters: Single place that knows how to talk over the wire.
 */

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class ClientConnection implements Closeable {
    private String host;
    private int port;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String sessionId;

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
        if (!isConnected()) throw new IOException("Not connected");
        if (sessionId != null && !sessionId.isEmpty()) req.setSessionId(sessionId);

        synchronized (this) {
            out.writeObject(req);
            out.flush();
            try {
                Object obj = in.readObject();
                if (!(obj instanceof CommunicationPacket)) throw new IOException("Invalid response type");
                return (CommunicationPacket) obj;
            } catch (ClassNotFoundException e) {
                throw new IOException("Class error reading response", e);
            }
        }
    }

    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public String getSessionId() { return sessionId; }

    @Override public void close() throws IOException {
        IOException err = null;
        try { if (out != null) out.close(); } catch (IOException e) { err = e; }
        try { if (in != null) in.close(); } catch (IOException e) { if (err == null) err = e; }
        try { if (socket != null) socket.close(); } catch (IOException e) { if (err == null) err = e; }
        if (err != null) throw err;
    }
}
