package com.wedaj;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        // This is where your GUI setup code goes
        Label label = new Label("Wedaj is starting up!");
        Scene scene = new Scene(new StackPane(label), 300, 200);
        
        stage.setTitle("Wedaj - Mood Companion");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args); // pass CLI args to JavaFX runtime
    }
}