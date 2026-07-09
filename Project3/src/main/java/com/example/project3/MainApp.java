// Ethan Nguyen
// 1002097430
// Project 3 -- Database Integration

package com.example.project3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Initialize database tables on startup
        DatabaseConnection.initializeDatabase();

        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);
        stage.setTitle("INSY 4305 - Project 3");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
