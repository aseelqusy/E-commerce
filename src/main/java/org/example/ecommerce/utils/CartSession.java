package org.example.ecommerce.utils;

import org.example.ecommerce.models.Product;

import java.util.ArrayList;
import java.util.List;

public class CartSession {
    private static final List<Product> cartItems = new ArrayList<>();

    public static void addItem(Product product) {
        cartItems.add(product);
    }

    public static List<Product> getItems() {
        return new ArrayList<>(cartItems); // نسخة آمنة
    }

    public static void clear() {
        cartItems.clear();
    }

    public static double getTotal() {
        return cartItems.stream()
                .mapToDouble(Product::getPrice)
                .sum();
    }
}
