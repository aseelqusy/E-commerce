package org.example.ecommerce.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.ecommerce.models.CartItem;
import org.example.ecommerce.services.CartService;
import org.example.ecommerce.utils.Session;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CartController   implements Initializable {
    @FXML
    private BorderPane cartPane;

    @FXML private VBox cartItemsBox;
    @FXML private Label subtotalLabel, taxLabel, shippingLabel, totalLabel;
    @FXML private Button checkoutBtn;
    @FXML
    private MenuButton profileMenu;  // اربط مع fx:id من FXML


    private final CartService cartService = new CartService();
    private double tax = 50;
    private double shipping = 29;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadCartItems();
        if (Session.getCurrentUser() != null) {
            String name = Session.getCurrentUser().getName();
            profileMenu.setText("👤 " + name);
        }

    }

    private void loadCartItems() {
        cartItemsBox.getChildren().clear();

        int userId = Session.getCurrentUser().getId();
        List<CartItem> cartItems = cartService.getCartByUser(userId);

        double subtotal = 0;
        for (CartItem item : cartItems) {
            HBox row = new HBox(15);
            row.setAlignment(Pos.CENTER_LEFT);
            row.getStyleClass().add("cart-item");

            VBox info = new VBox(3);
            Label nameLabel = new Label(item.getProductName());
            nameLabel.getStyleClass().add("item-title");
            Label idLabel = new Label("#" + item.getProductId());
            idLabel.getStyleClass().add("item-id");
            info.getChildren().addAll(nameLabel, idLabel);

            Label qtyLabel = new Label(String.valueOf(item.getQuantity()));
            Button plus = new Button("+");
            Button minus = new Button("-");
            plus.setOnAction(e -> {
                cartService.updateQuantity(item.getId(), item.getQuantity() + 1);
                loadCartItems();
            });
            minus.setOnAction(e -> {
                int newQty = item.getQuantity() - 1;
                if (newQty > 0) {
                    cartService.updateQuantity(item.getId(), newQty);
                } else {
                    cartService.removeItem(item.getId());
                }
                loadCartItems();
            });

            HBox qtyBox = new HBox(5, minus, qtyLabel, plus);
            qtyBox.setAlignment(Pos.CENTER);

            Label priceLabel = new Label(String.format("%.2f", item.getPrice() * item.getQuantity()));
            priceLabel.getStyleClass().add("price");

            Button removeBtn = new Button("X");
            removeBtn.getStyleClass().add("remove-btn");
            removeBtn.setOnAction(e -> {
                cartService.removeItem(item.getId());
                loadCartItems();
            });

            row.getChildren().addAll(info, new Pane(), qtyBox, priceLabel, removeBtn);
            HBox.setHgrow(row.getChildren().get(1), Priority.ALWAYS);

            cartItemsBox.getChildren().add(row);

            subtotal += item.getPrice() * item.getQuantity();
        }

        subtotalLabel.setText("Subtotal: $" + subtotal);
        taxLabel.setText("Estimated Tax: $" + tax);
        shippingLabel.setText("Estimated shipping  Handling: $" + shipping);
        totalLabel.setText("Total: $" + (subtotal + tax + shipping));

    }

    @FXML
    private void goToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) cartPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Home");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void goToCart() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Cart.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) cartPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Cart");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        Session.clear();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) cartPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {}
    }


    @FXML
    private void goToProducts() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/products.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) cartPane.getScene().getWindow(); // cartPane معرف بـ fx:id
            stage.setScene(new Scene(root));
            stage.setTitle("Products");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Products page.").showAndWait();
        }
    }

    @FXML
    private void goToShipping() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/shipping.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) cartPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Shipping Details");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load shipping screen").showAndWait();
        }
    }



}
