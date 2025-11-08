import java.util.ArrayList;

public interface UserAccInterface {
    
    //getter methods
    String getUsername(); //returns username
    String getFullName(); //returns full name
    String getEmail(); //returns email
    ArrayList<Reservation> getReservations(); //returns all reservations that the user has

    //setter methods
    void setUsername(String username); //sets username
    void setFullName(String fullName); //sets full name
    void setEmail(String email); //sets email
    void setPassword(String password); //sets password

    //other methods
    boolean addReservation(Reservation res); //method that adds a reservation
    boolean removeReservation(Reservation res); //method that cancels a reservation
    boolean equals(String enteredUserName, String enteredPassword); //method to check if the entered username and password are correct.
    void deleteAccount(); //method to delete account
    String toString(); //method to print account details (added in case just to be safe)
}
