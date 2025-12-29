package core;

import java.util.Random;

public class ResponseGenerator {
    private final Random random = new Random();

    public static class MoodResponse {
        private final String reply;
        public MoodResponse(String reply) { this.reply = reply; }
        public String reply() { return reply; }
    }

    // Arrays of varied responses
    private final String[] greetings = {
        "Hello! How are you feeling today?", 
        "Hey there! What's on your mind?", 
        "Hi! I'm here to listen. How's your day going?"
    };

    private final String[] happyReplies = {
        "That's wonderful! Your energy is contagious.",
        "I'm so glad to hear that! What made your day so bright?",
        "That's great! Keep that positive vibe going."
    };

    private final String[] sadReplies = {
        "I'm sorry you're feeling this way. I'm here for you.",
        "It's okay to feel sad. Do you want to vent about it?",
        "I hear you. Take all the time you need to process things."
    };

    private final String[] angryReplies = {
        "I can tell you're frustrated. It's valid to feel that way.",
        "That sounds really upsetting. Let's work through it.",
        "I'm here to listen if you want to let it all out."
    };

    private final String[] neutralReplies = {
        "I see. Tell me more about that.",
        "Interesting. How does that affect you?",
        "I'm following. Go on..."
    };

    public MoodResponse getResponse(Mood mood, String userMessage) {
        String response;

        switch (mood) {
            case Greeting: response = pick(greetings); break;
            case Happy:    response = pick(happyReplies); break;
            case Sad:      response = pick(sadReplies); break;
            case Angry:    response = pick(angryReplies); break;
            case Excited:  response = "That's awesome! Your excitement is amazing!"; break;
            case Neutral:  response = pick(neutralReplies); break;
            case Unknown:  response = "I didn't quite catch that. Could you explain it differently?"; break;
            default:       response = "I'm listening. Tell me more."; break;
        }

        return new MoodResponse(response);
    }

    // Helper method to pick a random string from an array
    private String pick(String[] options) {
        return options[random.nextInt(options.length)];
    }
}