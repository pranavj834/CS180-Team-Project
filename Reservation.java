import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents a reservation with basic input validation:
 * - name, username, date, time cannot be blank
 * - date must be in YYYY-MM-DD format
 * - time must be in HH:MM format
 * - partySize must be > 0
 * - seatNumbers must be non-empty, all > 0, and size must equal partySize
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *  * @author zhu1220, jastip
 *  * @version November 20, 2025
 */
public class Reservation implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String DATE_REGEX = "^\\d{4}-\\d{2}-\\d{2}$";
	private static final String TIME_REGEX = "^\\d{2}:\\d{2}$";

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
		validateSeats(seatNumbers, partySize);

		this.name = name;
		this.username = username;
		this.date = date;
		this.time = time;
		this.partySize = partySize;
		// defensive copy
		this.seatNumbers = new ArrayList<>(seatNumbers);
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
		if (!date.matches(DATE_REGEX)) {
			throw new IllegalArgumentException("Date must be in format YYYY-MM-DD");
		}
	}

	private static void validateTime(String time) {
		if (time == null || time.trim().isEmpty()) {
			throw new IllegalArgumentException("Time cannot be blank");
		}
		if (!time.matches(TIME_REGEX)) {
			throw new IllegalArgumentException("Time must be in format HH:MM");
		}
	}

	private static void validatePartySize(int partySize) {
		if (partySize <= 0) {
			throw new IllegalArgumentException("Party size must be greater than 0");
		}
	}

	private static void validateSeats(ArrayList<Integer> seats, int partySize) {
		if (seats == null || seats.isEmpty()) {
			throw new IllegalArgumentException("Seat list cannot be empty");
		}
		if (seats.size() != partySize) {
			throw new IllegalArgumentException("Number of seats must equal party size");
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

	// Needed for tests (and maybe GUI)
	public void setTime(String t) {
		validateTime(t);
		this.time = t;
	}

	public int getPartySize() {
		return partySize;
	}

	// For your existing unit tests
	public int getNumPeople() {
		return partySize;
	}

	public ArrayList<Integer> getTheSeatNumbers() {
		return new ArrayList<>(seatNumbers);
	}

	// Alias method (for new code/tests)
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
