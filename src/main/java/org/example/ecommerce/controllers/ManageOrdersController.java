package org.example.ecommerce.controllers;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.ecommerce.models.Order;
import org.example.ecommerce.services.OrderService;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ManageOrdersController {

    @FXML private TextField searchField;
    @FXML private TableView<Order> orderTable;
    @FXML private TableColumn<Order, Integer> idCol;
    @FXML private TableColumn<Order, Integer> userIdCol;
    @FXML private TableColumn<Order, String> userNameCol;
    @FXML private TableColumn<Order, Double> totalCol;
    @FXML private TableColumn<Order, String> dateCol;
    @FXML private TableColumn<Order, Void> actionsCol;

    private final OrderService orderService = new OrderService();
    private final ObservableList<Order> orderData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        setupColumns();
        loadAllOrders();
    }

    private void setupColumns() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        dateCol.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        );

        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button deleteBtn = new Button("🗑");

            {
                deleteBtn.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");
                deleteBtn.setOnAction(e -> {
                    Order order = getTableView().getItems().get(getIndex());
                    handleDelete(order);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });
    }

    private void loadAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        orderData.setAll(orders);
        orderTable.setItems(orderData);
    }

    @FXML
    private void handleSearch() {
        String input = searchField.getText().trim();
        if (input.isEmpty()) {
            loadAllOrders();
            return;
        }

        try {
            int orderId = Integer.parseInt(input);
            Order order = orderService.getOrderById(orderId);
            if (order != null) {
                orderData.setAll(order);
            } else {
                showAlert("Not Found", "No order found with ID: " + orderId);
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid Order ID.");
        }
    }

    private void handleDelete(Order order) {
        boolean confirmed = confirmDialog("Delete Order", "Are you sure you want to delete this order?");
        if (confirmed) {
            boolean success = orderService.deleteOrder(order.getId());
            if (success) {
                orderData.remove(order);
                showAlert("Deleted", "Order deleted successfully.");
            } else {
                showAlert("Error", "Failed to delete the order.");
            }
        }
    }

    @FXML
    private void goBack() {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/admin_dashboard.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) orderTable.getScene().getWindow();
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
