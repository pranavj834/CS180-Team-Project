import java.util.ArrayList;

/**
 * A class that creates accounts for users who want to create reservations at a restaurant.
 *
 * <p>Purdue University -- CS18000 -- Fall 2025</p>
 *
 * @author jastip, chan531, lab sec L23
 * @version November 8, 2025
 */

public class UserAccount implements UserAccountInterface {
	
	//instance variables (usual attributes of an account)
	private String username;
	private String fullName;
	private String email;
	private String password;
	private ArrayList<Reservation> reservations; //tracks all the user's reservations

	//constructor; sets all the details of the account like username and password to the given inputs in the parameters
	public UserAccount(String username, String password, String fullName, String email) {
        if (!username.equals("") && !username.equals("null")) {
            this.username = username;
        }
        if (!password.equals("") && !password.equals("null")) {
            this.password = password;
        }
        if (!fullName.equals("") && !fullName.equals("null")) {
            this.fullName = fullName;
        }
        if (!email.equals("") && !email.equals("null")) {
            this.email = email;
        }
        this.reservations = new ArrayList<>();
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
		return reservations;
	}

	//setter methods; sets the respective instance variable to the respective inputs given
	public synchronized void setUsername(String username) {
    	this.username = username;
    }
    public synchronized void setFullName(String fullName) {
    	this.fullName = fullName;
    }
    public synchronized void setEmail(String email) {
    	this.email = email;
    }
    public synchronized void setPassword(String password) {
    	this.password = password;
    }

	//other methods
	//checks to see if the entered username and password is correct


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
	public String toString() {
		return String.format("UserAccount for %s, %s - %s", fullName, username, email);
	}
}
