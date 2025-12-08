import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Provides server-side functionality through the
 * command-line. Retrieves information and sends information to the client.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author chan531, lab sec L23
 * @version December 6, 2025
 */

public class Server implements Runnable, ServerInterface {
    //instance variables
    private static final int PORT = 500;
    private static Database db;

    //constructor
    public Server() {
        db = new Database("accounts.txt", "reservations.txt");
    }

    //connects with client and handles actions by the user
    private void handleClient(Socket socket, Server server) {
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.flush();
            while (true) {
                Object obj = in.readObject();
                if (!(obj instanceof Packet)) {
                    System.err.println("Received non-packet object: " + obj);
                    continue;
                }

                Packet packet = (Packet) obj;
                System.out.println("Received packet with type " + packet.getType().toString());
                if (packet.getType() == PacketType.LOGIN) {
                    UserAccount account = (UserAccount) packet.getObj()[0];
                    UserAccount returnAccount = null;
                    for (UserAccount acct: db.getAccounts()) {
                        if (acct.equals(account)) {
                            returnAccount = acct;
                            break;
                        }
                    }
                    out.writeObject(new Packet(PacketType.ADD_ACCOUNT, new Object[]{returnAccount}));
                } else if (packet.getType() == PacketType.ADD_ACCOUNT) {
                    UserAccount account = (UserAccount) packet.getObj()[0];
                    boolean added = db.addAccount(account);
                    out.writeObject(new Packet(PacketType.ADD_ACCOUNT, new Object[]{added}));

                } else if (packet.getType() == PacketType.ADD_RESERVATION) {
                    UserAccount account = (UserAccount) packet.getObj()[0];
                    Reservation reservation = (Reservation) packet.getObj()[1];
                    boolean added = db.addReservation(account, reservation);
                    out.writeObject(new Packet(PacketType.ADD_RESERVATION, new Object[]{added}));

                } else if (packet.getType() == PacketType.DELETE_ACCOUNT) {
                    UserAccount account = (UserAccount) packet.getObj()[0];
                    boolean deleted = db.deleteAccount(account);
                    out.writeObject(new Packet(PacketType.ADD_RESERVATION, new Object[]{deleted}));

                } else if (packet.getType() == PacketType.DELETE_RESERVATION) {
                    System.out.println("server");
                    UserAccount account = (UserAccount) packet.getObj()[0];
                    Reservation reservation = (Reservation) packet.getObj()[1];
                    System.out.println(account);
                    System.out.println(reservation);
                    boolean deleted = db.deleteReservation(account, reservation);
                    System.out.println("deleted: " + deleted);
                    out.writeObject(new Packet(PacketType.ADD_RESERVATION, new Object[]{deleted}));
                } else if (packet.getType() == PacketType.GET_SEAT_STATUSES_AT_TIME) {
                    String timestamp = (String) packet.getObj()[0];
                    boolean[] statuses = db.getSeatStatusesAtTime(timestamp);
                    ArrayList<Integer> availableSeats = new ArrayList<>();
                    for (int i = 0; i < statuses.length; i++) {
                        if (!statuses[i]) {
                            availableSeats.add(i);
                        }
                    }
                    // String msg = "Available Seats: " + availableSeats;
                    out.writeObject(new Packet(PacketType.GET_SEAT_STATUSES_AT_TIME, new Object[]{statuses}));

                } else if (packet.getType() == PacketType.GET_ACCT_RESERVATIONS) {
                    UserAccount account = (UserAccount) packet.getObj()[0];
                    out.writeObject(new Packet(PacketType.ADD_RESERVATION,
                            new Object[]{db.getAccountReservations(account)}));

                } else if (packet.getType() == PacketType.TO_STRING) {
                    out.writeObject(new Packet(PacketType.TO_STRING, new Object[]{db.toString()}));

                } else {
                    System.out.println("Packet type not recognized.");
                    out.writeObject(new Packet(PacketType.INVALID));
                }
                out.reset();
                out.flush();
            }

        } catch (IOException e) {
            System.out.println("Client disconnected: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Unknown object from client: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unknown error: " + e.getMessage());
        } finally {
            try {
                if (socket != null && !socket.isClosed()) socket.close();
            } catch (IOException ignored) {
                System.err.println("Error returned while closing socket: " + ignored.getMessage());
            }
        }
    }

    private void startServer() {
        Thread t = new Thread(this);
        t.start();
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.startServer();
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getRemoteSocketAddress());

                Thread handler = new Thread(() -> handleClient(clientSocket, this));
                handler.start();
            }
        } catch (IOException e) {
            System.err.println("Fatal server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
