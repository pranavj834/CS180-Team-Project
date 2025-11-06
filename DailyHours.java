/*
What it is: Open/close times (server-owned truth).

Used by: HoursClient, date/time pickers.
 */

import java.io.Serializable;
import java.time.LocalTime;

public class DailyHours implements Serializable {
    private static final long serialVersionUID = 1L;
    private final LocalTime open;
    private final LocalTime close;

    public DailyHours(LocalTime open, LocalTime close) {
        this.open = open; this.close = close;
    }
    public LocalTime getOpen() { return open; }
    public LocalTime getClose() { return close; }
}
