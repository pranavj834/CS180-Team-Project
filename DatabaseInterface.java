import java.io.IOException;
import java.util.ArrayList;

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

    // Phase 2 persistence API

    void saveToFiles(String accountFile, String reservationFile) throws IOException;

    void loadFromFiles(String accountFile, String reservationFile) throws IOException;
}
