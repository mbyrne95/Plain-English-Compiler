package PlainEnglish;

public class SyntaxErrorException extends Exception
{
    private final int LineNumber;
    private final int CharacterPosition;

    public SyntaxErrorException(String message, int LineNumber, int CharacterPosition)  
    {
        super(message);
        this.LineNumber = LineNumber;
        this.CharacterPosition = CharacterPosition;
    }

    @Override
    public /*override*/ String toString()
    {
        return "Error at line " + LineNumber + " at character "  + CharacterPosition +" at " + super.toString();
    }
} 

