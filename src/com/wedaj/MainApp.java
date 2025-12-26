package com.wedaj;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Wedaj - Your Mood Companion");

        // Chat history area
        ListView<String> lstChat = new ListView<>();
        lstChat.setId("lstChat");

        // Input field
        TextField txtInput = new TextField();
        txtInput.setPromptText("Type your message...");
        txtInput.setId("txtInput");

        // Buttons
        Button btnSend = new Button("Send");
        btnSend.setId("btnSend");

        Button btnClear = new Button("Clear Chat");
        btnClear.setId("btnClear");

        // Mood indicator
        Label lblMood = new Label("Mood: —");
        lblMood.setId("lblMood");

        // Layout
        HBox inputBar = new HBox(8, txtInput, btnSend, btnClear);
        inputBar.setPadding(new Insets(10));

        VBox topBox = new VBox(5, new Label("Wedaj Chatbot"), lblMood);
        topBox.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setTop(topBox);
        root.setCenter(lstChat);
        root.setBottom(inputBar);

        Scene scene = new Scene(root, 720, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

