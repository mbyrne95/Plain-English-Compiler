package PlainEnglish;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Lexer    {

    private final HashMap<String, Token.TokenTypes> keywords;
    private final HashMap<String, Token.TokenTypes> punctuation;    // for later
    private final TextManager tm;
    private enum State {
        ENTRY,
        WORD,
        NUMBER,
    }

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


    public List<Token> lex() {
        State CurrentState = State.ENTRY;
        List<Token> ListOfTokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        String CurrentWord = "";

        int ColumnNumber = 0;
        int LineNumber = 0;
        int TokenStartColumn = 0;
        int TokenStartLine = 0;

        while(tm.peekCharacter() != '~') {
            char c = tm.peekCharacter();
            switch(CurrentState){
                case ENTRY:
                    if (c == ' '){
                        tm.getCharacter();
                        ColumnNumber += 1;
                        break;
                    }
                    // newline token for a newline
                    if (c == '\n'){
                        tm.getCharacter();
                        ListOfTokens.add(new Token(Token.TokenTypes.NEWLINE, ColumnNumber,LineNumber));
                        LineNumber += 1;
                        ColumnNumber = 0;
                        break;
                    }
                    // we hit a new word, starting with a letter
                    if (Character.isLetter(c)) {
                        TokenStartLine = LineNumber;
                        TokenStartColumn = ColumnNumber;
                        CurrentState = State.WORD;
                        sb.append(tm.getCharacter());
                        ColumnNumber += 1;
                        break;
                    }

                    // we hit a new digit
                    if (Character.isDigit(c)){
                        CurrentState = State.NUMBER;
                        sb.append(tm.getCharacter());
                        break;
                    }
                case WORD:
                    // if we hit a space or a \n, the word or identifier is complete
                    // so we should pass it on to readWord()
                    if (c == ' ' || c == '\n'){
                        ListOfTokens.add( readWord(sb.toString(), TokenStartLine, TokenStartColumn) );  // send as lowercase for consistency
                        sb.setLength(0); // reset stringbuilder
                        CurrentState = State.ENTRY;
                        break;
                    }
                    // we're still in a word, so just concat the stringbuilder
                    if (Character.isLetter(c) || Character.isDigit(c)) {
                        sb.append(tm.getCharacter());
                        ColumnNumber += 1;
                        break;
                    }

                case NUMBER:
                    break;
            }
        }
        // if we're at the end of the file, we need to recheck to make sure that we didn't miss anything
        if ( CurrentState == State.WORD && !sb.isEmpty() ){
            ListOfTokens.add( readWord(sb.toString(), TokenStartLine, TokenStartColumn) );
        }
        // TODO: same for numbers
        if ( CurrentState == State.NUMBER && !sb.isEmpty() ){
            ListOfTokens.add( readNumber(sb.toString(), TokenStartLine, TokenStartColumn) );
        }

        return ListOfTokens;
    }

    private Token readWord(String token, int StartLine, int StartPosition) {
        String t = token.toLowerCase(); // lowercase for comparing to hashmap
        //check if the word is a token type, return that token type if so
        if (keywords.containsKey(t)){
            return new Token(keywords.get(token), StartLine, StartPosition);
        }

        // for now just assume it's an identifier if we didn't get a real word
        return new Token(Token.TokenTypes.IDENTIFIER, StartLine, StartPosition, token);
    }

    // TODO: implement this
    private Token readNumber(String token, int StartLine, int StartPosition) {
        return new Token(Token.TokenTypes.A, StartLine, StartPosition);
    }
}

