import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

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
}
