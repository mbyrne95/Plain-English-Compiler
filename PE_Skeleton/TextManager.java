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
        if ( position >= text.length() ) {
            return '~';    // TODO: ~ tilde should be safe and unused in PE - consider something more pragmatic
        }
        char c = text.charAt(position);
        position++;
        return c;
    }

    public char peekCharacter() {
        if ( position >= text.length() ) {
            return '~';    // TODO: ~ tilde should be safe and unused in PE - consider something more pragmatic
        }
        return text.charAt(position);
    }
}

