public interface DatabaseInterface {
    boolean addAccount(UserAccount account);
    boolean addReservation(UserAccount account, Reservation reservation);
    boolean deleteAccount(UserAccount account);
    boolean deleteReservation(UserAccount account, Reservation reservation);
}