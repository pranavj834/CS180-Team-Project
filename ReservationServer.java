
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Core server-side logic for the restaurant reservation system.
 *
 * Holds shared state such as user accounts, reservations, seating layout,
 * and pricing rules. Provides thread-safe methods for handling
 * incoming CommunicationPacket requests.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author zhu1220, lab sec L23
 * @version November 8, 2025
 */
public class ReservationServer implements ReservationServerInterface {

    // === Shared server state ===
    private final Database database;          // your existing Database (UserAccount + Reservation)
    private final Map<String, String> sessions; // sessionId -> username mapping

    /**
     * Constructs the server state and loads initial configuration.
     * You can load from files here if you want persistence.
     */
    public ReservationServer() {
        this.database = new Database();           // your in-memory DB (Phase 1)
        this.sessions = new HashMap<>();          // track active sessions

        // TODO: load pricing / reservations from files if you want persistence
        // e.g., Reservation.configurePricing(savedBase, savedPerPerson);
    }

    /**
     * Thread-safe entry point: handles a single request packet and returns a response packet.
     *
     * @param req the request from a client
     * @return response to be sent back
     */
    public CommunicationPacket handlePacket(CommunicationPacket req) {
        CommunicationPacket res = new CommunicationPacket()
                .setPacketType(req.getPacketType())
                .setSessionId(req.getSessionId()); // echo sessionId back

        try {
            switch (req.getPacketType()) {
                // ---------- AUTH ----------
                case REGISTER:
                    handleRegister(req, res);
                    break;
                case LOGIN:
                    handleLogin(req, res);
                    break;
                case LOGOUT:
                    handleLogout(req, res);
                    break;

                // ---------- SEATING / LAYOUT ----------
                case LOCK_SECTION:
                    handleLockSection(req, res);
                    break;

                // ---------- AVAILABILITY / BOOKING ----------
                case HOLD_SEATS:
                    handleHoldSeats(req, res);
                    break;
                case CONFIRM_RESERVATION:
                    handleConfirmReservation(req, res);
                    break;
                case CANCEL_RESERVATION:
                    handleCancelReservation(req, res);
                    break;
                case GET_RESERVATIONS:
                    handleGetReservations(req, res);
                    break;

                // ---------- PRICING ----------
                case QUOTE_PRICE:
                    handleQuotePrice(req, res);
                    break;
                case SET_PRICE_RULE:
                    handleSetPriceRule(req, res);
                    break;

                // ---------- PAYMENT ----------
                case DEPOSIT_MONEY:
                    handleDeposit(req, res);
                    break;
                case WITHDRAW_MONEY:
                    handleWithdraw(req, res);
                    break;
                case GET_BALANCE:
                    handleGetBalance(req, res);
                    break;

                default:
                    res.setErrorCode(ErrorCode.INVALID_INPUT)
                            .setMessage("Unsupported packet type: " + req.getPacketType());
            }
        } catch (Exception e) {
            // Any unhandled exception becomes an INTERNAL_ERROR
            res.setErrorCode(ErrorCode.INTERNAL_ERROR)
                    .setMessage("Server error: " + e.getMessage());
            e.printStackTrace();
        }

        return res;
    }

    // ====================== AUTH HANDLERS ======================

    private synchronized void handleRegister(CommunicationPacket req, CommunicationPacket res) {
        String[] creds = (String[]) req.getPayload();       // [username, password]
        String username = creds[0];
        String password = creds[1];

        // TODO: check for duplicate usernames using your Database

        res.setErrorCode(ErrorCode.NONE)
                .setMessage("Registered (stub)");
        res.setPayload("Registered user: " + username);
    }

    private synchronized void handleLogin(CommunicationPacket req, CommunicationPacket res) {
        String[] creds = (String[]) req.getPayload();       // [username, password]
        String username = creds[0];
        String password = creds[1];

        // TODO: verify username/password using your UserAccount + Database

        // Example: simple "always success" stub with generated session:
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, username);                  // track session
        res.setErrorCode(ErrorCode.NONE)
                .setPayload(sessionId)                      // client expects sessionId in payload
                .setMessage("Login OK (stub)");
    }

    private synchronized void handleLogout(CommunicationPacket req, CommunicationPacket res) {
        String sessionId = req.getSessionId();
        if (sessionId != null) {
            sessions.remove(sessionId);                     // remove session mapping
        }
        res.setErrorCode(ErrorCode.NONE)
                .setMessage("Logged out");
    }

    // ================== SEATING / LAYOUT HANDLERS ================== TODO: remove layout

    private synchronized void handleLockSection(CommunicationPacket req, CommunicationPacket res) {
        Object[] arr = (Object[]) req.getPayload();         // [sectionId, lockFlag]
        String sectionId = (String) arr[0];
        boolean lock = (boolean) arr[1];

        // TODO: update 'layout' so seats in this section become locked/unlocked.

        res.setErrorCode(ErrorCode.NONE)
                .setMessage("Section " + sectionId + " lock=" + lock + " (stub)");
    }

    // ================== BOOKING / AVAILABILITY HANDLERS ==================

    private synchronized void handleHoldSeats(CommunicationPacket req, CommunicationPacket res) {
        // [LocalDate, LocalTime, seatIds, ttlSeconds]
        Object[] arr = (Object[]) req.getPayload();
        LocalDate date = (LocalDate) arr[0];
        LocalTime time = (LocalTime) arr[1];
        @SuppressWarnings("unchecked")
        List<String> seatIds = (List<String>) arr[2];
        long ttlSeconds = (long) arr[3];

        // TODO: implement actual temporary hold logic using (date, time, seatIds, ttlSeconds)

        res.setErrorCode(ErrorCode.NONE)
                .setMessage("Seats held for ~" + ttlSeconds + " seconds (stub)");
    }

    private synchronized void handleConfirmReservation(CommunicationPacket req, CommunicationPacket res) {
        // [LocalDate, LocalTime, seatIds, partySize]
        Object[] arr = (Object[]) req.getPayload();
        LocalDate date = (LocalDate) arr[0];
        LocalTime time = (LocalTime) arr[1];
        @SuppressWarnings("unchecked")
        List<String> seatIds = (List<String>) arr[2];
        int partySize = (int) arr[3];

        String sessionId = req.getSessionId();
        String username = sessions.get(sessionId);          // find username for this session

        if (username == null) {
            res.setErrorCode(ErrorCode.NOT_AUTHORIZED)
                    .setMessage("Not logged in");
            return;
        }

        // Convert date/time to Strings (Reservation uses String for these)
        String dateString = date.toString();
        String timeString = time.toString();

        // Stub: convert seatIds to numeric seat numbers (or leave empty for now)
        ArrayList<Integer> seatNumbers = new ArrayList<>();
        // TODO: real mapping from seatId -> seatNumber if needed

        // For now, use username as "name" as well; you can add a real name field later
        Reservation reservation = new Reservation(username, username, dateString, timeString, partySize, seatNumbers);

        // TODO: add to database structure (associate with this username)
        // Example:
        // UserAccount acct = database.getUser(username);
        // if (acct != null) {
        //     acct.addReservation(reservation);
        // }

        res.setErrorCode(ErrorCode.NONE)
                .setPayload(reservation)
                .setMessage("Reservation confirmed");
    }

    private synchronized void handleCancelReservation(CommunicationPacket req, CommunicationPacket res) {
        String reservationId = (String) req.getPayload();

        // TODO: find and remove reservation by ID
        // (Depends on how you identify reservations in your Database.)

        res.setErrorCode(ErrorCode.NONE)
                .setMessage("Reservation " + reservationId + " cancelled (stub)");
    }

    private synchronized void handleGetReservations(CommunicationPacket req, CommunicationPacket res) {
        String sessionId = req.getSessionId();
        String username = sessions.get(sessionId);

        if (username == null) {
            res.setErrorCode(ErrorCode.NOT_AUTHORIZED)
                    .setMessage("Not logged in");
            return;
        }

        // TODO: query Database for all reservations belonging to this username.
        List<Reservation> myRes = new ArrayList<>();

        // Example if Database supports it:
        // UserAccount acct = database.getUser(username);
        // if (acct != null) {
        //     myRes = acct.getReservations();
        // }

        res.setErrorCode(ErrorCode.NONE)
                .setPayload(myRes)
                .setMessage("Reservations retrieved (stub)");
    }

    // ====================== PRICING HANDLERS ======================

    private synchronized void handleQuotePrice(CommunicationPacket req, CommunicationPacket res) {
        // [seatIds, LocalDate, LocalTime, partySize]
        Object[] arr = (Object[]) req.getPayload();
        // We ignore seatIds/date/time for now and just price by party size
        int partySize = (int) arr[3];

        // Use Reservation's simple pricing rule
        double price = Reservation.computePrice(partySize);

        res.setErrorCode(ErrorCode.NONE)
                .setPayload(price)
                .setMessage("Quote computed");
    }

    private synchronized void handleSetPriceRule(CommunicationPacket req, CommunicationPacket res) {
        // Payload: [Double basePrice, Double perPersonPrice]
        Object payload = req.getPayload();

        if (!(payload instanceof Object[])) {
            res.setErrorCode(ErrorCode.INVALID_INPUT)
                    .setMessage("SET_PRICE_RULE payload must be Object[] [basePrice, perPersonPrice]");
            return;
        }

        Object[] arr = (Object[]) payload;
        double base = (double) arr[0];
        double perPerson = (double) arr[1];

        // Update global pricing config in Reservation
        Reservation.configurePricing(base, perPerson);

        res.setErrorCode(ErrorCode.NONE)
                .setMessage("Price rule updated");
    }

    // ======================= PAYMENT HANDLERS =======================

    private synchronized void handleDeposit(CommunicationPacket req, CommunicationPacket res) {
        double amount = (double) req.getPayload();
        String username = sessions.get(req.getSessionId());

        if (username == null) {
            res.setErrorCode(ErrorCode.NOT_AUTHORIZED)
                    .setMessage("Not logged in");
            return;
        }

        // TODO: update user's balance in Database

        res.setErrorCode(ErrorCode.NONE)
                .setMessage("Deposited " + amount + " (stub)");
    }

    private synchronized void handleWithdraw(CommunicationPacket req, CommunicationPacket res) {
        double amount = (double) req.getPayload();
        String username = sessions.get(req.getSessionId());

        if (username == null) {
            res.setErrorCode(ErrorCode.NOT_AUTHORIZED)
                    .setMessage("Not logged in");
            return;
        }

        // TODO: check balance and subtract if possible

        res.setErrorCode(ErrorCode.NONE)
                .setMessage("Withdrew " + amount + " (stub)");
    }

    private synchronized void handleGetBalance(CommunicationPacket req, CommunicationPacket res) {
        String username = sessions.get(req.getSessionId());

        if (username == null) {
            res.setErrorCode(ErrorCode.NOT_AUTHORIZED)
                    .setMessage("Not logged in");
            return;
        }

        // TODO: lookup user balance in Database.
        double balance = 0.0; // stub

        res.setErrorCode(ErrorCode.NONE)
                .setPayload(balance)
                .setMessage("Balance retrieved");
    }
}
