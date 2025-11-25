import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Test class for the Database methods.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author zhu1220, jastip, chan531, lab sec L23
 * @version November 23, 2025
 */

public class DatabaseTest {

    private static final String ACCOUNT_FILE = "test_accounts.txt";
    private static final String RESERVATION_FILE = "test_reservations.txt";

    private Database db;

    @Before
    public void setUp() {
        deleteIfExists(ACCOUNT_FILE);
        deleteIfExists(RESERVATION_FILE);
        db = new Database(ACCOUNT_FILE, RESERVATION_FILE);
    }

    @After
    public void tearDown() {
        deleteIfExists(ACCOUNT_FILE);
        deleteIfExists(RESERVATION_FILE);
    }

    private void deleteIfExists(String path) {
        try {
            File f = new File(path);
            if (f.delete()) {
                f.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------
    // EMPTY DATABASE
    // -------------------------------------------------------

    @Test
    public void testSaveAndLoadEmptyDatabase() throws IOException {
        db.saveToFiles(ACCOUNT_FILE, RESERVATION_FILE);

        Database loaded = new Database(ACCOUNT_FILE, RESERVATION_FILE);
        loaded.loadFromFiles(ACCOUNT_FILE, RESERVATION_FILE);

        assertEquals(0, loaded.getAccounts().size());
        assertEquals(0, loaded.getReservations().size());
    }

    // -------------------------------------------------------
    // SINGLE ACCOUNT, NO RESERVATIONS
    // -------------------------------------------------------

    @Test
    public void testSaveAndLoadSingleAccountNoReservations() throws IOException {
        UserAccount acct = new UserAccount("user1", "pass1", "Alice", "alice@example.com");
        assertTrue(db.addAccount(acct));

        db.saveToFiles(ACCOUNT_FILE, RESERVATION_FILE);

        Database loaded = new Database(ACCOUNT_FILE, RESERVATION_FILE);
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

    // -------------------------------------------------------
    // ONE ACCOUNT WITH ONE RESERVATION
    // -------------------------------------------------------

    @Test
    public void testSaveAndLoadAccountWithReservation() throws IOException {
        UserAccount acct = new UserAccount("user2", "pass2", "Bob", "bob@example.com");
        assertTrue(db.addAccount(acct));

        ArrayList<Integer> seats = new ArrayList<>(Arrays.asList(5, 6, 7));
        Reservation res = new Reservation("Bob", "user2",
                "2025-11-23", "19:00", 3, seats);

        assertTrue(db.addReservation(acct, res));

        db.saveToFiles(ACCOUNT_FILE, RESERVATION_FILE);

        Database loaded = new Database(ACCOUNT_FILE, RESERVATION_FILE);
        loaded.loadFromFiles(ACCOUNT_FILE, RESERVATION_FILE);

        assertEquals(1, loaded.getAccounts().size());
        UserAccount loadedAcct = loaded.getAccounts().get(0);

        assertEquals(1, loaded.getReservations().size());
        Reservation loadedRes = loaded.getReservations().get(0);
        assertEquals("Bob", loadedRes.getName());
        assertEquals("user2", loadedRes.getUsername());
        assertEquals("2025-11-23", loadedRes.getDate());
        assertEquals("19:00", loadedRes.getTime());
        assertEquals(3, loadedRes.getPartySize());
        assertEquals(Arrays.asList(5, 6, 7), loadedRes.getSeats());

        assertEquals(1, loadedAcct.getReservations().size());
        Reservation fromAcct = loadedAcct.getReservations().get(0);
        assertEquals(Arrays.asList(5, 6, 7), fromAcct.getSeats());
    }

    // -------------------------------------------------------
    // RESERVATION COLLISION
    // -------------------------------------------------------

    @Test
    public void testReservationCollisionAfterLoad() throws IOException {
        UserAccount acct = new UserAccount("user3", "pass3", "Carl", "carl@example.com");
        assertTrue(db.addAccount(acct));

        ArrayList<Integer> seats1 = new ArrayList<>(Arrays.asList(1, 2));
        Reservation r1 = new Reservation("Carl", "user3",
                "2025-11-24", "18:00", 2, seats1);

        assertTrue(db.addReservation(acct, r1));

        db.saveToFiles(ACCOUNT_FILE, RESERVATION_FILE);

        Database loaded = new Database(ACCOUNT_FILE, RESERVATION_FILE);
        loaded.loadFromFiles(ACCOUNT_FILE, RESERVATION_FILE);

        UserAccount loadedAcct = loaded.getAccounts().get(0);

        ArrayList<Integer> seats2 = new ArrayList<>(Arrays.asList(3, 4));
        Reservation r2 = new Reservation("Carl", "user3",
                "2025-11-24", "18:00", 2, seats2);

        assertFalse(loaded.addReservation(loadedAcct, r2));
        assertEquals(1, loaded.getReservations().size());
    }

    // -------------------------------------------------------
    // DELETE ACCOUNT AND PERSIST REMOVAL
    // -------------------------------------------------------

    @Test
    public void testDeleteAccountPersists() throws IOException {
        UserAccount acct = new UserAccount("user4", "pass4", "Dana", "dana@example.com");
        assertTrue(db.addAccount(acct));

        ArrayList<Integer> seats = new ArrayList<>(Arrays.asList(10, 11));
        Reservation res = new Reservation("Dana", "user4",
                "2025-11-25", "20:00", 2, seats);
        assertTrue(db.addReservation(acct, res));

        assertTrue(db.deleteAccount(acct));
        assertEquals(0, db.getAccounts().size());
        assertEquals(0, db.getReservations().size());

        db.saveToFiles(ACCOUNT_FILE, RESERVATION_FILE);

        Database loaded = new Database(ACCOUNT_FILE, RESERVATION_FILE);
        loaded.loadFromFiles(ACCOUNT_FILE, RESERVATION_FILE);

        assertEquals(0, loaded.getAccounts().size());
        assertEquals(0, loaded.getReservations().size());
    }

    // -------------------------------------------------------
    // NEW TEST: WRONG USERNAME/PASSWORD/FULLNAME/EMAIL
    // -------------------------------------------------------

    @Test
    public void testCannotAddReservationWithWrongAccountInfo() {
        // Real account stored in DB
        UserAccount real = new UserAccount("goodUser", "goodPass",
                "RealName", "real@example.com");
        assertTrue(db.addAccount(real));

        // Fake account with wrong username, password, fullName, and email
        UserAccount fake = new UserAccount("wrongUser", "wrongPass",
                "FakeName", "fake@example.com");

        ArrayList<Integer> seats = new ArrayList<>(Arrays.asList(1, 2));
        Reservation res = new Reservation("FakeName", "wrongUser",
                "2025-12-01", "18:00", 2, seats);

        // Attempt to add reservation with an invalid account → must fail
        boolean added = db.addReservation(fake, res);
        assertFalse("Reservation should NOT be added with invalid login info", added);

        assertEquals(0, db.getReservations().size());
        assertEquals(0, db.getAccounts().get(0).getReservations().size());
    }

    // -------------------------------------------------------
    // LOADING FROM NON-EXISTENT FILES
    // -------------------------------------------------------

    @Test
    public void testLoadFromMissingFiles() throws IOException {
        deleteIfExists(ACCOUNT_FILE);
        deleteIfExists(RESERVATION_FILE);

        Database loaded = new Database(ACCOUNT_FILE, RESERVATION_FILE);
        loaded.loadFromFiles(ACCOUNT_FILE, RESERVATION_FILE);

        assertEquals(0, loaded.getAccounts().size());
        assertEquals(0, loaded.getReservations().size());
    }

    @Test
    public void testSeatStatusMethods() {
        Database db = new Database(ACCOUNT_FILE, RESERVATION_FILE);

        //tests getSeatStatuses() so that returns modifiable map
        HashMap<String, boolean[]> map = db.getSeatStatuses();
        assertNotNull("Should be not null since statuses exist", map);
        assertSame(map, db.getSeatStatuses());

        //tests getSeatStatusesAtTime() creates a new entry when needed
        assertFalse(map.containsKey("10:00"));
        boolean[] created = db.getSeatStatusesAtTime("10:00");
        assertTrue(map.containsKey("10:00"));

        assertNotNull(created);
        assertEquals(30, created.length);
        assertSame(created, map.get("10:00"));

        created[5] = true;
        assertTrue(map.get("10:00")[5]);

        //tests getSeatStatusesAtTime() returns existing entry
        boolean[] existing = new boolean[30];
        existing[2] = true;

        map.put("11:00", existing);

        boolean[] fetched = db.getSeatStatusesAtTime("11:00");

        assertSame(existing, fetched);
        assertTrue(fetched[2]);
    }

}
