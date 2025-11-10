import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

/**
 * Test class for the Database class.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author Ryan Chan, lab sec L23
 * @version November 10, 2025
 */

public class DatabaseTest {

    @Test(timeout = 1000)
    public void testConstructorAndGetters() {
        // ----- setup -----
        Database db = new Database();
        UserAccount john = new UserAccount("johndoe", "123",
                "JohnDoe", "john@email.com");
        db.addAccount(john);

        ArrayList<Integer> seats = new ArrayList<>();
        seats.add(1);
        seats.add(2);
        db.addReservation(john, new Reservation("JohnDoe", "johndoe",
                "2025-01-01", "13:30", 2, seats));

        // ----- testing -----
        assertEquals("Getter should return the correct account",
                "[UserAccount for JohnDoe, johndoe - john@email.com]",
                db.getAccounts().toString());
        assertEquals("Getter should return the correct reservation",
                "[Reservation for JohnDoe @ 2025-01-01 13:30 for 2]", db.getReservations().toString());
    }

    @Test(timeout = 1000)
    public void testAddFunctions() {
        // ----- setup -----
        Database db = new Database();
        UserAccount john = new UserAccount("johndoe", "123",
                "JohnDoe", "john@email.com");
        UserAccount jane = new UserAccount("janedoe", "234",
                "JaneDoe", "jane@email.com");

        ArrayList<Integer> seats1 = new ArrayList<>();
        seats1.add(1);
        seats1.add(2);
        ArrayList<Integer> seats2 = new ArrayList<>();
        seats2.add(2);
        seats2.add(3);
        seats2.add(4);

        // ----- testing -----
        assertTrue("addAccount should return true for an account that doesn't already exist",
                db.addAccount(john));
        assertFalse("addAccount should return false for an account that exists already",
                db.addAccount(john));
        assertTrue("addReservation should return true when the account exists and there is no time conflict",
                db.addReservation(john,
                        new Reservation("JohnDoe", "johndoe", "2025-01-01",
                                "13:30", 2, seats1)));
        assertFalse("addReservation should return false if the account does not exist",
                db.addReservation(jane,
                        new Reservation("JaneDoe", "janedoe", "2025-01-31",
                                "18:00", 3, seats2)));

        db.addAccount(jane);
        db.addReservation(jane,
                new Reservation("JaneDoe", "janedoe", "2025-01-31",
                        "18:00", 3, seats2));

        assertFalse("addReservation should return false when a time conflict occurs",
                db.addReservation(jane,
                        new Reservation("JaneDoe", "janedoe", "2025-01-31",
                                "18:00", 2, seats1)));
    }

    @Test(timeout = 1000)
    public void testDeleteFunctions() {
        // ----- setup -----
        Database db = new Database();
        UserAccount john = new UserAccount("johndoe", "123",
                "JohnDoe", "john@email.com");
        UserAccount jane = new UserAccount("janedoe", "234",
                "JaneDoe", "jane@email.com");
        db.addAccount(john);
        db.addAccount(jane);

        ArrayList<Integer> seats1 = new ArrayList<>();
        seats1.add(1);
        seats1.add(2);
        ArrayList<Integer> seats2 = new ArrayList<>();
        seats2.add(2);
        seats2.add(3);
        seats2.add(4);
        ArrayList<Integer> seats3 = new ArrayList<>();
        seats3.add(5);
        seats3.add(6);
        seats3.add(7);
        seats3.add(8);

        db.addReservation(john,
                new Reservation("JohnDoe", "johndoe", "2025-01-01",
                        "13:30", 2, seats1));
        db.addReservation(jane,
                new Reservation("JaneDoe", "janedoe", "2025-01-31",
                        "18:00", 3, seats2));
        db.addReservation(jane,
                new Reservation("JaneDoe", "janedoe", "2025-02-01",
                        "07:00", 4, seats3));

        // ----- testing -----
        assertTrue("deleteReservation should return true when given a valid account and reservation",
                db.deleteReservation(jane,
                        new Reservation("JaneDoe", "janedoe", "2025-01-31",
                                "18:00", 3, seats2)));
        assertFalse("deleteReservation should return false when given an invalid reservation",
                db.deleteReservation(jane,
                        new Reservation("JaneDoe", "janedoe", "2025-01-31",
                                "18:00", 3, seats2)));
        assertFalse("deleteReservation should return false when given the wrong account for the reservation",
                db.deleteReservation(jane,
                        new Reservation("JohnDoe", "johndoe", "2025-01-01",
                                "13:30", 2, seats1)));

        assertTrue("deleteAccount should return true when given a valid account",
                db.deleteAccount(john));
        assertFalse("deleteAccount should return false when given an invalid account",
                db.deleteAccount(john));
    }

    @Test(timeout = 1000)
    public void testToString() {
        // ----- setup -----
        Database db = new Database();
        UserAccount john = new UserAccount("johndoe", "123",
                "JohnDoe", "john@email.com");
        UserAccount jane = new UserAccount("janedoe", "234",
                "JaneDoe", "jane@email.com");
        db.addAccount(john);
        db.addAccount(jane);

        ArrayList<Integer> seats1 = new ArrayList<>();
        seats1.add(1);
        seats1.add(2);
        ArrayList<Integer> seats2 = new ArrayList<>();
        seats2.add(2);
        seats2.add(3);
        seats2.add(4);
        ArrayList<Integer> seats3 = new ArrayList<>();
        seats3.add(5);
        seats3.add(6);
        seats3.add(7);
        seats3.add(8);
        db.addReservation(john,
                new Reservation("JohnDoe", "johndoe", "2025-01-01",
                                "13:30", 2, seats1));
        db.addReservation(jane,
                new Reservation("JaneDoe", "janedoe", "2025-01-31",
                                "18:00", 3, seats2));
        db.addReservation(jane,
                new Reservation("JaneDoe", "janedoe", "2025-02-01",
                        "07:00", 4, seats3));

        // ----- testing -----
        assertEquals("toString should return all the information",
                "Accounts:\n" +
                "UserAccount for JohnDoe, johndoe - john@email.com\n" +
                "UserAccount for JaneDoe, janedoe - jane@email.com\n" +
                "\n" +
                "Reservations:\n" +
                "Reservation for JohnDoe @ 2025-01-01 13:30 for 2\n" +
                "Reservation for JaneDoe @ 2025-01-31 18:00 for 3\n" +
                "Reservation for JaneDoe @ 2025-02-01 07:00 for 4\n", db.toString());
    }
}