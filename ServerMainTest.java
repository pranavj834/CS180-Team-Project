import org.junit.Test;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.*;

/**
 * Tests that ServerMain successfully accepts a connection and can process
 * a CommunicationPacket through ReservationServer.
 */
public class ServerMainTest {

    /**
     * This test does NOT run ServerMain.main() because that would block forever.
     *
     * Instead, we simulate the same behavior:
     *  - Start a temporary ServerSocket on a free port
     *  - Accept exactly one client
     *  - Process ONE packet using ReservationServer.handlePacket()
     */
    @Test(timeout = 4000)
    public void testServerAcceptsAndProcessesOnePacket() throws Exception {
        ReservationServer server = new ReservationServer();

        // Create temp server on random free port
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            int port = serverSocket.getLocalPort();

            // Start "ServerMain-like" handler thread
            Thread serverThread = new Thread(() -> {
                try {
                    Socket client = serverSocket.accept();

                    ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                    out.flush();
                    ObjectInputStream in = new ObjectInputStream(client.getInputStream());

                    Object incoming = in.readObject();
                    if (incoming instanceof CommunicationPacket) {
                        CommunicationPacket req = (CommunicationPacket) incoming;
                        CommunicationPacket res = server.handlePacket(req);

                        out.writeObject(res);
                        out.flush();
                    }

                    in.close();
                    out.close();
                    client.close();
                } catch (Exception ignored) {}
            });
            serverThread.setDaemon(true);
            serverThread.start();

            // --- CLIENT SIDE ---
            try (
                    Socket socket = new Socket("localhost", port);
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
            ) {
                out.flush();

                // Build a request: GET_BALANCE (always safe)
                CommunicationPacket req = new CommunicationPacket()
                        .setPacketType(PacketType.GET_BALANCE);

                out.writeObject(req);
                out.flush();

                Object obj = in.readObject();
                assertTrue(obj instanceof CommunicationPacket);

                CommunicationPacket res = (CommunicationPacket) obj;

                assertEquals(PacketType.GET_BALANCE, res.getPacketType());
                assertEquals(ErrorCode.NONE, res.getErrorCode());
                assertEquals(0.0, (double) res.getPayload(), 0.0001);
                assertEquals("Balance retrieved", res.getMessage());
            }
        }
    }
}
