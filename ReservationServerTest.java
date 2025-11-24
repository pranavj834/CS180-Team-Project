import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class ReservationServerTest {

    @Test
    public void testSetPriceRuleAndQuotePriceThroughServer() {
        ReservationServer server = new ReservationServer();

        double base = 12.0;
        double perPerson = 3.0;

        CommunicationPacket setReq = new CommunicationPacket()
                .setPacketType(PacketType.SET_PRICE_RULE)
                .setPayload(new Object[]{base, perPerson});

        CommunicationPacket setRes = server.handlePacket(setReq);

        assertEquals(ErrorCode.NONE, setRes.getErrorCode());
        assertEquals("Price rule updated", setRes.getMessage());

        List<Integer> seats = new ArrayList<>();
        seats.add(1);
        seats.add(2);

        String date = "2025-06-15";
        String time = "18:00";

        CommunicationPacket quoteReq = new CommunicationPacket()
                .setPacketType(PacketType.QUOTE_PRICE)
                .setPayload(new Object[]{seats, date, time, 4});

        CommunicationPacket quoteRes = server.handlePacket(quoteReq);

        assertEquals(ErrorCode.NONE, quoteRes.getErrorCode());
        assertEquals("Quote computed", quoteRes.getMessage());

        double price = (double) quoteRes.getPayload(); // 12 + 3*4 = 24
        assertEquals(24.0, price, 0.0001);
    }

    @Test
    public void testSetPriceRuleWithInvalidPayload() {
        ReservationServer server = new ReservationServer();

        CommunicationPacket badReq = new CommunicationPacket()
                .setPacketType(PacketType.SET_PRICE_RULE)
                .setPayload("not an array");

        CommunicationPacket badRes = server.handlePacket(badReq);

        assertEquals(ErrorCode.INVALID_INPUT, badRes.getErrorCode());
    }
}
