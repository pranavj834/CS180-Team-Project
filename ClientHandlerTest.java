import org.junit.Test;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.*;

public class ClientHandlerTest {

    @Test(timeout = 4000)
    public void testHandlerProcessesLoginPacket() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            int port = serverSocket.getLocalPort();
            ReservationServer server = new ReservationServer();

            // Thread to accept client and run ClientHandler
            Thread handlerThread = new Thread(() -> {
                try {
                    Socket clientSocket = serverSocket.accept();
                    ClientHandler handler = new ClientHandler(clientSocket, server);
                    handler.run(); // run in this thread
                } catch (Exception ignored) { }
            });
            handlerThread.setDaemon(true);
            handlerThread.start();

            // Client side: connect directly with raw Socket/streams
            try (
                    Socket socket = new Socket("localhost", port);
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
            ) {
                out.flush();

                CommunicationPacket loginReq = new CommunicationPacket()
                        .setPacketType(PacketType.LOGIN)
                        .setPayload(new String[]{"user1", "pw1"});

                out.writeObject(loginReq);
                out.flush();

                Object obj = in.readObject();
                assertTrue("Response should be a CommunicationPacket", obj instanceof CommunicationPacket);

                CommunicationPacket res = (CommunicationPacket) obj;
                assertEquals(PacketType.LOGIN, res.getPacketType());
                // We don't enforce success here because auth depends on DB contents,
                // but we at least check that the handler/server produced some response.
            }
        }
    }
}
