public class UserAccount implements UserAccInterface {
	private String username;
	private String fullName;
	private String email;
	private String password;
	
	
	public UserAccount(String username, String password, String fullName, String email) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
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
    
	public boolean checkPassword(String password) {
		if (password.equals(this.password)) {
			return true;
		}
		return false;
	}
    public void deleteAccount() {
		username = null;
		password = null;
		fullName = null;
		email = null;
	}
	public boolean login(String enteredUserName, String enteredPassword) {
		if (enteredUserName == username && enteredPassword == password) {
			return true;
		}
		return false;
	}
    public void reserve(String date, String time, int amtOfPeople, int tableNum) {
        Reservation res = new Reservation(date, time, amtOfPeople);
        res.assignTable();
    }
	public String toString() {
        return "UserAccount:\n" +
                "username = " + username + "\n" +
                "fullName = " + fullName + "\n" +
                "email = " + email;
    }
}
