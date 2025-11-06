/*
What it is: A small cache of frequently-used data to avoid extra round-trips.

Holds: SeatingLayout, DailyHours, open seat lists per timeslot, your Reservation list, and wallet balance.

Why it matters: Faster UI, fewer network calls.
 */

import java.time.LocalDateTime;
import java.util.*;
public class ClientCache {
    private SeatingLayout layout;
    private DailyHours hours;
    private final Map<LocalDateTime, List<String>> openSeatsByTimeslot = new HashMap<>();
    private List<Reservation> myReservations = new ArrayList<>();
    private Double walletBalance;

    public SeatingLayout getLayout() { return layout; }
    public void setLayout(SeatingLayout layout) { this.layout = layout; }

    public DailyHours getHours() { return hours; }
    public void setHours(DailyHours hours) { this.hours = hours; }

    public void putOpenSeats(LocalDateTime key, List<String> ids) { openSeatsByTimeslot.put(key, ids); }
    public List<String> getOpenSeats(LocalDateTime key) { return openSeatsByTimeslot.getOrDefault(key, Collections.emptyList()); }

    public List<Reservation> getMyReservations() { return Collections.unmodifiableList(myReservations); }
    public void setMyReservations(List<Reservation> list) {
        this.myReservations = list == null ? new ArrayList<>() : new ArrayList<>(list);
    }

    public Double getWalletBalance() { return walletBalance; }
    public void setWalletBalance(Double walletBalance) { this.walletBalance = walletBalance; }
}
