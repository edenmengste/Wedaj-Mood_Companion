package core;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class Main extends Application {

    private final ChatBot chatBot = new ChatBot();
    private final SignupManager signupManager = new SignupManager();

    private final Map<String, ObservableList<HBox>> chats = new HashMap<>();
    private ObservableList<HBox> currentChat;
    private int chatCounter = 1;
    private Label lblMood = new Label("Mood: —"); // Moved to class level for access

    @Override
    public void start(Stage stage) {
        stage.setTitle("Wedaj - Your Mood Companion");

        // --- Signup Page ---
        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Email");
        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Password");

        Button btnSignup = new Button("Sign Up");
        Button btnLogin = new Button("Login");
        Label lblAuthStatus = new Label();

        VBox signupLayout = new VBox(10, new Label("Welcome to Wedaj 😊"), txtEmail, txtPassword,
                new HBox(10, btnSignup, btnLogin), lblAuthStatus);
        signupLayout.setPadding(new Insets(20));
        signupLayout.setBackground(new Background(new BackgroundFill(Color.web("#ffffff"), null, null)));
        Scene signupScene = new Scene(signupLayout, 400, 300);

        // --- Chat Page ---
        ListView<String> chatHistory = new ListView<>();
        chatHistory.setPrefWidth(150);
        chatHistory.setVisible(false);

        ListView<HBox> lstChat = new ListView<>();
        currentChat = FXCollections.observableArrayList();
        lstChat.setItems(currentChat);

        TextField txtInput = new TextField();
        txtInput.setPromptText("How are you feeling today?");
        txtInput.setPrefWidth(400);

        Button btnSend = new Button("Send");

        // --- Icons & Navigation ---
        Button btnToggleHistory = new Button("History", new ImageView(new Image("file:src/icons/history.png", 18, 18, true, true)));
        Button btnNewChat = new Button("New Chat");
        Button btnThemeToggle = new Button("Theme", new ImageView(new Image("file:src/icons/theme.png", 18, 18, true, true)));
        Button btnLogout = new Button("Logout", new ImageView(new Image("file:src/icons/logout.png", 18, 18, true, true)));

        VBox leftPanel = new VBox(10, btnToggleHistory, btnNewChat, chatHistory);
        leftPanel.setPadding(new Insets(10));

        HBox topBar = new HBox(lblMood, new Region(), btnThemeToggle, btnLogout);
        HBox.setHgrow(topBar.getChildren().get(1), Priority.ALWAYS);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER_LEFT);

        HBox inputBar = new HBox(8, txtInput, btnSend);
        inputBar.setAlignment(Pos.CENTER);
        inputBar.setPadding(new Insets(10));

        BorderPane chatLayout = new BorderPane();
        chatLayout.setTop(topBar);
        chatLayout.setCenter(lstChat);
        chatLayout.setBottom(inputBar);
        chatLayout.setLeft(leftPanel);

        Scene chatScene = new Scene(chatLayout, 900, 520);

        // --- Actions ---
        btnSend.setOnAction(e -> handleUserMessage(txtInput.getText(), txtInput));
        txtInput.setOnAction(e -> btnSend.fire());
        btnNewChat.setOnAction(e -> startNewChat(chatHistory, lstChat));
        btnToggleHistory.setOnAction(e -> chatHistory.setVisible(!chatHistory.isVisible()));
        
        btnSignup.setOnAction(e -> {
            if (signupManager.signup(txtEmail.getText(), txtPassword.getText()) == SignupManager.Status.SUCCESS) {
                stage.setScene(chatScene);
                startNewChat(chatHistory, lstChat);
            }
        });

        btnLogin.setOnAction(e -> {
            if (signupManager.login(txtEmail.getText(), txtPassword.getText()) == SignupManager.Status.SUCCESS) {
                stage.setScene(chatScene);
                if (chats.isEmpty()) startNewChat(chatHistory, lstChat);
            }
        });

        btnThemeToggle.setOnAction(e -> {
            boolean isWhite = chatLayout.getBackground().getFills().get(0).getFill().equals(Color.web("#ffffff"));
            String color = isWhite ? "#1e1e1e" : "#ffffff";
            String textColor = isWhite ? "white" : "black";
            chatLayout.setBackground(new Background(new BackgroundFill(Color.web(color), null, null)));
            lstChat.setStyle("-fx-control-inner-background: " + color + ";");
            lblMood.setStyle("-fx-text-fill: " + textColor + ";");
        });

        btnLogout.setOnAction(e -> {
            signupManager.logout();
            currentChat.clear();
            chatHistory.getItems().clear();
            stage.setScene(signupScene);
        });

        stage.setScene(signupScene);
        stage.show();
    }

    private void handleUserMessage(String text, TextField inputField) {
        if (text == null || text.isBlank()) return;
        addMessageToUI(text, true);
        if (inputField != null) inputField.clear();
        
        // Bot reply
        ChatBot.Result result = chatBot.reply(text, signupManager.currentUser());
        addMessageToUI(result.text, false);
        lblMood.setText("Mood: " + result.mood);
    }

    private void addMessageToUI(String text, boolean isUser) {
        Label lbl = new Label(text);
        lbl.setWrapText(true);
        lbl.setStyle("-fx-background-color: " + (isUser ? "#00aaff" : "#444") +
                "; -fx-text-fill: white; -fx-padding: 8; -fx-background-radius: 10;");

        HBox actions = new HBox(5);
        actions.setVisible(false);

        // Copy button for everyone
        Button btnCopy = new Button("Copy", new ImageView(new Image("file:src/icons/copy.png", 14, 14, true, true)));
        btnCopy.setOnAction(e -> {
            ClipboardContent content = new ClipboardContent();
            content.putString(lbl.getText());
            Clipboard.getSystemClipboard().setContent(content);
        });
        actions.getChildren().add(btnCopy);

        // Edit button ONLY for User
        if (isUser) {
            Button btnEdit = new Button("Edit", new ImageView(new Image("file:src/icons/edit.png", 14, 14, true, true)));
            btnEdit.setOnAction(e -> {
                TextInputDialog dialog = new TextInputDialog(lbl.getText());
                dialog.setTitle("Edit Message");
                dialog.setHeaderText(null);
                dialog.showAndWait().ifPresent(newText -> {
                    if (!newText.equals(lbl.getText())) {
                        int index = currentChat.indexOf(findParentHBox(lbl));
                        // Remove the user message and the subsequent bot response
                        if (index != -1) {
                            lbl.setText(newText);
                            // If there is a bot message immediately after, remove it
                            if (index + 1 < currentChat.size()) {
                                currentChat.remove(index + 1);
                            }
                            // Get new reply
                            ChatBot.Result result = chatBot.reply(newText, signupManager.currentUser());
                            addMessageToUI(result.text, false); 
                            // Re-inserting logic to keep order
                            HBox newBotMsg = currentChat.remove(currentChat.size() - 1);
                            currentChat.add(index + 1, newBotMsg);
                            lblMood.setText("Mood: " + result.mood);
                        }
                    }
                });
            });
            actions.getChildren().add(btnEdit);
        }

        VBox messageBox = new VBox(lbl, actions);
        messageBox.setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        messageBox.setOnMouseEntered(e -> actions.setVisible(true));
        messageBox.setOnMouseExited(e -> actions.setVisible(false));

        HBox container = new HBox(messageBox);
        container.setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        currentChat.add(container);
    }

    private HBox findParentHBox(Label lbl) {
        return (HBox) lbl.getParent().getParent();
    }

    private void startNewChat(ListView<String> chatHistory, ListView<HBox> lstChat) {
        String chatName = "Chat " + chatCounter++;
        ObservableList<HBox> newChat = FXCollections.observableArrayList();
        chats.put(chatName, newChat);
        chatHistory.getItems().add(chatName);
        currentChat = newChat;
        lstChat.setItems(currentChat);
    }

    public static void main(String[] args) { launch(args); }
}