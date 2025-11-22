import java.time.Duration;          // For representing how long seat holds last
import java.time.LocalDate;         // For reservation dates
import java.time.LocalDateTime;     // For date+time keys in cache
import java.time.LocalTime;         // For reservation times
import java.util.List;              // For lists of seats/reservations

/**
 * Unified client-side API for talking to the server.
 *
 * <p>This class combines what used to be multiple smaller client classes:</p>
 * <ul>
 *     <li>AuthClient (register/login/logout)</li>
 *     <li>Hours access (getHours/setHours)</li>
 *     <li>SeatingClient (seating layout + lockSection)</li>
 *     <li>BookingClient (open seats, hold, confirm, cancel, list reservations)</li>
 *     <li>PricingClient (price quote)</li>
 *     <li>PaymentClient (deposit/withdraw/getBalance)</li>
 * </ul>
 *
 * The GUI should only need a single instance of ClientAPI and can call its methods directly
 * without worrying about CommunicationPacket, PacketType, or socket logic.
 *
 * /**
 *  * A class that provides a unified client-side API for communicating with the
 *  * restaurant reservation server.
 *  *
 *  * <p>This class combines authentication, hours lookup, seating layout retrieval,
 *  * booking, pricing, and payment operations into a single interface that the GUI
 *  * can use without dealing directly with network packets or sockets.</p>
 *  *
 *  * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *  *
 *  * @author Zhu1220, lab sec L23
 *  * @version November 8, 2025
 *  */

public class ClientAPI {
    private final ClientConnection conn;  // low-level connection that sends/receives packets
    private final ClientCache cache;      // local cache of hours, layout, reservations, etc.

    /**
     * Constructs a new ClientAPI.
     *
     * @param conn    the ClientConnection used to communicate with the server
     * @param cache   the ClientCache to store frequently used data
     */
    public ClientAPI(ClientConnection conn, ClientCache cache) {
        this.conn = conn;         // save connection
        this.cache = new ClientCache();       // save cache
    }

    // ============================================================
    // ================ AUTH (REGISTER / LOGIN / LOGOUT) ==========
    // ============================================================

    /**
     * Registers a new user with the server.
     *
     * @param username desired username
     * @param password desired password
     * @return a message from the server (e.g., "Success!" or additional info)
     * @throws Exception if communication fails or the server returns an error
     */
    public String register(String username, String password) throws Exception {
        // Build and send REGISTER packet to server
        CommunicationPacket res = conn.send(PacketFactory.register(username, password));

        // If server reports an error, convert it to an exception for the GUI to handle
        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }

        // Return whatever payload server sent (often a success message)
        return String.valueOf(res.getPayload());
    }

    /**
     * Attempts to log in the user with the given credentials.
     *
     * @param username username
     * @param password password
     * @return true if login succeeded, false if credentials were invalid
     * @throws Exception if communication fails
     */
    public boolean login(String username, String password) throws Exception {
        // Ask server to log in with username/password
        CommunicationPacket res = conn.send(PacketFactory.login(username, password));

        // If login failed (AUTH_FAILED or other error), just return false
        if (res.getErrorCode() != ErrorCode.NONE) {
            return false;
        }

        // On success, server should send back a session ID (string)
        String sessionId = (String) res.getPayload();

        // Store that session ID in the connection so it's attached to all future packets
        conn.setSessionId(sessionId);

        return true; // login succeeded
    }

    /**
     * Logs out the currently logged-in user.
     *
     * @throws Exception if communication fails or the server returns an error
     */
    public void logout() throws Exception {
        // Ask server to log out current session
        CommunicationPacket res = conn.send(PacketFactory.logout());

        // If server says logout failed, throw an exception
        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }

        // stop sending session ID
        conn.setSessionId(null);
    }

    // ============================================================
    // ====================== HOURS OF OPERATION ===================
    // ============================================================

    /**
     * Retrieves restaurant hours from the server (with caching).
     *
     * @return DailyHours representing open/close times
     * @throws Exception if communication fails or the server returns an error
     */
    public DailyHours getHours() throws Exception {
        // If we already have hours in cache, return them instead of hitting the server
        if (cache.getHours() != null) {
            return cache.getHours();
        }

        // Ask server for hours
        CommunicationPacket res = conn.send(PacketFactory.getHours());

        // If server reports an error, throw it
        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }

        // Payload should be a DailyHours object
        DailyHours hours = (DailyHours) res.getPayload();

        // Cache it so we don’t need to request again immediately
        cache.setHours(hours);

        return hours;
    }

    /**
     * Updates the restaurant's hours on the server.
     * Typically only allowed for manager/admin users.
     *
     * @param hours new hours to set
     * @throws Exception if communication fails or server returns an error
     */
    public void setHours(DailyHours hours) throws Exception {
        // Send SET_HOURS request with new DailyHours object
        CommunicationPacket res = conn.send(PacketFactory.setHours(hours));

        // Check for errors
        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }

        // Update local cache to reflect new official hours
        cache.setHours(hours);
    }

    // ============================================================
    // ================= SEATING / LAYOUT (STATIC MAP) ============
    // ============================================================

    /**
     * Locks or unlocks a specific section of the restaurant (e.g., for events).
     *
     * @param sectionId ID of the section
     * @param lock      true to lock the section, false to unlock
     * @throws Exception if communication fails or server returns an error
     */
    public void lockSection(String sectionId, boolean lock) throws Exception {
        // Ask server to lock/unlock section
        CommunicationPacket res = conn.send(PacketFactory.lockSection(sectionId, lock));

        // If error, throw exception
        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }
    }

    // ============================================================
    // ================= BOOKING / AVAILABILITY ===================
    // ============================================================

    /**
     * Gets a list of open seat IDs for a given date, time, and party size.
     *
     * @param date      reservation date
     * @param time      reservation time
     * @param partySize number of people in the party
     * @return a list of seat IDs that are available
     * @throws Exception if communication fails or server returns an error
     */
    public List<String> getOpenSeats(LocalDate date, LocalTime time, int partySize) throws Exception {
        // Build a key for caching open seats by date+time
        LocalDateTime key = LocalDateTime.of(date, time);

        // Ask server for open seats
        CommunicationPacket res = conn.send(PacketFactory.getOpenSeats(date, time, partySize));

        // If server reports an error, throw exception
        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }

        // Payload should be a List<String> of seat IDs
        @SuppressWarnings("unchecked")
        List<String> seatIds = (List<String>) res.getPayload();

        // Cache the open seats for that timeslot
        cache.putOpenSeats(key, seatIds);

        return seatIds;
    }

    /**
     * Places a temporary hold on a set of seats for a given date and time.
     *
     * @param date    date of reservation
     * @param time    time of reservation
     * @param seatIds list of seat IDs to hold
     * @param ttl     time-to-live for the hold
     * @return true if hold was successful, false otherwise
     * @throws Exception if communication fails
     */
    public boolean holdSeats(LocalDate date, LocalTime time, List<String> seatIds, Duration ttl) throws Exception {
        // Ask server to hold seats
        CommunicationPacket res = conn.send(PacketFactory.holdSeats(date, time, seatIds, ttl));

        // If server responded with an error, treat as hold failure
        if (res.getErrorCode() != ErrorCode.NONE) {
            return false;
        }

        // If no error code, assume hold succeeded
        return true;
    }

    /**
     * Confirms a reservation for the given date/time/seats and party size.
     *
     * @param date      reservation date
     * @param time      reservation time
     * @param seatIds   list of seat IDs being reserved
     * @param partySize number of people in the reservation
     * @return a Reservation DTO describing the confirmed reservation
     * @throws Exception if communication fails or server returns an error
     */
    public Reservation confirmReservation(LocalDate date, LocalTime time, List<String> seatIds, int partySize)
            throws Exception {
        // Ask server to confirm reservation
        CommunicationPacket res = conn.send(PacketFactory.confirmReservation(date, time, seatIds, partySize));

        // If server error, throw exception
        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }

        // Payload should be a Reservation object
        return (Reservation) res.getPayload();
    }

    /**
     * Cancels a reservation by ID.
     *
     * @param reservationId ID of the reservation to cancel
     * @return true if cancellation succeeded, false otherwise
     * @throws Exception if communication fails
     */
    public boolean cancelReservation(String reservationId) throws Exception {
        // Ask server to cancel reservation
        CommunicationPacket res = conn.send(PacketFactory.cancelReservation(reservationId));

        // If server error, treat as cancellation failure
        if (res.getErrorCode() != ErrorCode.NONE) {
            return false;
        }

        // If no error code, assume cancellation success
        return true;
    }

    /**
     * Retrieves all reservations for the currently logged-in user.
     *
     * @return list of Reservation objects
     * @throws Exception if communication fails or server returns an error
     */
    public List<Reservation> getReservations() throws Exception {
        // Ask server for all reservations for current user
        CommunicationPacket res = conn.send(PacketFactory.getReservations());

        // If server error, throw exception
        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }

        // Payload should be a List<Reservation>
        @SuppressWarnings("unchecked")
        List<Reservation> list = (List<Reservation>) res.getPayload();

        // Cache them for quick access
        cache.setMyReservations(list);

        return list;
    }

    // ============================================================
    // ========================= PRICING ==========================
    // ============================================================

    /**
     * Gets a price quote from the server for a set of seats at a given time.
     *
     * @param seatIds   seats being reserved
     * @param date      reservation date
     * @param time      reservation time
     * @param partySize number of people
     * @return total price as a double
     * @throws Exception if communication fails or server returns an error
     */
    public double quote(List<String> seatIds, LocalDate date, LocalTime time, int partySize) throws Exception {
        // Ask server for quote
        CommunicationPacket res = conn.send(PacketFactory.quotePrice(seatIds, date, time, partySize));

        // If server error, throw exception
        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }

        // Payload should be a Double
        return (double) res.getPayload();
    }

    // ============================================================
    // ========================= PAYMENT ==========================
    // ============================================================

    /**
     * Deposits money into the current user's wallet on the server.
     *
     * @param amount amount to deposit (should be positive)
     * @throws Exception if communication fails or server returns an error
     */
    public void deposit(double amount) throws Exception {
        // Ask server to deposit money
        CommunicationPacket res = conn.send(PacketFactory.depositMoney(amount));

        // If error, throw exception
        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }

        // Refresh cached balance
        cache.setWalletBalance(getBalance());
    }

    /**
     * Withdraws money from the current user's wallet on the server.
     *
     * @param amount amount to withdraw
     * @return true if withdraw succeeded, false otherwise (like insufficient funds)
     * @throws Exception if communication fails
     */
    public boolean withdraw(double amount) throws Exception {
        // Ask server to withdraw money
        CommunicationPacket res = conn.send(PacketFactory.withdrawMoney(amount));

        // If server error (e.g., insufficient funds), treat as failure
        if (res.getErrorCode() != ErrorCode.NONE) {
            return false;
        }

        // Refresh cached balance after withdrawal
        cache.setWalletBalance(getBalance());

        return true;
    }

    /**
     * Retrieves the current wallet balance from the server and updates cache.
     *
     * @return current wallet balance
     * @throws Exception if communication fails or server returns an error
     */
    public double getBalance() throws Exception {
        // Ask server for balance
        CommunicationPacket res = conn.send(PacketFactory.getBalance());

        // If error, throw exception
        if (res.getErrorCode() != ErrorCode.NONE) {
            throw new IllegalStateException(res.getMessage());
        }

        // Payload should be a Double
        double bal = (double) res.getPayload();

        // Update cached balance
        cache.setWalletBalance(bal);

        return bal;
    }
}
