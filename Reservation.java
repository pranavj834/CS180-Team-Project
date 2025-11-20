import java.util.ArrayList;

/**
 * A class that creates reservations at a restaurant.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author jastip, chan531, lab sec L23
 * @version November 8, 2025
 */

public class Reservation implements ReservationInterface {

	//instance variables; variables are based on whatever is important to create reservations
	private String name;
	private String username;
	private String date;
	private String time;
    private int numPeople;
	private ArrayList<Integer> seats;

	//constructor; sets the respective instance variables based on their respective inputs given
	public Reservation(String name, String username, String date,
					   String time, int numPeople, ArrayList<Integer> seats) {
		this.name = name;
		this.username = username;
		this.date = date;
		this.time = time;
        this.numPeople = numPeople;
		this.seats = seats;
	}

	//getters; returns the respective instance variables based on their respective inputs given
	public synchronized String getName() { return name; }
	public synchronized String getUsername() { return username; }
	public synchronized String getDate() { return date; }
	public synchronized String getTime() { return time; }
	public synchronized int getNumPeople() {
		return numPeople;
	}
	public synchronized ArrayList<Integer> getSeats() {
		return seats;
	}

	//setters; sets the respective instance variables based on their respective inputs given
	public synchronized void setName(String name) { this.name = name; }
	public synchronized void setUsername(String username) { this.username = username; }
	public synchronized void setDate(String date) {
		this.date = date;
	}
	public synchronized void setTime(String time) {
		this.time = time;
	}
	public synchronized void setNumPeople(int numPeople) {
		this.numPeople = numPeople;
	}
	public synchronized void setSeats(ArrayList<Integer> seats) { this.seats = seats; }

	@Override
	public synchronized boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		Reservation reservation = (Reservation) obj;
		String tempName = reservation.getName();
		String tempUsername = reservation.getUsername();
		String tempDate = reservation.getDate();
		String tempTime = reservation.getTime();
		int tempNumPeople = reservation.getNumPeople();
		ArrayList<Integer> tempSeats = reservation.getSeats();

		if (name.equals(tempName) && username.equals(tempUsername) && date.equals(tempDate) &&
				time.equals(tempTime) && numPeople == tempNumPeople && seats.equals(tempSeats)) {
			return true;
		}
		return false;
	}

	public String toString() {
		return String.format("Reservation for %s @ %s %s for %d", name, date, time, numPeople);
	}
}
