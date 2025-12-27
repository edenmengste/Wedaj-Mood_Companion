package com.wedaj;

public class ChatBot {
    private Memory memory;
    private MoodAnalyzer moodAnalyzer;
    private ResponseGenerator responseGenerator;
    
    public ChatBot() {
        this.memory = new Memory();
        this.moodAnalyzer = new MoodAnalyzer();
        this.responseGenerator = new ResponseGenerator();
    }
    
    public String processMessage(String userInput) {
        // Analyze mood
        String mood = moodAnalyzer.analyzeMood(userInput);
        
        // Check for personal info
        memory.checkForPersonalInfo(userInput);
        
        // Generate response
        return responseGenerator.generateResponse(mood, memory);
    }
    
    // Getters for testing purposes
    public Memory getMemory() {
        return memory;
    }
    
    public MoodAnalyzer getMoodAnalyzer() {
        return moodAnalyzer;
    }
    
    public ResponseGenerator getResponseGenerator() {
        return responseGenerator;
    }
}