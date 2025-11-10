import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

/**
 * Test class for the UserAccount class.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author Ryan Chan, lab sec L23
 * @version November 8, 2025
 */

public class UserAccountTest {

    @Test(timeout = 1000)
    public void testConstructorAndGetters() {
        UserAccount acc = new UserAccount("user1", "pass123",
                "TestUser", "user1@example.com");

        assertEquals("Username should match constructor value", "user1", acc.getUsername());
        assertEquals("Full name should match constructor value", "TestUser", acc.getFullName());
        assertEquals("Email should match constructor value", "user1@example.com", acc.getEmail());
        assertEquals("Password should match constructor value", "pass123", acc.getPassword());
        assertNotNull("Reservations list should be initialized", acc.getReservations());
        assertTrue("Reservations list should be empty initially", acc.getReservations().isEmpty());
    }

    @Test(timeout = 1000)
    public void testSetters() {
        UserAccount acc = new UserAccount("user1", "pass123",
                "TestUser", "user1@example.com");

        acc.setUsername("newuser");
        acc.setPassword("123");
        acc.setFullName("NewName");
        acc.setEmail("new@example.com");

        assertEquals("Username should be updated", "newuser", acc.getUsername());
        assertEquals("Full name should be updated", "NewName", acc.getFullName());
        assertEquals("Email should be updated", "new@example.com", acc.getEmail());
        assertEquals("Updated credentials should match", new UserAccount("newuser",
                "123", "NewName", "new@example.com"), acc);
    }

    @Test(timeout = 1000)
    public void testEqualsValidAndInvalid() {
        UserAccount acc = new UserAccount("user1", "pass123",
                        "TestUser", "user1@example.com");

        assertEquals("Correct username and password should return true",
                new UserAccount("user1", "pass123", "asdf", "email@email.com"), acc);

        assertNotEquals("Wrong username should return false",
                new UserAccount("asdf", "pass123", "asdf", "email@email.com"), acc);

        assertNotEquals("Wrong password should return false",
                new UserAccount("user1", "asdf", "asdf", "email@email.com"), acc);
    }

    @Test(timeout = 1000)
    public void testAddAndRemoveReservation() {
        UserAccount acc = new UserAccount("user1", "pass123",
                        "TestUser", "user1@example.com");

        ArrayList<Integer> seats = new ArrayList<>();
        seats.add(1);
        Reservation res = new Reservation("JohnDoe", "johndoe", "2025-11-10",
                            "18:30", 2, seats);

        assertTrue("addReservation should return true when added",
                acc.addReservation(res));
        assertEquals("Reservations list should contain 1 element", 1, acc.getReservations().size());

        assertTrue("removeReservation should return true when present",
                acc.removeReservation(res));
        assertTrue("Reservations list should be empty after removal",
                acc.getReservations().isEmpty());

        assertFalse("removeReservation should return false when not present",
                acc.removeReservation(res));
    }

    @Test(timeout = 1000)
    public void testToString() {
        UserAccount acc = new UserAccount("user1", "pass123",
                        "TestUser", "user1@example.com");
        String s = acc.toString();
        assertNotNull("toString() should not return null", s);
        assertEquals("toString() should match expected format and content",
                "UserAccount for TestUser, user1 - user1@example.com", s);
    }
}