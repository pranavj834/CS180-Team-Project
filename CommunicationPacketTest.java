import org.junit.Test;
import static org.junit.Assert.*;

public class CommunicationPacketTest {

    @Test
    public void testFluentSettersStoreValuesCorrectly() {
        CommunicationPacket packet = new CommunicationPacket()
                .setPacketType(PacketType.LOGIN)
                .setPayload("payload")
                .setErrorCode(ErrorCode.INVALID_INPUT)
                .setMessage("Bad input");

        assertEquals(PacketType.LOGIN, packet.getPacketType());
        assertEquals("payload", packet.getPayload());
        assertEquals(ErrorCode.INVALID_INPUT, packet.getErrorCode());
        assertEquals("Bad input", packet.getMessage());
    }

    @Test
    public void testRequestIdIsNotNullAndUnique() {
        CommunicationPacket p1 = new CommunicationPacket();
        CommunicationPacket p2 = new CommunicationPacket();

        assertNotNull(p1.getRequestId());
        assertNotNull(p2.getRequestId());
        assertNotEquals(p1.getRequestId(), p2.getRequestId());
    }

    @Test
    public void testDefaultErrorCodeIsNone() {
        CommunicationPacket packet = new CommunicationPacket();
        assertEquals(ErrorCode.NONE, packet.getErrorCode());
    }
}
