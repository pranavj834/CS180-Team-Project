import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the ClientCache class.
 * @author zhu1220, lab sec L23
 * @version November 8, 2025
 */
public class ClientCacheTest {

    @Test(timeout = 1000)
    public void testLayoutAndHours() { // TODO: remove layout tests
        ClientCache cache = new ClientCache();

        assertNull(cache.getHours());

        DailyHours hours = new DailyHours(
                java.time.LocalTime.of(11, 0),
                java.time.LocalTime.of(22, 0));

        cache.setHours(hours);

        assertEquals(hours, cache.getHours());
    }

    @Test(timeout = 1000)
    public void testOpenSeatsCaching() {
        ClientCache cache = new ClientCache();
        LocalDateTime slot = LocalDateTime.of(2025, Month.NOVEMBER, 10, 18, 30);

        List<String> seats = Arrays.asList("T1", "T2");
        cache.putOpenSeats(slot, seats);

        assertEquals(seats, cache.getOpenSeats(slot));
        assertTrue("Unknown slot should return empty list",
                cache.getOpenSeats(slot.plusHours(1)).isEmpty());
    }

    @Test(timeout = 1000)
    public void testReservationsCaching() {
        ClientCache cache = new ClientCache();

        assertTrue(cache.getMyReservations().isEmpty());

        Reservation res = new Reservation("JohnDoe", "johndoe","2025-11-10", "18:30", 2,
                new ArrayList<>());

        cache.setMyReservations(java.util.Collections.singletonList(res));

        assertEquals(1, cache.getMyReservations().size());
        assertEquals(res, cache.getMyReservations().get(0));

        cache.setMyReservations(null);
        assertTrue("Setting null should clear list",
                cache.getMyReservations().isEmpty());
    }

    @Test(timeout = 1000)
    public void testWalletBalance() {
        ClientCache cache = new ClientCache();

        assertNull(cache.getWalletBalance());

        cache.setWalletBalance(42.5);
        assertEquals(42.5, cache.getWalletBalance(), 0.0001);
    }
}
