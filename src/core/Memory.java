package core;

import java.util.HashMap;
import java.util.Map;

public class Memory {
    private final Map<String, String> store = new HashMap<>();
    private String lastReply = "";

    public void captureFacts(String message) {
        if (message == null || message.isBlank()) return;

        String t = message.toLowerCase().trim();

        // 1. NAME DETECTION
        if (t.contains("my name is ")) {
            int index = t.indexOf("my name is ") + "my name is ".length();
            String name = message.substring(index).trim();
            store.put("name", name);
            lastReply = "Nice to meet you, " + name + " 🙂";
            return;
        }

        // 2. AGE DETECTION (Handles "I am 22 years old" or "I am 22")
        if (t.matches(".*i am \\d+.*") || t.contains("years old")) {
            // Regex to find the number in the string
            String age = message.replaceAll("[^0-9]", "");
            if (!age.isEmpty()) {
                store.put("age", age);
                lastReply = "Got it! You are " + age + " years old. 🎂";
                return;
            }
        }

        // 3. HOBBY DETECTION
        if (t.contains("my hobby is ") || t.contains("i love to ") || t.contains("i enjoy ")) {
            String prefix = t.contains("my hobby is ") ? "my hobby is " : 
                            t.contains("i love to ") ? "i love to " : "i enjoy ";
            
            int index = t.indexOf(prefix) + prefix.length();
            String hobby = message.substring(index).trim();
            store.put("hobby", hobby);
            lastReply = "That sounds fun! I'll remember that you enjoy " + hobby + " 🎯";
            return;
        }

        // 4. LIKES DETECTION
        if (t.contains("i like ")) {
            int index = t.indexOf("i like ") + "i like ".length();
            String like = message.substring(index).trim();
            store.put("like", like);
            lastReply = "Got it. I’ll remember you like " + like + " 📝";
            return;
        }

        // --- QUESTION HANDLING ---

        // "What is my name?"
        if (t.contains("what") && t.contains("my name")) {
            String n = store.get("name");
            lastReply = (n != null) ? "You told me your name is " + n + " 🙂" : "I don’t know your name yet.";
            return;
        }

        // "How old am I?"
        if (t.contains("how old") && t.contains("am i")) {
            String a = store.get("age");
            lastReply = (a != null) ? "You are " + a + " years old. 🎂" : "I don't know your age yet.";
            return;
        }

        // "What is my hobby?"
        if (t.contains("what") && (t.contains("hobby") || t.contains("hobbies"))) {
            String h = store.get("hobby");
            lastReply = (h != null) ? "You enjoy " + h + " 🎯" : "I haven't learned about your hobbies yet.";
            return;
        }

        // "What do I like?"
        if (t.contains("what") && t.contains("i like")) {
            String l = store.get("like");
            lastReply = (l != null) ? "You mentioned you like " + l + " 🌟" : "I don’t recall your likes yet.";
            return;
        }

        // Reset
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