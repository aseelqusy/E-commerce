package org.example.ecommerce.models;

public class AdminProduct {
    private int id;
    private String name;
    private String description;
    private String imagePath;
    private double price;
    private int quantity;
    private String category;
    private String brand;
    private String genre;

    public AdminProduct() {}

    public AdminProduct(int id, String name, String description, String imagePath, double price, int quantity, String category, String brand, String genre) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.brand = brand;
        this.genre = genre;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
}
