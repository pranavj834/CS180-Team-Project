import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the Session class.
 * @author Shawn Zhu, lab sec L23
 * @version November 8, 2025
 */

public class SessionTest {

    @Test(timeout = 1000)
    public void testSetAndGet() {
        Session s = new Session();
        s.set("alice", "session-123", true);

        assertEquals("alice", s.getUsername());
        assertEquals("session-123", s.getSessionId());
        assertTrue(s.isManager());
        assertTrue("isLoggedIn should be true when sessionId is set",
                s.isLoggedIn());
    }

    @Test(timeout = 1000)
    public void testClear() {
        Session s = new Session();
        s.set("alice", "session-123", false);

        s.clear();

        assertNull(s.getUsername());
        assertNull(s.getSessionId());
        assertFalse(s.isManager());
        assertFalse("isLoggedIn should be false after clear",
                s.isLoggedIn());
    }
}
