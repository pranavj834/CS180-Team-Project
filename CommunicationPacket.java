import java.io.Serializable;
import java.util.UUID;

/**
 * Serializable container used for all messages between client and server.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author zhu1220, lab sec L23
 * @version November 8, 2025 (thread-safety tightened)
 */
public class CommunicationPacket implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String requestId = UUID.randomUUID().toString();
    private PacketType packetType;
    private Object payload;
    private ErrorCode errorCode = ErrorCode.NONE;
    private String message;

    public synchronized String getRequestId() {
        return requestId;
    }

    public synchronized PacketType getPacketType() {
        return packetType;
    }

    public synchronized Object getPayload() {
        return payload;
    }

    public synchronized ErrorCode getErrorCode() {
        return errorCode;
    }

    public synchronized String getMessage() {
        return message;
    }

    public synchronized CommunicationPacket setPacketType(PacketType packetType) {
        this.packetType = packetType;
        return this;
    }

    public synchronized CommunicationPacket setPayload(Object payload) {
        this.payload = payload;
        return this;
    }

    public synchronized CommunicationPacket setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public synchronized CommunicationPacket setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public synchronized String toString() {
        return "Packet{" +
                packetType +
                ", req=" + requestId +
                ", err=" + errorCode +
                "}";
    }
}
