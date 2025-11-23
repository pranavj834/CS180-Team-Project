import java.util.ArrayList;

/**
 * Interface for the Database class.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author chan531, lab sec L23
 * @version November 10, 2025
 */

public interface DatabaseInterface {
    boolean addAccount(UserAccount account);
    boolean addReservation(UserAccount account, Reservation reservation);
    boolean deleteAccount(UserAccount account);
    boolean deleteReservation(UserAccount account, Reservation reservation);
    ArrayList<UserAccount> getAccounts();
    ArrayList<Reservation> getReservations();
    void readFromFile();
    void writeToFile();
    String toString();
}