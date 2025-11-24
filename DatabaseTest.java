import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Tests for the Database class, focusing on
 * saving to and loading from text files.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author zhu1220
 */
public class DatabaseTest {

    private static final String ACCOUNT_FILE = "test_accounts.txt";
    private static final String RESERVATION_FILE = "test_reservations.txt";

    private Database db;

    @Before
    public void setUp() {
        db = new Database();
        // Make sure we start from clean files
        deleteIfExists(ACCOUNT_FILE);
        deleteIfExists(RESERVATION_FILE);
    }

    @After
    public void tearDown() {
        deleteIfExists(ACCOUNT_FILE);
        deleteIfExists(RESERVATION_FILE);
    }

    private void deleteIfExists(String path) {
        File f = new File(path);
        if (f.exists()) {
            // ignore result
            f.delete();
        }
    }

    /**
     * Saving and loading an empty database should not throw
     * and should produce an empty database.
     */
    @Test
    public void testSaveAndLoadEmptyDatabase() throws IOException {
        // save empty DB
        db.saveToFiles(ACCOUNT_FILE, RESERVATION_FILE);

        // load into a new DB
        Database loaded = new Database();
        loaded.loadFromFiles(ACCOUNT_FILE, RESERVATION_FILE);

        assertEquals("Accounts size after loading empty DB", 0, loaded.getAccounts().size());
        assertEquals("Reservations size after loading empty DB", 0, loaded.getReservations().size());
    }

    /**
     * One account, no reservations. Check that all fields are correctly stored and reloaded.
     */
    @Test
    public void testSaveAndLoadSingleAccountNoReservations() throws IOException {
        UserAccount acct = new UserAccount("user1", "pass1", "Alice", "alice@example.com");
        assertTrue(db.addAccount(acct));

        db.saveToFiles(ACCOUNT_FILE, RESERVATION_FILE);

        Database loaded = new Database();
        loaded.loadFromFiles(ACCOUNT_FILE, RESERVATION_FILE);

        assertEquals(1, loaded.getAccounts().size());
        UserAccount loadedAcct = loaded.getAccounts().get(0);

        assertEquals("user1", loadedAcct.getUsername());
        assertEquals("pass1", loadedAcct.getPassword());
        assertEquals("Alice", loadedAcct.getFullName());
        assertEquals("alice@example.com", loadedAcct.getEmail());
        assertEquals(0, loadedAcct.getReservations().size());
        assertEquals(0, loaded.getReservations().size());
    }

    /**
     * One account with one reservation. Check that both the reservation list
     * in the Database and in the UserAccount are restored properly, including seats.
     */
    @Test
    public void testSaveAndLoadAccountWithReservation() throws IOException {
        UserAccount acct = new UserAccount("user2", "pass2", "Bob", "bob@example.com");
        assertTrue(db.addAccount(acct));

        ArrayList<Integer> seats = new ArrayList<>(Arrays.asList(5, 6, 7));
        Reservation res = new Reservation("Bob", "user2",
                "2025-11-23", "19:00", 3, seats);

        assertTrue(db.addReservation(acct, res));

        db.saveToFiles(ACCOUNT_FILE, RESERVATION_FILE);

        Database loaded = new Database();
        loaded.loadFromFiles(ACCOUNT_FILE, RESERVATION_FILE);

        // Check accounts
        assertEquals(1, loaded.getAccounts().size());
        UserAccount loadedAcct = loaded.getAccounts().get(0);
        assertEquals("user2", loadedAcct.getUsername());
        assertEquals("Bob", loadedAcct.getFullName());

        // Check reservations in global list
        assertEquals(1, loaded.getReservations().size());
        Reservation loadedRes = loaded.getReservations().get(0);
        assertEquals("Bob", loadedRes.getName());
        assertEquals("user2", loadedRes.getUsername());
        assertEquals("2025-11-23", loadedRes.getDate());
        assertEquals("19:00", loadedRes.getTime());
        assertEquals(3, loadedRes.getNumPeople());
        assertEquals(Arrays.asList(5, 6, 7), loadedRes.getSeats());

        // Check reservations attached to user account
        assertEquals(1, loadedAcct.getReservations().size());
        Reservation fromAcct = loadedAcct.getReservations().get(0);
        assertEquals("Bob", fromAcct.getName());
        assertEquals(Arrays.asList(5, 6, 7), fromAcct.getSeats());
    }

    /**
     * Make sure reservation time collision logic is preserved even after loading.
     */
    @Test
    public void testReservationCollisionAfterLoad() throws IOException {
        UserAccount acct = new UserAccount("user3", "pass3", "Carl", "carl@example.com");
        assertTrue(db.addAccount(acct));

        ArrayList<Integer> seats1 = new ArrayList<>(Arrays.asList(1, 2));
        Reservation r1 = new Reservation("Carl", "user3",
                "2025-11-24", "18:00", 2, seats1);
        assertTrue(db.addReservation(acct, r1));

        db.saveToFiles(ACCOUNT_FILE, RESERVATION_FILE);

        // Load into new DB
        Database loaded = new Database();
        loaded.loadFromFiles(ACCOUNT_FILE, RESERVATION_FILE);

        // Create another reservation with SAME date + time (should fail)
        UserAccount loadedAcct = loaded.getAccounts().get(0);
        ArrayList<Integer> seats2 = new ArrayList<>(Arrays.asList(3, 4));
        Reservation r2 = new Reservation("Carl", "user3",
                "2025-11-24", "18:00", 2, seats2);

        assertFalse("Second reservation with same date+time should fail",
                loaded.addReservation(loadedAcct, r2));
        assertEquals(1, loaded.getReservations().size());
    }

    /**
     * Delete an account with a reservation, save, reload,
     * and verify both the account and its reservations are gone.
     */
    @Test
    public void testDeleteAccountPersists() throws IOException {
        UserAccount acct = new UserAccount("user4", "pass4", "Dana", "dana@example.com");
        assertTrue(db.addAccount(acct));

        ArrayList<Integer> seats = new ArrayList<>(Arrays.asList(10, 11));
        Reservation res = new Reservation("Dana", "user4",
                "2025-11-25", "20:00", 2, seats);
        assertTrue(db.addReservation(acct, res));

        // Now delete account and confirm in-memory
        assertTrue(db.deleteAccount(acct));
        assertEquals(0, db.getAccounts().size());
        assertEquals(0, db.getReservations().size());

        db.saveToFiles(ACCOUNT_FILE, RESERVATION_FILE);

        Database loaded = new Database();
        loaded.loadFromFiles(ACCOUNT_FILE, RESERVATION_FILE);

        assertEquals(0, loaded.getAccounts().size());
        assertEquals(0, loaded.getReservations().size());
    }

    /**
     * Loading from non-existent files should not throw and should keep DB empty.
     */
    @Test
    public void testLoadFromMissingFiles() throws IOException {
        // Make sure files do NOT exist
        deleteIfExists(ACCOUNT_FILE);
        deleteIfExists(RESERVATION_FILE);

        Database loaded = new Database();
        loaded.loadFromFiles(ACCOUNT_FILE, RESERVATION_FILE);

        assertEquals(0, loaded.getAccounts().size());
        assertEquals(0, loaded.getReservations().size());
    }
}
