public interface UserAccInterface {
    String getUsername();
    String getFullName();
    String getEmail();

    void setUsername(String username);
    void setFullName(String fullName);
    void setEmail(String email);
    void setPassword(String password);

    boolean checkPassword(String password);
    void deleteAccount();
    boolean login(String enteredUserName, String enteredPassword);
}
