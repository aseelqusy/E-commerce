package org.example.ecommerce.services;

import java.sql.*;

public class PaymentService {
    private final Connection conn;

    public PaymentService() {
        try {
            conn = org.example.ecommerce.utils.DBConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean processPayment(String cardholder, String number, String expiry, String cvv, double amount) {
        String sql = "SELECT * FROM visa_cards WHERE cardholder_name=? AND card_number=? AND expiry_date=? AND cvv=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cardholder);
            stmt.setString(2, number);
            stmt.setString(3, expiry);
            stmt.setString(4, cvv);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                if (balance >= amount) {
                    // Deduct amount
                    PreparedStatement update = conn.prepareStatement("UPDATE visa_cards SET balance = balance - ? WHERE id = ?");
                    update.setDouble(1, amount);
                    update.setInt(2, rs.getInt("id"));
                    update.executeUpdate();
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
