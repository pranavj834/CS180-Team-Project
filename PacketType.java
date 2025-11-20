/*
What it is: An enum of all API operations.

Examples: REGISTER, LOGIN, GET_SEATING_LAYOUT, HOLD_SEATS, CONFIRM_RESERVATION, DEPOSIT_MONEY, etc.

Why it matters: Router key on the server; makes client calls type-safe.
 * @author zhu1220, lab sec L23
 * @version November 8, 2025
 */

import java.io.Serializable;

public enum PacketType implements Serializable {
    // Auth
    REGISTER, LOGIN, LOGOUT, GET_USER,
    // Hours
    GET_HOURS, SET_HOURS,
    // Seating/Layout
    // TODO: REMOVE SEATING LAYOUT FIELDS
    GET_SEATING_LAYOUT, LOCK_SECTION, LOCK_SEAT,
    // Availability/Booking
    GET_OPEN_SEATS, HOLD_SEATS, CONFIRM_RESERVATION, CANCEL_RESERVATION, GET_RESERVATIONS,
    // Pricing
    QUOTE_PRICE, SET_PRICE_RULE,
    // Payment
    DEPOSIT_MONEY, WITHDRAW_MONEY, GET_BALANCE
}
