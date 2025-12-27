package com.wedaj;

import java.util.*;

public class ResponseGenerator {
    private Map<String, List<String>> responses;
    private Random random;
    
    public ResponseGenerator() {
        this.responses = new HashMap<>();
        this.random = new Random();
        initializeResponses();
    }
    
    private void initializeResponses() {
        // Happy responses
        responses.put("happy", Arrays.asList(
            "That's wonderful to hear! 😊",
            "I'm so glad you're feeling happy! 🌟",
            "Your positivity is contagious! ✨",
            "Awesome! Keep spreading the joy! 🎉",
            "Happiness looks good on you! 🌈",
            "That makes me happy too! 😄"
        ));
        
        // Sad responses
        responses.put("sad", Arrays.asList(
            "I'm sorry you're feeling this way. 💔",
            "It's okay to feel sad sometimes. 🌧️",
            "I'm here for you. Would you like to talk about it? 🤗",
            "Remember, tomorrow is a new day. 🌈",
            "Take your time. I'm listening. 👂",
            "Sending you virtual hugs! 🫂"
        ));
        
        // Angry responses
        responses.put("angry", Arrays.asList(
            "I understand you're feeling angry. 😠",
            "Take a deep breath. You've got this! 🌬️",
            "Would you like to talk about what's bothering you? 💬",
            "Sometimes it helps to express your feelings. 🗣️",
            "I'm here to listen without judgment. 👂",
            "Let's try to find a calm space. 🧘"
        ));
        
        // Neutral responses
        responses.put("neutral", Arrays.asList(
            "How are you feeling today? 🤔",
            "Tell me more about your day! 💬",
            "I'm here to listen. What's on your mind? 👂",
            "How can I be a good companion today? 🤝",
            "What would you like to talk about? 💭",
            "I'm always here for a chat! 💬"
        ));
        
        // Greeting responses
        responses.put("greeting", Arrays.asList(
            "Hello! How are you today? 😊",
            "Hi there! Nice to see you! 👋",
            "Welcome back! How have you been? 🌟",
            "Hey! Ready for a chat? 💬"
        ));
    }
    
    public String generateResponse(String mood, Memory memory) {
        // Check for greetings
        String response;
        if (mood.equals("neutral") && memory.getUserName() == null) {
            response = responses.get("greeting").get(random.nextInt(responses.get("greeting").size()));
        } else {
            List<String> moodResponses = responses.getOrDefault(mood, responses.get("neutral"));
            response = moodResponses.get(random.nextInt(moodResponses.size()));
        }
        
        // Personalize response with user info
        response = personalizeResponse(response, memory);
        
        return response;
    }
    
    private String personalizeResponse(String response, Memory memory) {
        String userName = memory.getUserName();
        String favoriteColor = memory.getFavoriteColor();
        
        if (userName != null && random.nextInt(3) == 0) { // 33% chance to use name
            if (!response.startsWith(userName)) {
                response = userName + ", " + response.toLowerCase();
            }
        }
        
        if (favoriteColor != null && random.nextInt(5) == 0) { // 20% chance to mention color
            response += " By the way, " + favoriteColor + " is a beautiful color! 🎨";
        }
        
        return response;
    }
    
    public void addCustomResponse(String mood, String response) {
        responses.computeIfAbsent(mood, k -> new ArrayList<>()).add(response);
    }
}