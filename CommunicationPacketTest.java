import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the CommunicationPacket class.
 * @author zhu1220, lab sec L23
 * @version November 8, 2025
 */

public class CommunicationPacketTest {

    @Test(timeout = 1000)
    public void testSettersAndGetters() {
        CommunicationPacket p = new CommunicationPacket()
                .setPacketType(PacketType.LOGIN)
                .setPayload(new String[]{"user", "pw"})
                .setSessionId("session-123")
                .setErrorCode(ErrorCode.NONE)
                .setMessage("OK");

        assertEquals(PacketType.LOGIN, p.getPacketType());
        assertArrayEquals(new String[]{"user", "pw"}, (String[]) p.getPayload());
        assertEquals("session-123", p.getSessionId());
        assertEquals(ErrorCode.NONE, p.getErrorCode());
        assertEquals("OK", p.getMessage());
        assertNotNull("requestId should be auto-generated", p.getRequestId());
    }
}
