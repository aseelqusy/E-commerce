package org.example.ecommerce.models;

public class Product {
    private int id;
    private String name;
    private String description;
    private String imagePath;
    private double price;
    private int quantity;
    private String category;
    private String brand;
    private String genre;

    // Getters
    public String getBrand() {
        return brand;
    }

    public String getGenre() {
        return genre;
    }

    // Setters
    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Product(int id, String name, String description, String imagePath, double price, int quantity, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }
    public Product() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
