import java.util.List;

/**
 * High-level client API used by the GUI.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author zhu1220
 * @version November 8, 2025
 */
public class ClientAPI {
    private final ClientConnection conn;
    private final ClientCache cache;

    public ClientAPI(ClientConnection conn, ClientCache cache) {
        this.conn = conn;
        // IMPORTANT: use the cache that was passed in (tests depend on this)
        this.cache = cache;
    }

    // ---------- AUTH ----------

    public String register(String username, String password) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.register(username, password));

        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }
        // Tests expect the *message* "Registered OK", not the payload
        return res.getMessage();
    }

    public boolean login(String username, String password) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.login(username, password));
        return res.getErrorCode() == ErrorCode.NONE;
    }

    public void logout() throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.logout());
        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }
    }

    // ---------- BOOKING ----------

    public List<Integer> getOpenSeats(String date, String time, int partySize) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.getOpenSeats(date, time, partySize));
        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }
        @SuppressWarnings("unchecked")
        List<Integer> seats = (List<Integer>) res.getPayload();
        return seats;
    }

    public boolean holdSeats(String date, String time,
                             List<Integer> seatNumbers, int holdSeconds) throws Exception {
        CommunicationPacket res = conn.send(
                PacketFactory.holdSeats(date, time, seatNumbers, holdSeconds));
        return res.getErrorCode() == ErrorCode.NONE;
    }

    public Reservation confirmReservation(String date, String time,
                                          List<Integer> seatNumbers, int partySize) throws Exception {
        CommunicationPacket res = conn.send(
                PacketFactory.confirmReservation(date, time, seatNumbers, partySize));
        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }
        return (Reservation) res.getPayload();
    }

    public boolean cancelReservation(String reservationId) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.cancelReservation(reservationId));
        return res.getErrorCode() == ErrorCode.NONE;
    }

    public List<Reservation> getReservations() throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.getReservations());
        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }
        @SuppressWarnings("unchecked")
        List<Reservation> list = (List<Reservation>) res.getPayload();
        // update the shared cache instance (JUnit checks this)
        cache.setMyReservations(list);
        return list;
    }

    // ---------- PRICING ----------

    public double quote(List<Integer> seatNumbers, String date, String time, int partySize)
            throws Exception {
        CommunicationPacket res = conn.send(
                PacketFactory.quotePrice(seatNumbers, date, time, partySize));
        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }
        return (double) res.getPayload();
    }

    public void setPriceRule(double basePrice, double perPersonPrice) throws Exception {
        CommunicationPacket res = conn.send(
                PacketFactory.setPriceRule(basePrice, perPersonPrice));
        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }
    }

    // ---------- PAYMENT ----------

    public void deposit(double amount) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.depositMoney(amount));
        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }
        // tests expect us to refresh the balance into the cache
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
