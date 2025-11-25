enum PacketType {
    LOGIN,
    ADD_ACCOUNT, ADD_RESERVATION,
    DELETE_ACCOUNT, DELETE_RESERVATION,
    GET_SEAT_STATUSES_AT_TIME, // gets seat availabilities at a certain time
    GET_ACCT_RESERVATIONS, // gets reservations under current account
    TO_STRING,
    INVALID
}