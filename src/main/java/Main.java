import backend.services.AuthService;
import backend.services.TutorialService;
import backend.models.User;
import backend.models.TutorialSection;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.util.List;

public class Main {
    private static final AuthService authService = new AuthService();
    private static final TutorialService tutorialService = new TutorialService();

    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/api/register", new RegisterHandler());
        server.createContext("/api/login", new LoginHandler());
        server.createContext("/api/user", new UserHandler());
        server.createContext("/api/tutorials", new TutorialsHandler());
        server.createContext("/api/tutorial", new TutorialHandler());
        server.createContext("/api/health", new HealthHandler());

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
                String username = extractValue(requestBody, "username");
                String password = extractValue(requestBody, "password");
                String email = extractValue(requestBody, "email");

                if (username == null || password == null || email == null) {
                    sendJsonResponse(exchange, 400, "{\"success\":false,\"message\":\"Missing required fields\"}");
                    return;
                }

                User newUser = authService.registerUser(username, password, email);
                if (newUser != null) {
                    String userJson = String.format("{\"username\":\"%s\",\"email\":\"%s\",\"balance\":%.2f}",
                            newUser.getUsername(), newUser.getEmail(), newUser.getBalance());
                    sendJsonResponse(exchange, 200, "{\"success\":true,\"message\":\"Registration successful\",\"data\":" + userJson + "}");
                } else {
                    sendJsonResponse(exchange, 400, "{\"success\":false,\"message\":\"Registration failed\"}");
                }
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"success\":false,\"message\":\"Internal server error\"}");
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
                    String userJson = String.format("{\"username\":\"%s\",\"email\":\"%s\",\"balance\":%.2f}",
                            user.getUsername(), user.getEmail(), user.getBalance());
                    sendJsonResponse(exchange, 200, "{\"success\":true,\"message\":\"Login successful\",\"data\":" + userJson + "}");
                } else {
                    sendJsonResponse(exchange, 401, "{\"success\":false,\"message\":\"Invalid credentials\"}");
                }
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"success\":false,\"message\":\"Internal server error\"}");
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
                    String userJson = String.format("{\"username\":\"%s\",\"email\":\"%s\",\"balance\":%.2f}",
                            user.getUsername(), user.getEmail(), user.getBalance());
                    sendJsonResponse(exchange, 200, "{\"success\":true,\"message\":\"User found\",\"data\":" + userJson + "}");
                } else {
                    sendJsonResponse(exchange, 404, "{\"success\":false,\"message\":\"User not found\"}");
                }
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"success\":false,\"message\":\"Internal server error\"}");
            }
        }
    }

    static class TutorialsHandler implements HttpHandler {
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
                List<TutorialSection> tutorials = tutorialService.getAllTutorials();
                String tutorialsJson = convertTutorialsToJson(tutorials);
                sendJsonResponse(exchange, 200, tutorialsJson);
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"error\":\"Failed to fetch tutorials\"}");
            }
        }
    }

    static class TutorialHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }

            String path = exchange.getRequestURI().getPath();
            try {
                if (path.endsWith("/validate")) {
                    String tutorialId = extractTutorialIdFromPath(path, "/validate");
                    handleValidateExercise(exchange, tutorialId);
                } else {
                    String tutorialId = extractTutorialIdFromPath(path, "");
                    handleGetTutorial(exchange, tutorialId);
                }
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"error\":\"Internal server error\"}");
            }
        }

        private String extractTutorialIdFromPath(String path, String suffix) {
            String basePath = "/api/tutorial/";
            if (path.startsWith(basePath)) {
                String remaining = path.substring(basePath.length());
                if (suffix.isEmpty()) {
                    return remaining;
                } else if (remaining.endsWith(suffix)) {
                    return remaining.substring(0, remaining.length() - suffix.length());
                }
            }
            return null;
        }

        private void handleGetTutorial(HttpExchange exchange, String tutorialId) throws IOException {
            if (!"GET".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }
            if (tutorialId == null || tutorialId.isEmpty()) {
                sendJsonResponse(exchange, 400, "{\"error\":\"Tutorial ID is required\"}");
                return;
            }
            TutorialSection tutorial = tutorialService.getTutorial(tutorialId);
            if (tutorial != null) {
                String tutorialJson = convertTutorialToJson(tutorial);
                sendJsonResponse(exchange, 200, tutorialJson);
            } else {
                sendJsonResponse(exchange, 404, "{\"error\":\"Tutorial not found\"}");
            }
        }

        private void handleValidateExercise(HttpExchange exchange, String tutorialId) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }
            if (tutorialId == null || tutorialId.isEmpty()) {
                sendJsonResponse(exchange, 400, "{\"error\":\"Tutorial ID is required\"}");
                return;
            }
            try {
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                String userAnswer = extractValue(requestBody, "answer");
                if (userAnswer == null || userAnswer.trim().isEmpty()) {
                    sendJsonResponse(exchange, 400, "{\"error\":\"Answer is required\"}");
                    return;
                }
                boolean isCorrect = tutorialService.validateExercise(tutorialId, userAnswer);
                String response = String.format("{\"correct\":%b}", isCorrect);
                sendJsonResponse(exchange, 200, response);
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"error\":\"Failed to validate exercise\"}");
            }
        }
    }

    static class HealthHandler implements HttpHandler {
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
            sendJsonResponse(exchange, 200, "{\"status\":\"OK\",\"service\":\"Tutorial API\"}");
        }
    }

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

    private static String convertTutorialsToJson(List<TutorialSection> tutorials) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < tutorials.size(); i++) {
            TutorialSection tutorial = tutorials.get(i);
            json.append(convertTutorialToJson(tutorial));
            if (i < tutorials.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }

    private static String convertTutorialToJson(TutorialSection tutorial) {
        return String.format(
                "{\"id\":\"%s\",\"title\":\"%s\",\"description\":\"%s\",\"content\":\"%s\",\"exerciseQuestion\":\"%s\",\"exerciseAnswer\":\"%s\",\"hint\":\"%s\"}",
                escapeJson(tutorial.getId()),
                escapeJson(tutorial.getTitle()),
                escapeJson(tutorial.getDescription()),
                escapeJson(tutorial.getContent()),
                escapeJson(tutorial.getExerciseQuestion()),
                escapeJson(tutorial.getExerciseAnswer()),
                escapeJson(tutorial.getHint())
        );
    }

    private static String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}