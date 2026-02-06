package PlainEnglish;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Lexer    {
    public Lexer(String input) {
        tm = new TextManager(input);

        // create the hashmap of string to token types
        keywords = new HashMap<>();
        keywords.put("to", Token.TokenTypes.TO);
        keywords.put("a", Token.TokenTypes.A);
        keywords.put("with", Token.TokenTypes.WITH);
        keywords.put("named", Token.TokenTypes.NAMED);
        keywords.put("an", Token.TokenTypes.AN);
        keywords.put("is", Token.TokenTypes.IS);
        keywords.put("if", Token.TokenTypes.IF);
        keywords.put("else", Token.TokenTypes.ELSE);
        keywords.put("loop", Token.TokenTypes.LOOP);
        keywords.put("set", Token.TokenTypes.SET);
        keywords.put("make", Token.TokenTypes.MAKE);
        keywords.put("of", Token.TokenTypes.OF);
        keywords.put("true", Token.TokenTypes.TRUE);
        keywords.put("false", Token.TokenTypes.FALSE);
        keywords.put("and", Token.TokenTypes.AND);
        keywords.put("or", Token.TokenTypes.OR);
        keywords.put("not", Token.TokenTypes.NOT);

        // for later
        punctuation = new HashMap<>();
    }

    private final HashMap<String, Token.TokenTypes> keywords;
    private final HashMap<String, Token.TokenTypes> punctuation;    // for later
    private final TextManager tm;

    public List<Token> lex() {
        List<Token> ListOfTokens = new ArrayList<>();
        String CurrentWord = "";

        while(tm.peekCharacter() != '~') {
            char c = tm.peekCharacter();

            // ignore spaces
            if (c == ' '){
                tm.getCharacter();
                continue;
            }

            // newline token for a newline
            if (c == '\n'){
                //TODO: use actual line/col instead of (0,0)
                tm.getCharacter();
                ListOfTokens.add(new Token(Token.TokenTypes.NEWLINE, 0,0));
                continue;
            }

            if (Character.isLetter(c)){
                ListOfTokens.add(readWord());
                continue;
            }

            if (Character.isDigit(c)){
                ListOfTokens.add(readNumber());
                continue;
            }

            //if we somehow get here we should probably just ignore and keep going
            tm.getCharacter();
        }

        return ListOfTokens;
    }

    private Token readWord() {
        // loop while the text manager is both not done and still on letters
        char c = tm.peekCharacter();

        // use stringbuilder instead of concating strings, more efficient
        StringBuilder sb = new StringBuilder();
        while( c != '~' &&  Character.isLetter(c) ){
            sb.append(tm.getCharacter());
            c = tm.peekCharacter();
        }
        String word = sb.toString().toLowerCase();

        //check if the word is a token type, return that token type if so
        if (keywords.containsKey(word)){
            return new Token(keywords.get(word), 0, 0);
        }

        // for now just assume it's an identifier if we didn't get a real word
        return new Token(Token.TokenTypes.IDENTIFIER, 0, 0);
    }

    // TODO: implement this
    private Token readNumber() {
        return new Token(Token.TokenTypes.A, 0, 0);
    }
}

