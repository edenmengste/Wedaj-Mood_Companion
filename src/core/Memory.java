package core;

import java.util.HashMap;
import java.util.Map;

public class Memory {
    private final Map<String, String> store = new HashMap<>();
    private String lastReply = "";

    // Capture facts from user input
    public void captureFacts(String message) {
        if (message == null || message.isBlank()) return;

        String t = message.toLowerCase().trim();

        if (t.startsWith("my name is ")) {
            String name = message.substring(11).trim();
            store.put("name", name);
            lastReply = "Nice to meet you, " + name + " 🙂";
            return;
        }

        if (t.startsWith("remember that i like ")) {
            String like = message.substring("remember that i like ".length()).trim();
            store.put("like", like);
            lastReply = "Got it. I’ll remember you like " + like + " 📝";
            return;
        }

        if (t.equals("what's my name?") || t.equals("whats my name?")) {
            String n = store.get("name");
            lastReply = (n != null)
                    ? "You told me your name is " + n + " 🙂"
                    : "I don’t have your name yet.";
            return;
        }

        if (t.equals("what do i like?")) {
            String l = store.get("like");
            lastReply = (l != null)
                    ? "You like " + l + " 🌟"
                    : "I don’t have that yet.";
            return;
        }

        if (t.equals("reset memory")) {
            store.clear();
            lastReply = "Memory reset 🧹";
            return;
        }

        // Not a memory command
        lastReply = "";
    }

    // Recall the last memory-related reply
    public String recall() {
        return lastReply;
    }
}
