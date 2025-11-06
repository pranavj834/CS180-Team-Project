/*
deposit(...), withdraw(...), getBalance() and keeps wallet balance in ClientCache.
 */

public class PaymentClient {
    private final ClientConnection conn;
    private final ClientCache cache;

    public PaymentClient(ClientConnection conn, ClientCache cache) {
        this.conn = conn; this.cache = cache;
    }

    public void deposit(double amount) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.depositMoney(amount));
        if (res.getErrorCode() != ErrorCode.NONE) throw new IllegalStateException(res.getMessage());
        cache.setWalletBalance(getBalance());
    }

    public boolean withdraw(double amount) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.withdrawMoney(amount));
        if (res.getErrorCode() != ErrorCode.NONE) return false;
        cache.setWalletBalance(getBalance());
        return true;
    }

    public double getBalance() throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.getBalance());
        if (res.getErrorCode() != ErrorCode.NONE) throw new IllegalStateException(res.getMessage());
        double bal = (double) res.getPayload();
        cache.setWalletBalance(bal);
        return bal;
    }
}
