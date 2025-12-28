import core.ChatBot;
import core.SignupManager;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    private final ChatBot chatBot = new ChatBot();
    private final SignupManager signupManager = new SignupManager();

    // Theme colors
    private static class Theme {
        final String bg, panel, text, accent;
        Theme(String bg, String panel, String text, String accent) {
            this.bg = bg; this.panel = panel; this.text = text; this.accent = accent;
        }
    }

    private final Theme dark = new Theme("#1e1e1e", "#2a2a2a", "#f0f0f0", "#00aaff");
    private final Theme light = new Theme("#ffffff", "#f3f3f3", "#111111", "#0066cc");

    @Override
    public void start(Stage stage) {
        stage.setTitle("Wedaj - Your Mood Companion");

        // Chat history
        ListView<String> lstChat = new ListView<>();

        // Input field
        TextField txtInput = new TextField();
        txtInput.setPromptText("How are you feeling today?");
        txtInput.setFont(Font.font("Segoe UI", 14));

        // Buttons
        Button btnSend = new Button("Send");
        Button btnClear = new Button("Clear Chat");

        // Mood indicator
        Label lblMood = new Label("Mood: —");
        lblMood.setFont(Font.font("Segoe UI", 16));

        // Signup/Login fields
        TextField txtEmail = new TextField();
        txtEmail.setPromptText("email");
        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("password");

        Button btnSignup = new Button("Sign Up");
        Button btnLogin = new Button("Login");
        Button btnLogout = new Button("Logout");
        Label lblAuthStatus = new Label();

        VBox signupBox = new VBox(6,
                new Label("Account"),
                new HBox(8, txtEmail, txtPassword),
                new HBox(8, btnSignup, btnLogin, btnLogout),
                lblAuthStatus
        );
        signupBox.setPadding(new Insets(10));

        // Dark/light mode toggle
        ToggleButton toggleTheme = new ToggleButton("Dark Mode");
        toggleTheme.setSelected(true); // default dark

        // Layout
        HBox inputBar = new HBox(8, txtInput, btnSend, btnClear, toggleTheme);
        inputBar.setPadding(new Insets(10));

        VBox topBox = new VBox(8, new Label("Wedaj Chatbot 😊"), lblMood, signupBox);
        topBox.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setTop(topBox);
        root.setCenter(lstChat);
        root.setBottom(inputBar);

        Scene scene = new Scene(root, 780, 520);
        stage.setScene(scene);

        // Apply default theme
        applyTheme(root, lstChat, txtInput, btnSend, btnClear, toggleTheme, signupBox, lblAuthStatus, lblMood, dark);

        stage.show();

        // Actions
        btnSend.setOnAction(e -> {
            String userText = txtInput.getText();
            if (userText == null || userText.isBlank()) return;

            ChatBot.Result result = chatBot.reply(userText);
            lstChat.getItems().add("You: " + userText);
            lstChat.getItems().add("Wedaj AI: " + result.text);
            lblMood.setText("Mood: " + result.mood);
            txtInput.clear();
        });

        btnClear.setOnAction(e -> {
            lstChat.getItems().clear();
            lblMood.setText("Mood: —");
        });

        btnSignup.setOnAction(e -> {
            var status = signupManager.signup(txtEmail.getText(), txtPassword.getText());
            lblAuthStatus.setText(status == SignupManager.Status.SUCCESS
                    ? "Signup successful! Logged in as: " + signupManager.currentUser()
                    : "Signup failed. Username may already exist or input invalid.");
        });

        btnLogin.setOnAction(e -> {
            var status = signupManager.login(txtEmail.getText(), txtPassword.getText());
            lblAuthStatus.setText(status == SignupManager.Status.SUCCESS
                    ? "Login successful! Welcome back: " + signupManager.currentUser()
                    : "Login failed. Wrong credentials or invalid input.");
        });

        btnLogout.setOnAction(e -> {
            var status = signupManager.logout();
            lblAuthStatus.setText(status == SignupManager.Status.SUCCESS
                    ? "Logged out."
                    : "No user logged in.");
        });

        toggleTheme.setOnAction(e -> {
            if (toggleTheme.isSelected()) {
                toggleTheme.setText("Dark Mode");
                applyTheme(root, lstChat, txtInput, btnSend, btnClear, toggleTheme, signupBox, lblAuthStatus, lblMood, dark);
            } else {
                toggleTheme.setText("Light Mode");
                applyTheme(root, lstChat, txtInput, btnSend, btnClear, toggleTheme, signupBox, lblAuthStatus, lblMood, light);
            }
        });
    }

    private void applyTheme(BorderPane root,
                            ListView<String> lstChat,
                            TextField txtInput,
                            Button btnSend,
                            Button btnClear,
                            ToggleButton toggleTheme,
                            VBox signupBox,
                            Label lblAuthStatus,
                            Label lblMood,
                            Theme theme) {

        root.setBackground(new Background(new BackgroundFill(Color.web(theme.bg), CornerRadii.EMPTY, Insets.EMPTY)));
        signupBox.setBackground(new Background(new BackgroundFill(Color.web(theme.panel), new CornerRadii(8), Insets.EMPTY)));

        lblMood.setTextFill(Color.web(theme.text));
        lblAuthStatus.setTextFill(Color.web(theme.text));

        lstChat.setStyle("-fx-control-inner-background: " + theme.bg + "; -fx-text-fill: " + theme.text + ";");

        txtInput.setStyle("-fx-background-color: transparent; -fx-border-color: " + theme.accent +
                "; -fx-border-radius: 10; -fx-text-fill: " + theme.text + "; -fx-padding: 6 10 6 10;");

        btnSend.setStyle("-fx-background-color: " + theme.accent + "; -fx-text-fill: white; -fx-border-radius: 10;");
        btnClear.setStyle("-fx-background-color: " + theme.panel + "; -fx-text-fill: " + theme.text + "; -fx-border-radius: 10;");
        toggleTheme.setStyle("-fx-background-color: " + theme.panel + "; -fx-text-fill: " + theme.text + ";");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
