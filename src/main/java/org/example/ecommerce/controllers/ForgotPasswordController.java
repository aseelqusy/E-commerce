package org.example.ecommerce.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.ecommerce.utils.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class ForgotPasswordController {

    @FXML
    private ImageView decorativeImage;

    @FXML
    private TextField emailField, codeField;
    @FXML
    private PasswordField newPasswordField, confirmPasswordField;

    @FXML
    private Button verifyButton, resetPassButton;

    private String generatedCode;

    @FXML
    public void initialize() {
        try {
            Image img = new Image(getClass().getResource("/assets/Sign.jpg").toExternalForm());
            decorativeImage.setImage(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSendCode() {
        String email = emailField.getText();
        if (email.isEmpty()) {
            showAlert("Error", "Please enter your email.");
            return;
        }


        generatedCode = generateCode();
        showAlert("Verification Code", "Your code is: " + generatedCode);

        codeField.setVisible(true);
        verifyButton.setVisible(true);
    }

    @FXML
    private void handleVerifyCode() {
        if (codeField.getText().equals(generatedCode)) {
            newPasswordField.setVisible(true);
            confirmPasswordField.setVisible(true);
            resetPassButton.setVisible(true);
            showAlert("Verified", "You can now reset your password.");
        } else {
            showAlert("Invalid Code", "Please check your code again.");
        }
    }

    @FXML
    private void handleResetPassword() {
        String email = emailField.getText().trim();
        String newPassword = newPasswordField.getText().trim();

        if (email.isEmpty() || newPassword.isEmpty()) {
            showAlert("Error", "Please fill all fields");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE users SET password = ? WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            // لا تنسى تشفير الباسورد لو تستخدم تشفير
            stmt.setString(1, newPassword);
            stmt.setString(2, email);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                showAlert("Success", "Password updated successfully");
            } else {
                showAlert("Error", "Email not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update password");
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

    private String generateCode() {
        String chars = "ABCDEFGH0123456789";
        StringBuilder code = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 6; i++) {
            code.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return code.toString();
    }

    private void showAlert(String title, String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }
}
