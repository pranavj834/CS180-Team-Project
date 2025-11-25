/**
 * Contains enums that help identify the purpose
 * of a Packet sent between the client and server.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author chan531, lab sec L23
 * @version November 24, 2025
 */

enum PacketType {
    LOGIN,
    ADD_ACCOUNT, ADD_RESERVATION,
    DELETE_ACCOUNT, DELETE_RESERVATION,
    GET_SEAT_STATUSES_AT_TIME, // gets seat availabilities at a certain time
    GET_ACCT_RESERVATIONS, // gets reservations under current account
    TO_STRING,
    INVALID
}