import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ReservationServerTest {

    @Test
    public void testSetPriceRuleAndQuotePriceThroughServer() {
        ReservationServer server = new ReservationServer();

        // 1) Send SET_PRICE_RULE to configure pricing
        double base = 12.0;
        double perPerson = 3.0;

        CommunicationPacket setReq = new CommunicationPacket()
                .setPacketType(PacketType.SET_PRICE_RULE)
                .setPayload(new Object[]{base, perPerson});

        CommunicationPacket setRes = server.handlePacket(setReq);

        assertEquals(ErrorCode.NONE, setRes.getErrorCode());
        assertEquals("Price rule updated", setRes.getMessage());

        // 2) Now ask for a quote for a party of 4
        List<String> seatIds = new ArrayList<>();
        seatIds.add("A1");
        seatIds.add("A2");

        LocalDate date = LocalDate.of(2025, 6, 15);
        LocalTime time = LocalTime.of(18, 0);

        CommunicationPacket quoteReq = new CommunicationPacket()
                .setPacketType(PacketType.QUOTE_PRICE)
                .setPayload(new Object[]{seatIds, date, time, 4});

        CommunicationPacket quoteRes = server.handlePacket(quoteReq);

        assertEquals(ErrorCode.NONE, quoteRes.getErrorCode());
        assertEquals("Quote computed", quoteRes.getMessage());

        double price = (double) quoteRes.getPayload();
        // price = base + perPerson * 4 = 12 + 3*4 = 24
        assertEquals(24.0, price, 0.0001);
    }

    @Test
    public void testSetPriceRuleWithInvalidPayload() {
        ReservationServer server = new ReservationServer();

        // Wrong payload type (String instead of Object[])
        CommunicationPacket badReq = new CommunicationPacket()
                .setPacketType(PacketType.SET_PRICE_RULE)
                .setPayload("not an array");

        CommunicationPacket badRes = server.handlePacket(badReq);

        assertEquals(ErrorCode.INVALID_INPUT, badRes.getErrorCode());
        assertTrue(badRes.getMessage().contains("SET_PRICE_RULE"));
    }

    @Test
    public void testQuotePriceWithMinimalValidPayload() {
        ReservationServer server = new ReservationServer();

        // Use default pricing from Reservation.configurePricing default
        List<String> seats = new ArrayList<>();
        LocalDate date = LocalDate.of(2025, 1, 1);
        LocalTime time = LocalTime.of(17, 30);
        int partySize = 1;

        CommunicationPacket quoteReq = new CommunicationPacket()
                .setPacketType(PacketType.QUOTE_PRICE)
                .setPayload(new Object[]{seats, date, time, partySize});

        CommunicationPacket quoteRes = server.handlePacket(quoteReq);

        assertEquals(ErrorCode.NONE, quoteRes.getErrorCode());
        assertNotNull(quoteRes.getPayload());
        assertTrue(quoteRes.getPayload() instanceof Double);
    }
}
