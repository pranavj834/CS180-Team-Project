import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Lightweight client-side cache for frequently accessed reservation data.
 *
 * <p>This simplified cache stores only the current user's reservations and
 * wallet balance.</p>
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 */
public class ClientCache {
    private List<Reservation> myReservations = new ArrayList<>();
    private Double walletBalance;   // may be null until first getBalance()

    public List<Reservation> getMyReservations() {
        return Collections.unmodifiableList(myReservations);
    }

    public void setMyReservations(List<Reservation> list) {
        this.myReservations = (list == null)
                ? new ArrayList<>()
                : new ArrayList<>(list);
    }

    public Double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(Double walletBalance) {
        this.walletBalance = walletBalance;
    }
}
