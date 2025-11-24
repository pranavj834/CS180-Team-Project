/**
 * Interface for the core server-side logic of the restaurant reservation system.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author zhu1220
 * @version November 10, 2025
 */
public interface ReservationServerInterface {

    CommunicationPacket handlePacket(CommunicationPacket req);
}
