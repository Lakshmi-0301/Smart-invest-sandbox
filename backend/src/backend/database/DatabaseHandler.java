package backend.database;

import java.sql.*;

import backend.models.User;

public class DatabaseHandler {
    private static final String DB_URL = "jdbc:sqlite:smartstock.db";
    private Connection connection;

    public DatabaseHandler() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            initializeDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ensures the users table exists
    public void initializeDatabase() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT UNIQUE NOT NULL, "
                + "passwordHash TEXT NOT NULL, "
                + "email TEXT UNIQUE NOT NULL, "
                + "balance REAL DEFAULT 100000.0"
                + ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createUsersTable);
            System.out.println("Users table ready.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Close the connection
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    // Add a user
    public boolean addUser(User user) {
        String sql = "INSERT INTO users(username, passwordHash, email, balance) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPasswordHash());
            pstmt.setString(3, user.getEmail());
            pstmt.setDouble(4, user.getBalance());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
            return false;
        }
    }

    // Fetch a user by username
    public User getUserByUsername(String username) {
        String sql = "SELECT username, passwordHash, email, balance FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String uname = rs.getString("username");
                String passHash = rs.getString("passwordHash");
                String mail = rs.getString("email");
                double balance = rs.getDouble("balance");

                return new User(uname, passHash, mail, balance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update balance
    public boolean updateUserBalance(String username, double newBalance) {
        String sql = "UPDATE users SET balance = ? WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, newBalance);
            pstmt.setString(2, username);
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Validate login
    public boolean validateUser(String username, String passwordHash) {
        String sql = "SELECT 1 FROM users WHERE username = ? AND passwordHash = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, passwordHash);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
