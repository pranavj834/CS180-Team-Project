/*
What it is: The static seat map (list of Seat), sent by server to draw the chart.

Used by: SeatingClient, seating chart UI.
 */

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class SeatingLayout implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<Seat> seats;

    public SeatingLayout(List<Seat> seats) {
        this.seats = seats == null ? Collections.emptyList() : Collections.unmodifiableList(seats);
    }

    public List<Seat> getAllSeats() { return seats; }
}
