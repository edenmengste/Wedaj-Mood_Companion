import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        TextArea chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);

        TextField userInput = new TextField();
        userInput.setPromptText("Say something to Wedaj...");

        Button sendButton = new Button("Send");

        VBox layout = new VBox(10, chatArea, new HBox(5, userInput, sendButton));
        layout.setPrefSize(400, 300);
        Scene scene = new Scene(layout);

        sendButton.setOnAction(e -> {
            String input = userInput.getText().trim();
            if (!input.isEmpty()) {
                chatArea.appendText("You: " + input + "\n");
                String response = getWedajResponse(input);
                chatArea.appendText("Wedaj: " + response + "\n\n");
                userInput.clear();
            }
        });

        userInput.setOnAction(sendButton.getOnAction());

        primaryStage.setTitle("Wedaj Chat Bot");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String getWedajResponse(String input) {
        if (input.toLowerCase().contains("hello")) {
            return "Hi there! How can I help you today?";
        } else if (input.toLowerCase().contains("bye")) {
            return "Goodbye! Talk to you soon.";
        } else {
            return "I'm still learning. Can you say that another way?";
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}