package org.example.ecommerce.services;

import org.example.ecommerce.models.CartItem;
import org.example.ecommerce.models.Order;
import org.example.ecommerce.utils.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private final Connection conn;

    public OrderService() {
        try {
            conn = DBConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 🔹 Get All Orders
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = """
                SELECT o.*, u.name as user_name 
                FROM orders o
                JOIN users u ON o.user_id = u.id
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getDouble("total_amount"),
                        rs.getDate("order_date").toLocalDate()
                );
                order.setUserName(rs.getString("user_name"));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // 🔹 Get Order by ID
    public Order getOrderById(int id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Order(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getDouble("total_amount"),
                        rs.getDate("order_date").toLocalDate()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔹 Add Order
    public boolean addOrder(Order order) {
        String sql = "INSERT INTO orders (user_id, total_amount, order_date) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, order.getUserId());
            stmt.setDouble(2, order.getTotalAmount());
            stmt.setDate(3, Date.valueOf(order.getOrderDate()));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 Update Order
    public boolean updateOrder(Order order) {
        String sql = "UPDATE orders SET user_id = ?, total_amount = ?, order_date = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, order.getUserId());
            stmt.setDouble(2, order.getTotalAmount());
            stmt.setDate(3, Date.valueOf(order.getOrderDate()));
            stmt.setInt(4, order.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 Delete Order
    public boolean deleteOrder(int id) {
        String sql = "DELETE FROM orders WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 Create Order with Items
    public int createOrder(int userId, double totalAmount, List<CartItem> cartItems) {
        String orderSql = "INSERT INTO orders (user_id, total_amount, order_date) VALUES (?, ?, ?)";
        String itemSql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        String updateBalanceSql = "UPDATE users SET balance = balance - ? WHERE id = ? AND balance >= ?";

        try {
            conn.setAutoCommit(false); // Start transaction

            // ✅ Try to charge balance
            try (PreparedStatement balanceStmt = conn.prepareStatement(updateBalanceSql)) {
                balanceStmt.setDouble(1, totalAmount);
                balanceStmt.setInt(2, userId);
                balanceStmt.setDouble(3, totalAmount);
                int rows = balanceStmt.executeUpdate();

                if (rows == 0) {
                    conn.rollback();
                    System.out.println("❌ Insufficient balance.");
                    return -2; // Balance not enough
                }
            }

            // ✅ Insert Order
            PreparedStatement orderStmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setInt(1, userId);
            orderStmt.setDouble(2, totalAmount);
            orderStmt.setDate(3, Date.valueOf(LocalDate.now()));
            int affectedRows = orderStmt.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                return -1;
            }

            ResultSet generatedKeys = orderStmt.getGeneratedKeys();
            int orderId = -1;
            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
            } else {
                conn.rollback();
                return -1;
            }

            // ✅ Insert Order Items
            PreparedStatement itemStmt = conn.prepareStatement(itemSql);
            for (CartItem item : cartItems) {
                itemStmt.setInt(1, orderId);
                itemStmt.setInt(2, item.getProductId());
                itemStmt.setInt(3, item.getQuantity());
                itemStmt.setDouble(4, item.getPrice());
                itemStmt.addBatch();
            }
            itemStmt.executeBatch();

            conn.commit(); // All good
            return orderId;

        } catch (SQLException e) {
            try {
                conn.rollback(); // On error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return -1;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



}
