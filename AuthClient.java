/*
login/register/logout and (optionally) getUser.

On successful login, sets the sessionId in ClientConnection and updates Session.
 */

public class AuthClient {
    private final ClientConnection conn;
    private final Session session;

    public AuthClient(ClientConnection conn, Session session) {
        this.conn = conn; this.session = session;
    }

    public String register(String username, String password) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.register(username, password));
        if (res.getErrorCode() != ErrorCode.NONE) throw new IllegalStateException(res.getMessage());
        return String.valueOf(res.getPayload());
    }

    public boolean login(String username, String password) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.login(username, password));
        if (res.getErrorCode() != ErrorCode.NONE) return false;
        String sessionId = (String) res.getPayload();
        conn.setSessionId(sessionId);
        session.set(username, sessionId, false);
        return true;
    }

    public void logout() throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.logout());
        if (res.getErrorCode() == ErrorCode.NONE) {
            session.clear();
            conn.setSessionId(null);
        } else {
            throw new IllegalStateException(res.getMessage());
        }
    }
}
