import java.util.ArrayList;

/**
 * A class that creates accounts for users who want to create reservations at a restaurant.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author jastip, chan531, lab sec L23
 * @version November 8, 2025 (input validation added by zhu1220)
 */

public class UserAccount implements UserAccountInterface {

	// Simple email regex: some non-space/@ chars, "@", some non-space/@ chars, ".", some non-space/@ chars
	// Example: "alice@example.com". This is not perfect but is good enough for this project.
	private static final String EMAIL_REGEX = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";

	//instance variables (usual attributes of an account)
	private String username;
	private String fullName;
	private String email;
	private String password;
	private final ArrayList<Reservation> reservations; //tracks all the user's reservations

	//constructor; sets all the details of the account like username and password to the given inputs in the parameters
	public UserAccount(String username, String password, String fullName, String email) {
		validateUsername(username);
		validatePassword(password);
		validateFullName(fullName);
		validateEmail(email);

		this.username = username;
		this.password = password;
		this.fullName = fullName;
		this.email = email;
		reservations = new ArrayList<>();
	}

	// ========== VALIDATION HELPERS ==========

	private static void validateUsername(String username) {
		if (username == null || username.trim().isEmpty()) {
			throw new IllegalArgumentException("Username cannot be blank");
		}
	}

	private static void validatePassword(String password) {
		if (password == null || password.trim().isEmpty()) {
			throw new IllegalArgumentException("Password cannot be blank");
		}
	}

	private static void validateFullName(String fullName) {
		if (fullName == null || fullName.trim().isEmpty()) {
			throw new IllegalArgumentException("Full name cannot be blank");
		}
	}

	private static void validateEmail(String email) {
		if (email == null || email.trim().isEmpty()) {
			throw new IllegalArgumentException("Email cannot be blank");
		}
		if (!email.matches(EMAIL_REGEX)) {
			throw new IllegalArgumentException("Email is not in a valid format");
		}
	}

	//getter methods; gives the respective instance variable to the respective inputs given
	public synchronized String getUsername() {
		return username;
	}
	public synchronized String getFullName() {
		return fullName;
	}
	public synchronized String getEmail() {
		return email;
	}
	public synchronized String getPassword() {
		return password;
	}

	public synchronized ArrayList<Reservation> getReservations() {
		return new ArrayList<>(reservations); // defensive copy
	}

	//setter methods; sets the respective instance variable to the respective inputs given
	public synchronized void setUsername(String username) {
		validateUsername(username);
		this.username = username;
	}
	public synchronized void setFullName(String fullName) {
		validateFullName(fullName);
		this.fullName = fullName;
	}
	public synchronized void setEmail(String email) {
		validateEmail(email);
		this.email = email;
	}
	public synchronized void setPassword(String password) {
		validatePassword(password);
		this.password = password;
	}

	//books a reservation
	public synchronized boolean addReservation(Reservation res) {
		return reservations.add(res);
	}

	//cancels a reservation
	public synchronized boolean removeReservation(Reservation res) {
		return reservations.remove(res);
	}

	@Override
	public synchronized boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		UserAccount acct = (UserAccount) obj;
		String enteredUserName = acct.getUsername();
		String enteredPassword = acct.getPassword();
		return username.equals(enteredUserName) && password.equals(enteredPassword);
	}

	//prints the account details in case necessary
	public synchronized String toString() {
		return String.format("UserAccount for %s, %s - %s", fullName, username, email);
	}
}
