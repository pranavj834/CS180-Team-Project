import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

/**
 * Spot tests for PacketFactory to verify packetType and payload shapes.
 * @author zhu1220, lab sec L23
 * @version November 8, 2025
 */

public class PacketFactoryTest {

    @Test(timeout = 1000)
    public void testRegisterPacket() {
        CommunicationPacket p = PacketFactory.register("alice", "pw");
        assertEquals(PacketType.REGISTER, p.getPacketType());
        assertArrayEquals(new String[]{"alice", "pw"}, (String[]) p.getPayload());
    }

    @Test(timeout = 1000)
    public void testGetOpenSeatsPacket() {
        LocalDate d = LocalDate.of(2025, 11, 10);
        LocalTime t = LocalTime.of(18, 30);

        CommunicationPacket p = PacketFactory.getOpenSeats(d, t, 4);
        assertEquals(PacketType.GET_OPEN_SEATS, p.getPacketType());

        Object[] payload = (Object[]) p.getPayload();
        assertEquals(d, payload[0]);
        assertEquals(t, payload[1]);
        assertEquals(4, payload[2]);
    }

    @Test(timeout = 1000)
    public void testConfirmReservationPacket() {
        LocalDate d = LocalDate.of(2025, 11, 10);
        LocalTime t = LocalTime.of(18, 30);

        CommunicationPacket p = PacketFactory.confirmReservation(
                d, t, Arrays.asList("T1", "T2"), 4);

        assertEquals(PacketType.CONFIRM_RESERVATION, p.getPacketType());

        Object[] payload = (Object[]) p.getPayload();
        assertEquals(d, payload[0]);
        assertEquals(t, payload[1]);
        assertEquals(Arrays.asList("T1", "T2"), payload[2]);
        assertEquals(4, payload[3]);
    }
}
