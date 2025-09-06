package test;

import backend.database.DatabaseHandler;

public class TestMain {
    public static void main(String[] args) {
        // Create a DatabaseHandler instance
        DatabaseHandler dbHandler = new DatabaseHandler();

        // Initialize the database
        dbHandler.initializeDatabase();

        // Close the connection
        dbHandler.closeConnection();
    }
}
