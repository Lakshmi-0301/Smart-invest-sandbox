package auth;

public class User {
    private String username;
    private String passwordHash;
    private double balance;

    private static final double DEFAULT_BALANCE = 100000.0;

    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.balance = DEFAULT_BALANCE;
    }

    public User(String username, String passwordHash, double balance) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", balance=" + balance +
                '}';
    }
}
