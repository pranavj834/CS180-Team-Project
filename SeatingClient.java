/*
SeatingClient.java

getSeatingLayout(), lockSection(...) (manager).

Caches layout; invalidates after admin changes.
 */

public class SeatingClient {
    private final ClientConnection conn;
    private final ClientCache cache;

    public SeatingClient(ClientConnection conn, ClientCache cache) {
        this.conn = conn; this.cache = cache;
    }

    public SeatingLayout getSeatingLayout() throws Exception {
        if (cache.getLayout() != null) return cache.getLayout();
        CommunicationPacket res = conn.send(PacketFactory.getSeatingLayout());
        if (res.getErrorCode() != ErrorCode.NONE) throw new IllegalStateException(res.getMessage());
        SeatingLayout layout = (SeatingLayout) res.getPayload();
        cache.setLayout(layout);
        return layout;
    }

    public void lockSection(String sectionId, boolean lock) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.lockSection(sectionId, lock));
        if (res.getErrorCode() != ErrorCode.NONE) throw new IllegalStateException(res.getMessage());
        cache.setLayout(null); // force reload next time
    }
}
