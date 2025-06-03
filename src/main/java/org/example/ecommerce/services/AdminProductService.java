package org.example.ecommerce.services;

import org.example.ecommerce.models.AdminProduct;
import org.example.ecommerce.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminProductService {
    private Connection conn;

    public AdminProductService() {
        try {
            conn = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<AdminProduct> getAllProducts() {
        List<AdminProduct> list = new ArrayList<>();
        String query = "SELECT * FROM products";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                AdminProduct p = new AdminProduct(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("image_path"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getString("category"),
                        rs.getString("brand"),
                        rs.getString("genre")
                );
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void deleteProductById(int id) {
        String query = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addProduct(AdminProduct p) {
        String query = "INSERT INTO products (name, description, image_path, price, quantity, category, brand, genre) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, p.getName());
            stmt.setString(2, p.getDescription());
            stmt.setString(3, p.getImagePath());
            stmt.setDouble(4, p.getPrice());
            stmt.setInt(5, p.getQuantity());
            stmt.setString(6, p.getCategory());
            stmt.setString(7, p.getBrand());
            stmt.setString(8, p.getGenre());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProduct(AdminProduct p) {
        String query = "UPDATE products SET name=?, description=?, image_path=?, price=?, quantity=?, category=?, brand=?, genre=? WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, p.getName());
            stmt.setString(2, p.getDescription());
            stmt.setString(3, p.getImagePath());
            stmt.setDouble(4, p.getPrice());
            stmt.setInt(5, p.getQuantity());
            stmt.setString(6, p.getCategory());
            stmt.setString(7, p.getBrand());
            stmt.setString(8, p.getGenre());
            stmt.setInt(9, p.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<AdminProduct> searchProductsByName(String keyword) {
        List<AdminProduct> list = new ArrayList<>();
        String query = "SELECT * FROM products WHERE LOWER(name) LIKE ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + keyword.toLowerCase() + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                AdminProduct p = new AdminProduct(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("image_path"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getString("category"),
                        rs.getString("brand"),
                        rs.getString("genre")
                );
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
