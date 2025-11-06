/*
What it is: Static builders for every request.

Examples:

login(u,p) → CommunicationPacket(LOGIN, String[]{u,p})

getOpenSeats(date,time,partySize) → CommunicationPacket(GET_OPEN_SEATS, Object[]{...})

confirmReservation(...), depositMoney(amount), etc.

Why it matters: Avoids duplicate boilerplate; keeps payloads consistent.
 */

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public final class PacketFactory {
    private PacketFactory() {}

    // Auth
    public static CommunicationPacket register(String u, String p) {
        return new CommunicationPacket().setPacketType(PacketType.REGISTER).setPayload(new String[]{u, p});
    }
    public static CommunicationPacket login(String u, String p) {
        return new CommunicationPacket().setPacketType(PacketType.LOGIN).setPayload(new String[]{u, p});
    }
    public static CommunicationPacket logout() {
        return new CommunicationPacket().setPacketType(PacketType.LOGOUT);
    }
    public static CommunicationPacket getUser(String username) {
        return new CommunicationPacket().setPacketType(PacketType.GET_USER).setPayload(username);
    }

    // Hours
    public static CommunicationPacket getHours() {
        return new CommunicationPacket().setPacketType(PacketType.GET_HOURS);
    }
    public static CommunicationPacket setHours(DailyHours hours) {
        return new CommunicationPacket().setPacketType(PacketType.SET_HOURS).setPayload(hours);
    }

    // Seating / Layout
    public static CommunicationPacket getSeatingLayout() {
        return new CommunicationPacket().setPacketType(PacketType.GET_SEATING_LAYOUT);
    }
    public static CommunicationPacket lockSection(String sectionId, boolean lock) {
        return new CommunicationPacket().setPacketType(PacketType.LOCK_SECTION).setPayload(new Object[]{sectionId, lock});
    }

    // Availability / Booking
    public static CommunicationPacket getOpenSeats(LocalDate d, LocalTime t, int partySize) {
        return new CommunicationPacket().setPacketType(PacketType.GET_OPEN_SEATS).setPayload(new Object[]{d, t, partySize});
    }
    public static CommunicationPacket holdSeats(LocalDate d, LocalTime t, List<String> seatIds, Duration ttl) {
        return new CommunicationPacket().setPacketType(PacketType.HOLD_SEATS)
                .setPayload(new Object[]{d, t, seatIds, ttl == null ? 0L : ttl.toSeconds()});
    }
    public static CommunicationPacket confirmReservation(LocalDate d, LocalTime t, List<String> seatIds, int partySize) {
        return new CommunicationPacket().setPacketType(PacketType.CONFIRM_RESERVATION)
                .setPayload(new Object[]{d, t, seatIds, partySize});
    }
    public static CommunicationPacket cancelReservation(String reservationId) {
        return new CommunicationPacket().setPacketType(PacketType.CANCEL_RESERVATION).setPayload(reservationId);
    }
    public static CommunicationPacket getReservations() {
        return new CommunicationPacket().setPacketType(PacketType.GET_RESERVATIONS);
    }

    // Pricing
    public static CommunicationPacket quotePrice(List<String> seatIds, LocalDate d, LocalTime t, int partySize) {
        return new CommunicationPacket().setPacketType(PacketType.QUOTE_PRICE)
                .setPayload(new Object[]{seatIds, d, t, partySize});
    }
    public static CommunicationPacket setPriceRule(PriceRule rule) {
        return new CommunicationPacket().setPacketType(PacketType.SET_PRICE_RULE).setPayload(rule);
    }

    // Payment
    public static CommunicationPacket depositMoney(double amount) {
        return new CommunicationPacket().setPacketType(PacketType.DEPOSIT_MONEY).setPayload(amount);
    }
    public static CommunicationPacket withdrawMoney(double amount) {
        return new CommunicationPacket().setPacketType(PacketType.WITHDRAW_MONEY).setPayload(amount);
    }
    public static CommunicationPacket getBalance() {
        return new CommunicationPacket().setPacketType(PacketType.GET_BALANCE);
    }
}
