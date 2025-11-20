import java.io.Serializable;
import java.time.LocalTime;

/**
 * Simple value object representing a day's opening and closing times.
 *
 * <p>The server is considered the source of truth for these values. Clients
 * request {@code DailyHours} to determine which times should be shown or
 * disabled in date/time pickers for reservations.</p>
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author zhu1220, lab sec L23
 * @version November 8, 2025
 */
public class DailyHours implements Serializable {
    private static final long serialVersionUID = 1L;

    private final LocalTime open;  // opening time for the restaurant
    private final LocalTime close; // closing time for the restaurant

    /**
     * Constructs a new DailyHours object.
     *
     * @param open  opening time
     * @param close closing time
     */
    public DailyHours(LocalTime open, LocalTime close) {
        this.open = open;
        this.close = close;
    }

    /**
     * Returns the opening time.
     *
     * @return open time
     */
    public LocalTime getOpen() {
        return open;
    }

    /**
     * Returns the closing time.
     *
     * @return close time
     */
    public LocalTime getClose() {
        return close;
    }
}
