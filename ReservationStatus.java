/*
Enum: PENDING, CONFIRMED, CANCELLED.

Used by: Reservation and list displays.
 */

import java.io.Serializable;
public enum ReservationStatus implements Serializable {
    PENDING, CONFIRMED, CANCELLED
}
