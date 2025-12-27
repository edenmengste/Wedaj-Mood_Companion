package com.wedaj;

import java.util.Arrays;
import java.util.List;

public class MoodAnalyzer {
    private final List<String> positiveWords = Arrays.asList(
        "happy", "good", "great", "awesome", "excellent", 
        "wonderful", "love", "like", "joy", "amazing",
        "fantastic", "perfect", "beautiful", "nice", "cool"
    );
    
    private final List<String> negativeWords = Arrays.asList(
        "sad", "bad", "terrible", "awful", "hate", 
        "angry", "upset", "depressed", "lonely", "miserable",
        "horrible", "worst", "sick", "tired", "bored"
    );
    
    private final List<String> angryWords = Arrays.asList(
        "angry", "mad", "furious", "rage", "annoyed",
        "frustrated", "irritated", "hate", "dislike"
    );
    
    public String analyzeMood(String input) {
        input = input.toLowerCase();
        int positiveCount = 0;
        int negativeCount = 0;
        int angryCount = 0;
        
        // Count positive words
        for (String word : positiveWords) {
            if (input.contains(word)) {
                positiveCount++;
            }
        }
        
        // Count negative words
        for (String word : negativeWords) {
            if (input.contains(word)) {
                negativeCount++;
            }
        }
        
        // Count angry words
        for (String word : angryWords) {
            if (input.contains(word)) {
                angryCount++;
            }
        }
        
        // Determine mood based on counts
        if (angryCount > 0 && angryCount >= negativeCount) {
            return "angry";
        } else if (positiveCount > negativeCount) {
            return "happy";
        } else if (negativeCount > positiveCount) {
            return "sad";
        } else {
            return "neutral";
        }
    }
    
    // Get mood score for more advanced analysis
    public int getMoodScore(String input) {
        input = input.toLowerCase();
        int score = 0;
        
        for (String word : positiveWords) {
            if (input.contains(word)) {
                score += 2;
            }
        }
        
        for (String word : negativeWords) {
            if (input.contains(word)) {
                score -= 2;
            }
        }
        
        for (String word : angryWords) {
            if (input.contains(word)) {
                score -= 3;
            }
        }
        
        return score;
    }
}