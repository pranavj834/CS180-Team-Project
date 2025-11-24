import java.util.ArrayList;
import java.util.List;

/**
 * Core server-side logic for the restaurant reservation system.
 *
 * Holds shared state such as user accounts and reservations.
 * Provides thread-safe methods for handling incoming
 * CommunicationPacket requests.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author zhu1220, lab sec L23
 * @version November 23, 2025
 */
public class ReservationServer implements ReservationServerInterface {

    /** In-memory database of accounts and reservations. */
    private final Database database;

    /**
     * Constructs the server state and loads initial configuration.
     */
    public ReservationServer() {
        this.database = new Database();
        // Default pricing: everything is free until SET_PRICE_RULE is called.
        Reservation.configurePricing(0.0, 0.0);
    }

    /**
     * Thread-safe entry point: handles a single request packet and returns a response packet.
     *
     * @param req the request from a client
     * @return response to be sent back
     */
    @Override
    public synchronized CommunicationPacket handlePacket(CommunicationPacket req) {
        CommunicationPacket res = new CommunicationPacket()
                .setPacketType(req.getPacketType());

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
                case GET_USER:
                    handleGetUser(req, res);
                    break;

                // ---------- (LEGACY) LAYOUT / SECTIONS ----------
                // We no longer track layout; these are simple stubs.
                case LOCK_SECTION:
                    handleLockSection(req, res);
                    break;

                // ---------- AVAILABILITY / BOOKING ----------
                case GET_OPEN_SEATS:
                    handleGetOpenSeats(req, res);
                    break;
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
            res.setErrorCode(ErrorCode.INTERNAL_ERROR)
                    .setMessage("Server error: " + e.getMessage());
            e.printStackTrace();
        }

        return res;
    }

    // ============================================================
    // ======================== AUTH ==============================
    // ============================================================

    /** Payload: String[]{username, password} */
    private void handleRegister(CommunicationPacket req, CommunicationPacket res) {
        String[] creds = (String[]) req.getPayload();
        String username = creds[0];
        String password = creds[1];

        // Use username for fullName and a dummy email for now.
        UserAccount acct = new UserAccount(username, password, username, username + "@email.com");

        boolean ok = database.addAccount(acct);
        if (!ok) {
            res.setErrorCode(ErrorCode.INVALID_INPUT)
                    .setMessage("Account already exists");
        } else {
            res.setErrorCode(ErrorCode.NONE)
                    .setPayload("Registered user: " + username)
                    .setMessage("Registered");
        }
    }

    /** Payload: String[]{username, password} */
    private void handleLogin(CommunicationPacket req, CommunicationPacket res) {
        String[] creds = (String[]) req.getPayload();
        String username = creds[0];
        String password = creds[1];

        for (UserAccount acct : database.getAccounts()) {
            if (acct.getUsername().equals(username)
                    && acct.getPassword().equals(password)) {
                // No sessions; just say OK.
                res.setErrorCode(ErrorCode.NONE)
                        .setMessage("Login OK");
                return;
            }
        }

        res.setErrorCode(ErrorCode.AUTH_FAILED)
                .setMessage("Invalid username or password");
    }

    private void handleLogout(CommunicationPacket req, CommunicationPacket res) {
        // No session tracking; always "success".
        res.setErrorCode(ErrorCode.NONE)
                .setMessage("Logged out");
    }

    /** Payload: String username */
    private void handleGetUser(CommunicationPacket req, CommunicationPacket res) {
        String username = (String) req.getPayload();

        for (UserAccount acct : database.getAccounts()) {
            if (acct.getUsername().equals(username)) {
                res.setErrorCode(ErrorCode.NONE)
                        .setPayload(acct)
                        .setMessage("User found");
                return;
            }
        }

        res.setErrorCode(ErrorCode.NOT_FOUND)
                .setMessage("User not found");
    }

    // ============================================================
    // ============ (LEGACY) LAYOUT / SECTIONS (STUBS) ============
    // ============================================================

    /** Payload: Object[]{String sectionId, boolean lock} */
    private void handleLockSection(CommunicationPacket req, CommunicationPacket res) {
        // Layout has been removed; acknowledge but do nothing.
        res.setErrorCode(ErrorCode.NONE)
                .setMessage("Section lock/unlock ignored (layout disabled)");
    }

    // ============================================================
    // ================= AVAILABILITY / BOOKING ===================
    // ============================================================

    /** Payload: Object[]{String date, String time, int partySize} */
    private void handleGetOpenSeats(CommunicationPacket req, CommunicationPacket res) {
        Object[] arr = (Object[]) req.getPayload();
        String date = (String) arr[0]; // "2025-01-01"
        String time = (String) arr[1]; // "13:30"
        int partySize = (int) arr[2];

        // No layout → we can't compute real availability yet.
        // Return an empty list stub.
        List<Integer> openSeats = new ArrayList<>();

        res.setErrorCode(ErrorCode.NONE)
                .setPayload(openSeats)
                .setMessage("Open seats for " + date + " " + time + " (stub)");
    }

    /** Payload: Object[]{String date, String time, List<Integer> seats, int holdSeconds} */
    private void handleHoldSeats(CommunicationPacket req, CommunicationPacket res) {
        Object[] arr = (Object[]) req.getPayload();
        String date = (String) arr[0];
        String time = (String) arr[1];
        @SuppressWarnings("unchecked")
        List<Integer> seats = (List<Integer>) arr[2];
        int seconds = (int) arr[3];

        // No real hold tracking; just say OK.
        res.setErrorCode(ErrorCode.NONE)
                .setMessage("Held seats " + seats + " for ~" + seconds + "s (stub) on " + date + " " + time);
    }

    /** Payload: Object[]{String date, String time, List<Integer> seats, int partySize} */
    private void handleConfirmReservation(CommunicationPacket req, CommunicationPacket res) {
        Object[] arr = (Object[]) req.getPayload();
        String date = (String) arr[0];
        String time = (String) arr[1];
        @SuppressWarnings("unchecked")
        List<Integer> seatNums = (List<Integer>) arr[2];
        int partySize = (int) arr[3];

        // For now, we don't know which user is logged in, so use a generic guest.
        ArrayList<Integer> seatsCopy = new ArrayList<>(seatNums);

        Reservation reservation = new Reservation(
                "Guest",          // name
                "guest",          // username
                date,             // "YYYY-MM-DD"
                time,             // "HH:MM"
                partySize,
                seatsCopy
        );

        // We could add this to the database under a dummy account if desired,
        // but for now we just return it.
        res.setErrorCode(ErrorCode.NONE)
                .setPayload(reservation)
                .setMessage("Reservation confirmed (stub)");
    }

    /** Payload: String reservationId (unused for now) */
    private void handleCancelReservation(CommunicationPacket req, CommunicationPacket res) {
        // We don't have IDs wired up yet, so this is a stub.
        res.setErrorCode(ErrorCode.NONE)
                .setMessage("Reservation cancelled (stub)");
    }

    /** Payload: none */
    private void handleGetReservations(CommunicationPacket req, CommunicationPacket res) {
        // No per-user tracking right now; just return all reservations in DB.
        List<Reservation> all = database.getReservations();

        res.setErrorCode(ErrorCode.NONE)
                .setPayload(all)
                .setMessage("Reservations retrieved (stub)");
    }

    // ============================================================
    // ========================= PRICING ==========================
    // ============================================================

    /**
     * Payload: Object[]{List<Integer> seats, String date, String time, int partySize}
     *
     * Right now, we ignore seats/date/time and just price by partySize using
     * Reservation.computePrice().
     */
    private void handleQuotePrice(CommunicationPacket req, CommunicationPacket res) {
        Object[] arr = (Object[]) req.getPayload();
        int partySize = (int) arr[3];

        double price = Reservation.computePrice(partySize);

        res.setErrorCode(ErrorCode.NONE)
                .setPayload(price)
                .setMessage("Quote computed");
    }

    /**
     * Payload: Object[]{Double basePrice, Double perPersonPrice}
     *
     * This updates the static pricing rule stored in Reservation.
     */
    private void handleSetPriceRule(CommunicationPacket req, CommunicationPacket res) {
        Object payload = req.getPayload();

        if (!(payload instanceof Object[])) {
            res.setErrorCode(ErrorCode.INVALID_INPUT)
                    .setMessage("SET_PRICE_RULE payload must be Object[] [basePrice, perPersonPrice]");
            return;
        }

        Object[] arr = (Object[]) payload;
        double base = (double) arr[0];
        double perPerson = (double) arr[1];

        Reservation.configurePricing(base, perPerson);

        res.setErrorCode(ErrorCode.NONE)
                .setMessage("Price rule updated");
    }

    // ============================================================
    // ========================= PAYMENT ==========================
    // ============================================================

    /** Payload: Double amount */
    private void handleDeposit(CommunicationPacket req, CommunicationPacket res) {
        double amount = (double) req.getPayload();
        // No real wallet tracking yet.
        res.setErrorCode(ErrorCode.NONE)
                .setMessage("Deposited " + amount + " (stub)");
    }

    /** Payload: Double amount */
    private void handleWithdraw(CommunicationPacket req, CommunicationPacket res) {
        double amount = (double) req.getPayload();
        // No real wallet tracking yet.
        res.setErrorCode(ErrorCode.NONE)
                .setMessage("Withdrew " + amount + " (stub)");
    }

    /** Payload: none */
    private void handleGetBalance(CommunicationPacket req, CommunicationPacket res) {
        double balance = 0.0; // stub
        res.setErrorCode(ErrorCode.NONE)
                .setPayload(balance)
                .setMessage("Balance retrieved");
    }
}
