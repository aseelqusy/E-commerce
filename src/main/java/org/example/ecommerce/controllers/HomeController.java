package org.example.ecommerce.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.ecommerce.utils.Session;


import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class HomeController {

    @FXML
    private ImageView centerImage;

    @FXML
    private ImageView heroImage;
    @FXML
    private ImageView userAvatar;
    @FXML
    private MenuButton profileMenuButton;


    @FXML
    private TextField searchField;
    @FXML
    private Button adminDashboardBtn;

    @FXML
    public void initialize() {

        try {
            Image image = new Image(getClass().getResource("/assets/RTX.jpg").toExternalForm());
            heroImage.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Session.getCurrentUser() == null) {
            System.out.println("No user session found.");
            adminDashboardBtn.setVisible(false);
            adminDashboardBtn.setManaged(false);
            return;
        }

        String role = Session.getCurrentUser().getRole();
        System.out.println("Logged in as: " + role);

        if (role.equalsIgnoreCase("admin")) {
            adminDashboardBtn.setVisible(true);
            adminDashboardBtn.setManaged(true);
        } else {
            adminDashboardBtn.setVisible(false);
            adminDashboardBtn.setManaged(false);
        }

        if (Session.getCurrentUser() != null) {

            profileMenuButton.setText("👤 " + Session.getCurrentUser().getName());
        }

    }


    @FXML
    private void goToProducts(ActionEvent event) {
        openProductsPage("all");
    }

    @FXML
    private void filterPhones(ActionEvent event) {
        openProductsPage("phones");
    }

    @FXML
    private void filterWatches(ActionEvent event) {
        openProductsPage("games");
    }

    @FXML
    private void filterCameras(ActionEvent event) {
        openProductsPage("cameras");
    }

    @FXML
    private void filterHeadphones(ActionEvent event) {
        openProductsPage("headphones");
    }

    @FXML
    private void filterComputers(ActionEvent event) {
        openProductsPage("computers");
    }


    @FXML
    private void filterGaming(ActionEvent event) {
        openProductsPage("gaming");
    }

    @FXML
    private void filterGames(ActionEvent event) {
        openProductsPage("games");
    }

    @FXML
    private void goToAdminDashboard() {
        String role = Session.getCurrentUser().getRole();

        if (!role.equalsIgnoreCase("admin")) {
            showAlert("Access Denied", "You do not have permission to access the Admin Dashboard.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/admin_dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) adminDashboardBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Admin Dashboard.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) searchField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openProductsPage(String category) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/products.fxml"));
            Parent root = loader.load();

            ProductsController controller = loader.getController();
            controller.setSelectedCategory(category);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles/products.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Products - " + category);
            stage.setScene(scene);
            stage.show();

            heroImage.getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToHome(ActionEvent actionEvent) {
    }

    public void goToAbout(ActionEvent actionEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/checkout.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void goToBlog(ActionEvent actionEvent) {
    }


    public void goToCart(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/Cart.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToContact(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/products.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SHOP(ActionEvent actionEvent) {
        openProductsPage("All"); // أو أي تصنيف افتراضي مثل "Electronics", "Clothes", إلخ
    }

}