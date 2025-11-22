import java.io.Serializable;

/**
 * Enumeration of standard error codes that the server can attach to
 * {@link CommunicationPacket} responses.
 *
 * <p>These values allow the client to distinguish between different failure cases,
 * such as authentication problems, invalid input, or conflicts like double-booked
 * seats, and to react appropriately in the user interface.</p>
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author zhu1220, lab sec L23
 * @version November 8, 2025
 */
public enum ErrorCode implements Serializable {
    NONE,
    AUTH_FAILED,
    NOT_AUTHORIZED,
    INVALID_INPUT,
    NOT_FOUND,
    CONFLICT,
    INSUFFICIENT_FUNDS,
    INTERNAL_ERROR
}
