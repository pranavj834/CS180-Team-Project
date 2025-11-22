
import java.util.*;

/**
 * Lightweight client-side cache for frequently accessed reservation data.
 *
 * <p>This cache is used to avoid unnecessary round trips to the server by
 * storing the seating layout, hours of operation, open seat lists per
 * timeslot, the current user's reservations, and the wallet balance.</p>
 *
 * <p>The server remains the ultimate source of truth; the cache simply
 * improves responsiveness of the GUI.</p>
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author zhu1220, lab sec L23
 * @version November 8, 2025
 */
public class ClientCache {
    private DailyHours hours;                                   // cached hours of operation
    private final Map<LocalDateTime, List<String>> openSeatsByTimeslot =
            new HashMap<>();                                    // cached open seats per date+time
    private List<Reservation> myReservations = new ArrayList<>(); // cached reservations for current user
    private Double walletBalance;                               // cached wallet balance (may be null)

    /**
     * Returns the cached hours of operation, or null if not yet fetched.
     *
     * @return DailyHours object or null
     */
    public DailyHours getHours() {
        return hours;
    }

    /**
     * Updates the cached hours of operation.
     *
     * @param hours new hours to store
     */
    public void setHours(DailyHours hours) {
        this.hours = hours;
    }

    /**
     * Caches a list of open seat IDs for the given timeslot.
     *
     * @param key date and time representing the slot
     * @param ids list of open seat IDs
     */
    public void putOpenSeats(LocalDateTime key, List<String> ids) {
        openSeatsByTimeslot.put(key, ids);
    }

    /**
     * Returns the cached open seats for the given timeslot, or an empty list
     * if none are cached.
     *
     * @param key date and time representing the slot
     * @return list of seat IDs, possibly empty
     */
    public List<String> getOpenSeats(LocalDateTime key) {
        return openSeatsByTimeslot.getOrDefault(key, Collections.emptyList());
    }

    /**
     * Returns an unmodifiable view of the cached reservations for the current user.
     *
     * @return list of reservations
     */
    public List<Reservation> getMyReservations() {
        return Collections.unmodifiableList(myReservations);
    }

    /**
     * Updates the cached reservations for the current user.
     *
     * @param list new reservations list; null will be treated as empty
     */
    public void setMyReservations(List<Reservation> list) {
        this.myReservations = (list == null)
                ? new ArrayList<>()
                : new ArrayList<>(list);
    }

    /**
     * Returns the cached wallet balance, or null if not yet retrieved.
     *
     * @return wallet balance or null
     */
    public Double getWalletBalance() {
        return walletBalance;
    }

    /**
     * Updates the cached wallet balance.
     *
     * @param walletBalance new balance value
     */
    public void setWalletBalance(Double walletBalance) {
        this.walletBalance = walletBalance;
    }
}
