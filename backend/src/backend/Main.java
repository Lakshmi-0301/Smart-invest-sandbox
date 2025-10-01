package backend;

import backend.services.AuthService;
import backend.models.User;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

public class Main {
    private static final AuthService authService = new AuthService();

    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/api/register", new RegisterHandler());
        server.createContext("/api/login", new LoginHandler());
        server.createContext("/api/user", new UserHandler());

        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port " + port);
    }

    static class RegisterHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);

            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }

            if (!"POST".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            try {
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                // Simple JSON parsing without Jackson
                String username = extractValue(requestBody, "username");
                String password = extractValue(requestBody, "password");
                String email = extractValue(requestBody, "email");

                if (username == null || password == null || email == null) {
                    sendJsonResponse(exchange, 400, "{\"success\":false,\"message\":\"Missing required fields\"}");
                    return;
                }

                User newUser = authService.registerUser(username, password, email);

                if (newUser != null) {
                    String userJson = String.format(
                            "{\"username\":\"%s\",\"email\":\"%s\",\"balance\":%.2f}",
                            newUser.getUsername(), newUser.getEmail(), newUser.getBalance()
                    );
                    sendJsonResponse(exchange, 200,
                            "{\"success\":true,\"message\":\"Registration successful\",\"data\":" + userJson + "}");
                } else {
                    sendJsonResponse(exchange, 400,
                            "{\"success\":false,\"message\":\"Registration failed\"}");
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendJsonResponse(exchange, 500,
                        "{\"success\":false,\"message\":\"Internal server error\"}");
            }
        }
    }

    static class LoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);

            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }

            if (!"POST".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            try {
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                String username = extractValue(requestBody, "username");
                String password = extractValue(requestBody, "password");

                if (username == null || password == null) {
                    sendJsonResponse(exchange, 400, "{\"success\":false,\"message\":\"Missing username or password\"}");
                    return;
                }

                User user = authService.loginUser(username, password);

                if (user != null) {
                    String userJson = String.format(
                            "{\"username\":\"%s\",\"email\":\"%s\",\"balance\":%.2f}",
                            user.getUsername(), user.getEmail(), user.getBalance()
                    );
                    sendJsonResponse(exchange, 200,
                            "{\"success\":true,\"message\":\"Login successful\",\"data\":" + userJson + "}");
                } else {
                    sendJsonResponse(exchange, 401,
                            "{\"success\":false,\"message\":\"Invalid credentials\"}");
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendJsonResponse(exchange, 500,
                        "{\"success\":false,\"message\":\"Internal server error\"}");
            }
        }
    }

    static class UserHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);

            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }

            if (!"GET".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            try {
                String query = exchange.getRequestURI().getQuery();
                String username = getParameterFromQuery(query, "username");

                if (username == null) {
                    sendJsonResponse(exchange, 400, "{\"success\":false,\"message\":\"Username parameter required\"}");
                    return;
                }

                User user = authService.getUser(username);
                if (user != null) {
                    String userJson = String.format(
                            "{\"username\":\"%s\",\"email\":\"%s\",\"balance\":%.2f}",
                            user.getUsername(), user.getEmail(), user.getBalance()
                    );
                    sendJsonResponse(exchange, 200,
                            "{\"success\":true,\"message\":\"User found\",\"data\":" + userJson + "}");
                } else {
                    sendJsonResponse(exchange, 404,
                            "{\"success\":false,\"message\":\"User not found\"}");
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendJsonResponse(exchange, 500,
                        "{\"success\":false,\"message\":\"Internal server error\"}");
            }
        }
    }

    // Helper methods
    private static void setCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
    }

    private static void sendJsonResponse(HttpExchange exchange, int statusCode, String jsonResponse) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, jsonResponse.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(jsonResponse.getBytes());
        }
    }

    private static String getParameterFromQuery(String query, String paramName) {
        if (query == null) return null;
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2 && keyValue[0].equals(paramName)) {
                return keyValue[1];
            }
        }
        return null;
    }

    // Simple JSON value extractor without Jackson
    private static String extractValue(String json, String key) {
        try {
            String searchKey = "\"" + key + "\":\"";
            int startIndex = json.indexOf(searchKey);
            if (startIndex == -1) return null;

            startIndex += searchKey.length();
            int endIndex = json.indexOf("\"", startIndex);
            if (endIndex == -1) return null;

            return json.substring(startIndex, endIndex);
        } catch (Exception e) {
            return null;
        }
    }
}