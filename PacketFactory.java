import java.time.Duration;          // Duration is used to represent how long a seat hold should last
import java.time.LocalDate;         // LocalDate is used for reservation dates
import java.time.LocalTime;         // LocalTime is used for reservation times
import java.util.List;              // List is used for collections of seat IDs

/**
 * Utility class that builds {@link CommunicationPacket} objects for all
 * supported client→server operations.
 *
 * <p>Each static method corresponds to one high-level API call (such as logging
 * in, asking for open seats, or depositing money) and constructs a packet with
 * the correct {@link PacketType} and payload structure. This keeps packet
 * creation consistent and avoids copy-pasting boilerplate across the GUI.</p>
 *
 * It's essentially a helper class for building packets correctly.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author zhu1220, lab sec L23
 * @version November 8, 2025
 */
public final class PacketFactory {

    /** Private constructor to prevent creating instances of this utility class. */
    private PacketFactory() { }

    // ======================== AUTH ========================

    public static CommunicationPacket register(String u, String p) {
        return new CommunicationPacket()
                .setPacketType(PacketType.REGISTER)
                .setPayload(new String[]{u, p});
    }

    public static CommunicationPacket login(String u, String p) {
        return new CommunicationPacket()
                .setPacketType(PacketType.LOGIN)
                .setPayload(new String[]{u, p});
    }

    public static CommunicationPacket logout() {
        return new CommunicationPacket()
                .setPacketType(PacketType.LOGOUT);
    }

    public static CommunicationPacket getUser(String username) {
        return new CommunicationPacket()
                .setPacketType(PacketType.GET_USER)
                .setPayload(username);
    }

    // ================== SEATING / SECTIONS ===================

    public static CommunicationPacket lockSection(String sectionId, boolean lock) {
        return new CommunicationPacket()
                .setPacketType(PacketType.LOCK_SECTION)
                .setPayload(new Object[]{sectionId, lock});
    }

    // ============ AVAILABILITY / BOOKING ===================

    public static CommunicationPacket getOpenSeats(LocalDate d, LocalTime t, int partySize) {
        return new CommunicationPacket()
                .setPacketType(PacketType.GET_OPEN_SEATS)
                .setPayload(new Object[]{d, t, partySize});
    }

    public static CommunicationPacket holdSeats(LocalDate d, LocalTime t,
                                                List<String> seatIds, Duration ttl) {
        long seconds = (ttl == null ? 0L : ttl.toSeconds());

        return new CommunicationPacket()
                .setPacketType(PacketType.HOLD_SEATS)
                .setPayload(new Object[]{d, t, seatIds, seconds});
    }

    public static CommunicationPacket confirmReservation(LocalDate d, LocalTime t,
                                                         List<String> seatIds, int partySize) {
        return new CommunicationPacket()
                .setPacketType(PacketType.CONFIRM_RESERVATION)
                .setPayload(new Object[]{d, t, seatIds, partySize});
    }

    public static CommunicationPacket cancelReservation(String reservationId) {
        return new CommunicationPacket()
                .setPacketType(PacketType.CANCEL_RESERVATION)
                .setPayload(reservationId);
    }

    public static CommunicationPacket getReservations() {
        return new CommunicationPacket()
                .setPacketType(PacketType.GET_RESERVATIONS);
    }

    // ====================== PRICING =========================

    /**
     * Builds a packet to request a price quote.
     * Server currently only uses partySize in the logic, but we send seat/date/time
     * too so it's easy to add more complex rules later.
     */
    public static CommunicationPacket quotePrice(List<String> seatIds, LocalDate d,
                                                 LocalTime t, int partySize) {
        return new CommunicationPacket()
                .setPacketType(PacketType.QUOTE_PRICE)
                .setPayload(new Object[]{seatIds, d, t, partySize});
    }

    /**
     * Builds a packet to update the server's simple pricing rules:
     *   totalPrice = basePrice + perPersonPrice * numPeople
     */
    public static CommunicationPacket setPriceRule(double basePrice, double perPersonPrice) {
        return new CommunicationPacket()
                .setPacketType(PacketType.SET_PRICE_RULE)
                .setPayload(new Object[]{basePrice, perPersonPrice});
    }

    // ======================= PAYMENT ========================

    public static CommunicationPacket depositMoney(double amount) {
        return new CommunicationPacket()
                .setPacketType(PacketType.DEPOSIT_MONEY)
                .setPayload(amount);
    }

    public static CommunicationPacket withdrawMoney(double amount) {
        return new CommunicationPacket()
                .setPacketType(PacketType.WITHDRAW_MONEY)
                .setPayload(amount);
    }

    public static CommunicationPacket getBalance() {
        return new CommunicationPacket()
                .setPacketType(PacketType.GET_BALANCE);
    }
}
