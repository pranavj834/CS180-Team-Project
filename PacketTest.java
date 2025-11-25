import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Interface for the Packet class.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author jastip, lab sec L23
 * @version November 24, 2025
 */

public class PacketTest {

    @Test
    public void testConstructors() {
        Packet p = new Packet(PacketType.LOGIN);
        assertEquals("Packet type should match constructor value", PacketType.LOGIN, p.getType());

        Object[] obj = {"hello", "hi", "bye"};
        Packet p2 = new Packet(PacketType.ADD_ACCOUNT, obj);
        assertEquals("Packet type should match constructor value", PacketType.ADD_ACCOUNT, p2.getType());
        assertArrayEquals("Objects array should match the one provided", obj, p2.getObj());
    }

    @Test
    public void testGetters() {
        Packet p = new Packet(PacketType.INVALID);
        assertNull("Objects array should be null when not provided", p.getObj());

        Object[] obj = {"test", "case", "packet"};
        Packet p2 = new Packet(PacketType.INVALID, obj);
        assertEquals("Type getter should return correct type", PacketType.INVALID, p2.getType());
        assertArrayEquals("Objects array getter should return correct Objects array", obj, p2.getObj());
    }
}