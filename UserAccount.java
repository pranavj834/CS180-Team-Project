import java.util.ArrayList;

public class UserAccount implements UserAccInterface {
	private String username;
	private String fullName;
	private String email;
	private String password;
	private ArrayList<Reservation> reservations;

	public UserAccount(String username, String password, String fullName, String email) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
		reservations = new ArrayList<>();
    }

	public void deleteAccount() {
		username = null;
		password = null;
		fullName = null;
		email = null;
		reservations = null;
	}

	public boolean equals(String enteredUserName, String enteredPassword) {
		if (enteredUserName.equals(username) && enteredPassword.equals(password)) {
			return true;
		}
		return false;
	}

	public boolean addReservation(Reservation res) {
		return reservations.add(res);
	}

	public boolean removeReservation(Reservation res) {
		return reservations.remove(res);
	}

	public String toString() {
		return "UserAccount:\n" +
				"username = " + username + "\n" +
				"fullName = " + fullName + "\n" +
				"email = " + email;
	}
	
	public String getUsername() {
		return username; 
	}

    public String getFullName() { 
    	return fullName; 
    }

    public String getEmail() { 
    	return email;
    }

	public ArrayList<Reservation> getReservations() {
		return reservations;
	}

	public void setUsername(String username) {
    	this.username = username;
    }

    public void setFullName(String fullName) { 
    	this.fullName = fullName;
    }

    public void setEmail(String email) { 
    	this.email = email; 
    }

    public void setPassword(String password) {
    	this.password = password;
    }
}
