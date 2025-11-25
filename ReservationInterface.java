import java.util.ArrayList;

/**
 * An interface for the Reservation class.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author jastip, chan531, lab sec L23
 * @version November 8, 2025
 */

public interface ReservationInterface {
    //getters
    String getName();
    String getUsername();
    String getDate(); //returns the date for the reservation
    String getTime(); //returns the time for the reservation
    int getPartySize(); //returns the number of people reserving the seats
    ArrayList<Integer> getSeats(); //returns the seats of the people who booked the reservation

    //setters
    void setName(String name);
    void setUsername(String username);
    void setDate(String date); //sets the date for the reservation
    void setTime(String time); //sets the time for the reservation
    void setPartySize(int partySize); //sets the number of people reserving the seats
    void setSeats(ArrayList<Integer> seats); //sets aka reserves the seats of the people who booked the reservation

    //basic methods
    boolean equals(Object obj);
    String toString();
}