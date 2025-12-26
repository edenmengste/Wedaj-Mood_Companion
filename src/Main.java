import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    //State & UI Components
    private boolean isDarkMode = false;
    private VBox chatBox, loginView;
    private BorderPane chatView;
    private VBox topBox;
    private Label lblTitle, lblMood;
    private TextField txtInput;
    private ChatBot bot = new ChatBot(); 

    @Override
    public void start(Stage stage) {
        stage.setTitle("Wedaj - Your Mood Companion");

        // 1. LOGIN INTERFACE (Starting Screen)
        Label lblLoginTitle = new Label("Wedaj Mood Companion 😊");
        lblLoginTitle.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: white;");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Email");
        txtEmail.setMaxWidth(300);
        txtEmail.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: #444; -fx-border-radius: 5;");

        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Password");
        txtPass.setMaxWidth(300);
        txtPass.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: #444; -fx-border-radius: 5;");

        Button btnLogin = new Button("Sign Up / Login");
        btnLogin.setPrefWidth(300);
        btnLogin.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");

        loginView = new VBox(20, lblLoginTitle, txtEmail, txtPass, btnLogin);
        loginView.setAlignment(Pos.CENTER);
        loginView.setStyle("-fx-background-color: #0d1117;"); // Dark Blue-Black

        //CHAT INTERFACE (Hidden initially)
        chatBox = new VBox(15);
        chatBox.setPadding(new Insets(15));
        
        ScrollPane scrollPane = new ScrollPane(chatBox);
        scrollPane.setFitToWidth(true);
        // Ensure the scroll pane scrolls to the bottom when new messages arrive
        chatBox.heightProperty().addListener((obs, oldVal, newVal) -> scrollPane.setVvalue(1.0));
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        // Top Navigation
        Button btnNewChat = new Button("+ New Chat");
        btnNewChat.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-background-radius: 20; -fx-cursor: hand;");
        
        lblTitle = new Label("Wedaj AI");
        lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        lblMood = new Label("Mood: —");
        
        Button btnTheme = new Button("🌙");
        btnTheme.setStyle("-fx-background-radius: 20; -fx-cursor: hand;");
        
        HBox topContent = new HBox(15, btnNewChat, lblTitle, lblMood, new Region(), btnTheme);
        HBox.setHgrow(topContent.getChildren().get(3), Priority.ALWAYS); 
        topContent.setAlignment(Pos.CENTER_LEFT);
        
        topBox = new VBox(topContent);
        topBox.setPadding(new Insets(15));
        topBox.setStyle("-fx-background-color: white; -fx-border-color: #EEE; -fx-border-width: 0 0 1 0;");

        // Bottom Bar
        txtInput = new TextField();
        txtInput.setPromptText("How are you feeling today?");
        txtInput.setPrefHeight(45);
        txtInput.setStyle("-fx-background-color: transparent; " +
                  "-fx-border-color: #34495e; " + 
                  "-fx-border-width: 1.5; " +
                  "-fx-border-radius: 25; " +
                  "-fx-background-radius: 25; " + 
                  "-fx-padding: 0 15 0 15;");        
        Button btnSend = new Button("Send");
        btnSend.setPrefHeight(45);
        btnSend.setPrefWidth(80);
        btnSend.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-background-radius: 25; -fx-font-weight: bold; -fx-cursor: hand;");

        HBox inputBar = new HBox(10, txtInput, btnSend);
        inputBar.setPadding(new Insets(20));
        HBox.setHgrow(txtInput, Priority.ALWAYS);

        chatView = new BorderPane();
        chatView.setTop(topBox);
        chatView.setCenter(scrollPane);
        chatView.setBottom(inputBar);
        chatView.setVisible(false);

        // Enter Key for Login
        txtPass.setOnKeyPressed(e -> { if(e.getCode() == KeyCode.ENTER) btnLogin.fire(); });

        // Login Action
        btnLogin.setOnAction(e -> {
            loginView.setVisible(false);
            chatView.setVisible(true);
        });

        // Chat Actions
        btnSend.setOnAction(e -> handleSendMessage());
        txtInput.setOnAction(e -> btnSend.fire());
        btnNewChat.setOnAction(e -> chatBox.getChildren().clear());
        btnTheme.setOnAction(e -> toggleTheme(btnTheme));

        // Start App
        StackPane rootContainer = new StackPane(chatView, loginView);
        stage.setScene(new Scene(rootContainer, 800, 600));
        stage.show();
    }

    private void handleSendMessage() {
        String msg = txtInput.getText().trim();
        if (!msg.isEmpty()) {
            addBubble(msg, true);
            txtInput.clear();
            
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(0.5));
            pause.setOnFinished(event -> {
                ChatBot.Result r = bot.reply(msg);
                addBubble(r.text, false);
                lblMood.setText("Mood: " + r.mood);
            });
            pause.play();
        }
    }

    private void addBubble(String text, boolean isUser) {
        StackPane bubbleStack = new StackPane();
        Label lbl = new Label(text);
        lbl.setWrapText(true);
        lbl.setMaxWidth(400);
        applyBubbleStyle(lbl, isUser);

        String iconColor = isDarkMode ? "#CCC" : "#666";
        Button btnCopy = new Button("❐"); 
        btnCopy.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-text-fill: " + iconColor + ";");
        
        HBox actionBar = new HBox(5, btnCopy);
        actionBar.setVisible(false);
        actionBar.setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        if (isUser) {
            Button btnEdit = new Button("✎");
            btnEdit.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-text-fill: " + iconColor + ";");
            actionBar.getChildren().add(btnEdit);

            btnEdit.setOnAction(e -> {
                TextField editField = new TextField(lbl.getText());
                editField.setStyle("-fx-background-radius: 15; -fx-border-color: #34495e;");
                bubbleStack.getChildren().setAll(editField);
                
                editField.setOnAction(ev -> {
                    String newText = editField.getText();
                    lbl.setText(newText);
                    bubbleStack.getChildren().setAll(lbl);
                    
                    // Smart Re-branching: remove all messages AFTER this one
                    int index = chatBox.getChildren().indexOf(bubbleStack.getParent().getParent());
                    if (index != -1 && index < chatBox.getChildren().size() - 1) {
                        chatBox.getChildren().remove(index + 1, chatBox.getChildren().size());
                    }
                    ChatBot.Result r = bot.reply(newText);
                    addBubble(r.text, false);
                    lblMood.setText("Mood: " + r.mood);
                });
            });
        }

        btnCopy.setOnAction(e -> {
            ClipboardContent content = new ClipboardContent();
            content.putString(lbl.getText());
            Clipboard.getSystemClipboard().setContent(content);
        });

        bubbleStack.getChildren().add(lbl);
        VBox vContainer = new VBox(2, bubbleStack, actionBar);
        HBox wrapper = new HBox(vContainer);
        wrapper.setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        wrapper.setOnMouseEntered(e -> actionBar.setVisible(true));
        wrapper.setOnMouseExited(e -> actionBar.setVisible(false));

        chatBox.getChildren().add(wrapper);
    }

    private void applyBubbleStyle(Label lbl, boolean isUser) {
        if (isUser) {
            lbl.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-padding: 10 15; -fx-background-radius: 18;");
        } else {
            String bg = isDarkMode ? "#333333" : "#E0E0E0";
            String fg = isDarkMode ? "white" : "black";
            lbl.setStyle("-fx-background-color: " + bg + "; -fx-text-fill: " + fg + "; -fx-padding: 10 15; -fx-background-radius: 18;");
        }
    }

    private void toggleTheme(Button btn) {
        isDarkMode = !isDarkMode;
        String iconColor = isDarkMode ? "#CCC" : "#666";

        if (isDarkMode) {
            chatView.setStyle("-fx-background-color: #121212;");
            topBox.setStyle("-fx-background-color: #1e1e1e; -fx-border-color: #333;");
            lblTitle.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
            lblMood.setStyle("-fx-text-fill: #bdc3c7;");
            // Set background to transparent in Dark Mode
            txtInput.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-background-radius: 25; -fx-border-color: #34495e; -fx-border-radius: 25; -fx-border-width: 1.5; -fx-padding: 0 15 0 15;");
            btn.setText("☀️");
        } else {
            chatView.setStyle("-fx-background-color: #F5F7F9;");
            topBox.setStyle("-fx-background-color: white; -fx-border-color: #F0F0F0;");
            lblTitle.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
            lblMood.setStyle("-fx-text-fill: black;");
            // Set background to transparent in Light Mode
            txtInput.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-background-radius: 25; -fx-border-color: #34495e; -fx-border-radius: 25; -fx-border-width: 1.5; -fx-padding: 0 15 0 15;");
            btn.setText("🌙");
        }

        chatBox.getChildren().forEach(n -> {
            HBox h = (HBox) n;
            VBox v = (VBox) h.getChildren().get(0);
            StackPane s = (StackPane) v.getChildren().get(0);
            HBox actions = (HBox) v.getChildren().get(1);

            if (s.getChildren().get(0) instanceof Label) {
                applyBubbleStyle((Label) s.getChildren().get(0), h.getAlignment() == Pos.CENTER_RIGHT);
            }
            actions.getChildren().forEach(node -> node.setStyle(node.getStyle() + "-fx-text-fill: " + iconColor + ";"));
        });
    }

    public static void main(String[] args) { launch(args); }
}