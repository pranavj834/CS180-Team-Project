import java.time.Duration;          // For representing how long seat holds last
import java.util.List;              // For lists of seats/reservations

/**
 * A class that provides a unified client-side API for communicating with the
 * restaurant reservation server.
 *
 * <p>This class combines authentication, seating layout, booking,
 * pricing, and payment operations into a single interface that the GUI
 * can use without dealing directly with network packets or sockets.</p>
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author Zhu1220, lab sec L23
 * @version November 8, 2025
 */
public class ClientAPI {
    private final ClientConnection conn;  // low-level connection that sends/receives packets
    private final ClientCache cache;      // local cache of layout, reservations, wallet, etc.

    /**
     * Constructs a new ClientAPI.
     *
     * @param conn    the ClientConnection used to communicate with the server
     * @param cache   the ClientCache to store frequently used data
     */
    public ClientAPI(ClientConnection conn, ClientCache cache) {
        this.conn = conn;     // save connection
        this.cache = cache;   // use the provided cache (don't create a new one)
    }

    // ============================================================
    // ================ AUTH (REGISTER / LOGIN / LOGOUT) ==========
    // ============================================================

    public String register(String username, String password) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.register(username, password));

        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }

        return String.valueOf(res.getPayload());
    }

    public boolean login(String username, String password) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.login(username, password));

        if (res.getErrorCode() != ErrorCode.NONE) {
            return false;
        }

        String sessionId = (String) res.getPayload();
        conn.setSessionId(sessionId);

        return true;
    }

    public void logout() throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.logout());

        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }

        conn.setSessionId(null);
    }

    // ============================================================
    // ================= SEATING / LAYOUT (STATIC MAP) ============
    // ============================================================

    public void lockSection(String sectionId, boolean lock) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.lockSection(sectionId, lock));

        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }
    }

    // ============================================================
    // ================= BOOKING / AVAILABILITY ===================
    // ============================================================

    public List<String> getOpenSeats(LocalDate date, LocalTime time, int partySize) throws Exception {
        LocalDateTime key = LocalDateTime.of(date, time);

        CommunicationPacket res = conn.send(PacketFactory.getOpenSeats(date, time, partySize));

        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }

        @SuppressWarnings("unchecked")
        List<String> seatIds = (List<String>) res.getPayload();

        cache.putOpenSeats(key, seatIds);

        return seatIds;
    }

    public boolean holdSeats(LocalDate date, LocalTime time, List<String> seatIds, Duration ttl) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.holdSeats(date, time, seatIds, ttl));

        if (res.getErrorCode() != ErrorCode.NONE) {
            return false;
        }

        return true;
    }

    public Reservation confirmReservation(LocalDate date, LocalTime time, List<String> seatIds, int partySize)
            throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.confirmReservation(date, time, seatIds, partySize));

        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }

        return (Reservation) res.getPayload();
    }

    public boolean cancelReservation(String reservationId) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.cancelReservation(reservationId));

        if (res.getErrorCode() != ErrorCode.NONE) {
            return false;
        }

        return true;
    }

    public List<Reservation> getReservations() throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.getReservations());

        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }

        @SuppressWarnings("unchecked")
        List<Reservation> list = (List<Reservation>) res.getPayload();

        cache.setMyReservations(list);

        return list;
    }

    // ============================================================
    // ========================= PRICING ==========================
    // ============================================================

    /**
     * Gets a price quote from the server for a set of seats at a given time.
     * For now, the server only uses partySize for pricing, but we keep the
     * full signature for compatibility.
     */
    public double quote(List<String> seatIds, LocalDate date, LocalTime time, int partySize) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.quotePrice(seatIds, date, time, partySize));

        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }

        return (double) res.getPayload();
    }

    /**
     * Allows a manager to update base & per-person pricing on the server.
     */
    public void setPriceRule(double basePrice, double perPersonPrice) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.setPriceRule(basePrice, perPersonPrice));

        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }
    }

    // ============================================================
    // ========================= PAYMENT ==========================
    // ============================================================

    public void deposit(double amount) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.depositMoney(amount));

        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }

        cache.setWalletBalance(getBalance());
    }

    public boolean withdraw(double amount) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.withdrawMoney(amount));

        if (res.getErrorCode() != ErrorCode.NONE) {
            return false;
        }

        cache.setWalletBalance(getBalance());

        return true;
    }

    public double getBalance() throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.getBalance());

        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }

        double bal = (double) res.getPayload();

        cache.setWalletBalance(bal);

        return bal;
    }
}
