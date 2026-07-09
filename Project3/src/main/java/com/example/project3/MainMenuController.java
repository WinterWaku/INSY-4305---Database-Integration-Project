// Ethan Nguyen
// 1002097430
// Project 3 -- Database Integration

package com.example.project3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// Handles navigation to the Customer window, Employee window, or Exit.
public class MainMenuController {

    @FXML
    protected void onCustomerButtonClick(ActionEvent event) throws IOException {
        openWindow("CustomerWindow.fxml", "Customer Data Entry", 530, 430);
    }

    @FXML
    protected void onEmployeeButtonClick(ActionEvent event) throws IOException {
        openWindow("EmployeeWindow.fxml", "Employee Data Entry", 530, 590);
    }

    @FXML
    protected void onExitButtonClick(ActionEvent event) {
        System.exit(0);
    }

    private void openWindow(String fxmlFile, String title, int width, int height) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(fxmlFile));
        Scene scene = new Scene(loader.load(), width, height);
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
