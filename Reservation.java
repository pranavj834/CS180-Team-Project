import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Reservation implements Serializable {

	private String name;
	private String username;
	private String date;   // "YYYY-MM-DD"
	private String time;   // "HH:MM"
	private int partySize;
	private ArrayList<Integer> seatNumbers;

	public Reservation(String name,
					   String username,
					   String date,
					   String time,
					   int partySize,
					   ArrayList<Integer> seatNumbers) {

		this.name = name;
		this.username = username;
		this.date = date;
		this.time = time;
		this.partySize = partySize;
		this.seatNumbers = seatNumbers;
	}

	public String getName() {
		return name;
	}

	public String getUsername() {
		return username;
	}

	public String getDate() {
		return date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String t) { this.time = t; }   // Needed for your test

	public int getPartySize() {
		return partySize;
	}

	public int getNumPeople() { return partySize; }    // For your unit test

	public ArrayList<Integer> getTheSeatNumbers() {
		return seatNumbers;
	}

	public ArrayList<Integer> getSeats() {             // NEW: alias method
		return seatNumbers;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Reservation)) return false;
		Reservation r = (Reservation) o;
		return Objects.equals(name, r.name)
				&& Objects.equals(username, r.username)
				&& Objects.equals(date, r.date)
				&& Objects.equals(time, r.time)
				&& partySize == r.partySize
				&& Objects.equals(seatNumbers, r.seatNumbers);
	}

	@Override
	public String toString() {
		return "Reservation for " + name + " @ " + date + " " + time + " for " + partySize;
	}
}
