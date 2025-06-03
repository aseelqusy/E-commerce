package org.example.ecommerce.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.ecommerce.models.AdminProduct;
import org.example.ecommerce.services.AdminProductService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProductFormController  implements Initializable {

    @FXML private TableView<AdminProduct> productTable;
    @FXML private TableColumn<AdminProduct, Integer> idCol;
    @FXML private TableColumn<AdminProduct, String> nameCol;
    @FXML private TableColumn<AdminProduct, String> descCol;
    @FXML private TableColumn<AdminProduct, Double> priceCol;
    @FXML private TableColumn<AdminProduct, Integer> qtyCol;
    @FXML private TableColumn<AdminProduct, String> categoryCol;
    @FXML private TableColumn<AdminProduct, String> brandCol;
    @FXML private TableColumn<AdminProduct, String> genreCol;

    @FXML private VBox formContainer;
    @FXML private TextField nameField;
    @FXML private TextArea descField;
    @FXML private TextField imagePathField;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;
    @FXML private TextField categoryField;
    @FXML private TextField brandField;
    @FXML private TextField genreField;
    @FXML private Label formTitle;

    @FXML private TextField searchField;

    private final AdminProductService productService = new AdminProductService();
    private final ObservableList<AdminProduct> productList = FXCollections.observableArrayList();
    private AdminProduct productToEdit = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getId()).asObject());
        nameCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));
        descCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getDescription()));
        priceCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getPrice()).asObject());
        qtyCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getQuantity()).asObject());
        categoryCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCategory()));
        brandCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getBrand()));
        genreCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getGenre()));

        loadProducts();
    }

    public void loadProducts() {
        productList.clear();
        List<AdminProduct> products = productService.getAllProducts();
        productList.addAll(products);
        productTable.setItems(productList);
        formContainer.setVisible(false);
        formContainer.setManaged(false);
    }

    public void handleAdd() {
        productToEdit = null;
        showForm("➕ Add New Product");
    }

    public void handleEdit() {
        productToEdit = productTable.getSelectionModel().getSelectedItem();
        if (productToEdit != null) {
            showForm("✏ Edit Product");
            nameField.setText(productToEdit.getName());
            descField.setText(productToEdit.getDescription());
            imagePathField.setText(productToEdit.getImagePath());
            priceField.setText(String.valueOf(productToEdit.getPrice()));
            quantityField.setText(String.valueOf(productToEdit.getQuantity()));
            categoryField.setText(productToEdit.getCategory());
            brandField.setText(productToEdit.getBrand());
            genreField.setText(productToEdit.getGenre());
        }
    }

    public void handleDelete() {
        AdminProduct selected = productTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            productService.deleteProductById(selected.getId());
            loadProducts();
        }
    }

    public void handleRefresh() {
        loadProducts();
    }

    public void handleSearch() {
        String keyword = searchField.getText().trim();
        if (!keyword.isEmpty()) {
            productList.clear();
            productList.addAll(productService.searchProductsByName(keyword));
            productTable.setItems(productList);
        } else {
            loadProducts();
        }
    }

    public void handleSave() {
        try {
            AdminProduct p = (productToEdit != null) ? productToEdit : new AdminProduct();

            p.setName(nameField.getText());
            p.setDescription(descField.getText());
            p.setImagePath(imagePathField.getText());
            p.setPrice(Double.parseDouble(priceField.getText()));
            p.setQuantity(Integer.parseInt(quantityField.getText()));
            p.setCategory(categoryField.getText());
            p.setBrand(brandField.getText());
            p.setGenre(genreField.getText());

            if (productToEdit != null) {
                productService.updateProduct(p);
            } else {
                productService.addProduct(p);
            }

            loadProducts();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Invalid data. Please check fields.").showAndWait();
        }
    }

    public void handleCancel() {
        formContainer.setVisible(false);
        formContainer.setManaged(false);
    }

    private void showForm(String title) {
        formTitle.setText(title);
        formContainer.setVisible(true);
        formContainer.setManaged(true);
        clearFormFields();
    }

    private void clearFormFields() {
        nameField.clear();
        descField.clear();
        imagePathField.clear();
        priceField.clear();
        quantityField.clear();
        categoryField.clear();
        brandField.clear();
        genreField.clear();
    }
}
