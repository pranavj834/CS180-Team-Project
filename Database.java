import java.util.ArrayList;

public class Database implements DatabaseInterface {
    private ArrayList<UserAccount> accounts;
    private ArrayList<Reservation> reservations;

    public Database() {
        accounts = new ArrayList<>();
        reservations = new ArrayList<>();
    }

    public boolean addAccount(UserAccount account) {
        if (accounts.contains(account)) {
            return false;
        }
        accounts.add(account);
        return true;
    }

    public boolean addReservation(UserAccount account, Reservation reservation) {
        for (Reservation r: reservations) {
            if (r.getTime().equals(reservation.getTime())) {
                return false;
            }
        }

        accounts.get(accounts.indexOf(account)).addReservation(reservation);
        reservations.add(reservation);
        return true;
    }

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

    public boolean deleteReservation(UserAccount account, Reservation reservation) {
        if (account.removeReservation(reservation) == true) {
            reservations.remove(reservation); // only remove if the given account created the reservation
            return true;
        }
        return false;
    }
}
