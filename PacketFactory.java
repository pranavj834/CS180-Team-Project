import java.time.Duration;          // Duration is used to represent how long a seat hold should last
import java.time.LocalDate;         // LocalDate is used for reservation dates
import java.time.LocalTime;         // LocalTime is used for reservation times
import java.util.List;              // List is used for collections of seat IDs

/**
 * A utility class that builds {@link CommunicationPacket} objects for all
 * supported client→server operations.
 *
 * <p>Each static method in this class corresponds to one high-level API call
 * (such as logging in, asking for open seats, or depositing money) and
 * constructs a packet with the correct {@link PacketType} and payload
 * structure. This keeps packet creation consistent and avoids copy-pasting
 * boilerplate across the GUI and client logic.</p>
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author zhu1220, lab sec L23
 * @version November 8, 2025
 */
public final class PacketFactory {

    /**
     * Private constructor to prevent creating instances of this utility class.
     * All functionality is provided via static methods.
     */
    private PacketFactory() { }  // no-op; ensures the class cannot be instantiated

    // ======================== AUTH ========================

    /**
     * Builds a packet to register a new user.
     *
     * @param u username
     * @param p password
     * @return CommunicationPacket configured for REGISTER
     */
    public static CommunicationPacket register(String u, String p) {
        // Create a new packet, set its type to REGISTER, and attach [username, password] as payload
        return new CommunicationPacket()
                .setPacketType(PacketType.REGISTER)      // tell the server this is a REGISTER request
                .setPayload(new String[]{u, p});         // payload is a String array with username and password
    }

    /**
     * Builds a packet to log in an existing user.
     *
     * @param u username
     * @param p password
     * @return CommunicationPacket configured for LOGIN
     */
    public static CommunicationPacket login(String u, String p) {
        // Similar to register, but with LOGIN packet type
        return new CommunicationPacket()
                .setPacketType(PacketType.LOGIN)         // identify this packet as a LOGIN request
                .setPayload(new String[]{u, p});         // payload carries username and password
    }

    /**
     * Builds a packet to log out the current session.
     *
     * @return CommunicationPacket configured for LOGOUT
     */
    public static CommunicationPacket logout() {
        // No payload is needed; just tell the server to terminate the session
        return new CommunicationPacket()
                .setPacketType(PacketType.LOGOUT);       // packet type alone is enough
    }

    /**
     * Builds a packet to request user information by username.
     *
     * @param username user name to look up
     * @return CommunicationPacket configured for GET_USER
     */
    public static CommunicationPacket getUser(String username) {
        // Request user details; payload is just the username string
        return new CommunicationPacket()
                .setPacketType(PacketType.GET_USER)      // tell server we want user information
                .setPayload(username);                   // payload is the username to query
    }

    // ======================== HOURS ========================

    /**
     * Builds a packet to request the restaurant's hours of operation.
     *
     * @return CommunicationPacket configured for GET_HOURS
     */
    public static CommunicationPacket getHours() {
        // No payload is required; server will return a DailyHours object
        return new CommunicationPacket()
                .setPacketType(PacketType.GET_HOURS);    // request to fetch hours of operation
    }

    /**
     * Builds a packet to update the restaurant's hours of operation.
     *
     * @param hours the new hours to set
     * @return CommunicationPacket configured for SET_HOURS
     */
    public static CommunicationPacket setHours(DailyHours hours) {
        // Send the new DailyHours object to the server for updating
        return new CommunicationPacket()
                .setPacketType(PacketType.SET_HOURS)     // request to change hours
                .setPayload(hours);                      // payload is the DailyHours DTO
    }

    // ================== SEATING / LAYOUT ===================

    /**
     * Builds a packet to request the static seating layout.
     *
     * @return CommunicationPacket configured for GET_SEATING_LAYOUT
     */
    public static CommunicationPacket getSeatingLayout() {
        // Ask the server for the full SeatingLayout; no extra parameters needed
        return new CommunicationPacket()
                .setPacketType(PacketType.GET_SEATING_LAYOUT); // layout fetch request
    }

    /**
     * Builds a packet to lock or unlock a specific section of the restaurant.
     *
     * @param sectionId identifier of the section to modify
     * @param lock      true to lock, false to unlock
     * @return CommunicationPacket configured for LOCK_SECTION
     */
    public static CommunicationPacket lockSection(String sectionId, boolean lock) {
        // Payload is an Object array: [sectionId, lockFlag]
        return new CommunicationPacket()
                .setPacketType(PacketType.LOCK_SECTION)  // request to change section lock state
                .setPayload(new Object[]{sectionId, lock}); // server will cast accordingly
    }

    // ============ AVAILABILITY / BOOKING ===================

    /**
     * Builds a packet to retrieve open seats for a given date/time and party size.
     *
     * @param d         date of interest
     * @param t         time of interest
     * @param partySize number of people to seat
     * @return CommunicationPacket configured for GET_OPEN_SEATS
     */
    public static CommunicationPacket getOpenSeats(LocalDate d, LocalTime t, int partySize) {
        // Payload bundles date, time, and desired party size
        return new CommunicationPacket()
                .setPacketType(PacketType.GET_OPEN_SEATS)     // query for open seats
                .setPayload(new Object[]{d, t, partySize});   // server unpacks [LocalDate, LocalTime, int]
    }

    /**
     * Builds a packet to place a temporary hold on certain seats.
     *
     * @param d       date of the reservation
     * @param t       time of the reservation
     * @param seatIds seat identifiers to hold
     * @param ttl     how long the hold should last; may be null
     * @return CommunicationPacket configured for HOLD_SEATS
     */
    public static CommunicationPacket holdSeats(LocalDate d, LocalTime t,
                                                List<String> seatIds, Duration ttl) {
        // Convert Duration to seconds, treating null as 0 (server can decide what 0 means)
        long seconds = (ttl == null ? 0L : ttl.toSeconds());

        // Payload is [date, time, list of seat IDs, hold duration in seconds]
        return new CommunicationPacket()
                .setPacketType(PacketType.HOLD_SEATS)         // request to hold seats temporarily
                .setPayload(new Object[]{d, t, seatIds, seconds});
    }

    /**
     * Builds a packet to confirm a reservation for the given seats and party size.
     *
     * @param d         reservation date
     * @param t         reservation time
     * @param seatIds   IDs of the seats being reserved
     * @param partySize number of people to seat
     * @return CommunicationPacket configured for CONFIRM_RESERVATION
     */
    public static CommunicationPacket confirmReservation(LocalDate d, LocalTime t,
                                                         List<String> seatIds, int partySize) {
        // Payload is [date, time, selected seat IDs, party size]
        return new CommunicationPacket()
                .setPacketType(PacketType.CONFIRM_RESERVATION)  // finalize a reservation
                .setPayload(new Object[]{d, t, seatIds, partySize});
    }

    /**
     * Builds a packet to cancel an existing reservation.
     *
     * @param reservationId unique identifier for the reservation to cancel
     * @return CommunicationPacket configured for CANCEL_RESERVATION
     */
    public static CommunicationPacket cancelReservation(String reservationId) {
        // Payload is simply the reservation ID string
        return new CommunicationPacket()
                .setPacketType(PacketType.CANCEL_RESERVATION)   // request to cancel a reservation
                .setPayload(reservationId);
    }

    /**
     * Builds a packet to retrieve all reservations for the current user.
     *
     * @return CommunicationPacket configured for GET_RESERVATIONS
     */
    public static CommunicationPacket getReservations() {
        // No payload is required; the server identifies the user via sessionId
        return new CommunicationPacket()
                .setPacketType(PacketType.GET_RESERVATIONS);    // query all reservations for current session
    }

    // ====================== PRICING =========================

    /**
     * Builds a packet to request a price quote for a set of seats at a given time.
     *
     * @param seatIds   list of seat IDs
     * @param d         date of reservation
     * @param t         time of reservation
     * @param partySize number of people in the party
     * @return CommunicationPacket configured for QUOTE_PRICE
     */
    public static CommunicationPacket quotePrice(List<String> seatIds, LocalDate d,
                                                 LocalTime t, int partySize) {
        // Payload is [seat IDs, date, time, party size]
        return new CommunicationPacket()
                .setPacketType(PacketType.QUOTE_PRICE)         // ask server to compute price
                .setPayload(new Object[]{seatIds, d, t, partySize});
    }

    /**
     * Builds a packet to update the server's pricing rules.
     *
     * @param rule PriceRule configuration object
     * @return CommunicationPacket configured for SET_PRICE_RULE
     */
    public static CommunicationPacket setPriceRule(PriceRule rule) {
        // Payload is the PriceRule DTO describing new pricing configuration
        return new CommunicationPacket()
                .setPacketType(PacketType.SET_PRICE_RULE)      // request to change pricing configuration
                .setPayload(rule);
    }

    // ======================= PAYMENT ========================

    /**
     * Builds a packet to deposit money into the current user's account.
     *
     * @param amount amount to deposit
     * @return CommunicationPacket configured for DEPOSIT_MONEY
     */
    public static CommunicationPacket depositMoney(double amount) {
        // Payload is just the numeric amount
        return new CommunicationPacket()
                .setPacketType(PacketType.DEPOSIT_MONEY)       // deposit operation
                .setPayload(amount);
    }

    /**
     * Builds a packet to withdraw money from the current user's account.
     *
     * @param amount amount to withdraw
     * @return CommunicationPacket configured for WITHDRAW_MONEY
     */
    public static CommunicationPacket withdrawMoney(double amount) {
        // Payload is the requested withdrawal amount
        return new CommunicationPacket()
                .setPacketType(PacketType.WITHDRAW_MONEY)      // withdraw operation
                .setPayload(amount);
    }

    /**
     * Builds a packet to request the current user's wallet balance.
     *
     * @return CommunicationPacket configured for GET_BALANCE
     */
    public static CommunicationPacket getBalance() {
        // No payload needed; server uses sessionId to find the user's balance
        return new CommunicationPacket()
                .setPacketType(PacketType.GET_BALANCE);        // query wallet balance
    }
}
