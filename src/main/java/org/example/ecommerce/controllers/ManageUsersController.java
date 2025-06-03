package org.example.ecommerce.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.ecommerce.models.User;
import org.example.ecommerce.services.UserService;

import java.util.List;

public class ManageUsersController {

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> idCol;
    @FXML private TableColumn<User, String> nameCol;
    @FXML private TableColumn<User, String> emailCol;
    @FXML private TableColumn<User, String> roleCol;
    @FXML private TableColumn<User, Void> actionsCol;
    @FXML private TextField searchField;

    private final ObservableList<User> userList = FXCollections.observableArrayList();
    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        loadUsers();
        setupActionColumn();
    }

    private void loadUsers() {
        userList.clear();
        List<User> users = userService.getAllUsers();
        userList.addAll(users);
        userTable.setItems(userList);
    }

    private void setupActionColumn() {
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("✏️");
            private final Button deleteBtn = new Button("🗑");
            private final HBox actionBox = new HBox(8, editBtn, deleteBtn);

            {
                editBtn.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    showEditForm(user);
                });

                deleteBtn.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    if (confirmDelete(user)) {
                        userService.deleteUser(user.getId());
                        loadUsers();
                    }
                });

                actionBox.setPadding(new Insets(0, 0, 0, 5));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : actionBox);
            }
        });
    }

    private void showEditForm(User user) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit User");

        TextField nameField = new TextField(user.getName());
        TextField emailField = new TextField(user.getEmail());
        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("admin", "user");
        roleBox.setValue(user.getRole());

        VBox content = new VBox(10,
                new Label("Name:"), nameField,
                new Label("Email:"), emailField,
                new Label("Role:"), roleBox);
        content.setPadding(new Insets(15));

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                user.setName(nameField.getText().trim());
                user.setEmail(emailField.getText().trim());
                user.setRole(roleBox.getValue());

                userService.updateUser(user);
                loadUsers();
            }
        });
    }

    private boolean confirmDelete(User user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Delete user: " + user.getName());
        alert.setContentText("Are you sure you want to delete this user?");
        return alert.showAndWait().filter(btn -> btn == ButtonType.OK).isPresent();
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadUsers();
        } else {
            List<User> results = userService.searchUsersByEmail(keyword);
            userList.setAll(results);
        }
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/admin_dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) userTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
