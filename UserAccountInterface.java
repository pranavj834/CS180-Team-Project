import java.util.ArrayList;

/**
 * An interface for the UserAccount class that contains all the methods for the class to implement.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author Pranav Jasti, lab sec L23
 * @version November 8, 2025
 */

public interface UserAccountInterface {
    
    //getter methods
    String getUsername(); //returns username
    String getFullName(); //returns full name
    String getEmail(); //returns email
    String getPassword();
    ArrayList<Reservation> getReservations(); //returns all reservations that the user has

    //setter methods
    void setUsername(String username); //sets username
    void setFullName(String fullName); //sets full name
    void setEmail(String email); //sets email
    void setPassword(String password); //sets password

    //other methods
    boolean addReservation(Reservation res); //method that adds a reservation
    boolean removeReservation(Reservation res); //method that cancels a reservation
    boolean equals(Object obj); //method to check if the entered username and password are correct.
    String toString(); //method to print account details (added in case just to be safe)
}