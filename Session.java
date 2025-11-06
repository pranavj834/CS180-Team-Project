/*
What it is: The logged-in user state on the client.

Fields: username, sessionId, manager.

Why it matters: Gates manager-only actions and ensures sessionId goes with requests.
 */

public class Session {
    private String username;
    private String sessionId;
    private boolean manager;

    public String getUsername() { return username; }
    public String getSessionId() { return sessionId; }
    public boolean isManager() { return manager; }

    public void set(String username, String sessionId, boolean manager) {
        this.username = username;
        this.sessionId = sessionId;
        this.manager = manager;
    }

    public void clear() { this.username = null; this.sessionId = null; this.manager = false; }
    public boolean isLoggedIn() { return sessionId != null && !sessionId.isEmpty(); }
}
