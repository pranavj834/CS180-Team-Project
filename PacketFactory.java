import java.io.Serializable;
import java.util.List;

/**
 * Utility class that builds {@link CommunicationPacket} objects for all
 * supported client→server operations.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author zhu1220, lab sec L23
 * @version November 8, 2025
 */
public final class PacketFactory implements Serializable {

    private PacketFactory() { }

    // -------- AUTH --------

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

    // -------- BOOKING --------

    public static CommunicationPacket getOpenSeats(String date, String time, int partySize) {
        return new CommunicationPacket()
                .setPacketType(PacketType.GET_OPEN_SEATS)
                .setPayload(new Object[]{date, time, partySize});
    }

    public static CommunicationPacket holdSeats(String date, String time,
                                                List<Integer> seatNumbers, int holdSeconds) {
        return new CommunicationPacket()
                .setPacketType(PacketType.HOLD_SEATS)
                .setPayload(new Object[]{date, time, seatNumbers, holdSeconds});
    }

    public static CommunicationPacket confirmReservation(String date, String time,
                                                         List<Integer> seatNumbers, int partySize) {
        return new CommunicationPacket()
                .setPacketType(PacketType.CONFIRM_RESERVATION)
                .setPayload(new Object[]{date, time, seatNumbers, partySize});
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

    // -------- PRICING --------

    public static CommunicationPacket quotePrice(List<Integer> seatNumbers,
                                                 String date, String time, int partySize) {
        return new CommunicationPacket()
                .setPacketType(PacketType.QUOTE_PRICE)
                .setPayload(new Object[]{seatNumbers, date, time, partySize});
    }

    public static CommunicationPacket setPriceRule(double basePrice, double perPersonPrice) {
        return new CommunicationPacket()
                .setPacketType(PacketType.SET_PRICE_RULE)
                .setPayload(new Object[]{basePrice, perPersonPrice});
    }

    // -------- PAYMENT --------

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
