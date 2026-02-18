package PlainEnglish;

public class TextManager
{
    private final /*readonly*/ String text;
    private int position;

    public TextManager(String input) {
        position = 0;
        text = input;
    }

    public char getCharacter() {
        if ( isAtEnd() ) {
            return '~';
        }
        char c = text.charAt(position);
        position++;
        return c;
    }

    public char peekCharacter() {
        if ( isAtEnd() ) {
            return '~';
        }
        return text.charAt(position);
    }

    public char peekCharacter(int distance) {
        int i = distance + position;
        if ( i >= text.length() ) {
            return '~';
        }
        return text.charAt(i);
    }

    public boolean isAtEnd() {
        return position >= text.length();
    }


}

