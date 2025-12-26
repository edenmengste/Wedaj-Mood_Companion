public class ChatBot {

    // This creates a small container to hold both the text and the mood
    public static class Result {
        public String text;
        public String mood;

        public Result(String text, String mood) {
            this.text = text;
            this.mood = mood;
        }
    }

    public Result reply(String userText) {
        String input = userText.toLowerCase();
        
        if (input.contains("hello") || input.contains("hi")) {
            return new Result("Hello! How are you feeling today?", "Happy 😊");
        } else if (input.contains("sad") || input.contains("bad")) {
            return new Result("I'm sorry to hear that. I'm here for you.", "Sympathetic 😔");
        } else {
            return new Result("Tell me more about that!", "Curious 🤔");
        }
    }
}