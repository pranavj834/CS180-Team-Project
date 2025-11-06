/*
What it is: The confirmed booking: id, user, seatIds, date, time, party size, total price, status.

Used by: BookingClient.getReservations(), receipts, history.
 */

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String reservationId;
    private final String username;
    private final List<String> seatIds;
    private final LocalDate date;
    private final LocalTime time;
    private final int partySize;
    private final double priceTotal;
    private final ReservationStatus status;

    public Reservation(String reservationId, String username, List<String> seatIds,
                       LocalDate date, LocalTime time, int partySize,
                       double priceTotal, ReservationStatus status) {
        this.reservationId = reservationId;
        this.username = username;
        this.seatIds = seatIds;
        this.date = date;
        this.time = time;
        this.partySize = partySize;
        this.priceTotal = priceTotal;
        this.status = status;
    }

    public String getReservationId() { return reservationId; }
    public String getUsername() { return username; }
    public List<String> getSeatIds() { return seatIds; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public int getPartySize() { return partySize; }
    public double getPriceTotal() { return priceTotal; }
    public ReservationStatus getStatus() { return status; }
}
