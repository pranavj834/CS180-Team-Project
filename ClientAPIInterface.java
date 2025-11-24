import java.util.List;

public interface ClientAPIInterface {

    // AUTH
    String register(String username, String password) throws Exception;
    boolean login(String username, String password) throws Exception;
    void logout() throws Exception;

    // BOOKING
    List<Integer> getOpenSeats(String date, String time, int partySize) throws Exception;
    boolean holdSeats(String date, String time, List<Integer> seatNumbers, int holdSeconds) throws Exception;
    Reservation confirmReservation(String date, String time, List<Integer> seatNumbers, int partySize) throws Exception;
    boolean cancelReservation(String reservationId) throws Exception;
    List<Reservation> getReservations() throws Exception;

    // PRICING (if kept client-side only)
    double quote(List<Integer> seatNumbers, String date, String time, int partySize) throws Exception;

    // PAYMENT
    void deposit(double amount) throws Exception;
    boolean withdraw(double amount) throws Exception;
    double getBalance() throws Exception;
}
