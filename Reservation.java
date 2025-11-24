import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents a reservation. Validates basic input:
 * - name, username, date, time not blank
 * - partySize > 0
 * - seat numbers > 0 (if present)
 */
public class Reservation implements Serializable {

	private static final long serialVersionUID = 1L;

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

		validateName(name);
		validateUsername(username);
		validateDate(date);
		validateTime(time);
		validatePartySize(partySize);
		validateSeats(seatNumbers);

		this.name = name;
		this.username = username;
		this.date = date;
		this.time = time;
		this.partySize = partySize;
		// defensive copy
		this.seatNumbers = (seatNumbers == null)
				? new ArrayList<>()
				: new ArrayList<>(seatNumbers);
	}

	// ========== VALIDATION HELPERS ==========

	private static void validateName(String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Reservation name cannot be blank");
		}
	}

	private static void validateUsername(String username) {
		if (username == null || username.trim().isEmpty()) {
			throw new IllegalArgumentException("Reservation username cannot be blank");
		}
	}

	private static void validateDate(String date) {
		if (date == null || date.trim().isEmpty()) {
			throw new IllegalArgumentException("Date cannot be blank");
		}
		// You could add a stricter regex here if you want (e.g., YYYY-MM-DD)
	}

	private static void validateTime(String time) {
		if (time == null || time.trim().isEmpty()) {
			throw new IllegalArgumentException("Time cannot be blank");
		}
		// Likewise, could enforce HH:MM with a regex if needed
	}

	private static void validatePartySize(int partySize) {
		if (partySize <= 0) {
			throw new IllegalArgumentException("Party size must be greater than 0");
		}
	}

	private static void validateSeats(ArrayList<Integer> seats) {
		if (seats == null) {
			return; // we allow null / empty → no seats booked yet
		}
		for (Integer s : seats) {
			if (s == null || s <= 0) {
				throw new IllegalArgumentException("Seat numbers must be positive integers");
			}
		}
	}

	// ========== GETTERS / SETTERS ==========

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

	// Needed for your test
	public void setTime(String t) {
		validateTime(t);
		this.time = t;
	}

	public int getPartySize() {
		return partySize;
	}

	// For your unit test
	public int getNumPeople() {
		return partySize;
	}

	public ArrayList<Integer> getTheSeatNumbers() {
		return new ArrayList<>(seatNumbers);
	}

	// NEW: alias method
	public ArrayList<Integer> getSeats() {
		return new ArrayList<>(seatNumbers);
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
