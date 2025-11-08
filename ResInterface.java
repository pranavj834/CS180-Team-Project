import java.util.ArrayList;
/**
 * An interface for the Reservation class that contains all the methods for the class to implement.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author Pranav Jasti, lab sec L23
 * @version November 8, 2025
 */
public interface ResInterface {
    //getters
    String getDate(); //returns the date for the reservation
    String getTime(); //returns the time for the reservation
    int getNumPeople(); //returns the number of people reserving the seats
    ArrayList<Integer> getSeats(); //returns the seats of the people who booked the reservation

    //setters
    void setDate(String date); //sets the date for the reservation
    void setTime(String time); //sets the time for the reservation
    void setNumPeople(int numPeople); //sets the number of people reserving the seats
    void setSeats(ArrayList<Integer> seats); //sets aka reserves the seats of the people who booked the reservation
}
