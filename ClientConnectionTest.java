import org.junit.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.*;

public class ClientConnectionTest {

    @Test(timeout = 2000)
    public void testConnectAndIsConnected() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            int port = serverSocket.getLocalPort();

            // Start server accept in background
            Thread serverThread = new Thread(() -> {
                try (Socket s = serverSocket.accept()) {
                    // Just accept and immediately close
                    // Create streams to complete the handshake
                    ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                    out.flush();
                    ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                    // We don't actually read/write here
                } catch (IOException ignored) { }
            });
            serverThread.setDaemon(true);
            serverThread.start();

            ClientConnection conn = new ClientConnection();
            conn.connect("localhost", port);

            assertTrue("Client should be connected after connect()", conn.isConnected());

            conn.close();
        }
    }

    @Test(timeout = 3000)
    public void testSendAndReceivePacket() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            int port = serverSocket.getLocalPort();

            // Server thread that receives a packet and sends back a response
            Thread serverThread = new Thread(() -> {
                try (
                        Socket s = serverSocket.accept();
                        ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                        ObjectInputStream in = new ObjectInputStream(s.getInputStream())
                ) {
                    out.flush();

                    Object obj = in.readObject();
                    if (obj instanceof CommunicationPacket) {
                        CommunicationPacket req = (CommunicationPacket) obj;

                        // Simple echo-like response with different message
                        CommunicationPacket res = new CommunicationPacket()
                                .setPacketType(req.getPacketType())
                                .setPayload("server-reply")
                                .setErrorCode(ErrorCode.NONE)
                                .setMessage("OK from server");

                        out.writeObject(res);
                        out.flush();
                    }
                } catch (Exception ignored) { }
            });
            serverThread.setDaemon(true);
            serverThread.start();

            // Client side
            ClientConnection conn = new ClientConnection();
            conn.connect("localhost", port);

            CommunicationPacket request = new CommunicationPacket()
                    .setPacketType(PacketType.LOGIN)
                    .setPayload(new String[]{"user", "pw"});

            CommunicationPacket response = conn.send(request);

            assertNotNull(response);
            assertEquals(PacketType.LOGIN, response.getPacketType());
            assertEquals(ErrorCode.NONE, response.getErrorCode());
            assertEquals("OK from server", response.getMessage());
            assertEquals("server-reply", response.getPayload());

            conn.close();
        }
    }
}
