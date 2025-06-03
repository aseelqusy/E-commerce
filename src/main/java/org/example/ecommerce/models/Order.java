package org.example.ecommerce.models;

import java.time.LocalDate;

public class Order {
    private int id;
    private int userId;
    private double totalAmount;
    private LocalDate orderDate;

    // Optional fields (if you want to include user info, etc.)
    private String userName;

    public Order(int id, int userId, double total, String status, String date) {}

    public Order(int id, int userId, double totalAmount, LocalDate orderDate) {
        this.id = id;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public String getUserName() {
        return userName;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
