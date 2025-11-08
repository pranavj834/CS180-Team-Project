import java.util.ArrayList;

public class UserAccount implements UserAccInterface {
	
	//instance variables (usual attributes of an account)
	private String username;
	private String fullName;
	private String email;
	private String password;
	private ArrayList<Reservation> reservations; //tracks all the user's reservations

	//constructor; sets all the details of the account like username and password to the given inputs in the parameters
	public UserAccount(String username, String password, String fullName, String email) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
		reservations = new ArrayList<>();
    }

	//getter methods; gives the respective instance variable to the respective inputs given
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

	//setter methods; sets the respective instance variable to the respective inputs given
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

	//other methods
	//sets all the instance variables to null to delete the account 
	public void deleteAccount() {
		username = null;
		password = null;
		fullName = null;
		email = null;
		reservations = null;
	}
	//checks to see if the entered username and password is correct
	public boolean equals(String enteredUserName, String enteredPassword) {
		if (enteredUserName.equals(username) && enteredPassword.equals(password)) {
			return true;
		}
		return false;
	}
	//books a reservation
	public boolean addReservation(Reservation res) {
		return reservations.add(res);
	}

	//cancels a reservation
	public boolean removeReservation(Reservation res) {
		return reservations.remove(res);
	}
	//prints the account details in case necessary
	public String toString() {
		return "UserAccount:\n" +
				"username = " + username + "\n" +
				"fullName = " + fullName + "\n" +
				"email = " + email;
	}
}
