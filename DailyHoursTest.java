import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalTime;

/**
 * Tests for the DailyHours class.
 * @author Shawn Zhu, lab sec L23
 * @version November 8, 2025
 */

public class DailyHoursTest {

    @Test(timeout = 1000)
    public void testConstructorAndGetters() {
        LocalTime open = LocalTime.of(11, 0);
        LocalTime close = LocalTime.of(22, 0);

        DailyHours hours = new DailyHours(open, close);

        assertEquals(open, hours.getOpen());
        assertEquals(close, hours.getClose());
    }
}
