import backend.services.AuthService;
import backend.services.TutorialService;
import backend.models.User;
import backend.models.TutorialSection;
import backend.models.Quiz;
import backend.models.Question;
import backend.models.Exercise;
import backend.models.CaseStudy;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.util.List;
import java.util.Map;

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
        server.createContext("/api/tutorials/search", new TutorialSearchHandler());
        server.createContext("/api/tutorials/by-level", new TutorialsByLevelHandler());
        server.createContext("/api/tutorials/by-category", new TutorialsByCategoryHandler());
        server.createContext("/api/tutorials/progress", new TutorialProgressHandler());
        server.createContext("/api/tutorial/quiz", new TutorialQuizHandler());
        server.createContext("/api/tutorial/exercise", new TutorialExerciseHandler());
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
                sendJsonResponse(exchange, 200, "{\"success\":true,\"data\":" + tutorialsJson + "}");
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"success\":false,\"error\":\"Failed to fetch tutorials\"}");
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
                sendJsonResponse(exchange, 500, "{\"success\":false,\"error\":\"Internal server error\"}");
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
                sendJsonResponse(exchange, 400, "{\"success\":false,\"error\":\"Tutorial ID is required\"}");
                return;
            }
            TutorialSection tutorial = tutorialService.getTutorial(tutorialId);
            if (tutorial != null) {
                String tutorialJson = convertTutorialToJson(tutorial);
                sendJsonResponse(exchange, 200, "{\"success\":true,\"data\":" + tutorialJson + "}");
            } else {
                sendJsonResponse(exchange, 404, "{\"success\":false,\"error\":\"Tutorial not found\"}");
            }
        }

        private void handleValidateExercise(HttpExchange exchange, String tutorialId) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }
            if (tutorialId == null || tutorialId.isEmpty()) {
                sendJsonResponse(exchange, 400, "{\"success\":false,\"error\":\"Tutorial ID is required\"}");
                return;
            }
            try {
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                String userAnswer = extractValue(requestBody, "answer");
                if (userAnswer == null || userAnswer.trim().isEmpty()) {
                    sendJsonResponse(exchange, 400, "{\"success\":false,\"error\":\"Answer is required\"}");
                    return;
                }
                boolean isCorrect = tutorialService.validateExercise(tutorialId, userAnswer);
                String response = String.format("{\"success\":true,\"correct\":%b}", isCorrect);
                sendJsonResponse(exchange, 200, response);
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"success\":false,\"error\":\"Failed to validate exercise\"}");
            }
        }
    }

    static class TutorialSearchHandler implements HttpHandler {
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
                String searchTerm = getParameterFromQuery(query, "q");
                String category = getParameterFromQuery(query, "category");

                List<TutorialSection> tutorials;
                if (category != null) {
                    tutorials = tutorialService.getTutorialsByCategory(category);
                } else if (searchTerm != null) {
                    tutorials = tutorialService.searchTutorials(searchTerm);
                } else {
                    sendJsonResponse(exchange, 400, "{\"success\":false,\"error\":\"Search term or category parameter required\"}");
                    return;
                }

                String tutorialsJson = convertTutorialsToJson(tutorials);
                sendJsonResponse(exchange, 200, "{\"success\":true,\"data\":" + tutorialsJson + "}");
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"success\":false,\"error\":\"Failed to search tutorials\"}");
            }
        }
    }

    static class TutorialsByLevelHandler implements HttpHandler {
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
                String level = getParameterFromQuery(query, "level");

                if (level == null) {
                    sendJsonResponse(exchange, 400, "{\"success\":false,\"error\":\"Level parameter required\"}");
                    return;
                }

                List<TutorialSection> tutorials = tutorialService.getTutorialsByLevel(level);
                String tutorialsJson = convertTutorialsToJson(tutorials);
                sendJsonResponse(exchange, 200, "{\"success\":true,\"data\":" + tutorialsJson + "}");
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"success\":false,\"error\":\"Failed to fetch tutorials by level\"}");
            }
        }
    }

    static class TutorialsByCategoryHandler implements HttpHandler {
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
                String category = getParameterFromQuery(query, "category");

                if (category == null) {
                    sendJsonResponse(exchange, 400, "{\"success\":false,\"error\":\"Category parameter required\"}");
                    return;
                }

                List<TutorialSection> tutorials = tutorialService.getTutorialsByCategory(category);
                String tutorialsJson = convertTutorialsToJson(tutorials);
                sendJsonResponse(exchange, 200, "{\"success\":true,\"data\":" + tutorialsJson + "}");
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"success\":false,\"error\":\"Failed to fetch tutorials by category\"}");
            }
        }
    }

    static class TutorialProgressHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }

            try {
                if ("POST".equals(exchange.getRequestMethod())) {
                    handleUpdateProgress(exchange);
                } else if ("GET".equals(exchange.getRequestMethod())) {
                    handleGetProgress(exchange);
                } else {
                    exchange.sendResponseHeaders(405, -1);
                }
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"success\":false,\"error\":\"Failed to process progress request\"}");
            }
        }

        private void handleUpdateProgress(HttpExchange exchange) throws IOException {
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            String username = extractValue(requestBody, "username");
            String tutorialId = extractValue(requestBody, "tutorialId");
            String completedStr = extractValue(requestBody, "completed");

            if (username == null || tutorialId == null || completedStr == null) {
                sendJsonResponse(exchange, 400, "{\"success\":false,\"error\":\"Missing required fields\"}");
                return;
            }

            boolean completed = Boolean.parseBoolean(completedStr);
            boolean success = tutorialService.updateUserProgress(username, tutorialId, completed);

            if (success) {
                sendJsonResponse(exchange, 200, "{\"success\":true,\"message\":\"Progress updated\"}");
            } else {
                sendJsonResponse(exchange, 400, "{\"success\":false,\"message\":\"Failed to update progress\"}");
            }
        }

        private void handleGetProgress(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            String username = getParameterFromQuery(query, "username");

            if (username == null) {
                sendJsonResponse(exchange, 400, "{\"success\":false,\"error\":\"Username parameter required\"}");
                return;
            }

            Map<String, Boolean> progress = tutorialService.getUserProgress(username);
            String progressJson = convertProgressToJson(progress);
            sendJsonResponse(exchange, 200, "{\"success\":true,\"data\":" + progressJson + "}");
        }

        private String convertProgressToJson(Map<String, Boolean> progress) {
            if (progress == null || progress.isEmpty()) {
                return "{}";
            }

            StringBuilder json = new StringBuilder("{");
            int i = 0;
            for (Map.Entry<String, Boolean> entry : progress.entrySet()) {
                json.append("\"").append(escapeJson(entry.getKey())).append("\":").append(entry.getValue());
                if (i < progress.size() - 1) {
                    json.append(",");
                }
                i++;
            }
            json.append("}");
            return json.toString();
        }
    }

    static class TutorialQuizHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }

            try {
                if ("GET".equals(exchange.getRequestMethod())) {
                    handleGetQuiz(exchange);
                } else if ("POST".equals(exchange.getRequestMethod())) {
                    handleSubmitQuiz(exchange);
                } else {
                    exchange.sendResponseHeaders(405, -1);
                }
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"success\":false,\"error\":\"Failed to process quiz request\"}");
            }
        }

        private void handleGetQuiz(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            String tutorialId = getParameterFromQuery(query, "tutorialId");

            if (tutorialId == null) {
                sendJsonResponse(exchange, 400, "{\"success\":false,\"error\":\"Tutorial ID parameter required\"}");
                return;
            }

            Quiz quiz = tutorialService.getQuiz(tutorialId);
            if (quiz != null) {
                String quizJson = convertQuizToJson(quiz);
                sendJsonResponse(exchange, 200, "{\"success\":true,\"data\":" + quizJson + "}");
            } else {
                sendJsonResponse(exchange, 404, "{\"success\":false,\"error\":\"Quiz not found for this tutorial\"}");
            }
        }


        private void handleSubmitQuiz(HttpExchange exchange) throws IOException {
            try {
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                System.out.println("=== QUIZ SUBMISSION DEBUG ===");
                System.out.println("Raw request body: " + requestBody);

                // Parse JSON properly
                String tutorialId = extractJsonValue(requestBody, "tutorialId");
                String username = extractJsonValue(requestBody, "username");
                String answersJson = extractJsonValue(requestBody, "answers");

                System.out.println("Parsed - Tutorial ID: " + tutorialId);
                System.out.println("Parsed - Username: " + username);
                System.out.println("Parsed - Answers: " + answersJson);

                if (tutorialId == null || username == null || answersJson == null) {
                    System.out.println("Missing required fields");
                    sendJsonResponse(exchange, 400, "{\"success\":false,\"error\":\"Missing required fields\"}");
                    return;
                }

                Map<String, Object> result = tutorialService.submitQuiz(tutorialId, username, answersJson);
                if (result != null) {
                    // Use the existing convertObjectToJson method
                    String resultJson = convertObjectToJson(result);
                    System.out.println("Quiz submission successful, sending response: " + resultJson);
                    sendJsonResponse(exchange, 200, "{\"success\":true,\"data\":" + resultJson + "}");
                } else {
                    sendJsonResponse(exchange, 400, "{\"success\":false,\"error\":\"Failed to submit quiz\"}");
                }
            } catch (Exception e) {
                System.err.println("Error in handleSubmitQuiz: " + e.getMessage());
                e.printStackTrace();
                sendJsonResponse(exchange, 500, "{\"success\":false,\"error\":\"Failed to submit quiz: " + e.getMessage() + "\"}");
            }
        }
        private static String extractJsonValue(String json, String key) {
            try {
                System.out.println("=== EXTRACTING JSON VALUE ===");
                System.out.println("Looking for key: " + key);

                String searchKey = "\"" + key + "\":";
                int keyIndex = json.indexOf(searchKey);
                if (keyIndex == -1) {
                    System.out.println("Key not found: " + key);
                    return null;
                }

                int valueStart = keyIndex + searchKey.length();

                // Skip whitespace
                while (valueStart < json.length() && Character.isWhitespace(json.charAt(valueStart))) {
                    valueStart++;
                }

                if (valueStart >= json.length()) {
                    System.out.println("No value after key");
                    return null;
                }

                char firstChar = json.charAt(valueStart);
                System.out.println("First character of value: '" + firstChar + "'");

                if (firstChar == '"') {
                    valueStart++; // Skip opening quote
                    StringBuilder valueBuilder = new StringBuilder();
                    int currentPos = valueStart;
                    boolean escaped = false;

                    while (currentPos < json.length()) {
                        char c = json.charAt(currentPos);

                        if (escaped) {
                            valueBuilder.append(c);
                            escaped = false;
                        } else if (c == '\\') {
                            escaped = true;
                        } else if (c == '"') {
                            // Found closing quote
                            String value = valueBuilder.toString();
                            System.out.println("Extracted string value: " + value);
                            return value;
                        } else {
                            valueBuilder.append(c);
                        }
                        currentPos++;
                    }
                    System.out.println("No closing quote found");
                    return null;
                } else {
                    // For other types, use simple extraction
                    int valueEnd = valueStart;
                    while (valueEnd < json.length()) {
                        char c = json.charAt(valueEnd);
                        if (c == ',' || c == '}' || Character.isWhitespace(c)) {
                            break;
                        }
                        valueEnd++;
                    }
                    String value = json.substring(valueStart, valueEnd);
                    System.out.println("Extracted value: " + value);
                    return value;
                }
            } catch (Exception e) {
                System.err.println("Error extracting JSON value for '" + key + "': " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
    }

    static class TutorialExerciseHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }

            try {
                if ("GET".equals(exchange.getRequestMethod())) {
                    handleGetExercise(exchange);
                } else if ("POST".equals(exchange.getRequestMethod())) {
                    handleSubmitExercise(exchange);
                } else {
                    exchange.sendResponseHeaders(405, -1);
                }
            } catch (Exception e) {
                sendJsonResponse(exchange, 500, "{\"success\":false,\"error\":\"Failed to process exercise request\"}");
            }
        }

        private void handleGetExercise(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            String tutorialId = getParameterFromQuery(query, "tutorialId");

            if (tutorialId == null) {
                sendJsonResponse(exchange, 400, "{\"success\":false,\"error\":\"Tutorial ID parameter required\"}");
                return;
            }

            Exercise exercise = tutorialService.getExercise(tutorialId);
            if (exercise != null) {
                String exerciseJson = convertExerciseToJson(exercise);
                sendJsonResponse(exchange, 200, "{\"success\":true,\"data\":" + exerciseJson + "}");
            } else {
                sendJsonResponse(exchange, 404, "{\"success\":false,\"error\":\"Exercise not found for this tutorial\"}");
            }
        }

        private void handleSubmitExercise(HttpExchange exchange) throws IOException {
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            String tutorialId = extractValue(requestBody, "tutorialId");
            String userAnswer = extractValue(requestBody, "answer");

            if (tutorialId == null || userAnswer == null) {
                sendJsonResponse(exchange, 400, "{\"success\":false,\"error\":\"Missing required fields\"}");
                return;
            }

            boolean isCorrect = tutorialService.validateExercise(tutorialId, userAnswer);
            sendJsonResponse(exchange, 200, String.format("{\"success\":true,\"correct\":%b}", isCorrect));
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
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
        exchange.getResponseHeaders().set("Access-Control-Max-Age", "3600");
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
                "{\"id\":\"%s\",\"title\":\"%s\",\"description\":\"%s\",\"level\":\"%s\",\"category\":\"%s\",\"estimatedMinutes\":%d,\"completionRate\":%.2f,\"videoUrl\":\"%s\",\"content\":\"%s\",\"infographics\":%s,\"keyPoints\":%s,\"glossary\":%s,\"quiz\":%s,\"exercise\":%s,\"hasVideo\":%b,\"hasSimulator\":%b,\"hasQuiz\":%b,\"prerequisites\":%s,\"nextTutorials\":%s,\"certificationId\":\"%s\"}",
                escapeJson(tutorial.getId()),
                escapeJson(tutorial.getTitle()),
                escapeJson(tutorial.getDescription()),
                escapeJson(tutorial.getLevel()),
                escapeJson(tutorial.getCategory()),
                tutorial.getEstimatedMinutes(),
                tutorial.getCompletionRate(),
                escapeJson(tutorial.getVideoUrl()),
                escapeJson(tutorial.getContent()),
                convertStringListToJson(tutorial.getInfographics()),
                convertStringListToJson(tutorial.getKeyPoints()),

                convertMapToJson(tutorial.getGlossary()),
                convertQuizToJson(tutorial.getQuiz()),
                convertExerciseToJson(tutorial.getExercise()),
                tutorial.isHasVideo(),
                tutorial.isHasSimulator(),
                tutorial.isHasQuiz(),
                convertStringListToJson(tutorial.getPrerequisites()),
                convertStringListToJson(tutorial.getNextTutorials()),
                escapeJson(tutorial.getCertificationId())
        );
    }

    private static String convertQuizToJson(Quiz quiz) {
        if (quiz == null) return "null";
        return String.format(
                "{\"id\":\"%s\",\"passingScore\":%d,\"timeLimit\":%d,\"allowRetakes\":%b,\"questions\":%s}",
                escapeJson(quiz.getId()),
                quiz.getPassingScore(),
                quiz.getTimeLimit(),
                quiz.isAllowRetakes(),
                convertQuestionsToJson(quiz.getQuestions())
        );
    }

    private static String convertQuestionsToJson(List<Question> questions) {
        if (questions == null || questions.isEmpty()) {
            return "[]";
        }
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            json.append(convertQuestionToJson(question));
            if (i < questions.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }

    private static String convertQuestionToJson(Question question) {
        if (question == null) return "null";
        return String.format(
                "{\"questionText\":\"%s\",\"options\":%s,\"correctAnswerIndex\":%d,\"explanation\":\"%s\",\"questionType\":\"%s\"}",
                escapeJson(question.getQuestionText()),
                convertStringListToJson(question.getOptions()),
                question.getCorrectAnswerIndex(),
                escapeJson(question.getExplanation()),
                escapeJson(question.getQuestionType())
        );
    }

    private static String convertExerciseToJson(Exercise exercise) {
        if (exercise == null) return "null";
        return String.format(
                "{\"question\":\"%s\",\"hint\":\"%s\",\"type\":\"%s\"}",
                escapeJson(exercise.getQuestion()),
                escapeJson(exercise.getHint()),
                escapeJson(exercise.getType())
        );
    }

    private static String convertStringListToJson(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            json.append("\"").append(escapeJson(list.get(i))).append("\"");
            if (i < list.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }

    private static String convertMapToJson(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return "{}";
        }
        StringBuilder json = new StringBuilder("{");
        int i = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            json.append("\"").append(escapeJson(entry.getKey())).append("\":\"")
                    .append(escapeJson(entry.getValue())).append("\"");
            if (i < map.size() - 1) {
                json.append(",");
            }
            i++;
        }
        json.append("}");
        return json.toString();
    }

private static String convertObjectMapToJson(Map<String, Object> map) {
    if (map == null || map.isEmpty()) {
        return "{}";
    }

    StringBuilder json = new StringBuilder("{");
    int i = 0;
    for (Map.Entry<String, Object> entry : map.entrySet()) {
        String key = entry.getKey();
        Object value = entry.getValue();

        json.append("\"").append(escapeJson(key)).append("\":");

        if (value instanceof String) {
            json.append("\"").append(escapeJson((String) value)).append("\"");
        } else if (value instanceof Number) {
            json.append(value);
        } else if (value instanceof Boolean) {
            json.append(value);
        } else if (value instanceof List) {
            json.append(convertListToJson((List<?>) value));
        } else if (value instanceof Map) {
            json.append(convertObjectMapToJson((Map<String, Object>) value));
        } else {
            json.append("\"").append(escapeJson(value != null ? value.toString() : "")).append("\"");
        }

        if (i < map.size() - 1) {
            json.append(",");
        }
        i++;
    }
    json.append("}");
    return json.toString();
}
private static String convertObjectToJson(Object obj) {
    if (obj == null) return "null";

    try {
        if (obj instanceof Map) {
            return convertObjectMapToJson((Map<String, Object>) obj);
        } else if (obj instanceof List) {
            return convertListToJson((List<?>) obj);
        } else if (obj instanceof String) {
            return "\"" + escapeJson(obj.toString()) + "\"";
        } else if (obj instanceof Number || obj instanceof Boolean) {
            return obj.toString();
        } else {
            // For other objects, use simple toString() and escape
            return "\"" + escapeJson(obj.toString()) + "\"";
        }
    } catch (Exception e) {
        System.err.println("Error converting object to JSON: " + e.getMessage());
        e.printStackTrace();
        return "null";
    }
}

    // Add this helper method for List serialization
    private static String convertListToJson(List<?> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            Object item = list.get(i);
            json.append(convertObjectToJson(item));
            if (i < list.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
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