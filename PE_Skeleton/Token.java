package PlainEnglish;

import java.util.Optional;

public class Token {
    public Optional<String> Value;
    public TokenTypes Type;
    public int ColumnNumber;
    public int LineNumber;
    public Token(TokenTypes type, int line, int column) {
        Type = type;
        ColumnNumber = column;
        LineNumber = line;
        Value = Optional.empty();
    }

    public Token(TokenTypes type, int line, int column, String val) {
        Type = type;
        ColumnNumber = column;
        LineNumber = line;
        Value = Optional.of(val);
    }

    public String toString() {
        return Type + " " + Value.orElse("") + " @ " + LineNumber + "," + ColumnNumber;
    }

    public enum TokenTypes {
        IDENTIFIER,
        NUMBER,
        INDENT,
        DEDENT,
        NEWLINE,
        STRINGLITERAL,
        CHARACTERLITERAL,
        TO,
        A,
        WITH,
        NAMED,
        AN,
        IS,
        IF,
        ELSE,
        LOOP,
        SET,
        MAKE,
        OF,
        TRUE,
        FALSE,
        AND,
        OR,
        NOT,
        COMMA,
        PLUS,
        HYPHEN,
        ASTERISK,
        SLASH,
        PERCENT,
        OPENPAREN,
        CLOSEPAREN,
        DOUBLEEQUAL,
        NOTEQUAL,
        LESSTHANEQUAL,
        GREATERTHANEQUAL,
        GREATERTHAN,
        LESSTHAN
    }
}
