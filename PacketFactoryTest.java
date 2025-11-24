import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PacketFactoryTest {

    @Test
    public void testRegisterPacket() {
        CommunicationPacket p = PacketFactory.register("user", "pass");

        assertEquals(PacketType.REGISTER, p.getPacketType());
        assertTrue(p.getPayload() instanceof String[]);

        String[] creds = (String[]) p.getPayload();
        assertEquals("user", creds[0]);
        assertEquals("pass", creds[1]);
    }

    @Test
    public void testLoginPacket() {
        CommunicationPacket p = PacketFactory.login("alice", "secret");

        assertEquals(PacketType.LOGIN, p.getPacketType());
        assertTrue(p.getPayload() instanceof String[]);

        String[] creds = (String[]) p.getPayload();
        assertEquals("alice", creds[0]);
        assertEquals("secret", creds[1]);
    }

    @Test
    public void testQuotePricePacketPayloadShape() {
        List<Integer> seats = Arrays.asList(1, 2);
        String date = "2025-01-01";
        String time = "18:30";
        int partySize = 4;

        CommunicationPacket p = PacketFactory.quotePrice(seats, date, time, partySize);

        assertEquals(PacketType.QUOTE_PRICE, p.getPacketType());
        assertTrue(p.getPayload() instanceof Object[]);

        Object[] arr = (Object[]) p.getPayload();
        assertEquals(4, arr.length);
        assertSame(seats, arr[0]);
        assertEquals(date, arr[1]);
        assertEquals(time, arr[2]);
        assertEquals(partySize, arr[3]);
    }

    @Test
    public void testSetPriceRulePacketPayload() {
        double base = 10.0;
        double perPerson = 2.5;

        CommunicationPacket p = PacketFactory.setPriceRule(base, perPerson);

        assertEquals(PacketType.SET_PRICE_RULE, p.getPacketType());
        assertTrue(p.getPayload() instanceof Object[]);

        Object[] arr = (Object[]) p.getPayload();
        assertEquals(2, arr.length);
        assertEquals(base, (double) arr[0], 0.0001);
        assertEquals(perPerson, (double) arr[1], 0.0001);
    }

    @Test
    public void testHoldSeatsPacketUsesSecondsInt() {
        List<Integer> seats = Collections.singletonList(5);
        String date = "2025-05-10";
        String time = "19:00";
        int seconds = 900;

        CommunicationPacket p = PacketFactory.holdSeats(date, time, seats, seconds);

        assertEquals(PacketType.HOLD_SEATS, p.getPacketType());
        assertTrue(p.getPayload() instanceof Object[]);

        Object[] arr = (Object[]) p.getPayload();
        assertEquals(4, arr.length);
        assertEquals(date, arr[0]);
        assertEquals(time, arr[1]);
        assertSame(seats, arr[2]);
        assertEquals(seconds, arr[3]);
    }
}
