package org.example.ecommerce.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.ecommerce.models.CartItem;
import org.example.ecommerce.models.User;
import org.example.ecommerce.services.CartService;
import org.example.ecommerce.services.UserService;
import org.example.ecommerce.utils.Session;

import java.util.List;

public class ManageCartController {

    @FXML private TextField searchField;
    @FXML private ComboBox<User> userSelector;
    @FXML private TableView<CartItem> cartTable;
    @FXML private TableColumn<CartItem, Integer> idCol;
    @FXML private TableColumn<CartItem, Integer> userIdCol;
    @FXML private TableColumn<CartItem, String> productNameCol;
    @FXML private TableColumn<CartItem, Double> priceCol;
    @FXML private TableColumn<CartItem, Integer> quantityCol;
    @FXML private TableColumn<CartItem, Void> actionsCol;

    private final CartService cartService = new CartService();
    private final UserService userService = new UserService();
    private final ObservableList<CartItem> cartData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        setupColumns();
        loadAllCartItems();

        if (Session.isAdmin()) {
            loadUserList();
            userSelector.setVisible(true);
        } else {
            userSelector.setVisible(false);
        }
    }

    private void setupColumns() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button deleteBtn = new Button("🗑 Delete");

            {
                deleteBtn.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");
                deleteBtn.setOnAction(e -> {
                    CartItem item = getTableView().getItems().get(getIndex());
                    handleDelete(item);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });
    }

    private void loadAllCartItems() {
        List<CartItem> items;
        if (Session.isAdmin()) {
            items = cartService.getAllCartItems();
        } else {
            items = cartService.getCartByUser(Session.getCurrentUser().getId());
        }
        cartData.setAll(items);
        cartTable.setItems(cartData);
    }

    private void loadUserList() {
        List<User> users = userService.getAllUsers();
        ObservableList<User> userList = FXCollections.observableArrayList(users);
        userSelector.setItems(userList);

        // Set custom display in dropdown
        userSelector.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName() + " (ID: " + item.getId() + ")");
            }
        });
        userSelector.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName() + " (ID: " + item.getId() + ")");
            }
        });
    }

    @FXML
    private void handleUserSelect() {
        User selected = userSelector.getValue();
        if (selected != null) {
            List<CartItem> userCart = cartService.getCartByUser(selected.getId());
            cartData.setAll(userCart);
        }
    }

    @FXML
    private void handleSearch() {
        String input = searchField.getText().trim();
        if (input.isEmpty()) {
            loadAllCartItems();
            return;
        }

        try {
            int userId = Integer.parseInt(input);
            List<CartItem> filtered = cartService.getCartByUser(userId);
            cartData.setAll(filtered);
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid numeric User ID.");
        }
    }

    private void handleDelete(CartItem item) {
        boolean confirmed = confirmDialog("Delete Item", "Are you sure you want to delete this cart item?");
        if (confirmed) {
            cartService.removeItem(item.getId());
            cartData.remove(item);
            showAlert("Deleted", "Cart item removed successfully.");
        }
    }

    @FXML
    private void goBack() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/admin_dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) cartTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean confirmDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
        return alert.getResult() == ButtonType.YES;
    }
}
