import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for ClientAPI using a stubbed ClientConnection.
 */
public class ClientAPITest {

    private FakeClientConnection conn;
    private ClientCache cache;
    private ClientAPI api;

    @Before
    public void setup() {
        conn = new FakeClientConnection();
        cache = new ClientCache();
        api = new ClientAPI(conn, cache);
    }

    // ---------- AUTH ----------

    @Test
    public void testRegisterSuccess() throws Exception {
        conn.nextResponse = ok("Registered OK");

        String msg = api.register("alice", "pw");
        assertEquals("Registered OK", msg);
        assertEquals(PacketType.REGISTER, conn.lastSent.getPacketType());
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterFailureThrows() throws Exception {
        conn.nextResponse = error(ErrorCode.INVALID_INPUT, "bad");
        api.register("bob", "pw");
    }

    @Test
    public void testLoginSuccess() throws Exception {
        conn.nextResponse = ok("ok");
        assertTrue(api.login("user", "pw"));
        assertEquals(PacketType.LOGIN, conn.lastSent.getPacketType());
    }

    @Test
    public void testLoginFailureReturnsFalse() throws Exception {
        conn.nextResponse = error(ErrorCode.AUTH_FAILED, "wrong");
        assertFalse(api.login("user", "pw"));
    }

    @Test
    public void testLogout() throws Exception {
        conn.nextResponse = ok("done");
        api.logout();
        assertEquals(PacketType.LOGOUT, conn.lastSent.getPacketType());
    }

    // ---------- BOOKING ----------

    @Test
    public void testGetOpenSeatsReturnsList() throws Exception {
        List<Integer> expected = Arrays.asList(1, 2, 3);
        conn.nextResponse = okPayload(expected);

        List<Integer> result = api.getOpenSeats("2025-01-01", "18:00", 3);

        assertEquals(expected, result);
        assertEquals(PacketType.GET_OPEN_SEATS, conn.lastSent.getPacketType());
    }

    @Test
    public void testHoldSeatsSuccess() throws Exception {
        conn.nextResponse = ok("held");

        boolean held = api.holdSeats("2025-01-01", "18:00",
                Arrays.asList(1, 2), 20);

        assertTrue(held);
        assertEquals(PacketType.HOLD_SEATS, conn.lastSent.getPacketType());
    }

    @Test
    public void testHoldSeatsFailureReturnsFalse() throws Exception {
        conn.nextResponse = error(ErrorCode.CONFLICT, "taken");

        boolean held = api.holdSeats("2025-01-01", "18:00",
                Arrays.asList(1), 20);

        assertFalse(held);
    }

    @Test
    public void testConfirmReservationReturnsReservation() throws Exception {
        Reservation r = new Reservation("John", "j", "2025-05-01", "18:00",
                2, new ArrayList<Integer>());

        conn.nextResponse = okPayload(r);

        Reservation returned = api.confirmReservation(
                "2025-05-01", "18:00", Arrays.asList(10), 2);

        assertEquals(r, returned);
    }

    @Test
    public void testCancelReservationSuccess() throws Exception {
        conn.nextResponse = ok("deleted");
        boolean ok = api.cancelReservation("R1");
        assertTrue(ok);
        assertEquals(PacketType.CANCEL_RESERVATION, conn.lastSent.getPacketType());
    }

    @Test
    public void testCancelReservationFailure() throws Exception {
        conn.nextResponse = error(ErrorCode.NOT_FOUND, "nope");
        boolean ok = api.cancelReservation("R1");
        assertFalse(ok);
    }

    @Test
    public void testGetReservationsStoresInCache() throws Exception {
        Reservation r1 = new Reservation("A", "a", "2025", "12:00",
                1, new ArrayList<Integer>());
        Reservation r2 = new Reservation("B", "b", "2025", "12:30",
                2, new ArrayList<Integer>());

        List<Reservation> list = Arrays.asList(r1, r2);
        conn.nextResponse = okPayload(list);

        List<Reservation> out = api.getReservations();

        assertEquals(list, out);
        assertEquals(list, cache.getMyReservations());
    }

    // ---------- PRICING ----------

    @Test
    public void testQuotePriceReturnsDouble() throws Exception {
        conn.nextResponse = okPayload(25.0);

        double price = api.quote(Arrays.asList(1),
                "2025-02-01", "19:00", 3);

        assertEquals(25.0, price, 0.0001);
        assertEquals(PacketType.QUOTE_PRICE, conn.lastSent.getPacketType());
    }

    @Test
    public void testSetPriceRuleSuccess() throws Exception {
        conn.nextResponse = ok("done");
        api.setPriceRule(10.0, 2.0);
        assertEquals(PacketType.SET_PRICE_RULE, conn.lastSent.getPacketType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPriceRuleFailureThrows() throws Exception {
        conn.nextResponse = error(ErrorCode.INVALID_INPUT, "bad rule");
        api.setPriceRule(10, -2);
    }

    // ---------- PAYMENT ----------

    @Test
    public void testDepositUpdatesCacheBalance() throws Exception {
        conn.nextResponse = ok("ok");
        conn.nextBalance = 50.0;

        api.deposit(20.0);

        assertEquals(50.0, cache.getWalletBalance(), 0.0001);
    }

    @Test
    public void testWithdrawSuccessUpdatesCache() throws Exception {
        conn.nextResponse = ok("ok");
        conn.nextBalance = 30.0;

        boolean ok = api.withdraw(10);

        assertTrue(ok);
        assertEquals(30.0, cache.getWalletBalance(), 0.0001);
    }

    @Test
    public void testWithdrawFailureReturnsFalse() throws Exception {
        conn.nextResponse = error(ErrorCode.INSUFFICIENT_FUNDS, "no");

        boolean ok = api.withdraw(10);
        assertFalse(ok);
    }

    @Test
    public void testGetBalanceUpdatesCache() throws Exception {
        conn.nextResponse = okPayload(88.0);

        double bal = api.getBalance();

        assertEquals(88.0, bal, 0.0001);
        assertEquals(88.0, cache.getWalletBalance(), 0.0001);
    }

    // ---------- Fake connection ----------

    private static class FakeClientConnection extends ClientConnection {
        CommunicationPacket nextResponse;
        CommunicationPacket lastSent;
        Double nextBalance = null;

        @Override
        public CommunicationPacket send(CommunicationPacket p) {
            this.lastSent = p;

            if (p.getPacketType() == PacketType.GET_BALANCE && nextBalance != null) {
                return okPayload(nextBalance);
            }
            return nextResponse;
        }
    }

    // ---------- Helpers ----------

    private static CommunicationPacket ok(String msg) {
        return new CommunicationPacket()
                .setErrorCode(ErrorCode.NONE)
                .setMessage(msg);
    }

    private static CommunicationPacket okPayload(Object payload) {
        return new CommunicationPacket()
                .setErrorCode(ErrorCode.NONE)
                .setPayload(payload)
                .setMessage("OK");
    }

    private static CommunicationPacket error(ErrorCode code, String msg) {
        return new CommunicationPacket()
                .setErrorCode(code)
                .setMessage(msg);
    }
}
