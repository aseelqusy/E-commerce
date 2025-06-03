package org.example.ecommerce.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.ecommerce.models.User;
import org.example.ecommerce.utils.Session;

public abstract class BaseController {

    protected void setupUserInfo(Label profileLabel, Button adminButton) {
        User user = Session.getCurrentUser();

        if (user == null) {
            System.out.println("No user session found.");
            if (adminButton != null) {
                adminButton.setVisible(false);
                adminButton.setManaged(false);
            }
            return;
        }

        String role = user.getRole();
        System.out.println("Logged in as: " + role);

        if (adminButton != null) {
            boolean isAdmin = role.equalsIgnoreCase("admin");
            adminButton.setVisible(isAdmin);
            adminButton.setManaged(isAdmin);
        }

        if (profileLabel != null) {
            profileLabel.setText("👤 " + user.getName());
        }
    }
}
