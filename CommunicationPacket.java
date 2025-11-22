import java.io.Serializable;
import java.util.UUID;

/**
 * Serializable container used for all messages between client and server.
 *
 * <p>A CommunicationPacket wraps:</p>
 * <ul>
 *     <li>The type of operation to perform ({@link PacketType}).</li>
 *     <li>The payload data required for that operation (as an Object).</li>
 *     <li>An optional session ID so the server knows which user is calling.</li>
 *     <li>Error information and a message when used as a response.</li>
 * </ul>
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author zhu1220, lab sec L23
 * @version November 8, 2025
 */
public class CommunicationPacket implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Unique ID automatically generated for each packet instance (useful for logging/debugging). */
    private String requestId = UUID.randomUUID().toString();

    /** The type of request/response (LOGIN, QUOTE_PRICE, etc.). */
    private PacketType packetType;

    /** Payload object (must be Serializable over the wire). */
    private Object payload;

    /** Session identifier used to associate this packet with a logged-in user. */
    private String sessionId;

    /** Error status; default is NONE meaning "no error". */
    private ErrorCode errorCode = ErrorCode.NONE;

    /** Optional human-readable message (e.g., "Success", or error detail). */
    private String message;

    // ==================== GETTERS ====================

    public String getRequestId() {
        return requestId;
    }

    public PacketType getPacketType() {
        return packetType;
    }

    public Object getPayload() {
        return payload;
    }

    public String getSessionId() {
        return sessionId;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    // ==================== FLUENT SETTERS ====================

    public CommunicationPacket setPacketType(PacketType packetType) {
        this.packetType = packetType;
        return this;
    }

    public CommunicationPacket setPayload(Object payload) {
        this.payload = payload;
        return this;
    }

    public CommunicationPacket setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public CommunicationPacket setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public CommunicationPacket setMessage(String message) {
        this.message = message;
        return this;
    }

    // ==================== DEBUG STRING ====================

    @Override
    public String toString() {
        return "Packet{" +
                packetType +
                ", req=" + requestId +
                ", err=" + errorCode +
                "}";
    }
}
