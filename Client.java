import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Provides client-side functionality through the
 * command-line. Communicates to the server.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author chan531, lab sec L23
 * @version November 24, 2025
 */

public class Client implements ClientInterface {

    private static final String HOST = "localhost";
    private static final int PORT = 500;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean loggedIn;
    private UserAccount currentAccount;

    public Client() {
        loggedIn = false;
        currentAccount = null;
        
        try {
            socket = new Socket(HOST, PORT);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
        } catch (IOException e) {
            System.err.println("IOException occurred while creating client.");
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Client networkClient = new Client();
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Restaurant Reservation Client");

            // TODO: update Screen constructor when you want to pass api.
            Screen screen = new Screen(networkClient);

            frame.setContentPane(screen);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    public UserAccount login(String username, String password) {
        currentAccount = new UserAccount(username, password,
                "placeholder", "p@p.com");
        Packet request = new Packet(PacketType.LOGIN, new Object[]{currentAccount});
        Packet response = null;
        try {
            out.writeObject(request);
            response = (Packet) in.readObject();
        } catch (IOException e) {
            System.err.println("Communication error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Serialization error: " + e.getMessage());
        }

        currentAccount = (UserAccount) response.getObj()[0];
        if (currentAccount != null) {
            loggedIn = true;
            return currentAccount;
        } else {
            return null;
        }
    }


    public boolean addAccount(String username, String password, String fullName, String email) {
        UserAccount newAcct = new UserAccount(username, password, fullName, email);
        Packet request = new Packet(PacketType.ADD_ACCOUNT, new Object[]{newAcct});
        Packet response = null;
        try {
            out.writeObject(request);
            response = (Packet) in.readObject();
        } catch (IOException e) {
            System.err.println("Communication error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Serialization error: " + e.getMessage());
        }

        if (response != null && response.getObj() != null) {
            boolean added = (boolean) response.getObj()[0];
            return added;
        } else {
            System.err.println("Null packet from server during account login.");
            return false;
        }
    }

    public boolean addReservation(String name, String timestamp,
                                  int partySize, ArrayList<Integer> seats) {
        Reservation reservation = new Reservation(name, currentAccount.getUsername(), timestamp, partySize, seats);
        Packet request = new Packet(PacketType.ADD_RESERVATION, new Object[]{currentAccount, reservation});
        Packet response = null;
        try {
            out.writeObject(request);
            response = (Packet) in.readObject();
        } catch (IOException e) {
            System.err.println("Communication error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Serialization error: " + e.getMessage());
        }

        if (response.getObj() != null && (boolean) response.getObj()[0]) {
            return true;
        } else {
            return false;
        }
    }

    // signs out of current account. returns true if successfully deleted and vice versa
    public boolean deleteAccount() {
        Packet request = new Packet(PacketType.DELETE_ACCOUNT, new Object[]{currentAccount});
        Packet response = null;
        try {
            out.writeObject(request);
            response = (Packet) in.readObject();
        } catch (IOException e) {
            System.err.println("Communication error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Serialization error: " + e.getMessage());
        }

        loggedIn = false;
        if (response.getObj() != null && (boolean) response.getObj()[0]) {
            System.out.println("Account successfully deleted.");
            currentAccount = null;
            return true;
        } else {
            System.out.println("Failed to delete account.");
            return false;
        }
    }

    public boolean deleteReservation(String timestamp,
                                     int partySize, ArrayList<Integer> seats) {
        Reservation reservationToDelete = new Reservation(currentAccount.getFullName(),
                currentAccount.getUsername(), timestamp, partySize, seats);

        Packet request = new Packet(PacketType.DELETE_RESERVATION, new Object[]{currentAccount, reservationToDelete});
        Packet response = null;
        try {
            out.writeObject(request);
            response = (Packet) in.readObject();
        } catch (IOException e) {
            System.err.println("Communication error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Serialization error: " + e.getMessage());
        }

        if (response.getObj() != null && (boolean) response.getObj()[0]) {
            return true;
        } else {
            return false;
        }
    }

    public boolean[] updateSeatStatuses(String timestamp) {
        Packet request = new Packet(PacketType.GET_SEAT_STATUSES_AT_TIME, new Object[]{timestamp});
        Packet response = null;
        try {
            out.writeObject(request);
            response = (Packet) in.readObject();
        } catch (IOException e) {
            System.err.println("Communication error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Serialization error: " + e.getMessage());
        }

        if (response.getObj() != null && response.getObj()[0] instanceof boolean[]) {
            return (boolean[]) response.getObj()[0];
        } else {
            System.out.println("Invalid seat statuses");
            return null;
        }
    }

    public String getReservations() {
        Packet request = new Packet(PacketType.GET_ACCT_RESERVATIONS, new Object[]{currentAccount});
        Packet response = null;
        try {
            out.writeObject(request);
            response = (Packet) in.readObject();
        } catch (IOException e) {
            System.err.println("Communication error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Serialization error: " + e.getMessage());
        }

        String str = "";
        if (response.getObj() != null) {
            ArrayList<Reservation> reservations = (ArrayList<Reservation>) response.getObj()[0];
            if (reservations.isEmpty()) {
                str += "0\n";
            } else {
                str += reservations.size();
                for (Reservation r : reservations) {
                    str = str + r + "\n";
                }
            }
        } else {
            str += "-1";
        }
        return str;
    }

    public String printDatabase() {
        Packet request = new Packet(PacketType.TO_STRING, new Object[]{});
        Packet response = null;
        try {
            out.writeObject(request);
            response = (Packet) in.readObject();
        } catch (IOException e) {
            System.err.println("Communication error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Serialization error: " + e.getMessage());
        }

        String str = "";
        if (response.getObj() != null) {
            str += response.getObj()[0];
        } else {
            str += "Error with returning database details.";
        }
        return str;
    }
}



//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        UserAccount currentAccount = null;
//
//        try (Socket clientSocket = new Socket(HOST, PORT)) {
//            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
//            out.flush(); // flush header
//            Objectin in = new Objectin(clientSocket.getin());
//
//            boolean loggedIn = false;
//
//            while (true) {
//                try {
//                    if (!loggedIn) {
//                        System.out.println("\n--- Menu (Logged Out) ---");
//                        System.out.println("0 - Login");
//                        System.out.println("1 - Add/Register UserAccount");
//                        System.out.println("7 - Print Full Database Details (Server Debug)");
//                        System.out.println("8 - Exit");
//                    } else {
//                        System.out.println("\n--- Menu (Logged In as " + currentAccount.getUsername() + ") ---");
//                        System.out.println("2 - Add Reservation");
//                        System.out.println("3 - Delete UserAccount");
//                        System.out.println("4 - Delete Reservation");
//                        System.out.println("5 - Get Seat Statuses (Check Availability)");
//                        System.out.println("6 - Get My Reservations");
//                        System.out.println("7 - Print Full Database Details (Server Debug)");
//                        System.out.println("8 - Exit");
//                    }
//                    System.out.println("Enter choice: ");
//
//                    String input = sc.nextLine();
//                    if (input.isEmpty()) continue;
//                    int choice = Integer.parseInt(input);
//
//                    // --- LOGIC FOR LOGGED OUT USERS ---
//                    if (!loggedIn) {
//                        if (choice == 0) {
//                            System.out.println("Enter username (e.g., johndoe): ");
//                            String username = sc.nextLine().trim();
//                            System.out.println("Enter password: ");
//                            String password = sc.nextLine().trim();
//                            currentAccount = new UserAccount(username, password,
//                                    "placeholder", "p@p.com");
//
//                            Packet request = new Packet(PacketType.LOGIN, new Object[]{currentAccount});
//                            out.writeObject(request);
//
//                            Packet response = (Packet) in.readObject();
//                            currentAccount = (UserAccount) response.getObj()[0];
//                            if (currentAccount != null) {
//                                loggedIn = true;
//                                System.out.println("Logged in as " + username + ".");
//                            } else {
//                                System.out.println("Account not found.");
//                            }
//
//                        } else if (choice == 1) { // Add UserAccount (REGISTER)
//                            System.out.println("Enter username: (e.g., johndoe)");
//                            String username = sc.nextLine().trim();
//                            System.out.println("Enter password: ");
//                            String password = sc.nextLine().trim();
//                            System.out.println("Enter full name: (e.g., JohnDoe)");
//                            String fullName = sc.nextLine().trim();
//                            System.out.println("Enter email: ");
//                            String email = sc.nextLine().trim();
//
//                            UserAccount newAcct = new UserAccount(username, password, fullName, email);
//                            Packet request = new Packet(PacketType.ADD_ACCOUNT, new Object[]{newAcct});
//                            out.writeObject(request);
//
//                            Packet response = (Packet) in.readObject();
//                            if (response.getObj() != null && (boolean) response.getObj()[0]) {
//                                System.out.println("Account successfully added (registered).");
//                            } else {
//                                System.out.println("Failed to add account (username might be taken).");
//                            }
//                        } else if (choice == 7) { // Print Database Details (TO_STRING)
//                            Packet request = new Packet(PacketType.TO_STRING, new Object[]{});
//                            out.writeObject(request);
//
//                            Packet response = (Packet) in.readObject();
//                            if (response.getObj() != null) {
//                                System.out.println("\n--- Full Database Snapshot ---");
//                                System.out.println(response.getObj()[0]);
//                                System.out.println("------------------------------");
//                            } else {
//                                System.out.println("Failed to retrieve database details.");
//                            }
//
//                        } else if (choice == 8) {
//                            break; // Exit
//                        } else {
//                            System.out.println("Unrecognized input. Please try again.");
//                        }
//
//                    // --- LOGIC FOR LOGGED IN USERS ---
//                    } else {
//                        if (choice == 2) { // Add Reservation
//                            System.out.println("Enter reservation details:");
//                            System.out.println("Name: (e.g., JohnDoe)");
//                            String name = sc.nextLine().trim();
//
//                            String username = currentAccount.getUsername();
//
//                            System.out.println("Date (YYYY-MM-DD): ");
//                            String date = sc.nextLine().trim();
//                            System.out.println("Time (HH:MM): ");
//                            String time = sc.nextLine().trim();
//                            System.out.println("Party Size: ");
//                            int partySize = Integer.parseInt(sc.nextLine().trim());
//                            System.out.println("Seat numbers (space-separated, e.g., 1 2 3): ");
//                            ArrayList<Integer> seats = new ArrayList<>();
//                            String[] seatInfo = sc.nextLine().trim().split("\\s+");
//                            for (String seat : seatInfo) {
//                                seats.add(Integer.parseInt(seat));
//                            }
//
//                            Reservation reservation = new Reservation(name, username, date, time, partySize, seats);
//                            Packet request = new Packet(PacketType.ADD_RESERVATION, new Object[]{currentAccount, reservation});
//                            out.writeObject(request);
//
//                            Packet response = (Packet) in.readObject();
//                            if (response.getObj() != null && (boolean) response.getObj()[0]) {
//                                System.out.println("Reservation successfully added.");
//                            } else {
//                                System.out.println("Failed to add reservation (date/time conflict or account mismatch).");
//                            }
//
//                        } else if (choice == 3) { // Delete UserAccount
//                            Packet request = new Packet(PacketType.DELETE_ACCOUNT, new Object[]{currentAccount});
//                            out.writeObject(request);
//
//                            Packet response = (Packet) in.readObject();
//                            if (response.getObj() != null && (boolean) response.getObj()[0]) {
//                                System.out.println("Account successfully deleted.");
//                                currentAccount = null;
//                                loggedIn = false;
//                            } else {
//                                System.out.println("Failed to delete account.");
//                            }
//
//                        } else if (choice == 4) { // Delete Reservation
//                            System.out.println("Enter reservation details to delete:");
//                            System.out.println("Date (YYYY-MM-DD): ");
//                            String date = sc.nextLine().trim();
//                            System.out.println("Time (HH:MM): ");
//                            String time = sc.nextLine().trim();
//                            System.out.println("Party Size: ");
//                            int partySize = Integer.parseInt(sc.nextLine().trim());
//                            System.out.println("Seat numbers (space-separated): ");
//                            ArrayList<Integer> seats = new ArrayList<>();
//                            String[] seatInfo = sc.nextLine().trim().split("\\s+");
//                            for (String seat : seatInfo) {
//                                seats.add(Integer.parseInt(seat));
//                            }
//
//                            // Note: We need a dummy Reservation object that matches the one to delete.
//                            Reservation reservationToDelete = new Reservation(currentAccount.getFullName(), currentAccount.getUsername(), date, time, partySize, seats);
//
//                            Packet request = new Packet(PacketType.DELETE_RESERVATION, new Object[]{currentAccount, reservationToDelete});
//                            out.writeObject(request);
//
//                            Packet response = (Packet) in.readObject();
//                            if (response.getObj() != null && (boolean) response.getObj()[0]) {
//                                System.out.println("Reservation successfully deleted.");
//                            } else {
//                                System.out.println("Failed to delete reservation.");
//                            }
//
//                        } else if (choice == 5) { // Get Seat Statuses (Availability)
//                            System.out.println("Enter timestamp to check (YYYY-MM-DD HH:MM): ");
//                            String timestamp = sc.nextLine().trim();
//
//                            Packet request = new Packet(PacketType.GET_SEAT_STATUSES_AT_TIME, new Object[]{timestamp});
//                            out.writeObject(request);
//
//                            Packet response = (Packet) in.readObject();
//                            if (response.getObj() != null) {
//                                System.out.println(response.getObj()[0]);
//                            } else {
//                                System.out.println("Failed to retrieve seat statuses.");
//                            }
//
//                        } else if (choice == 6) { // Get Account's Reservations
//                            Packet request = new Packet(PacketType.GET_ACCT_RESERVATIONS, new Object[]{currentAccount});
//                            out.writeObject(request);
//
//                            Packet response = (Packet) in.readObject();
//                            if (response.getObj() != null) {
//                                List<Reservation> reservations = (List<Reservation>) response.getObj()[0];
//                                System.out.println("Your Reservations:");
//                                if (reservations.isEmpty()) {
//                                    System.out.println(" - None.");
//                                } else {
//                                    for (Reservation r : reservations) {
//                                        System.out.println(" - " + r);
//                                    }
//                                }
//                            } else {
//                                System.out.println("Failed to retrieve reservations.");
//                            }
//
//                        } else if (choice == 7) { // Print Database Details (TO_STRING)
//                            Packet request = new Packet(PacketType.TO_STRING, new Object[]{});
//                            out.writeObject(request);
//
//                            Packet response = (Packet) in.readObject();
//                            if (response.getObj() != null) {
//                                System.out.println("\n--- Full Database Snapshot ---");
//                                System.out.println(response.getObj()[0]);
//                                System.out.println("------------------------------");
//                            } else {
//                                System.out.println("Failed to retrieve database details.");
//                            }
//
//                        } else if (choice == 8) {
//                            break; // Exit
//                        } else {
//                            System.out.println("Unrecognized input. Please try again.");
//                        }
//                    }
//
//                } catch (NumberFormatException e) {
//                    System.out.println("Number expected.");
//                } catch (IllegalArgumentException e) {
//                    e.printStackTrace();
//                    //System.err.println("Input Error: " + e.getMessage());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    //System.err.println("Communication error: " + e.getMessage());
//                    break;
//                } catch (ClassNotFoundException e) {
//                    System.err.println("Serialization error: Could not read object from server.");
//                    break;
//                }
//            }
//
//        } catch (IOException e) {
//            System.err.println("Failed to connect to server at " + HOST + ":" + PORT);
//            System.err.println("Ensure the server is running first.");
//        }
//    }
