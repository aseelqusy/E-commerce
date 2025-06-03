package org.example.ecommerce.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;

public class ReportsController {

    private void onGenerateReport(ActionEvent event) {
        try {
            // Load report file
            InputStream reportStream = getClass().getResourceAsStream("/reports/report.jrxml");
            InputStream reportStream2 = getClass().getResourceAsStream("/reports/order_report.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // DB connection
            Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/e-commerce", "postgres", "12345"
            );

            // Fill report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), conn);

            // View report
            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void goBack(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/admin_dashboard.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
