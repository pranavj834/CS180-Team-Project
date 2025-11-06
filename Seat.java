/*
What it is: A single seat/table definition in the static layout (id, capacity, locked).

Used by: SeatingLayout, UI rendering.
 */

import java.io.Serializable;

public class Seat implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String seatId;
    private final int capacity;
    private final boolean locked;

    public Seat(String seatId, int capacity, boolean locked) {
        this.seatId = seatId; this.capacity = capacity; this.locked = locked;
    }

    public String getSeatId() { return seatId; }
    public int getCapacity() { return capacity; }
    public boolean isLocked() { return locked; }
}
