/*

 */

public class ClientHours {
    private ClientConnection conn;
    private ClientCache cache;

    public ClientHours(ClientConnection conn, ClientCache cache) {
        this.conn = conn; this.cache = cache;
    }

    public DailyHours getHours() throws Exception {
        if (cache.getHours() != null) return cache.getHours();
        CommunicationPacket res = conn.send(PacketFactory.getHours());
        if (res.getErrorCode() != ErrorCode.NONE) throw new IllegalStateException(res.getMessage());
        DailyHours hours = (DailyHours) res.getPayload();
        cache.setHours(hours);
        return hours;
    }

    public void setHours(DailyHours hours) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.setHours(hours));
        if (res.getErrorCode() != ErrorCode.NONE) throw new IllegalStateException(res.getMessage());
        cache.setHours(hours);
    }
}
