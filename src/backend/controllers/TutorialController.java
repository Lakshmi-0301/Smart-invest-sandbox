package backend.controllers;

import backend.services.TutorialService;
import backend.models.TutorialSection;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class TutorialController {
    private TutorialService tutorialService;
    private Gson gson;

    public TutorialController() {
        this.tutorialService = new TutorialService();
        this.gson = new Gson();
        setupRoutes();
    }

    private void setupRoutes() {
        // Enable CORS
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "GET,POST,PUT,DELETE,OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
            response.type("application/json");
        });

        // Get all tutorials
        get("/api/tutorials", (req, res) -> {
            try {
                List<TutorialSection> tutorials = tutorialService.getAllTutorials();
                return gson.toJson(tutorials);
            } catch (Exception e) {
                res.status(500);
                Map<String, String> error = new HashMap<>();
                error.put("error", "Failed to fetch tutorials: " + e.getMessage());
                return gson.toJson(error);
            }
        });

        // Get specific tutorial
        get("/api/tutorials/:id", (req, res) -> {
            try {
                String tutorialId = req.params(":id");
                TutorialSection tutorial = tutorialService.getTutorial(tutorialId);

                if (tutorial != null) {
                    return gson.toJson(tutorial);
                } else {
                    res.status(404);
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "Tutorial not found");
                    return gson.toJson(error);
                }
            } catch (Exception e) {
                res.status(500);
                Map<String, String> error = new HashMap<>();
                error.put("error", "Failed to fetch tutorial: " + e.getMessage());
                return gson.toJson(error);
            }
        });

        // Validate exercise answer
        post("/api/tutorials/:id/validate", (req, res) -> {
            try {
                String tutorialId = req.params(":id");

                // Parse JSON body
                Map<String, String> body = gson.fromJson(req.body(), Map.class);
                String userAnswer = body != null ? body.get("answer") : null;

                if (userAnswer == null || userAnswer.trim().isEmpty()) {
                    res.status(400);
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "Answer is required");
                    return gson.toJson(error);
                }

                boolean isCorrect = tutorialService.validateExercise(tutorialId, userAnswer);
                Map<String, Boolean> result = new HashMap<>();
                result.put("correct", isCorrect);

                return gson.toJson(result);
            } catch (Exception e) {
                res.status(500);
                Map<String, String> error = new HashMap<>();
                error.put("error", "Failed to validate exercise: " + e.getMessage());
                return gson.toJson(error);
            }
        });

        // Health check
        get("/api/health", (req, res) -> {
            Map<String, String> status = new HashMap<>();
            status.put("status", "OK");
            status.put("service", "Tutorial API");
            return gson.toJson(status);
        });
    }
}