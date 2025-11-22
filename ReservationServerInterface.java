/**
 * Interface for the core server-side logic of the restaurant reservation system.
 *
 * <p>Defines the contract for handling incoming {@link CommunicationPacket}
 * requests from clients. The concrete implementation in
 * {@link ReservationServer} is responsible for interpreting the packet type,
 * performing the appropriate action (authentication, booking, pricing, etc.),
 * and returning a response packet.</p>
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author zhu1220, lab sec L23
 * @version November 10, 2025
 */
public interface ReservationServerInterface {

    /**
     * Handles a single request packet from a client and returns a response.
     *
     * <p>Typical usage: the network layer (e.g., {@code ServerMain} / client
     * handler threads) receives a {@link CommunicationPacket} from a socket,
     * passes it to this method, and then sends the returned
     * {@link CommunicationPacket} back to the client.</p>
     *
     * @param req the request packet sent by the client
     * @return a response packet containing results or error information
     */
    CommunicationPacket handlePacket(CommunicationPacket req);
}
