package core;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

public class MoodAnalyzer {
    private final StanfordCoreNLP pipeline;


    public MoodAnalyzer() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,parse,sentiment");
        pipeline = new StanfordCoreNLP(props);
    }

    public Mood detectMood(String message) {
        if (message == null || message.isBlank()) return Mood.Unknown;

        // Gibberish check
        if (message.matches("[^a-zA-Z0-9 ]{3,}")) {
            return Mood.Unknown;
        }

        String t = normalize(message);

        // Prioritize grief/loss keywords
        if (containsAny(t, new String[]{
                "died", "die", "loss", "passed away", "funeral", "grief", "mourning",
                "my dog died", "my cat died"})) {
            return Mood.Sad;
        }

        boolean hasHappy = hasWord(t, "happy") || hasWord(t, "glad");
        boolean hasSad = hasWord(t, "sad") || containsAny(t, new String[]{"not happy", "crash", "loss", "grief"});
        boolean hasAngry = containsAny(t, new String[]{"angry", "mad", "furious", "upset"});

        // Detect conflicting emotions
        if ((hasHappy && hasSad) || (hasHappy && hasAngry) || (hasSad && hasAngry)) {
            return Mood.Conflicted;
        }

        if (hasSad) return Mood.Sad;
        if (hasHappy) return Mood.Happy;
        if (hasAngry) return Mood.Angry;

        // Greeting keywords
        if (containsAny(t, new String[]{
                "hi", "hello", "hey", "yo", "sup", "greetings",
                "good morning", "good afternoon", "good evening"})) {
            return Mood.Greeting;
        }

        // Tired keywords
        if (containsAny(t, new String[]{
                "tired", "exhausted", "sleepy", "drained", "low energy",
                "worn out", "yawning"})) {
            return Mood.Tired;
        }

        // Anxious keywords
        if (containsAny(t, new String[]{
                "anxious", "worried", "stressed", "nervous",
                "afraid", "fearful", "tense"})) {
            return Mood.Anxious;
        }

        // Step 2: CoreNLP sentiment analysis
        try {
            Annotation annotation = new Annotation(message);
            pipeline.annotate(annotation);

            List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
            if (sentences == null || sentences.isEmpty()) return Mood.Neutral;

            int score = 0;
            for (CoreMap sentence : sentences) {
                String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
                if (sentiment == null) continue;

                switch (sentiment) {
                    case "Very Positive": score += 2; break;
                    case "Positive":      score += 1; break;
                    case "Neutral":       score += 0; break;
                    case "Negative":      score -= 2; break;
                    case "Very Negative": score -= 3; break;
                }
            }

            if (score >= 2) return Mood.Happy;
            if (score == 1) return Mood.Excited;
            if (score == 0) return fallbackMood(message);
            if (score == -1) return Mood.Sad;
            if (score <= -2) return Mood.Angry;

            return Mood.Neutral;
        } catch (Exception e) {
            return Mood.Neutral;
        }
    }

    private String normalize(String s) {
        s = s.toLowerCase();
        s = s.replaceAll("[’‘`]", "'");        // normalize apostrophes
        s = s.replaceAll("[^a-z0-9' ]+", " "); // strip punctuation
        s = s.replaceAll("\\s+", " ").trim();
        return s;
    }

    private boolean hasWord(String text, String word) {
        return Pattern.compile("\\b" + Pattern.quote(word) + "\\b").matcher(text).find();
    }

    private boolean containsAny(String text, String[] keys) {
        for (String k : keys) {
            if (hasWord(text, k)) return true;
        }
        return false;
    }

    private Mood fallbackMood(String message) {
        String t = normalize(message);
        if (containsAny(t, new String[]{"hi", "hello", "hey", "yo", "sup"})) return Mood.Greeting;
        if (containsAny(t, new String[]{"tired", "exhausted", "sleepy", "drained"})) return Mood.Tired;
        if (containsAny(t, new String[]{"worried", "anxious", "stressed"})) return Mood.Anxious;
        if (containsAny(t, new String[]{"sad", "died", "die", "loss", "depressed","grief", "brokeup"})) return Mood.Sad;
        if (containsAny(t, new String[]{"happy", "glad"})) return Mood.Happy;
        return Mood.Neutral;
    }
}
