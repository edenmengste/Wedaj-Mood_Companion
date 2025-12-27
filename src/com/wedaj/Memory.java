package com.wedaj;

import java.util.HashMap;
import java.util.Map;

public class Memory {
    private Map<String, String> userFacts;
    
    public Memory() {
        this.userFacts = new HashMap<>();
    }
    
    public void checkForPersonalInfo(String input) {
        input = input.toLowerCase();
        
        // Extract name
        if (input.contains("my name is")) {
            String[] parts = input.split("my name is");
            if (parts.length > 1) {
                userFacts.put("name", parts[1].trim());
            }
        } else if (input.contains("i'm") || input.contains("i am")) {
            String[] parts = input.split("i'm|i am");
            if (parts.length > 1) {
                userFacts.put("name", parts[1].trim());
            }
        }
        
        // Extract favorite color
        if (input.contains("favorite color") && input.contains("is")) {
            String[] parts = input.split("is");
            if (parts.length > 1) {
                userFacts.put("favoriteColor", parts[parts.length-1].trim());
            }
        }
        
        // Extract age
        if (input.contains("i am") && input.contains("years old")) {
            String[] words = input.split(" ");
            for (int i = 0; i < words.length; i++) {
                if (words[i].matches("\\d+") && i+1 < words.length && 
                    words[i+1].equals("years")) {
                    userFacts.put("age", words[i]);
                    break;
                }
            }
        }
    }
    
    public String getUserName() {
        return userFacts.get("name");
    }
    
    public String getFavoriteColor() {
        return userFacts.get("favoriteColor");
    }
    
    public String getAge() {
        return userFacts.get("age");
    }
    
    public Map<String, String> getAllFacts() {
        return new HashMap<>(userFacts);
    }
    
    public void clearMemory() {
        userFacts.clear();
    }
}