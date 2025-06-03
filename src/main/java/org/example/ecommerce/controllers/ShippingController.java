package org.example.ecommerce.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.MenuButton;
import javafx.stage.Stage;
import org.example.ecommerce.utils.Session;


public class ShippingController {

    @FXML
    private MenuButton profileMenu;
    @FXML
    public void initialize() {
        if (Session.getCurrentUser() != null) {
            String name = Session.getCurrentUser().getName();
            profileMenu.setText("👤 " + name);
        }
    }


    @FXML
    private void goToCart() {
        navigate("/views/Cart.fxml", "Your Cart");
    }

    @FXML
    private void goToHome() {
        navigate("/views/home.fxml", "Home");
    }

    @FXML
    private void goToProducts() {
        navigate("/views/products.fxml", "Products");
    }

    @FXML
    private void goToPayment() {
        navigate("/views/payment.fxml", "Payment");
    }

    @FXML
    private void handleLogout() {
        navigate("/views/login.fxml", "Login");
    }

    private void navigate(String path, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent root = loader.load();
            Stage stage = (Stage) javafx.stage.Window.getWindows().stream()
                    .filter(Window -> Window.isShowing())
                    .findFirst().orElseThrow().getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
