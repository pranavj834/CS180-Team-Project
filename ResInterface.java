import java.util.ArrayList;

public interface ResInterface {
    String getDate();
    String getTime();
    int getNumPeople();
    ArrayList<Integer> getSeats();

    void setDate(String date);
    void setTime(String time);
    void setNumPeople(int numPeople);
    void setSeats(ArrayList<Integer> seats);
}