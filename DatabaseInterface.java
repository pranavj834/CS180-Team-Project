import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Interface for the Database class storing user accounts and reservations.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 */
public interface DatabaseInterface {

    // Core CRUD

    boolean addAccount(UserAccount account);

    boolean addReservation(UserAccount account, Reservation reservation);

    boolean deleteAccount(UserAccount account);

    boolean deleteReservation(UserAccount account, Reservation reservation);

    ArrayList<UserAccount> getAccounts();

    ArrayList<Reservation> getReservations();

    ArrayList<Reservation> getAccountReservations(UserAccount account);

    HashMap<String, boolean[]> getSeatStatuses();

    boolean[] getSeatStatusesAtTime(String timestamp);

    String toString();

    // Phase 2 persistence API

    void saveToFiles(String accountFile, String reservationFile) throws IOException;

    void loadFromFiles(String accountFile, String reservationFile) throws IOException;
}
