import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

/**
 * Tests for Seat and SeatingLayout classes.
 * @author Shawn Zhu, lab sec L23
 * @version November 8, 2025
 */

public class SeatAndSeatingLayoutTest {

    @Test(timeout = 1000)
    public void testSeat() {
        Seat seat = new Seat("T1", 4, false);

        assertEquals("T1", seat.getSeatId());
        assertEquals(4, seat.getCapacity());
        assertFalse(seat.isLocked());
    }

    @Test(timeout = 1000)
    public void testSeatingLayout() {
        Seat s1 = new Seat("T1", 4, false);
        Seat s2 = new Seat("T2", 2, true);

        SeatingLayout layout = new SeatingLayout(Arrays.asList(s1, s2));

        assertEquals(2, layout.getAllSeats().size());
        assertEquals("T1", layout.getAllSeats().get(0).getSeatId());
        assertEquals("T2", layout.getAllSeats().get(1).getSeatId());
    }
}
