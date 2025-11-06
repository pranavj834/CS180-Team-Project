/*
Core booking flow:

getOpenSeats(date,time,partySize) → list of free seat IDs (also cached by timeslot).

holdSeats(date,time,ids,ttl) → tentatively reserve seats for a short time.

confirmReservation(date,time,ids,partySize) → returns a Reservation DTO.

cancelReservation(resId) and getReservations().
 */

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class BookingClient {
    private final ClientConnection conn;
    private final ClientCache cache;
    private final Session session;

    public BookingClient(ClientConnection conn, ClientCache cache, Session session) {
        this.conn = conn; this.cache = cache; this.session = session;
    }

    public List<String> getOpenSeats(LocalDate date, LocalTime time, int partySize) throws Exception {
        LocalDateTime key = LocalDateTime.of(date, time);
        CommunicationPacket res = conn.send(PacketFactory.getOpenSeats(date, time, partySize));
        if (res.getErrorCode() != ErrorCode.NONE) throw new IllegalStateException(res.getMessage());
        @SuppressWarnings("unchecked")
        List<String> seatIds = (List<String>) res.getPayload();
        cache.putOpenSeats(key, seatIds);
        return seatIds;
    }

    public boolean holdSeats(LocalDate date, LocalTime time, List<String> seatIds, Duration ttl) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.holdSeats(date, time, seatIds, ttl));
        if (res.getErrorCode() != ErrorCode.NONE) return false;
        return "Held".equals(String.valueOf(res.getMessage())) || res.getErrorCode() == ErrorCode.NONE;
    }

    public Reservation confirmReservation(LocalDate date, LocalTime time, List<String> seatIds, int partySize) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.confirmReservation(date, time, seatIds, partySize));
        if (res.getErrorCode() != ErrorCode.NONE) throw new IllegalStateException(res.getMessage());
        return (Reservation) res.getPayload();
    }

    public boolean cancelReservation(String reservationId) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.cancelReservation(reservationId));
        if (res.getErrorCode() != ErrorCode.NONE) return false;
        return "Success!".equals(String.valueOf(res.getMessage())) || res.getErrorCode() == ErrorCode.NONE;
    }

    public List<Reservation> getReservations() throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.getReservations());
        if (res.getErrorCode() != ErrorCode.NONE) throw new IllegalStateException(res.getMessage());
        @SuppressWarnings("unchecked")
        List<Reservation> list = (List<Reservation>) res.getPayload();
        cache.setMyReservations(list);
        return list;
    }
}
