package core;

import java.util.HashMap;
import java.util.Map;

public class ChatBot {
    private final MoodAnalyzer moodAnalyzer = new MoodAnalyzer();
    private final ResponseGenerator responseGenerator = new ResponseGenerator();

    // Memory per user (keyed by username/email)
    private final Map<String, Memory> userMemory = new HashMap<>();

    // Get memory for current user (creates if missing)
    public Memory getMemoryForUser(String username) {
        return userMemory.computeIfAbsent(username, k -> new Memory());
    }

    public static class Result {
        public final String text;
        public final Mood mood;

        public Result(String text, Mood mood) {
            this.text = text;
            this.mood = mood;
        }
    }

    public Result reply(String userMessage, String username) {
        // 1. Detect the mood first
        Mood mood = moodAnalyzer.detectMood(userMessage);

        // 2. Get the memory for this specific user
        Memory memory = getMemoryForUser(username);
        
        // 3. Let memory try to capture a fact or prepare a recall answer
        memory.captureFacts(userMessage);
        String recalledFact = memory.recall();

        // 4. Logic: If memory has a specific answer 
        if (!recalledFact.isEmpty()) {
            return new Result(recalledFact, mood);
        }

        // 5. Fallback: If no memory fact was triggered, use the standard generator
        ResponseGenerator.MoodResponse response = responseGenerator.getResponse(mood, userMessage);
        return new Result(response.reply(), mood);
    }
}