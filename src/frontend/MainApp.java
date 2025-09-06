package frontend;

import backend.models.User;
import backend.services.AuthService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainApp extends Application {

    private final AuthService authService = new AuthService();
    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        showLoginPage();
    }

    private void showLoginPage() {
        Label titleLabel = new Label("Welcome Back");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#2c3e50"));

        Label subtitleLabel = new Label("Sign in to your account");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setTextFill(Color.web("#7f8c8d"));

        Label userLabel = new Label("Username:");
        userLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
        userLabel.setTextFill(Color.web("#34495e"));

        TextField userField = new TextField();
        styleTextField(userField);
        userField.setPromptText("Enter your username");

        Label passLabel = new Label("Password:");
        passLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
        passLabel.setTextFill(Color.web("#34495e"));

        PasswordField passField = new PasswordField();
        styleTextField(passField);
        passField.setPromptText("Enter your password");

        Button loginBtn = new Button("Sign In");
        stylePrimaryButton(loginBtn);

        Button goToRegisterBtn = new Button("Create Account");
        styleSecondaryButton(goToRegisterBtn);

        Label messageLabel = new Label();
        messageLabel.setFont(Font.font("Arial", 12));
        messageLabel.setWrapText(true);

        loginBtn.setOnAction(e -> {
            User user = authService.loginUser(userField.getText(), passField.getText());
            if (user != null) {
                messageLabel.setText("✅ Login successful!");
                messageLabel.setTextFill(Color.web("#27ae60"));
                showHomePage(user);
            } else {
                messageLabel.setText("❌ Invalid credentials");
                messageLabel.setTextFill(Color.web("#e74c3c"));
            }
        });

        goToRegisterBtn.setOnAction(e -> showRegisterPage());

        VBox formBox = new VBox(15);
        formBox.getChildren().addAll(
                userLabel, userField,
                passLabel, passField,
                loginBtn, goToRegisterBtn,
                messageLabel
        );
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(30));
        formBox.setMaxWidth(320);

        VBox headerBox = new VBox(5, titleLabel, subtitleLabel);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(20, 0, 10, 0));

        VBox mainLayout = new VBox(20, headerBox, formBox);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(40));

        VBox container = new VBox(mainLayout);
        container.setAlignment(Pos.CENTER);
        styleContainer(container);

        Scene scene = new Scene(container, 450, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        primaryStage.show();
    }

    private void showRegisterPage() {
        Label titleLabel = new Label("Create Account");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#2c3e50"));

        Label subtitleLabel = new Label("Join us today");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setTextFill(Color.web("#7f8c8d"));

        Label userLabel = new Label("Username:");
        userLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
        userLabel.setTextFill(Color.web("#34495e"));

        TextField userField = new TextField();
        styleTextField(userField);
        userField.setPromptText("Choose a username");

        Label emailLabel = new Label("Email:");
        emailLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
        emailLabel.setTextFill(Color.web("#34495e"));

        TextField emailField = new TextField();
        styleTextField(emailField);
        emailField.setPromptText("Enter your email");

        Label passLabel = new Label("Password:");
        passLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
        passLabel.setTextFill(Color.web("#34495e"));

        PasswordField passField = new PasswordField();
        styleTextField(passField);
        passField.setPromptText("Create a password");

        Button registerBtn = new Button("Create Account");
        stylePrimaryButton(registerBtn);

        Button backToLoginBtn = new Button("Back to Login");
        styleSecondaryButton(backToLoginBtn);

        Label messageLabel = new Label();
        messageLabel.setFont(Font.font("Arial", 12));
        messageLabel.setWrapText(true);

        registerBtn.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();
            String email = emailField.getText();

            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                messageLabel.setText("⚠️ Invalid email format");
                messageLabel.setTextFill(Color.web("#f39c12"));
                return;
            }

            User newUser = authService.registerUser(username, password, email);
            if (newUser != null) {
                messageLabel.setText("✅ Registered successfully!");
                messageLabel.setTextFill(Color.web("#27ae60"));
                showLoginPage();
            } else {
                messageLabel.setText("⚠️ Registration failed (maybe user exists)");
                messageLabel.setTextFill(Color.web("#e74c3c"));
            }
        });

        backToLoginBtn.setOnAction(e -> showLoginPage());

        VBox formBox = new VBox(15);
        formBox.getChildren().addAll(
                userLabel, userField,
                emailLabel, emailField,
                passLabel, passField,
                registerBtn, backToLoginBtn,
                messageLabel
        );
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(30));
        formBox.setMaxWidth(320);

        VBox headerBox = new VBox(5, titleLabel, subtitleLabel);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(20, 0, 10, 0));

        VBox mainLayout = new VBox(20, headerBox, formBox);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(40));

        VBox container = new VBox(mainLayout);
        container.setAlignment(Pos.CENTER);
        styleContainer(container);

        Scene scene = new Scene(container, 450, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Register");
        primaryStage.show();
    }

    private void showHomePage(User user) {
        Label titleLabel = new Label("Dashboard");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.web("#2c3e50"));

        Label welcomeLabel = new Label("Welcome back, " + user.getUsername() + "!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        welcomeLabel.setTextFill(Color.web("#34495e"));

        Label successIcon = new Label("🎉");
        successIcon.setFont(Font.font(40));

        VBox welcomeBox = new VBox(15, successIcon, titleLabel, welcomeLabel);
        welcomeBox.setAlignment(Pos.CENTER);
        welcomeBox.setPadding(new Insets(50));

        VBox container = new VBox(welcomeBox);
        container.setAlignment(Pos.CENTER);
        styleContainer(container);

        Scene scene = new Scene(container, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Home");
        primaryStage.show();
    }

    private void styleTextField(TextField field) {
        field.setPrefHeight(40);
        field.setFont(Font.font("Arial", 13));
        field.setStyle(
                "-fx-background-color: #ffffff;" +
                        "-fx-border-color: #bdc3c7;" +
                        "-fx-border-width: 1.5px;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-padding: 10px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"
        );

        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle(
                        "-fx-background-color: #ffffff;" +
                                "-fx-border-color: #3498db;" +
                                "-fx-border-width: 2px;" +
                                "-fx-border-radius: 8px;" +
                                "-fx-background-radius: 8px;" +
                                "-fx-padding: 10px;" +
                                "-fx-effect: dropshadow(gaussian, rgba(52,152,219,0.3), 6, 0, 0, 2);"
                );
            } else {
                field.setStyle(
                        "-fx-background-color: #ffffff;" +
                                "-fx-border-color: #bdc3c7;" +
                                "-fx-border-width: 1.5px;" +
                                "-fx-border-radius: 8px;" +
                                "-fx-background-radius: 8px;" +
                                "-fx-padding: 10px;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"
                );
            }
        });
    }

    private void stylePrimaryButton(Button button) {
        button.setPrefHeight(45);
        button.setPrefWidth(250);
        button.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
        button.setTextFill(Color.WHITE);

        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#3498db")),
                new Stop(1, Color.web("#2980b9"))
        );

        button.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #3498db, #2980b9);" +
                        "-fx-background-radius: 8px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 6, 0, 0, 3);" +
                        "-fx-cursor: hand;"
        );

        button.setOnMouseEntered(e -> {
            button.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #5dade2, #3498db);" +
                            "-fx-background-radius: 8px;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 4);" +
                            "-fx-cursor: hand;" +
                            "-fx-scale-x: 1.02;" +
                            "-fx-scale-y: 1.02;"
            );
        });

        button.setOnMouseExited(e -> {
            button.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #3498db, #2980b9);" +
                            "-fx-background-radius: 8px;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 6, 0, 0, 3);" +
                            "-fx-cursor: hand;" +
                            "-fx-scale-x: 1.0;" +
                            "-fx-scale-y: 1.0;"
            );
        });
    }

    private void styleSecondaryButton(Button button) {
        button.setPrefHeight(40);
        button.setPrefWidth(250);
        button.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        button.setTextFill(Color.web("#3498db"));

        button.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: #3498db;" +
                        "-fx-border-width: 1.5px;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-cursor: hand;"
        );

        button.setOnMouseEntered(e -> {
            button.setStyle(
                    "-fx-background-color: #ecf0f1;" +
                            "-fx-border-color: #2980b9;" +
                            "-fx-border-width: 1.5px;" +
                            "-fx-border-radius: 8px;" +
                            "-fx-background-radius: 8px;" +
                            "-fx-cursor: hand;"
            );
            button.setTextFill(Color.web("#2980b9"));
        });

        button.setOnMouseExited(e -> {
            button.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-border-color: #3498db;" +
                            "-fx-border-width: 1.5px;" +
                            "-fx-border-radius: 8px;" +
                            "-fx-background-radius: 8px;" +
                            "-fx-cursor: hand;"
            );
            button.setTextFill(Color.web("#3498db"));
        });
    }

    private void styleContainer(VBox container) {
        LinearGradient backgroundGradient = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#f8f9fa")),
                new Stop(1, Color.web("#e9ecef"))
        );

        BackgroundFill backgroundFill = new BackgroundFill(
                backgroundGradient, CornerRadii.EMPTY, Insets.EMPTY
        );

        container.setBackground(new Background(backgroundFill));
    }

    public static void main(String[] args) {
        launch(args);
    }
}