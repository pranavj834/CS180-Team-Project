/*
PricingClient.java

quote(seatIds, date, time, partySize) → double.

The server computes the price; client just displays it.
 */

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PricingClient {
    private final ClientConnection conn;
    public PricingClient(ClientConnection conn) { this.conn = conn; }

    public double quote(List<String> seatIds, LocalDate date, LocalTime time, int partySize) throws Exception {
        CommunicationPacket res = conn.send(PacketFactory.quotePrice(seatIds, date, time, partySize));
        if (res.getErrorCode() != ErrorCode.NONE) throw new IllegalStateException(res.getMessage());
        return (double) res.getPayload();
    }
}
