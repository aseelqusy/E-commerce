package org.example.ecommerce.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/admin_dashboard.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles/home.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/styles/products.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/styles/checkout.css").toExternalForm());
            primaryStage.setTitle("E-Commerce PC  ");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
