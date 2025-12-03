public class SadResponse extends MoodResponse 
{
    @Override
    public String reply() 
    {
        return "😢 I'm sorry you're feeling down. Things will get better!";
    }
}