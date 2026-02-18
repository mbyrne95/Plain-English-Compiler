package PlainEnglish;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Lexer1Tests {

    @Test
    public void Test1() throws Exception {
        var code = "A square is\n"+
                "    number length\n"+
                "";
        var tokens = new Lexer(code).lex();
        Assertions.assertEquals(Token.TokenTypes.A, tokens.get(0).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(1).Type);
        Assertions.assertEquals("square",  tokens.get(1).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.IS, tokens.get(2).Type);
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(3).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(4).Type);
        Assertions.assertEquals("number",  tokens.get(4).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(5).Type);
        Assertions.assertEquals("length",  tokens.get(5).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(6).Type);
    }

    @Test
    public void Test2() throws Exception {
        var code = "To SetSquare a square with number l\n"+
                "    Set length to l\n"+
                "";
        var tokens = new Lexer(code).lex();
        Assertions.assertEquals(Token.TokenTypes.TO, tokens.get(0).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(1).Type);
        Assertions.assertEquals("SetSquare",  tokens.get(1).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.A, tokens.get(2).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(3).Type);
        Assertions.assertEquals("square",  tokens.get(3).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.WITH, tokens.get(4).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(5).Type);
        Assertions.assertEquals("number",  tokens.get(5).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(6).Type);
        Assertions.assertEquals("l",  tokens.get(6).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(7).Type);
        Assertions.assertEquals(Token.TokenTypes.SET, tokens.get(8).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(9).Type);
        Assertions.assertEquals("length",  tokens.get(9).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.TO, tokens.get(10).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(11).Type);
        Assertions.assertEquals("l",  tokens.get(11).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(12).Type);
    }

    @Test
    public void Test3() throws Exception {
        var code = "To Display with word\n"+
                "    If true\n"+
                "        Print with word\n"+
                "    else\n"+
                "        Make square named sqr\n"+
                "";
        var tokens = new Lexer(code).lex();
        Assertions.assertEquals(Token.TokenTypes.TO, tokens.get(0).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(1).Type);
        Assertions.assertEquals("Display",  tokens.get(1).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.WITH, tokens.get(2).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(3).Type);
        Assertions.assertEquals("word",  tokens.get(3).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(4).Type);
        Assertions.assertEquals(Token.TokenTypes.IF, tokens.get(5).Type);
        Assertions.assertEquals(Token.TokenTypes.TRUE, tokens.get(6).Type);
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(7).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(8).Type);
        Assertions.assertEquals("Print",  tokens.get(8).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.WITH, tokens.get(9).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(10).Type);
        Assertions.assertEquals("word",  tokens.get(10).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(11).Type);
        Assertions.assertEquals(Token.TokenTypes.ELSE, tokens.get(12).Type);
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(13).Type);
        Assertions.assertEquals(Token.TokenTypes.MAKE, tokens.get(14).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(15).Type);
        Assertions.assertEquals("square",  tokens.get(15).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NAMED, tokens.get(16).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(17).Type);
        Assertions.assertEquals("sqr",  tokens.get(17).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(18).Type);
    }

    @Test
    public void Test4() throws Exception {
        var code = "To Run\n"+
                "    Make string named str\n"+
                "    Display str\n"+
                "    Loop done\n"+
                "    Set done to false or done and not done\n"+
                "";
        var tokens = new Lexer(code).lex();
        Assertions.assertEquals(Token.TokenTypes.TO, tokens.get(0).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(1).Type);
        Assertions.assertEquals("Run",  tokens.get(1).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(2).Type);
        Assertions.assertEquals(Token.TokenTypes.MAKE, tokens.get(3).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(4).Type);
        Assertions.assertEquals("string",  tokens.get(4).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NAMED, tokens.get(5).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(6).Type);
        Assertions.assertEquals("str",  tokens.get(6).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(7).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(8).Type);
        Assertions.assertEquals("Display",  tokens.get(8).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(9).Type);
        Assertions.assertEquals("str",  tokens.get(9).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(10).Type);
        Assertions.assertEquals(Token.TokenTypes.LOOP, tokens.get(11).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(12).Type);
        Assertions.assertEquals("done",  tokens.get(12).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(13).Type);
        Assertions.assertEquals(Token.TokenTypes.SET, tokens.get(14).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(15).Type);
        Assertions.assertEquals("done",  tokens.get(15).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.TO, tokens.get(16).Type);
        Assertions.assertEquals(Token.TokenTypes.FALSE, tokens.get(17).Type);
        Assertions.assertEquals(Token.TokenTypes.OR, tokens.get(18).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(19).Type);
        Assertions.assertEquals("done",  tokens.get(19).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.AND, tokens.get(20).Type);
        Assertions.assertEquals(Token.TokenTypes.NOT, tokens.get(21).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(22).Type);
        Assertions.assertEquals("done",  tokens.get(22).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(23).Type);
    }

}
