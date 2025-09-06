package test;

import backend.services.AuthService;
import backend.models.User;

public class TestAuth {
    public static void main(String[] args) {
        AuthService authService = new AuthService();

        User registered = authService.registerUser("john_doe", "password123","johndoe@abcmail.com");
        System.out.println("User registered: " + registered);

        User user = authService.loginUser("john_doe", "password123");
        if (user != null) {
            System.out.println("Login successful. Welcome, " + user.getUsername());
        } else {
            System.out.println("Login failed.");
        }
    }
}
