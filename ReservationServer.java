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
 */
public class ReservationServer implements ReservationServerInterface {

    /** In-memory database of accounts and reservations. */
    private final Database database;

    /**
     * Constructs the server state.
     */
    public ReservationServer() {
        this.database = new Database();
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

        UserAccount acct = new UserAccount(username, password, username, username + "@email.com");

        boolean ok = database.addAccount(acct);
        if (!ok) {
            res.setErrorCode(ErrorCode.INVALID_INPUT)
                    .setMessage("Account already exists");
        } else {
            res.setErrorCode(ErrorCode.NONE)
                    .setMessage("Registered OK");
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
                res.setErrorCode(ErrorCode.NONE)
                        .setMessage("Login OK");
                return;
            }
        }

        res.setErrorCode(ErrorCode.AUTH_FAILED)
                .setMessage("Invalid username or password");
    }

    private void handleLogout(CommunicationPacket req, CommunicationPacket res) {
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
        String date = (String) arr[0];
        String time = (String) arr[1];
        int partySize = (int) arr[2];

        // No real seat map yet → return empty list stub.
        List<Integer> openSeats = new ArrayList<>();

        res.setErrorCode(ErrorCode.NONE)
                .setPayload(openSeats)
                .setMessage("Open seats for " + date + " " + time + " (stub, no layout)");
    }

    /** Payload: Object[]{String date, String time, List<Integer> seats, int holdSeconds} */
    private void handleHoldSeats(CommunicationPacket req, CommunicationPacket res) {
        Object[] arr = (Object[]) req.getPayload();
        String date = (String) arr[0];
        String time = (String) arr[1];
        @SuppressWarnings("unchecked")
        List<Integer> seats = (List<Integer>) arr[2];
        int seconds = (int) arr[3];

        res.setErrorCode(ErrorCode.NONE)
                .setMessage("Held seats " + seats + " for ~" + seconds + "s on " + date + " " + time + " (stub)");
    }

    /** Payload: Object[]{String date, String time, List<Integer> seats, int partySize} */
    private void handleConfirmReservation(CommunicationPacket req, CommunicationPacket res) {
        Object[] arr = (Object[]) req.getPayload();
        String date = (String) arr[0];
        String time = (String) arr[1];
        @SuppressWarnings("unchecked")
        ArrayList<Integer> seats = new ArrayList<>((List<Integer>) arr[2]);
        int partySize = (int) arr[3];

        // For now we don't have session tracking; store under a generic guest.
        Reservation reservation = new Reservation(
                "Guest",
                "guest",
                date,
                time,
                partySize,
                seats
        );

        // Optionally add to DB under a dummy account if it exists.
        for (UserAccount acct : database.getAccounts()) {
            if (acct.getUsername().equals("guest")) {
                database.addReservation(acct, reservation);
                break;
            }
        }

        res.setErrorCode(ErrorCode.NONE)
                .setPayload(reservation)
                .setMessage("Reservation confirmed (stub)");
    }

    /** Payload: String reservationId (unused for now) */
    private void handleCancelReservation(CommunicationPacket req, CommunicationPacket res) {
        // No IDs wired up yet → stub.
        res.setErrorCode(ErrorCode.NONE)
                .setMessage("Reservation cancelled (stub)");
    }

    /** Payload: none */
    private void handleGetReservations(CommunicationPacket req, CommunicationPacket res) {
        List<Reservation> all = database.getReservations();
        res.setErrorCode(ErrorCode.NONE)
                .setPayload(all)
                .setMessage("Reservations retrieved");
    }

    // ============================================================
    // ========================= PAYMENT ==========================
    // ============================================================

    /** Payload: Double amount */
    private void handleDeposit(CommunicationPacket req, CommunicationPacket res) {
        double amount = (double) req.getPayload();
        // No wallet tracking implemented yet.
        res.setErrorCode(ErrorCode.NONE)
                .setMessage("Deposited " + amount + " (stub)");
    }

    /** Payload: Double amount */
    private void handleWithdraw(CommunicationPacket req, CommunicationPacket res) {
        double amount = (double) req.getPayload();
        // No wallet tracking implemented yet.
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
