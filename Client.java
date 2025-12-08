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
 * @version December 7, 2025
 */

public class Client implements ClientInterface {

    //instance variables; the first five are to start the program
    private static final String HOST = "localhost";
    private static final int PORT = 500;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean loggedIn; //to make sure the user is logged in to access other facilities
    private UserAccount currentAccount; //sets the account for storing reservations in the correct account

    //constructor
    public Client() {
        loggedIn = false;
        currentAccount = null;

        //server and client connect
        try {
            socket = new Socket(HOST, PORT);

            //data from files that store the database are loaded
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
        } catch (IOException e) { //happens if client is started before server
            System.err.println("IOException occurred while creating client.");
            System.err.println(e.getMessage());
        }
    }
    //starts the GUI and shows the login GUI
    public static void main(String[] args) {
        Client networkClient = new Client();
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Restaurant Reservation Client");
            Screen screen = new Screen(networkClient);
            frame.setContentPane(screen);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    //checks if the user entered their username and password details properly
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

    //adds account when registered
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

    /*adds a reservation when user wants to book reservation; checks if the booking is
    between 9 AM to 9 PM because those are the hours of operation
     */
    public boolean addReservation(String name, String timestamp,
                                  int partySize, ArrayList<Integer> seats) {
        try {
            if (timestamp == null || timestamp.length() < 16) {
                return false;
            }

            String timePart = timestamp.substring(11, 16);
            String hourString = timePart.substring(0, 2);
            int hour = Integer.parseInt(hourString);

            if (hour < 9 || hour > 21) {
                return false;
            }

            if (hour == 21) {
                String minuteString = timePart.substring(3, 5);
                int minute = Integer.parseInt(minuteString);
                if (minute > 0) {
                    return false;
                }
            }

        } catch (NumberFormatException e) {
            return false;
        }
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

    public boolean deleteReservation(String name, String timestamp,
                                     int partySize, ArrayList<Integer> seats) {
        System.out.println("client delete reservation");
        Reservation reservationToDelete = new Reservation(name,
                currentAccount.getUsername(), timestamp, partySize, seats);
        System.out.println(reservationToDelete);
        System.out.println(currentAccount.getUsername());
        System.out.println(seats);

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
        //System.out.println(response.getObj()[0]);
        if (response.getObj() != null && (boolean) response.getObj()[0]) {
            return true;
        } else {
            return false;
        }
    }


    //updates seats after a reservation is made and some seats are booked so that people don't book already booked seats
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

    //gets reservations for the user to see what reservation they want to delete
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
                str += reservations.size() + "\n";
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