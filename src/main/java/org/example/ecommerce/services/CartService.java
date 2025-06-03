package org.example.ecommerce.services;

import org.example.ecommerce.models.CartItem;
import org.example.ecommerce.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartService {
    private final Connection conn;

    public CartService() {
        try {
            conn = DBConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addToCart(int userId, int productId, int quantity) {
        String checkSql = "SELECT id, quantity FROM cart_items WHERE user_id = ? AND product_id = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, userId);
            checkStmt.setInt(2, productId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                int currentQty = rs.getInt("quantity");
                int itemId = rs.getInt("id");
                updateQuantity(itemId, currentQty + quantity);
            } else {
                String insertSql = "INSERT INTO cart_items (user_id, product_id, quantity) VALUES (?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, userId);
                    insertStmt.setInt(2, productId);
                    insertStmt.setInt(3, quantity);
                    insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<CartItem> getCartByUser(int userId) {
        List<CartItem> list = new ArrayList<>();
        String sql = """
                SELECT ci.*, p.name as product_name, p.price, p.image_path
                FROM cart_items ci
                JOIN products p ON ci.product_id = p.id
                WHERE ci.user_id = ?
                """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                CartItem item = new CartItem(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity")
                );
                item.setProductName(rs.getString("product_name"));
                item.setPrice(rs.getDouble("price"));
                item.setImagePath(rs.getString("image_path"));
                list.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<CartItem> getAllCartItems() {
        List<CartItem> list = new ArrayList<>();
        String sql = """
            SELECT ci.*, p.name as product_name, p.price, p.image_path
            FROM cart_items ci
            JOIN products p ON ci.product_id = p.id
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                CartItem item = new CartItem(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity")
                );
                item.setProductName(rs.getString("product_name"));
                item.setPrice(rs.getDouble("price"));
                item.setImagePath(rs.getString("image_path"));
                list.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void removeItem(int itemId) {
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM cart_items WHERE id = ?");
            stmt.setInt(1, itemId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateQuantity(int itemId, int quantity) {
        try {
            PreparedStatement stmt = conn.prepareStatement("UPDATE cart_items SET quantity = ? WHERE id = ?");
            stmt.setInt(1, quantity);
            stmt.setInt(2, itemId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearCart(int userId) {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getSubtotal(int userId) {
        double subtotal = 0.0;
        String query = """
            SELECT p.price, ci.quantity
            FROM cart_items ci
            JOIN products p ON ci.product_id = p.id
            WHERE ci.user_id = ?
        """;

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                subtotal += rs.getDouble("price") * rs.getInt("quantity");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return subtotal;
    }
    public boolean deleteCartItem(int cartId) {
        String sql = "DELETE FROM cart_items WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
