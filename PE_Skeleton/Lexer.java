package PlainEnglish;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Lexer    {

    private final HashMap<String, Token.TokenTypes> keywords;
    private final HashMap<String, Token.TokenTypes> punctuation;    // for later
    private final TextManager tm;
    private enum State {
        ENTRY,
        WORD,
        NUMBER,
        STRING_LITERAL,
        COMMENT,
        PUNCTUATION,
        START_OF_NEWLINE
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

        punctuation = new HashMap<>();
        punctuation.put(",", Token.TokenTypes.COMMA);
        punctuation.put("+", Token.TokenTypes.PLUS);
        punctuation.put("-", Token.TokenTypes.HYPHEN);
        punctuation.put("*", Token.TokenTypes.ASTERISK);
        punctuation.put("/", Token.TokenTypes.SLASH);
        punctuation.put("%", Token.TokenTypes.PERCENT);
        punctuation.put("(", Token.TokenTypes.OPENPAREN);
        punctuation.put(")", Token.TokenTypes.CLOSEPAREN);
        punctuation.put("==", Token.TokenTypes.DOUBLEEQUAL);
        punctuation.put("!=", Token.TokenTypes.NOTEQUAL);
        punctuation.put("<=", Token.TokenTypes.LESSTHANEQUAL);
        punctuation.put(">=", Token.TokenTypes.GREATERTHANEQUAL);
        punctuation.put(">", Token.TokenTypes.GREATERTHAN);
        punctuation.put("<", Token.TokenTypes.LESSTHAN);
    }

    boolean debug = false;

    public LinkedList<Token> lex() throws SyntaxErrorException {
        State CurrentState = State.ENTRY;
        LinkedList<Token> ListOfTokens = new LinkedList<>();
        StringBuilder sb = new StringBuilder();
        String CurrentWord = "";

        int startPosition = 1;
        int ColumnNumber = startPosition;
        int LineNumber = startPosition;
        int TokenStartColumn = startPosition;
        int TokenStartLine = startPosition;
        int currentIndentLevel = 0;

        while( !tm.isAtEnd() ) {
            char c = tm.peekCharacter();
            if (c == '\r') {
                tm.getCharacter();
                continue;
            }
            if (debug)
                System.out.println("State: " + CurrentState + ", Char: '" + c + "', Line: " + LineNumber + ", Col: " + ColumnNumber);
            switch(CurrentState){
                case ENTRY:
                    if (c == ' '){
                        tm.getCharacter();
                        ColumnNumber += 1;
                        break;
                    }
                    //string literal entry condition
                    if (c == '"') {
                        tm.getCharacter();  // dont append the string with the double quote, but push the tm forward
                        ColumnNumber += 1;
                        TokenStartLine = LineNumber;
                        TokenStartColumn = ColumnNumber;
                        CurrentState = State.STRING_LITERAL;
                        break;
                    }
                    // newline token for a newline
                    if (c == '\n'){
                        ListOfTokens.add(new Token(Token.TokenTypes.NEWLINE, ColumnNumber,LineNumber));
                        tm.getCharacter();
                        LineNumber += 1;
                        ColumnNumber = startPosition;
                        CurrentState = State.START_OF_NEWLINE;
                        break;
                    }
                    // word entry condition
                    if (Character.isLetter(c)) {
                        sb.append(tm.getCharacter());
                        ColumnNumber += 1;
                        TokenStartLine = LineNumber;
                        TokenStartColumn = ColumnNumber;
                        CurrentState = State.WORD;
                        break;
                    }
                    // number entry condition
                    if (Character.isDigit(c)){
                        sb.append(tm.getCharacter());
                        ColumnNumber += 1;
                        TokenStartLine = LineNumber;
                        TokenStartColumn = ColumnNumber;
                        CurrentState = State.NUMBER;
                        break;
                    }
                    //punctuation entry condition
                    if (c == ',' || c == '+' || c == '-' || c == '*' || c == '/' || c == '%' || c == '(' ||
                            c == ')' || c == '=' || c == '!' || c == '<' || c == '>' ) {
                        TokenStartLine = LineNumber;
                        TokenStartColumn = ColumnNumber;
                        CurrentState = State.PUNCTUATION;
                        sb.append(tm.getCharacter());
                        ColumnNumber += 1;
                        break;
                    }
                    break;
                case START_OF_NEWLINE:
                    // started a new line and have immediate space
                    if (c == ' ') {
                        if (debug)
                            System.out.println("About to handle indent. currentIndentLevel=" + currentIndentLevel);
                        TokenStartLine = LineNumber;
                        TokenStartColumn = ColumnNumber;
                        // count the spaces thereafter
                        int spaces = 0;
                        while (tm.peekCharacter(spaces) == ' ') {
                            spaces += 1;
                        }
                        // consume the spaces
                        for (int i = 0; i < spaces; i++) {
                            ColumnNumber += 1;
                            tm.getCharacter();
                        }

                        if (spaces % 4 != 0)
                            throw new SyntaxErrorException("Invalid indent level", LineNumber, ColumnNumber);
                        int level = spaces / 4;
                        if (level > currentIndentLevel) {
                            for (int i = 0; i < (level - currentIndentLevel); i++)
                                ListOfTokens.add(new Token(Token.TokenTypes.INDENT, TokenStartLine, TokenStartColumn));
                        } else {
                            for (int i = 0; i < (currentIndentLevel - level); i++)
                                ListOfTokens.add(new Token(Token.TokenTypes.DEDENT, TokenStartLine, TokenStartColumn));
                        }
                        currentIndentLevel = level;
                        CurrentState = State.ENTRY; // back to normal
                        break;
                    }
                    // new line that doesn't have anything
                    if (c == '\n'){
                        tm.getCharacter();
                        ListOfTokens.add(new Token(Token.TokenTypes.NEWLINE,ColumnNumber,LineNumber));
                        LineNumber+=1;
                        ColumnNumber = startPosition;
                        break; // stay in this state
                    }
                    // anything else, if indent level isn't zero we need to dedent until zero
                    if (currentIndentLevel > 0) {
                        for (int i = 0; i < currentIndentLevel; i++) {
                            ListOfTokens.add(new Token(Token.TokenTypes.DEDENT, LineNumber, ColumnNumber));
                        }
                        currentIndentLevel = 0;
                    }
                    CurrentState = State.ENTRY;
                    break;
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
                    // if we reach here, we hit an unrecognized character in the middle of a word and should read the word then return to entry
                    ListOfTokens.add( readWord(sb.toString(), TokenStartLine, TokenStartColumn) );
                    sb.setLength(0);
                    CurrentState = State.ENTRY;
                    break;
                case NUMBER:
                    // if we hit a space or a \n, the word or identifier is complete
                    // so we should pass it on to readWord()
                    if (c == ' ' || c == '\n'){
                        ListOfTokens.add( readNumber(sb.toString(), TokenStartLine, TokenStartColumn) );  // send as lowercase for consistency
                        sb.setLength(0); // reset stringbuilder
                        CurrentState = State.ENTRY;
                        break;
                    }
                    if (Character.isDigit(c)) {
                        sb.append(tm.getCharacter());
                        ColumnNumber += 1;
                        break;
                    }
                    if (Character.isLetter(c)) {
                        sb.append(tm.getCharacter());
                        ColumnNumber += 1;
                        throw new SyntaxErrorException("Invalid Number",LineNumber,ColumnNumber);
                    }
                    // if we reach here, we hit an unrecognized character in the middle of a word and probably should read the number then return to entry
                    ListOfTokens.add( readNumber(sb.toString(), TokenStartLine, TokenStartColumn) );
                    sb.setLength(0);
                    CurrentState = State.ENTRY;
                    break;
                case STRING_LITERAL:
                    if (c == '"') {      // end of string
                        tm.getCharacter();
                        ListOfTokens.add( readString(TokenStartLine,TokenStartColumn,sb.toString()) );
                        ColumnNumber += 1;
                        sb.setLength(0);
                        CurrentState = State.ENTRY;
                        break;
                    }
                    if (c == '\n'){
                        tm.getCharacter();
                        //ListOfTokens.add(new Token(Token.TokenTypes.NEWLINE, ColumnNumber,LineNumber));
                        //CurrentState = State.START_OF_NEWLINE;
                        LineNumber += 1;
                        ColumnNumber = 0;
                        break;
                    }
                    // all other cases we just keep going
                    sb.append(tm.getCharacter());
                    ColumnNumber += 1;
                    break;
                case PUNCTUATION:
                    // first we should check if we're starting a comment
                    if (sb.toString().equals("/") && tm.peekCharacter() == '*'){
                        sb.setLength(0);        // clear the slash we added
                        tm.getCharacter();      // clear the *
                        ColumnNumber += 1;
                        CurrentState = State.COMMENT;
                        break;
                    }
                    // next check if we're in a length two punctuation, i.e. <= >= != ==
                    // create a string that is the previous plus the current
                    String punc = sb.toString() + c;
                    if (punctuation.containsKey(punc)) {
                        tm.getCharacter();
                        ColumnNumber += 1;
                        ListOfTokens.add(new Token(punctuation.get(punc), TokenStartLine, TokenStartColumn));
                        sb.setLength(0);
                        CurrentState = State.ENTRY;
                        break;
                    }
                    // otherwise, it's a single length punctuation
                    if (punctuation.containsKey(sb.toString())) {
                        ListOfTokens.add(new Token(punctuation.get(sb.toString()), TokenStartLine, TokenStartColumn));
                        sb.setLength(0);
                        CurrentState = State.ENTRY;
                        break;
                    }
                    // if none of the above, we are in some sort of unrecognized punctuation and need to throw syntax error
                    throw new SyntaxErrorException("Unrecognized punctuation", TokenStartLine, TokenStartColumn);
                case COMMENT:
                    if (c == '\n'){
                        tm.getCharacter();
                        //ListOfTokens.add(new Token(Token.TokenTypes.NEWLINE, ColumnNumber,LineNumber));
                        LineNumber += 1;
                        ColumnNumber = startPosition;
                        //CurrentState = State.START_OF_NEWLINE;
                        break;
                    }
                    // we finished the comment
                    if (c == '*' && tm.peekCharacter(1) == '/'){
                        tm.getCharacter();
                        tm.getCharacter();
                        ColumnNumber += 2;
                        CurrentState = State.ENTRY;
                        break;
                    }
                    // otherwise we just keep chugging along
                    tm.getCharacter();
                    ColumnNumber += 1;
                    break;
            }
        }
        // if we're at the end of the file, we need to recheck to make sure that we didn't miss anything
        if ( CurrentState == State.WORD && !sb.isEmpty() ){
            ListOfTokens.add( readWord(sb.toString(), TokenStartLine, TokenStartColumn) );
        }
        if ( CurrentState == State.NUMBER && !sb.isEmpty() ){
            ListOfTokens.add( readNumber(sb.toString(), TokenStartLine, TokenStartColumn) );
        }
        if ( CurrentState == State.STRING_LITERAL)
            throw new SyntaxErrorException("Unterminated string", TokenStartLine, TokenStartColumn);
        if ( CurrentState == State.COMMENT )
            throw new SyntaxErrorException("Unterminated comment", TokenStartLine, TokenStartColumn);

        // dedent until we're back at level 0
        while (currentIndentLevel > 0){
            ListOfTokens.add(new Token(Token.TokenTypes.DEDENT, LineNumber, ColumnNumber));
            currentIndentLevel -= 1;
        }

        // add a newline for safety
        ListOfTokens.add( new Token(Token.TokenTypes.NEWLINE, LineNumber, ColumnNumber) );

        return ListOfTokens;
    }

    private Token readWord(String token, int StartLine, int StartCol) {
        String t = token.toLowerCase(); // lowercase for comparing to hashmap
        //check if the word is a token type, return that token type if so
        if (keywords.containsKey(t)){
            return new Token(keywords.get(t), StartLine, StartCol);
        }

        // for now just assume it's an identifier if we didn't get a real word
        return new Token(Token.TokenTypes.IDENTIFIER, StartLine, StartCol, token);
    }

    private Token readNumber(String token, int StartLine, int StartCol) {
        return new Token(Token.TokenTypes.NUMBER, StartLine, StartCol, token);
    }

    private Token readString(int StartLine, int StartCol, String str){
        return new Token(Token.TokenTypes.STRINGLITERAL, StartLine, StartCol, str);
    }
}

