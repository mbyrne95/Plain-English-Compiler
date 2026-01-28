package PlainEnglish;

public class Lexer    {
    public Lexer(String input) {
        tm = new TextManager(input);
    }

    private final TextManager tm;
}

