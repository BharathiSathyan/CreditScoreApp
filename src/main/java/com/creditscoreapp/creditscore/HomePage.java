package com.creditscoreapp.creditscore;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

import java.io.*;
import java.util.regex.*;

public class HomePage extends Application {

    private static final String USER_DATA_FILE = "C:\\Users\\bhara\\OneDrive\\Desktop\\java_package\\userdata.txt";  // File to store usernames and passwords

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {


        // Load the background image
        String backgroundImagePath = "C:\\Users\\bhara\\OneDrive\\Desktop\\java_package\\Screenshot 2024-10-06 190852.png";
        Image backgroundImage = new Image(new FileInputStream(backgroundImagePath));

        // Creating a background with the image
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true));

        // Root layout
        BorderPane root = new BorderPane();
        root.setBackground(new Background(background));

        // Welcome text
        Text welcomeText = new Text("Welcome to the Credit Score Prediction App");
        welcomeText.setFont(Font.font("Arial", 40));
        welcomeText.setStyle("-fx-fill: #001f3f; -fx-font-weight: bold;");
        BorderPane.setAlignment(welcomeText, Pos.CENTER);
        root.setTop(welcomeText);
        BorderPane.setMargin(welcomeText, new Insets(70, 0, 0, 0));

        // Ivory box for login/signup
        VBox userBox = new VBox(20);
        userBox.setAlignment(Pos.CENTER);
        userBox.setStyle("-fx-background-color: #d6d6c2; -fx-border-radius: 15; -fx-background-radius: 15;");
        userBox.setPrefSize(110, 125); // Set preferred size in cm (scaled to pixels)

        // Create a GridPane for the form layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Horizontal spacing
        grid.setVgap(10); // Vertical spacing

        // Create input fields with labels
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Username");
        usernameField.setStyle("-fx-background-color: white;");

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setStyle("-fx-background-color: white;");

        Label confirmPasswordLabel = new Label("Confirm Password (for signup):");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        confirmPasswordField.setStyle("-fx-background-color: white;");
        confirmPasswordField.setVisible(false);
        confirmPasswordLabel.setVisible(false); // Initially hidden (only for signup)

        // Add everything to the grid
        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(confirmPasswordLabel, 0, 2);
        grid.add(confirmPasswordField, 1, 2);

        // Toggle buttons for login/signup
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton loginRadio = new RadioButton("Login");
        RadioButton signupRadio = new RadioButton("Sign Up");
        loginRadio.setToggleGroup(toggleGroup);
        signupRadio.setToggleGroup(toggleGroup);
        loginRadio.setSelected(true);  // Default to login

        // Button to confirm login/signup
        Button confirmButton = new Button("Proceed");
        confirmButton.setFont(Font.font("Arial", 20));
        confirmButton.setStyle("-fx-background-color: white; -fx-text-fill: #001f3f; -fx-border-color: #001f3f; -fx-font-weight: bold;");

        Label statusLabel = new Label();  // Label to display login/signup status

        // Event handling for login/signup toggle
        toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == signupRadio) {
                confirmPasswordField.setVisible(true);
                confirmPasswordLabel.setVisible(true); // Show confirm password for signup
            } else {
                confirmPasswordField.setVisible(false);
                confirmPasswordLabel.setVisible(false); // Hide confirm password for login
            }
        });

        confirmButton.setOnMouseEntered(e -> {
            // Keep the style the same on hover
            confirmButton.setStyle("-fx-background-color: white; -fx-text-fill: #001f3f; -fx-border-color: #001f3f; -fx-font-weight: bold;");
        });

        confirmButton.setOnMouseExited(e -> {
            // Restore style on exit
            confirmButton.setStyle("-fx-background-color: white; -fx-text-fill: #001f3f; -fx-border-color: #001f3f; -fx-font-weight: bold;");
        });

        // Handling login/signup logic
        confirmButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String confirmPassword = confirmPasswordField.getText().trim();

            if (loginRadio.isSelected()) {
                // Handle login
                if (validateLogin(username, password)) {
                    statusLabel.setText("Login successful!");
                    goToCreditScoringUI(primaryStage);

                } else {
                    statusLabel.setText("Invalid username or password.");
                }
            } else {
                // Handle signup
                if (validateSignup(username, password, confirmPassword)) {
                    statusLabel.setText("Signup successful!");
                    saveUserDetails(username, password);
                    goToCreditScoringUI(primaryStage);
                } else {
                    statusLabel.setText("Signup failed. Try again.");
                }
            }
        });

        // Add components to userBox
        userBox.getChildren().addAll(loginRadio, signupRadio, grid, confirmButton, statusLabel);
        BorderPane.setMargin(userBox, new Insets(110, 420, 120, 420));
        // Center the userBox in the layout
        root.setCenter(userBox);

        // Scene setup
        Scene scene = new Scene(root, 1024, 768);
        primaryStage.setTitle("Credit Score Prediction App - Home");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    // Login validation (check username and password from file)
    private boolean validateLogin(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(":");
                if (userDetails[0].equals(username) && userDetails[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Signup validation (check if username is unique and password is strong)
    private boolean validateSignup(String username, String password, String confirmPassword) {
        if (usernameExists(username)) {
            System.out.println("Username already taken.");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match.");
            return false;
        }
        if (!isPasswordStrong(password)) {
            System.out.println("Password is not strong enough.");
            return false;
        }
        return true;
    }

    // Check if the username already exists in the file
    private boolean usernameExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(":");
                if (userDetails[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Check if the password is strong
    private boolean isPasswordStrong(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$";
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    // Save new user details to the file
    private void saveUserDetails(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_DATA_FILE, true))) {
            writer.write(username + ":" + password);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Navigate to the Credit Scoring UI page
    private void goToCreditScoringUI(Stage primaryStage) {
        CreditScoringUI creditScoringUI = new CreditScoringUI();
        try {
            creditScoringUI.start(primaryStage);
            primaryStage.setFullScreen(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
