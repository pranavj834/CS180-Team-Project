import java.util.ArrayList;

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
