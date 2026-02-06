package PlainEnglish;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// unit tests for the lexer
public class LexerTest {

    @Test
    public void TestTextManagerGet() {
        TextManager tm = new TextManager("hello world");
        assertEquals('h',tm.getCharacter());
        assertEquals('e',tm.getCharacter());
        assertEquals('l',tm.getCharacter());
        assertEquals('l',tm.getCharacter());
        assertEquals('o',tm.getCharacter());
        assertEquals(' ',tm.getCharacter());
        assertEquals('w',tm.getCharacter());
        assertEquals('o',tm.getCharacter());
        assertEquals('r',tm.getCharacter());
        assertEquals('l',tm.getCharacter());
        assertEquals('d',tm.getCharacter());
        assertEquals('~',tm.getCharacter());
    }

    @Test
    public void TestTextManagerPeek() {
        TextManager tm = new TextManager("hello world");
        assertEquals('h',tm.peekCharacter());
        assertEquals('h',tm.peekCharacter()); // double check that we didn't iterate
        tm.getCharacter();
        assertEquals('e',tm.peekCharacter());
        assertEquals('e',tm.peekCharacter());
    }

    @Test
    public void LexTestKeyword() {
        Lexer lex = new Lexer("else");
        List<Token> tokens = lex.lex();
        assertEquals(1, tokens.size());
        assertEquals(Token.TokenTypes.ELSE, tokens.get(0).Type);
    }

    @Test
    public void LexTestMulti() {
        Lexer lex = new Lexer("if else set");
        List<Token> tokens = lex.lex();
        assertEquals(3, tokens.size());
        assertEquals(Token.TokenTypes.IF, tokens.get(0).Type);
        assertEquals(Token.TokenTypes.ELSE, tokens.get(1).Type);
        assertEquals(Token.TokenTypes.SET, tokens.get(2).Type);
    }

    @Test
    public void LexTestIdentifier_NoNums() {
        Lexer lex = new Lexer("lksahf");
        List<Token> tokens = lex.lex();
        assertEquals(1, tokens.size());
        assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(0).Type);
    }
}
