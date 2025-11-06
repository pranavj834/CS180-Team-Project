/*
What it is: A serializable envelope for all client↔server messages.

Key fields:

packetType — what action this is (e.g., LOGIN, GET_OPEN_SEATS).

payload — the data for that action (e.g., String[]{username,password}, or a DTO).

sessionId — set after login so the server knows who you are.

requestId — unique id for debugging/correlation.

errorCode, message — how the server reports problems or success text.

Why it matters: Standardizes every request/response so both sides stay simple.
*/


import java.io.Serializable;
import java.util.UUID;

public class CommunicationPacket implements Serializable {
    private static final long serialVersionUID = 1L;

    private String requestId = UUID.randomUUID().toString();
    private PacketType packetType;
    private Object payload;
    private String sessionId;
    private ErrorCode errorCode = ErrorCode.NONE;
    private String message;

    public String getRequestId() { return requestId; }
    public PacketType getPacketType() { return packetType; }
    public Object getPayload() { return payload; }
    public String getSessionId() { return sessionId; }
    public ErrorCode getErrorCode() { return errorCode; }
    public String getMessage() { return message; }

    public CommunicationPacket setPacketType(PacketType packetType) { this.packetType = packetType; return this; }
    public CommunicationPacket setPayload(Object payload) { this.payload = payload; return this; }
    public CommunicationPacket setSessionId(String sessionId) { this.sessionId = sessionId; return this; }
    public CommunicationPacket setErrorCode(ErrorCode errorCode) { this.errorCode = errorCode; return this; }
    public CommunicationPacket setMessage(String message) { this.message = message; return this; }

    @Override public String toString() {
        return "Packet{" + packetType + ", req=" + requestId + ", err=" + errorCode + "}";
    }
}
