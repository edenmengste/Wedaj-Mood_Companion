package core;

public class ChatBot {
    private final MoodAnalyzer analyzer = new MoodAnalyzer();
    private final Memory memory = new Memory();
    private final ResponseGenerator generator = new ResponseGenerator();

    public String processMessage(String userMessage) {
        memory.captureFacts(userMessage);                
        Mood mood = analyzer.detectMood(userMessage);   
        MoodResponse responder = generator.getResponse(mood); 
        String reply = responder.reply();              
        String memoryInfo = memory.recall();          
        return reply + (memoryInfo.isEmpty() ? "" : "\n" + memoryInfo);
    }
}
