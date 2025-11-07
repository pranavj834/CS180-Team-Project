import java.util.ArrayList;

public class Reservation implements ResInterface {
	private String date;
	private String time;
    private int numPeople;
	private ArrayList<Integer> seats;
    
	public Reservation(String date, String time, int numPeople, ArrayList<Integer> seats) {
		this.date = date;
		this.time = time;
        this.numPeople = numPeople;
		this.seats = seats;
	}

	public String getDate() {
		return date;
	}

	public String getTime() {
		return time;
	}

	public int getNumPeople() {
		return numPeople;
	}

	public ArrayList<Integer> getSeats() {
		return seats;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setNumPeople(int numPeople) {
		this.numPeople = numPeople;
	}

	public void setSeats(ArrayList<Integer> seats) {
		this.seats = seats;
	}
}
