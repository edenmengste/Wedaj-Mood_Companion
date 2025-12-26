package core;

public class ResponseGenerator {
    public MoodResponse getResponse(Mood mood) {
        switch (mood) {
            case HAPPY: return new HappyResponse();
            case SAD:   return new SadResponse();
            case ANGRY: return new AngryResponse();
            default:    return new NeutralResponse();
        }
    }
}

