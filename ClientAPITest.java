//import org.junit.Test;
//import static org.junit.Assert.*;
//
//import java.io.IOException;
//import java.time.Duration;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.*;
//
///**
// * Tests for the ClientAPI class.
// *
// * <p>Purdue University -- CS18000 -- Fall 2025</p>
// *
// * @author zhu1220, lab sec L23
// * @version November 8, 2025
// */
//public class ClientAPITest {
//
//    /**
//     * Simple fake connection that returns queued responses instead of
//     * talking to a real server.
//     */
//    private static class FakeConnection extends ClientConnection {
//        CommunicationPacket lastRequest;
//        Queue<CommunicationPacket> responses = new ArrayDeque<>();
//
//        void addResponse(CommunicationPacket res) {
//            responses.add(res);
//        }
//
//        @Override
//        public CommunicationPacket send(CommunicationPacket req) throws IOException {
//            this.lastRequest = req;
//            if (responses.isEmpty()) {
//                CommunicationPacket err = new CommunicationPacket();
//                err.setErrorCode(ErrorCode.INTERNAL_ERROR)
//                        .setMessage("No stubbed response");
//                return err;
//            }
//            return responses.remove();
//        }
//    }
//
//    /** Helper: build a success packet with given payload. */
//    private CommunicationPacket ok(Object payload) {
//        return new CommunicationPacket()
//                .setErrorCode(ErrorCode.NONE)
//                .setPayload(payload);
//    }
//
//    /** Helper: build an error packet with a specific code. */
//    private CommunicationPacket err(ErrorCode code, String msg) {
//        return new CommunicationPacket()
//                .setErrorCode(code)
//                .setMessage(msg);
//    }
//
//    @Test(timeout = 1000)
//    public void testRegisterSuccess() throws Exception {
//        FakeConnection conn = new FakeConnection();
//        conn.addResponse(ok("Success!"));
//        ClientCache cache = new ClientCache();
//        Session session = new Session();
//        ClientAPI api = new ClientAPI(conn, cache, session);
//
//        String result = api.register("alice", "pw123");
//
//        assertEquals("Success!", result);
//        assertEquals(PacketType.REGISTER, conn.lastRequest.getPacketType());
//    }
//
//    @Test(timeout = 1000, expected = IllegalStateException.class)
//    public void testRegisterErrorThrows() throws Exception {
//        FakeConnection conn = new FakeConnection();
//        conn.addResponse(err(ErrorCode.CONFLICT, "Username taken"));
//        ClientAPI api = new ClientAPI(conn, new ClientCache(), new Session());
//
//        api.register("alice", "pw123"); // should throw
//    }
//
//    @Test(timeout = 1000)
//    public void testLoginSuccessSetsSession() throws Exception {
//        FakeConnection conn = new FakeConnection();
//        conn.addResponse(ok("session-123"));
//        ClientCache cache = new ClientCache();
//        Session session = new Session();
//        ClientAPI api = new ClientAPI(conn, cache, session);
//
//        boolean ok = api.login("alice", "pw123");
//
//        assertTrue(ok);
//        assertEquals("session-123", conn.getSessionId());
//        assertEquals("session-123", session.getSessionId());
//        assertEquals("alice", session.getUsername());
//    }
//
//    @Test(timeout = 1000)
//    public void testLoginFailureReturnsFalse() throws Exception {
//        FakeConnection conn = new FakeConnection();
//        conn.addResponse(err(ErrorCode.AUTH_FAILED, "Bad login"));
//        ClientAPI api = new ClientAPI(conn, new ClientCache(), new Session());
//
//        boolean ok = api.login("alice", "badpw");
//
//        assertFalse(ok);
//    }
//
//    @Test(timeout = 1000)
//    public void testLogoutClearsSession() throws Exception {
//        FakeConnection conn = new FakeConnection();
//        conn.setSessionId("session-123");
//        conn.addResponse(ok("Logged out"));
//        Session session = new Session();
//        session.set("alice", "session-123", false);
//
//        ClientAPI api = new ClientAPI(conn, new ClientCache(), session);
//
//        api.logout();
//
//        assertNull(conn.getSessionId());
//        assertFalse(session.isLoggedIn());
//    }
//
//    @Test(timeout = 1000)
//    public void testGetHoursUsesCache() throws Exception {
//        FakeConnection conn = new FakeConnection();
//        DailyHours hours = new DailyHours(
//                LocalTime.of(11, 0),
//                LocalTime.of(22, 0));
//        conn.addResponse(ok(hours));
//
//        ClientCache cache = new ClientCache();
//        ClientAPI api = new ClientAPI(conn, cache, new Session());
//
//        // first call hits fake server
//        DailyHours h1 = api.getHours();
//        // second call should come from cache; no extra responses queued
//        DailyHours h2 = api.getHours();
//
//        assertSame(hours, h1);
//        assertSame(h1, h2);
//    }
//
//    @Test(timeout = 1000)
//    public void testSetHoursUpdatesCache() throws Exception {
//        FakeConnection conn = new FakeConnection();
//        conn.addResponse(ok("Updated"));
//
//        ClientCache cache = new ClientCache();
//        ClientAPI api = new ClientAPI(conn, cache, new Session());
//
//        DailyHours newHours = new DailyHours(
//                LocalTime.of(10, 0),
//                LocalTime.of(23, 0));
//
//        api.setHours(newHours);
//
//        assertEquals(newHours, cache.getHours());
//    }
//
//    @Test(timeout = 1000)
//    public void testGetSeatingLayoutCaching() throws Exception {
//        FakeConnection conn = new FakeConnection();
//        SeatingLayout layout = new SeatingLayout(
//                Collections.singletonList(new Seat("T1", 4, false)));
//        conn.addResponse(ok(layout));
//
//        ClientCache cache = new ClientCache();
//        ClientAPI api = new ClientAPI(conn, cache, new Session());
//
//        SeatingLayout l1 = api.getSeatingLayout();
//        SeatingLayout l2 = api.getSeatingLayout(); // from cache
//
//        assertSame(layout, l1);
//        assertSame(l1, l2);
//    }
//
//    @Test(timeout = 1000)
//    public void testLockSectionInvalidatesLayout() throws Exception {
//        FakeConnection conn = new FakeConnection();
//        conn.addResponse(ok("Locked"));
//
//        ClientCache cache = new ClientCache();
//        cache.setLayout(new SeatingLayout(Collections.emptyList()));
//        ClientAPI api = new ClientAPI(conn, cache, new Session());
//
//        api.lockSection("MAIN", true);
//
//        assertNull("Layout cache should be cleared after lockSection",
//                cache.getLayout());
//    }
//
//    @Test(timeout = 1000)
//    public void testGetOpenSeatsCachesByTimeslot() throws Exception {
//        FakeConnection conn = new FakeConnection();
//        List<String> seats = Arrays.asList("T1", "T2");
//        conn.addResponse(ok(seats));
//
//        ClientCache cache = new ClientCache();
//        ClientAPI api = new ClientAPI(conn, cache, new Session());
//
//        LocalDate d = LocalDate.of(2025, 11, 10);
//        LocalTime t = LocalTime.of(18, 30);
//
//        List<String> result = api.getOpenSeats(d, t, 4);
//
//        assertEquals(seats, result);
//
//        // check that something got cached for that slot
//        assertFalse(cache.getOpe
