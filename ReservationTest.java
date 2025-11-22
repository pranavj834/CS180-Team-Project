import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ReservationTest {

    @Before
    public void resetPricing() {
        // Set a known pricing rule before each test
        Reservation.configurePricing(10.0, 5.0); // base = 10, perPerson = 5
    }

    @Test
    public void testComputePriceUsesConfiguredPricing() {
        double priceFor2 = Reservation.computePrice(2); // 10 + 5*2 = 20
        double priceFor4 = Reservation.computePrice(4); // 10 + 5*4 = 30

        assertEquals(20.0, priceFor2, 0.0001);
        assertEquals(30.0, priceFor4, 0.0001);
    }

    @Test
    public void testConstructorSetsFieldsAndComputesTotalPrice() {
        ArrayList<Integer> seats = new ArrayList<>();
        seats.add(1);
        seats.add(2);

        Reservation r = new Reservation(
                "John Doe",
                "jdoe",
                "2025-12-25",
                "18:30",
                3,
                seats
        );

        assertEquals("John Doe", r.getName());
        assertEquals("jdoe", r.getUsername());
        assertEquals("2025-12-25", r.getDate());
        assertEquals("18:30", r.getTime());
        assertEquals(3, r.getNumPeople());
        assertEquals(seats, r.getSeats());

        // totalPrice = 10 + 5*3 = 25
        assertEquals(25.0, r.getTotalPrice(), 0.0001);
    }

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
