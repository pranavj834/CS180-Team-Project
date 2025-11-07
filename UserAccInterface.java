import java.util.ArrayList;

public interface UserAccInterface {
    boolean addReservation(Reservation res);
    boolean removeReservation(Reservation res);
    boolean equals(String enteredUserName, String enteredPassword);
    void deleteAccount();
    String toString();

    String getUsername();
    String getFullName();
    String getEmail();
    ArrayList<Reservation> getReservations();

    void setUsername(String username);
    void setFullName(String fullName);
    void setEmail(String email);
    void setPassword(String password);
}