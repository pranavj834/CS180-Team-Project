import java.util.List;

/**
 * High-level client API used by the GUI.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 */
public class ClientAPI implements ClientAPIInterface {
    private final ClientConnection conn;
    private final ClientCache cache;

    public ClientAPI(ClientConnection conn, ClientCache cache) {
        this.conn = conn;
        this.cache = cache;
    }

    // ---------- AUTH ----------

    @Override
    public synchronized String register(String username, String password) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.register(username, password));

        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }
        return res.getMessage();
    }

    @Override
    public synchronized boolean login(String username, String password) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.login(username, password));
        return res.getErrorCode() == ErrorCode.NONE;
    }

    @Override
    public synchronized void logout() throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.logout());
        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }
    }

    // ---------- BOOKING ----------

    @Override
    public synchronized List<Integer> getOpenSeats(String date, String time, int partySize) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.getOpenSeats(date, time, partySize));
        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }
        @SuppressWarnings("unchecked")
        List<Integer> seats = (List<Integer>) res.getPayload();
        return seats;
    }

    @Override
    public synchronized boolean holdSeats(String date, String time,
                                          List<Integer> seatNumbers, int holdSeconds) throws Exception {
        CommunicationPacket res = conn.send(
                PacketFactory.holdSeats(date, time, seatNumbers, holdSeconds));
        return res.getErrorCode() == ErrorCode.NONE;
    }

    @Override
    public synchronized Reservation confirmReservation(String date, String time,
                                                       List<Integer> seatNumbers, int partySize) throws Exception {
        CommunicationPacket res = conn.send(
                PacketFactory.confirmReservation(date, time, seatNumbers, partySize));
        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }
        return (Reservation) res.getPayload();
    }

    @Override
    public synchronized boolean cancelReservation(String reservationId) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.cancelReservation(reservationId));
        return res.getErrorCode() == ErrorCode.NONE;
    }

    @Override
    public synchronized List<Reservation> getReservations() throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.getReservations());
        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }
        @SuppressWarnings("unchecked")
        List<Reservation> list = (List<Reservation>) res.getPayload();
        cache.setMyReservations(list);
        return list;
    }

    // ---------- PRICING (client-side only; server may stub) ----------

    @Override
    public synchronized double quote(List<Integer> seatNumbers, String date, String time, int partySize)
            throws Exception {
        CommunicationPacket res = conn.send(
                PacketFactory.quotePrice(seatNumbers, date, time, partySize));
        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }
        return (double) res.getPayload();
    }

    public synchronized void setPriceRule(double basePrice, double perPersonPrice) throws Exception {
        CommunicationPacket res = conn.send(
                PacketFactory.setPriceRule(basePrice, perPersonPrice));
        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }
    }

    // ---------- PAYMENT ----------

    @Override
    public synchronized void deposit(double amount) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.depositMoney(amount));
        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }
        cache.setWalletBalance(getBalance());
    }

    @Override
    public synchronized boolean withdraw(double amount) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.withdrawMoney(amount));
        if (res.getErrorCode() != ErrorCode.NONE) {
            return false;
        }
        cache.setWalletBalance(getBalance());
        return true;
    }

    @Override
    public synchronized double getBalance() throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.getBalance());
        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }
        double bal = (double) res.getPayload();
        cache.setWalletBalance(bal);
        return bal;
    }
}
