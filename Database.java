import java.io.*;
import java.util.ArrayList;

/**
 * Stores UserAccount and Reservation info to be
 * accessed by the server.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author chan531, lab sec L23
 * @version November 10, 2025
 */

public class Database implements DatabaseInterface, Serializable {
    private ArrayList<UserAccount> accounts;
    private ArrayList<Reservation> reservations;

    // initializes arraylists
    public Database() {
        accounts = new ArrayList<>();
        reservations = new ArrayList<>();
    }

    // adds account
    public synchronized boolean addAccount(UserAccount account) {
        if (accounts.contains(account)) {
            return false;
        }
        accounts.add(account);
        return true;
    }

    // adds reservation
    public synchronized boolean addReservation(UserAccount account, Reservation reservation) {
        for (Reservation r: reservations) {
            if (r.getDate().equals(reservation.getDate()) && r.getTime().equals(reservation.getTime())) {
                return false;
            }
        }

        // the account needs to exist to add a reservation under it
        if (!accounts.contains(account)) {
            return false;
        }

        accounts.get(accounts.indexOf(account)).addReservation(reservation);
        reservations.add(reservation);
        return true;
    }

    // deletes account
    public synchronized boolean deleteAccount(UserAccount account) {
        if (accounts.contains(account)) {
            for (Reservation r: account.getReservations()) {
                // must remove all associated reservations with the account
                reservations.remove(r);
            }
            accounts.remove(account);
            return true;
        }
        return false;
    }

    // deletes reservation
    public synchronized boolean deleteReservation(UserAccount account, Reservation reservation) {
        // fail if account doesn't exist
        int index = accounts.indexOf(account);
        if (index == -1) {
            return false;
        }

        // returns true if reservation was found under that account and removed, false otherwise
        boolean removed = accounts.get(index).removeReservation(reservation);
        if (removed) {
            reservations.remove(reservation); // only remove if the given account created the reservation
            return true;
        }
        return false;
    }

    // getters
    public synchronized ArrayList<UserAccount> getAccounts() {
        return accounts;
    }
    public synchronized ArrayList<Reservation> getReservations() {
        return reservations;
    }

    public synchronized void writeToFile() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("database.txt"));
            oos.writeObject(accounts);
            oos.writeObject(reservations);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public synchronized void readFromFile() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("database.txt"));
            accounts = (ArrayList<UserAccount>) ois.readObject();
            reservations = (ArrayList<Reservation>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        String str = "";
        str += "Accounts:\n";
        for (UserAccount account: accounts) {
            str += account + "\n";
        }
        str += "\nReservations:\n";
        for (Reservation reservation: reservations) {
            str += reservation + "\n";
        }
        return str;
    }
}
