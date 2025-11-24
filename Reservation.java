import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Reservation data object. Thread-safe via internal lock and
 * defensive copies of mutable collections.
 */
public class Reservation implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Object lock = new Object();

	private final String name;
	private final String username;
	private String date;   // "YYYY-MM-DD"
	private String time;   // "HH:MM"
	private int partySize;
	private final ArrayList<Integer> seatNumbers;

	public Reservation(String name,
					   String username,
					   String date,
					   String time,
					   int partySize,
					   ArrayList<Integer> seatNumbers) {

		synchronized (lock) {
			this.name = name;
			this.username = username;
			this.date = date;
			this.time = time;
			this.partySize = partySize;
			this.seatNumbers = (seatNumbers == null)
					? new ArrayList<>()
					: new ArrayList<>(seatNumbers);
		}
	}

	public String getName() {
		synchronized (lock) {
			return name;
		}
	}

	public String getUsername() {
		synchronized (lock) {
			return username;
		}
	}

	public String getDate() {
		synchronized (lock) {
			return date;
		}
	}

	public String getTime() {
		synchronized (lock) {
			return time;
		}
	}

	// Needed for your test
	public void setTime(String t) {
		synchronized (lock) {
			this.time = t;
		}
	}

	public int getPartySize() {
		synchronized (lock) {
			return partySize;
		}
	}

	// For your unit test
	public int getNumPeople() {
		synchronized (lock) {
			return partySize;
		}
	}

	/**
	 * Original accessor name kept for backwards compatibility.
	 */
	public ArrayList<Integer> getTheSeatNumbers() {
		synchronized (lock) {
			return new ArrayList<>(seatNumbers);
		}
	}

	/**
	 * Preferred accessor: returns a defensive copy of seat numbers.
	 */
	public ArrayList<Integer> getSeats() {
		synchronized (lock) {
			return new ArrayList<>(seatNumbers);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Reservation)) return false;
		if (this == o) return true;

		Reservation other = (Reservation) o;

		// Take snapshots under each object's lock to avoid deadlock
		String n1, u1, d1, t1;
		int p1;
		List<Integer> s1;

		synchronized (this.lock) {
			n1 = this.name;
			u1 = this.username;
			d1 = this.date;
			t1 = this.time;
			p1 = this.partySize;
			s1 = new ArrayList<>(this.seatNumbers);
		}

		String n2, u2, d2, t2;
		int p2;
		List<Integer> s2;

		synchronized (other.lock) {
			n2 = other.name;
			u2 = other.username;
			d2 = other.date;
			t2 = other.time;
			p2 = other.partySize;
			s2 = new ArrayList<>(other.seatNumbers);
		}

		return Objects.equals(n1, n2)
				&& Objects.equals(u1, u2)
				&& Objects.equals(d1, d2)
				&& Objects.equals(t1, t2)
				&& p1 == p2
				&& Objects.equals(s1, s2);
	}

	@Override
	public int hashCode() {
		synchronized (lock) {
			return Objects.hash(name, username, date, time, partySize, seatNumbers);
		}
	}

	@Override
	public String toString() {
		synchronized (lock) {
			return "Reservation for " + name + " @ " + date + " " + time + " for " + partySize;
		}
	}
}
