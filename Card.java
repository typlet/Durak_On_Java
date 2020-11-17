package Anton.Davidsson.ed;

public class Card{
    private String name;
    private int value;
    private char mark;
    
    public Card(String name, int value, char mark)
    {
        this.name = name;
        this.value = value;
        this.mark = mark;
    }
    
    public String getName()
    {
        return name;
    }
    
    public int getValue()
    {
        return value;
    }
    
    public char getMark()
    {
        return mark;
    }
    
    @Override
    public String toString()
    {
        return "{"+name+mark+"}";
    }
}
