package core;

import java.util.HashMap;
import java.util.Map;

public class Memory {
    private final Map<String, String> store = new HashMap<>();
    private String lastReply = "";

    public void captureFacts(String message) {
        if (message == null || message.isBlank()) return;

        // Use t for comparisons, but we extract from the original message casing
        String t = message.toLowerCase().trim();

        // Robust name detection
        if (t.contains("my name is ")) {
            int index = t.indexOf("my name is ") + "my name is ".length();
            String name = message.substring(index).trim();
            store.put("name", name);
            lastReply = "Nice to meet you, " + name + " 🙂";
            return;
        }

        // Robust "likes" detection
        if (t.contains("i like ")) {
            int index = t.indexOf("i like ") + "i like ".length();
            String like = message.substring(index).trim();
            store.put("like", like);
            lastReply = "Got it. I’ll remember you like " + like + " 📝";
            return;
        }

        // Question handling
        if (t.contains("what") && t.contains("my name")) {
            String n = store.get("name");
            lastReply = (n != null)
                    ? "You told me your name is " + n + " 🙂"
                    : "I don’t know your name yet. What is it?";
            return;
        }

        if (t.contains("what") && t.contains("i like")) {
            String l = store.get("like");
            lastReply = (l != null)
                    ? "You mentioned you like " + l + " 🌟"
                    : "I don’t recall you mentioning your interests yet.";
            return;
        }

        if (t.equals("reset memory") || t.contains("forget everything")) {
            store.clear();
            lastReply = "Memory reset 🧹";
            return;
        }

        lastReply = "";
    }

    public String recall() {
        return lastReply;
    }
}