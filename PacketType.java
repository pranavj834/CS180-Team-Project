import java.io.Serializable;

/**
 * Enumeration of all supported operations that can be requested via
 * {@link CommunicationPacket}s.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author zhu1220, lab sec L23
 * @version November 8, 2025
 */
public enum PacketType implements Serializable {

    // Auth
    REGISTER,
    LOGIN,
    LOGOUT,
    GET_USER,
    LOCK_SECTION,
    GET_OPEN_SEATS,

    // Availability / Booking

    HOLD_SEATS,
    CONFIRM_RESERVATION,
    CANCEL_RESERVATION,
    GET_RESERVATIONS,

    // Pricing
    QUOTE_PRICE,
    SET_PRICE_RULE,

    // Payment
    DEPOSIT_MONEY,
    WITHDRAW_MONEY,
    GET_BALANCE
}
