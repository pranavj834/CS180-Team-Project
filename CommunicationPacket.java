import java.io.Serializable; // Import the Serializable interface so this class can be sent over Object streams
import java.util.UUID;       // Import UUID to generate unique request IDs for each packet

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
public class CommunicationPacket implements Serializable { // Declare the class and indicate it can be serialized
    private static final long serialVersionUID = 1L;       // Explicit serial version ID for serialization compatibility

    private String requestId = UUID.randomUUID().toString(); // Unique ID automatically generated for each packet instance
    private PacketType packetType;                          // The type of request or response (e.g., LOGIN, GET_HOURS)
    private Object payload;                                 // The data associated with this packet (generic Object)
    private String sessionId;                               // Session identifier used to associate this packet with a user
    private ErrorCode errorCode = ErrorCode.NONE;           // Error status; default is NONE meaning "no error"
    private String message;                                 // Optional human-readable message (e.g., "Success" or error detail)

    /**
     * Returns the unique request ID for this packet.
     *
     * @return request ID string
     */
    public String getRequestId() {          // Getter method for requestId field
        return requestId;                   // Return the current value of requestId
    }

    /**
     * Returns the packet type.
     *
     * @return the PacketType for this packet
     */
    public PacketType getPacketType() {     // Getter method for packetType field
        return packetType;                  // Return the current value of packetType
    }

    /**
     * Returns the payload associated with this packet.
     *
     * @return the payload as an Object
     */
    public Object getPayload() {            // Getter method for payload field
        return payload;                     // Return the current value of payload
    }

    /**
     * Returns the session identifier attached to this packet.
     *
     * @return session ID string, or null if not set
     */
    public String getSessionId() {          // Getter method for sessionId field
        return sessionId;                   // Return the current value of sessionId
    }

    /**
     * Returns the error code for this packet.
     *
     * @return error code; ErrorCode.NONE means no error
     */
    public ErrorCode getErrorCode() {       // Getter method for errorCode field
        return errorCode;                   // Return the current value of errorCode
    }

    /**
     * Returns the message attached to this packet.
     *
     * @return message text, or null if no message was set
     */
    public String getMessage() {            // Getter method for message field
        return message;                     // Return the current value of message
    }

    /**
     * Sets the packet type for this packet.
     *
     * @param packetType type of operation (e.g., LOGIN, GET_OPEN_SEATS)
     * @return this CommunicationPacket instance (for method chaining)
     */
    public CommunicationPacket setPacketType(PacketType packetType) { // Setter method for packetType with fluent return
        this.packetType = packetType;       // Assign the provided packetType to the field
        return this;                        // Return this instance to allow chaining calls
    }

    /**
     * Sets the payload object for this packet.
     *
     * @param payload payload data to attach (must be Serializable on the wire)
     * @return this CommunicationPacket instance (for method chaining)
     */
    public CommunicationPacket setPayload(Object payload) { // Setter method for payload with fluent return
        this.payload = payload;             // Assign the provided payload object to the field
        return this;                        // Return this instance to allow chaining calls
    }

    /**
     * Sets the session ID for this packet.
     *
     * @param sessionId session identifier issued by the server after login
     * @return this CommunicationPacket instance (for method chaining)
     */
    public CommunicationPacket setSessionId(String sessionId) { // Setter method for sessionId with fluent return
        this.sessionId = sessionId;         // Assign the provided sessionId string to the field
        return this;                        // Return this instance to allow chaining calls
    }

    /**
     * Sets the error code for this packet.
     *
     * @param errorCode error status to attach
     * @return this CommunicationPacket instance (for method chaining)
     */
    public CommunicationPacket setErrorCode(ErrorCode errorCode) { // Setter method for errorCode with fluent return
        this.errorCode = errorCode;         // Assign the provided errorCode value to the field
        return this;                        // Return this instance to allow chaining calls
    }

    /**
     * Sets the message text for this packet.
     *
     * @param message descriptive message (success text or error detail)
     * @return this CommunicationPacket instance (for method chaining)
     */
    public CommunicationPacket setMessage(String message) { // Setter method for message with fluent return
        this.message = message;             // Assign the provided message string to the field
        return this;                        // Return this instance to allow chaining calls
    }

    /**
     * Returns a concise string representation of the packet, useful for logging.
     *
     * @return formatted string showing type, requestId, and errorCode
     */
    @Override
    public String toString() {              // Override of Object.toString() for debugging/logging purposes
        return "Packet{" +                 // Start building a string with the word "Packet{"
                packetType +               // Append the current packetType
                ", req=" + requestId +     // Append the request ID with a label
                ", err=" + errorCode +     // Append the error code with a label
                "}";                       // Close the brace and finish the string
    }
}
