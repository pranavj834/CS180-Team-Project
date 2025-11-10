import java.util.ArrayList;

/**
 * Stores UserAccount and Reservation info to be
 * accessed by the server.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author Ryan Chan, lab sec L23
 * @version November 10, 2025
 */

public class Database implements DatabaseInterface {
    private ArrayList<UserAccount> accounts;
    private ArrayList<Reservation> reservations;

    // initializes arraylists
    public Database() {
        accounts = new ArrayList<>();
        reservations = new ArrayList<>();
    }

    // adds account
    public boolean addAccount(UserAccount account) {
        if (accounts.contains(account)) {
            return false;
        }
        accounts.add(account);
        return true;
    }

    // adds reservation
    public boolean addReservation(UserAccount account, Reservation reservation) {
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
    public boolean deleteAccount(UserAccount account) {
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
    public boolean deleteReservation(UserAccount account, Reservation reservation) {
        // fail if account doesn't exist
        int index = accounts.indexOf(account);
        if (index == -1) {
            return false;
        }

        // returns true if reservation was found under that account and removed, false otherwise
        if (accounts.get(index).removeReservation(reservation)) {
            reservations.remove(reservation); // only remove if the given account created the reservation
            return true;
        }
        return false;
    }

    // getters
    public ArrayList<UserAccount> getAccounts() {
        return accounts;
    }
    public ArrayList<Reservation> getReservations() {
        return reservations;
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