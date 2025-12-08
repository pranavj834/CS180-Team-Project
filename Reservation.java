import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a reservation with basic input validation:
 * - name, username, date, time cannot be blank
 * - date must be in YYYY-MM-DD format
 * - time must be in HH:MM format
 * - partySize must be > 0
 * - seatNumbers must be non-empty, all > 0, and size must equal partySize
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author zhu1220, chan531
 * @version December 8, 2025
 */

public class Reservation implements Serializable, ReservationInterface {

    //instance variables
	private static final long serialVersionUID = 1L;
	private static final String TIMESTAMP_REGEX = "^\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}$"; //used to check for format

	private String name;
	private String username;
	private String timestamp;   // "YYYY-MM-DD is the correct format"
	private int partySize;
	private ArrayList<Integer> seats;

    //constructor; validates parameters and sets them to their respective variables
	public Reservation(String name, String username, String timestamp,
					   int partySize, ArrayList<Integer> seats) {
		validateName(name);
		validateUsername(username);
		validateTimestamp(timestamp);
		validatePartySize(partySize);
		validateSeats(seats, partySize);

		this.name = name;
		this.username = username;
		this.timestamp = timestamp;
		this.partySize = partySize;
		this.seats = seats;
	}

	// ========== VALIDATION HELPERS ==========

	private void validateName(String n) {
		if (n == null || n.trim().isEmpty()) {
			throw new IllegalArgumentException("Reservation name cannot be blank.");
		}
	}

	private void validateUsername(String un) {
		if (un == null || un.trim().isEmpty()) {
			throw new IllegalArgumentException("Reservation username cannot be blank.");
		}
	}

	private void validateTimestamp(String ts) {
		if (ts == null || ts.trim().isEmpty()) {
			throw new IllegalArgumentException("Timestamp cannot be blank.");
		}

		if (!ts.matches(TIMESTAMP_REGEX)) {
			throw new IllegalArgumentException("Time must be in format YYYY-MM-DD HH:MM.");
		}
	}

	private void validatePartySize(int ps) {
		if (ps <= 0) {
			throw new IllegalArgumentException("Party size must be greater than 0.");
		}
	}

	private void validateSeats(ArrayList<Integer> s, int ps) {
		if (s == null || s.isEmpty()) {
			throw new IllegalArgumentException("Seat list cannot be empty.");
		}
		if (s.size() != ps) {
			throw new IllegalArgumentException("Number of seats must equal party size.");
		}
		for (Integer seat : s) {
			if (seat < 0 || seat > 29) {
				throw new IllegalArgumentException("Seat numbers must be within the range 0-29 inclusive.");
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

    public String getTimestamp() {
        return timestamp;
    }

    public int getPartySize() {
		return partySize;
	}

	public ArrayList<Integer> getSeats() {
		return new ArrayList<>(seats);
	}

	public void setName(String name) {
		validateName(name);
		this.name = name;
	}

	public void setUsername(String username) {
		validateUsername(username);
		this.username = username;
	}

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setPartySize(int partySize) {
		validatePartySize(partySize);
		this.partySize = partySize;
	}

	@Override
	public void setSeats(ArrayList<Integer> seats) {
		validateSeats(seats, partySize);
		this.seats = seats;
	}

	// ========== GENERAL ==========

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Reservation)) return false;
		Reservation r = (Reservation) o;
		return name.equals(r.getName())
				&& username.equals(r.getUsername())
				&& timestamp.equals(r.getTimestamp())
				&& partySize == r.partySize
				&& seats.equals(r.getSeats());
	}

	@Override
	public String toString() {
		return "Reservation for " + name + " @ " + timestamp + " for " + partySize;
	}
}
