import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

/**
 * Test class for the Reservation class.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author Ryan Chan, lab sec L23
 * @version November 8, 2025
 */

public class ReservationTest {

    @Test(timeout = 1000)
    public void testConstructorAndGetters() {
        ArrayList<Integer> seats = new ArrayList<>();
        seats.add(1);
        seats.add(2);

        Reservation res = new Reservation("JohnDoe", "johndoe", "2025-11-10",
                "18:30", 2, seats);

        assertEquals("Name should match constructor value", "JohnDoe", res.getName());
        assertEquals("Username should match constructor value", "johndoe", res.getUsername());
        assertEquals("Date should match constructor value", "2025-11-10", res.getDate());
        assertEquals("Time should match constructor value", "18:30", res.getTime());
        assertEquals("Number of people should match constructor value", 2, res.getNumPeople());
        assertEquals("Seats list should match constructor value", seats, res.getSeats());
    }

    @Test(timeout = 1000)
    public void testSetters() {
        ArrayList<Integer> seats = new ArrayList<>();
        seats.add(5);

        Reservation res = new Reservation("JohnDoe", "johndoe", "2025-01-01",
                "12:00", 1, seats);
        res.setName("JaneDoe");
        res.setUsername("janedoe");
        res.setDate("2025-12-31");
        res.setTime("20:15");
        res.setNumPeople(4);

        ArrayList<Integer> newSeats = new ArrayList<>();
        newSeats.add(10);
        newSeats.add(11);
        res.setSeats(newSeats);

        assertEquals("Name should be updated", "JaneDoe", res.getName());
        assertEquals("Username should be updated", "janedoe", res.getUsername());
        assertEquals("Date should be updated", "2025-12-31", res.getDate());
        assertEquals("Time should be updated", "20:15", res.getTime());
        assertEquals("Number of people should be updated", 4, res.getNumPeople());
        assertEquals("Seats list should match constructor value", newSeats, res.getSeats());
    }

    @Test(timeout = 1000)
    public void testEquals() {
        ArrayList<Integer> seats = new ArrayList<>();
        seats.add(3);

        Reservation reservation1 = new Reservation("JohnDoe", "johndoe", "2025-01-01",
                "12:00", 1, seats);
        Reservation res1 = new Reservation("JohnDoe", "johndoe", "2025-01-01",
                "12:00", 1, seats);
        Reservation reservation2 = new Reservation("asdf", "johndoe", "2025-01-01",
                "12:00", 1, seats);

        assertEquals("equals() should match reservation with the same info", reservation1, res1);
        assertNotEquals("equals() should not match reservation with different info",
                reservation1, reservation2);

    }

    @Test(timeout = 1000)
    public void testToString() {
        ArrayList<Integer> seats = new ArrayList<>();
        Reservation res = new Reservation("JohnDoe", "johndoe", "2025-11-10",
                "18:30", 2, seats);

        String s = res.toString();
        assertNotNull("toString() should not return null", s);
        assertEquals("toString() should match expected format and content", "Reservation for JohnDoe @ 2025-11-10 18:30 for 2", s);
    }
}