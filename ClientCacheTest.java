import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tests for the ClientCache class.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author zhu1220, lab sec L23
 * @version November 8, 2025
 */
public class ClientCacheTest {

    @Test(timeout = 1000)
    public void testReservationsCaching() {
        ClientCache cache = new ClientCache();

        // Initially empty
        assertTrue(cache.getMyReservations().isEmpty());

        ArrayList<Integer> seats = new ArrayList<>();
        seats.add(1);
        seats.add(2);

        Reservation r = new Reservation("JohnDoe", "johndoe",
                "2025-11-10", "18:30", 2, seats);

        List<Reservation> list = Collections.singletonList(r);
        cache.setMyReservations(list);

        assertEquals(1, cache.getMyReservations().size());
        assertEquals(r, cache.getMyReservations().get(0));

        // Setting null clears list
        cache.setMyReservations(null);
        assertTrue(cache.getMyReservations().isEmpty());
    }

    @Test(timeout = 1000)
    public void testWalletBalance() {
        ClientCache cache = new ClientCache();

        assertNull(cache.getWalletBalance());

        cache.setWalletBalance(42.5);
        assertEquals(42.5, cache.getWalletBalance(), 0.0001);
    }
}
