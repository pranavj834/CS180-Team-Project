import java.io.Serializable;
import java.util.UUID;

/**
 * Serializable container used for all messages between client and server.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author zhu1220, lab sec L23
 * @version November 8, 2025
 */
public class CommunicationPacket implements Serializable {
    private static final long serialVersionUID = 1L;

    private String requestId = UUID.randomUUID().toString();
    private PacketType packetType;
    private Object payload;
    private ErrorCode errorCode = ErrorCode.NONE;
    private String message;

    public String getRequestId() {
        return requestId;
    }

    public PacketType getPacketType() {
        return packetType;
    }

    public Object getPayload() {
        return payload;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public CommunicationPacket setPacketType(PacketType packetType) {
        this.packetType = packetType;
        return this;
    }

    public CommunicationPacket setPayload(Object payload) {
        this.payload = payload;
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

    @Override
    public String toString() {
        return "Packet{" +
                packetType +
                ", req=" + requestId +
                ", err=" + errorCode +
                "}";
    }
}
