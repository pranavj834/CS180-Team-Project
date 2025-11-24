import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for ReservationServer logic using direct handlePacket calls.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 */
public class ReservationServerTest {

    private ReservationServer server;

    @Before
    public void setUp() {
        server = new ReservationServer();
    }

    @Test
    public void testRegisterAndGetUser() {
        // Register a user
        CommunicationPacket registerReq = PacketFactory.register("alice", "pw");
        CommunicationPacket registerRes = server.handlePacket(registerReq);

        assertEquals(ErrorCode.NONE, registerRes.getErrorCode());

        // Get the same user
        CommunicationPacket getReq = PacketFactory.getUser("alice");
        CommunicationPacket getRes = server.handlePacket(getReq);

        assertEquals(ErrorCode.NONE, getRes.getErrorCode());
        assertTrue(getRes.getPayload() instanceof UserAccount);

        UserAccount acct = (UserAccount) getRes.getPayload();
        assertEquals("alice", acct.getUsername());
    }

    @Test
    public void testRegisterDuplicateFails() {
        CommunicationPacket r1 = PacketFactory.register("bob", "pw");
        CommunicationPacket r2 = PacketFactory.register("bob", "pw");

        CommunicationPacket res1 = server.handlePacket(r1);
        CommunicationPacket res2 = server.handlePacket(r2);

        assertEquals(ErrorCode.NONE, res1.getErrorCode());
        assertEquals(ErrorCode.INVALID_INPUT, res2.getErrorCode());
    }

    @Test
    public void testLoginSuccessAndFailure() {
        // First register
        server.handlePacket(PacketFactory.register("charlie", "123"));

        // Correct login
        CommunicationPacket loginOk = server.handlePacket(PacketFactory.login("charlie", "123"));
        assertEquals(ErrorCode.NONE, loginOk.getErrorCode());

        // Wrong password
        CommunicationPacket loginBad = server.handlePacket(PacketFactory.login("charlie", "xxx"));
        assertEquals(ErrorCode.AUTH_FAILED, loginBad.getErrorCode());
    }

    @Test
    public void testGetReservationsInitiallyEmpty() {
        CommunicationPacket req = PacketFactory.getReservations();
        CommunicationPacket res = server.handlePacket(req);

        assertEquals(ErrorCode.NONE, res.getErrorCode());
        assertTrue(res.getPayload() instanceof List);

        @SuppressWarnings("unchecked")
        List<Reservation> list = (List<Reservation>) res.getPayload();
        assertTrue(list.isEmpty());
    }

    @Test
    public void testConfirmReservationReturnsReservation() {
        CommunicationPacket req = PacketFactory.confirmReservation(
                "2025-12-01", "19:00", Arrays.asList(1, 2), 2);

        CommunicationPacket res = server.handlePacket(req);

        assertEquals(ErrorCode.NONE, res.getErrorCode());
        assertTrue(res.getPayload() instanceof Reservation);

        Reservation r = (Reservation) res.getPayload();
        assertEquals("2025-12-01", r.getDate());
        assertEquals("19:00", r.getTime());
        assertEquals(2, r.getNumPeople());
        assertEquals(Arrays.asList(1, 2), r.getSeats());
    }
}
