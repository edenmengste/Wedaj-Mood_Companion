import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
<<<<<<< HEAD
import com.wedaj.core.ChatBot;
=======
>>>>>>> 8728703bdb5c2cb2367977bd407e61a9158f4a68

public class Main extends Application {

    private boolean isDarkMode = false;
    private VBox chatBox; 
    private BorderPane root;
    private VBox topBox;
    private Label lblTitle, lblMood;
    private TextField txtInput;
    private ChatBot bot = new ChatBot();

    @Override
    public void start(Stage stage) {
        stage.setTitle("Wedaj - Your Mood Companion");

        //MESSAGE DISPLAY CONTAINER (The whole chat area) ---
        chatBox = new VBox(15);
        chatBox.setPadding(new Insets(15));
        //ScrollPane allows the user to scroll through the message history
        ScrollPane scrollPane = new ScrollPane(chatBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        //Top Navigation & Status Bar
        Button btnNewChat = new Button("+ New Chat");
        btnNewChat.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-background-radius: 20; -fx-cursor: hand;");
        
        lblTitle = new Label("Wedaj AI");
        lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        
        lblMood = new Label("Mood: —");
        
        Button btnTheme = new Button("🌙");
        btnTheme.setStyle("-fx-background-radius: 20; -fx-cursor: hand;");
        
        //Layout for top elements: New Chat, Title, Mood, and Theme Toggle
        HBox topContent = new HBox(15, btnNewChat, lblTitle, lblMood, new Region(), btnTheme);
        HBox.setHgrow(topContent.getChildren().get(4), Priority.ALWAYS); 
        topContent.setAlignment(Pos.CENTER_LEFT);
        
        topBox = new VBox(topContent);
        topBox.setPadding(new Insets(15));
        topBox.setStyle("-fx-background-color: white; -fx-border-color: #EEE; -fx-border-width: 0 0 1 0;");

        //Bottom Message Input Area 
        txtInput = new TextField();
        txtInput.setPromptText("Type your message...");
        txtInput.setPrefHeight(45);
        txtInput.setStyle("-fx-background-radius: 25; -fx-border-radius: 25; -fx-border-color: #34495e; -fx-border-width: 1.5; -fx-padding: 0 15 0 15; -fx-background-color: white;");

        Button btnSend = new Button("Send");
        btnSend.setPrefHeight(45);
        btnSend.setPrefWidth(80);
        btnSend.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-background-radius: 25; -fx-font-weight: bold; -fx-cursor: hand;");

        HBox inputBar = new HBox(10, txtInput, btnSend);
        inputBar.setPadding(new Insets(20));
        inputBar.setAlignment(Pos.CENTER);
        HBox.setHgrow(txtInput, Priority.ALWAYS);

        //Scene Assembly
        root = new BorderPane();
        root.setTop(topBox);
        root.setCenter(scrollPane);
        root.setBottom(inputBar);
        root.setStyle("-fx-background-color: #F5F7F9;");

        //Event Listeners
        btnNewChat.setOnAction(e -> chatBox.getChildren().clear());
        btnTheme.setOnAction(e -> toggleTheme(btnTheme));
        btnSend.setOnAction(e -> handleSendMessage());
        txtInput.setOnAction(e -> btnSend.fire());

        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    // Handles the processing of sent messages and simulated bot delay
    private void handleSendMessage() {
        String msg = txtInput.getText().trim();
        if (!msg.isEmpty()) {
            addBubble(msg, true);
            txtInput.clear();
            
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(0.5));
            pause.setOnFinished(event -> {
<<<<<<< HEAD
                String response = bot.processMessage(msg);
                addBubble(response, false);
                lblMood.setText("Mood: " + bot.getCurrentMood());
=======
                ChatBot.Result r = bot.reply(msg);
                addBubble(r.text, false);
                lblMood.setText("Mood: " + r.mood);
>>>>>>> 8728703bdb5c2cb2367977bd407e61a9158f4a68
            });
            pause.play();
        }
    }

    // Creates and adds a chat bubble to the UI with copy/edit functionality
    private void addBubble(String text, boolean isUser) {
        StackPane bubbleStack = new StackPane();
        Label lbl = new Label(text);
        lbl.setWrapText(true);
        lbl.setMaxWidth(400);
        applyBubbleStyle(lbl, isUser);

        // Sets icon color to remain visible during theme changes
        String iconColor = isDarkMode ? "#CCC" : "#666";

        Button btnCopy = new Button("❐"); 
        btnCopy.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 14; -fx-text-fill: " + iconColor + ";");
        
        HBox actionBar = new HBox(5, btnCopy);
        actionBar.setVisible(false); // Icons hidden until hover
        actionBar.setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        if (isUser) {
            Button btnEdit = new Button("✎");
            btnEdit.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 14; -fx-text-fill: " + iconColor + ";");
            actionBar.getChildren().add(btnEdit);

            // Re-branching Logic: Removes old bot replies when a user edits an old message
            btnEdit.setOnAction(e -> {
                TextField editField = new TextField(lbl.getText());
                editField.setStyle("-fx-background-radius: 15; -fx-border-color: #34495e; -fx-border-radius: 15;");
                bubbleStack.getChildren().setAll(editField);
                
                editField.setOnAction(ev -> {
                    String newText = editField.getText();
                    lbl.setText(newText);
                    bubbleStack.getChildren().setAll(lbl);
                    
                    int index = chatBox.getChildren().indexOf(bubbleStack.getParent().getParent());
                    if (index != -1 && index < chatBox.getChildren().size() - 1) {
                        chatBox.getChildren().remove(index + 1, chatBox.getChildren().size());
                    }
<<<<<<< HEAD
                    String response = bot.processMessage(newText);
                    addBubble(response, false);
                    lblMood.setText("Mood: " + bot.getCurrentMood());
=======
                    ChatBot.Result r = bot.reply(newText);
                    addBubble(r.text, false);
                    lblMood.setText("Mood: " + r.mood);
>>>>>>> 8728703bdb5c2cb2367977bd407e61a9158f4a68
                });
            });
        }

        // Copy text to system clipboard
        btnCopy.setOnAction(e -> {
            ClipboardContent content = new ClipboardContent();
            content.putString(lbl.getText());
            Clipboard.getSystemClipboard().setContent(content);
        });

        bubbleStack.getChildren().add(lbl);
        VBox vContainer = new VBox(2, bubbleStack, actionBar);
        HBox wrapper = new HBox(vContainer);
        wrapper.setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        // Hover effect to show/hide Edit and Copy buttons
        wrapper.setOnMouseEntered(e -> actionBar.setVisible(true));
        wrapper.setOnMouseExited(e -> actionBar.setVisible(false));

        chatBox.getChildren().add(wrapper);
    }

    // Controls the visual style of chat bubbles based on sender and theme
    private void applyBubbleStyle(Label lbl, boolean isUser) {
        if (isUser) {
            lbl.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-padding: 10 15; -fx-background-radius: 18;");
        } else {
            String bg = isDarkMode ? "#333333" : "#E0E0E0";
            String fg = isDarkMode ? "white" : "black";
            lbl.setStyle("-fx-background-color: " + bg + "; -fx-text-fill: " + fg + "; -fx-padding: 10 15; -fx-background-radius: 18;");
        }
    }

    // Swaps colors for all UI elements and updates existing chat bubbles
    private void toggleTheme(Button btn) {
        isDarkMode = !isDarkMode;
        String iconColor = isDarkMode ? "#CCC" : "#666";

        if (isDarkMode) {
            root.setStyle("-fx-background-color: #121212;");
            topBox.setStyle("-fx-background-color: #1e1e1e; -fx-border-color: #333;");
            lblTitle.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
            lblMood.setStyle("-fx-text-fill: #bdc3c7;");
            txtInput.setStyle("-fx-background-color: #2c2c2c; -fx-text-fill: white; -fx-background-radius: 25; -fx-border-color: #34495e; -fx-border-radius: 25; -fx-border-width: 1.5;");
            btn.setText("☀️");
        } else {
            root.setStyle("-fx-background-color: #F5F7F9;");
            topBox.setStyle("-fx-background-color: white; -fx-border-color: #F0F0F0;");
            lblTitle.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
            lblMood.setStyle("-fx-text-fill: black;");
            txtInput.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-background-radius: 25; -fx-border-color: #34495e; -fx-border-radius: 25; -fx-border-width: 1.5;");
            btn.setText("🌙");
        }

        // Loop through current messages to apply the new theme colors immediately
        chatBox.getChildren().forEach(n -> {
            HBox h = (HBox) n;
            VBox v = (VBox) h.getChildren().get(0);
            StackPane s = (StackPane) v.getChildren().get(0);
            HBox actions = (HBox) v.getChildren().get(1);

            if (s.getChildren().get(0) instanceof Label) {
                applyBubbleStyle((Label) s.getChildren().get(0), h.getAlignment() == Pos.CENTER_RIGHT);
            }

            // Update action icon colors for the new theme
            actions.getChildren().forEach(node -> {
                if (node instanceof Button) {
                    node.setStyle(node.getStyle() + "-fx-text-fill: " + iconColor + ";");
                }
            });
        });
    }

    public static void main(String[] args) { launch(args); }
}