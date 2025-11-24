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

	// ================== PRICING CONFIG (STATIC) ==================

	/** Base fee for any reservation. */
	private static double basePrice = 0.0;

	/** Extra charge per person. */
	private static double perPersonPrice = 0.0;

	/**
	 * Configures the global pricing rule used for all reservations:
	 *   total = basePrice + perPersonPrice * numPeople
	 */
	public static synchronized void configurePricing(double base, double perPerson) {
		basePrice = base;
		perPersonPrice = perPerson;
	}

	/**
	 * Computes the total price for a party of size n using the
	 * globally configured pricing rule.
	 */
	public static synchronized double computePrice(int numPeople) {
		return basePrice + perPersonPrice * numPeople;
	}

	// ================== INSTANCE FIELDS ==================

	private String name;
	private String username;
	private String date;       // "YYYY-MM-DD", e.g., "2025-01-01"
	private String time;       // "HH:MM" 24h, e.g., "13:30"
	private int numPeople;
	private ArrayList<Integer> seats;
	private double totalPrice; // computed from numPeople

	//constructor; sets the respective instance variables based on their respective inputs given
	public Reservation(String name, String username, String date,
					   String time, int numPeople, ArrayList<Integer> seats) {
		this.name = name;
		this.username = username;
		this.date = date;
		this.time = time;
		this.numPeople = numPeople;
		this.seats = seats;
		this.totalPrice = computePrice(numPeople);
	}

	//getters
	public synchronized String getName() { return name; }
	public synchronized String getUsername() { return username; }
	public synchronized String getDate() { return date; }
	public synchronized String getTime() { return time; }
	public synchronized int getNumPeople() { return numPeople; }
	public synchronized ArrayList<Integer> getSeats() { return seats; }
	public synchronized double getTotalPrice() { return totalPrice; }

	//setters
	public synchronized void setName(String name) { this.name = name; }
	public synchronized void setUsername(String username) { this.username = username; }
	public synchronized void setDate(String date) { this.date = date; }
	public synchronized void setTime(String time) { this.time = time; }
	public synchronized void setNumPeople(int numPeople) {
		this.numPeople = numPeople;
		this.totalPrice = computePrice(numPeople);
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

		return name.equals(tempName)
				&& username.equals(tempUsername)
				&& date.equals(tempDate)
				&& time.equals(tempTime)
				&& numPeople == tempNumPeople
				&& seats.equals(tempSeats);
	}

	public String toString() {
		return String.format("Reservation for %s @ %s %s for %d",
				name, date, time, numPeople);
	}
}
