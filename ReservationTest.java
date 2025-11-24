import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ReservationTest {

    @Test
    public void testEqualsUsesAllFields() {
        ArrayList<Integer> seats1 = new ArrayList<>();
        seats1.add(1);
        seats1.add(2);

        ArrayList<Integer> seats2 = new ArrayList<>();
        seats2.add(1);
        seats2.add(2);

        Reservation r1 = new Reservation(
                "Alice",
                "alice",
                "2025-01-01",
                "19:00",
                2,
                seats1
        );

        Reservation r2 = new Reservation(
                "Alice",
                "alice",
                "2025-01-01",
                "19:00",
                2,
                seats2
        );

        assertTrue("Reservations with identical data should be equal", r1.equals(r2));

        // Change one thing -> equals should now be false
        r2.setTime("20:00");
        assertFalse(r1.equals(r2));
    }

    @Test
    public void testToStringContainsCoreInfo() {
        ArrayList<Integer> seats = new ArrayList<>();
        seats.add(5);
        seats.add(6);

        Reservation r = new Reservation(
                "Bob",
                "bob",
                "2025-03-10",
                "20:15",
                2,
                seats
        );

        String s = r.toString();
        assertTrue(s.contains("Bob"));
        assertTrue(s.contains("2025-03-10"));
        assertTrue(s.contains("20:15"));
        assertTrue(s.contains("2"));
    }

    // ---------- Validation tests ----------

    @Test(expected = IllegalArgumentException.class)
    public void testBlankNameNotAllowed() {
        new Reservation(
                "   ",            // blank name
                "user",
                "2025-01-01",
                "19:00",
                2,
                new ArrayList<>(Arrays.asList(1, 2))
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlankUsernameNotAllowed() {
        new Reservation(
                "Name",
                "",               // blank username
                "2025-01-01",
                "19:00",
                2,
                new ArrayList<>(Arrays.asList(1, 2))
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlankDateNotAllowed() {
        new Reservation(
                "Name",
                "user",
                "   ",            // blank date
                "19:00",
                2,
                new ArrayList<>(Arrays.asList(1, 2))
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlankTimeNotAllowed() {
        new Reservation(
                "Name",
                "user",
                "2025-01-01",
                "",               // blank time
                2,
                new ArrayList<>(Arrays.asList(1, 2))
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDateFormat() {
        new Reservation(
                "Name",
                "user",
                "01-01-2025",     // wrong format
                "19:00",
                2,
                new ArrayList<>(Arrays.asList(1, 2))
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTimeFormat() {
        new Reservation(
                "Name",
                "user",
                "2025-01-01",
                "7pm",            // wrong format
                2,
                new ArrayList<>(Arrays.asList(1, 2))
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPartySizeMustBePositive() {
        new Reservation(
                "Name",
                "user",
                "2025-01-01",
                "19:00",
                0,                // invalid party size
                new ArrayList<>(Arrays.asList(1, 2))
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSeatNumbersMustBePositive() {
        ArrayList<Integer> seats = new ArrayList<>();
        seats.add(-1);             // invalid seat
        seats.add(2);

        new Reservation(
                "Name",
                "user",
                "2025-01-01",
                "19:00",
                2,
                seats
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSeatCountMustMatchPartySize() {
        new Reservation(
                "Name",
                "user",
                "2025-01-01",
                "19:00",
                3,                                // party size 3
                new ArrayList<>(Arrays.asList(1, 2)) // only 2 seats
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptySeatListNotAllowed() {
        new Reservation(
                "Name",
                "user",
                "2025-01-01",
                "19:00",
                2,
                new ArrayList<Integer>()          // empty list
        );
    }
}
