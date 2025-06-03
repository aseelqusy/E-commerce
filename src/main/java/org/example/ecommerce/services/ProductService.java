package org.example.ecommerce.services;

import org.example.ecommerce.models.Product;
import org.example.ecommerce.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductService {
    private Connection conn;

    public ProductService() {
        try {
            conn = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Product> getProductsByCategory(String category) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM products WHERE LOWER(category) = LOWER(?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("image_path"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getString("category")
                );
                p.setBrand(rs.getString("brand"));
                p.setGenre(rs.getString("genre"));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM products";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("image_path"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getString("category")
                );
                p.setBrand(rs.getString("brand"));
                p.setGenre(rs.getString("genre"));
                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
