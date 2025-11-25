import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Interface for the Database class.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author chan531, lab sec L23
 * @version November 24, 2025
 */

public interface DatabaseInterface {

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

    void saveToFiles(String accountFile, String reservationFile) throws IOException;

    void loadFromFiles(String accountFile, String reservationFile) throws IOException;
}
