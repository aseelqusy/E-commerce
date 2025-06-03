package org.example.ecommerce.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.ecommerce.services.AuthService;

public class SignUpController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private ImageView decorativeImage;

    private final AuthService authService = new AuthService();

    public void initialize() {
        try {
            Image img = new Image(getClass().getResource("/assets/Sign.jpg").toExternalForm());
            decorativeImage.setImage(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSignUp() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            showAlert("Missing Fields", "Please fill in all fields.");
            return;
        }

        if (!password.equals(confirm)) {
            showAlert("Password Mismatch", "Passwords do not match.");
            return;
        }

        boolean registered = authService.register(name ,email, password, "user"); // default role
        if (registered) {
            showAlert("Success", "Account created successfully!");
            goToLogin();
        } else {
            showAlert("Error", "Account already exists or registration failed.");
        }
    }

    @FXML
    private void goToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/login.fxml"));
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load login page.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
