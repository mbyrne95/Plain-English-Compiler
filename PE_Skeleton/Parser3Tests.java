package PlainEnglish;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

public class Parser3Tests {

    @Test
    public void TestFactor() throws Exception {
        var code = "To FactorTests\n" +
                "    Set numFactor to 311\n" +
                "    Set charFactor to 'a'\n" +
                "    Set strFactor to \"Hello World\"\n" +
                "    Set boolFactor to true\n" +
                "    Set varFactor to numVar\n" +
                "";

        var tokens = new LinkedList<Token>();
        tokens.add(new Token(Token.TokenTypes.TO,1,2));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,1,14,"FactorTests"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,2,0));
        tokens.add(new Token(Token.TokenTypes.INDENT,2,4));
        tokens.add(new Token(Token.TokenTypes.SET,2,7));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,2,17,"numFactor"));
        tokens.add(new Token(Token.TokenTypes.TO,2,20));
        tokens.add(new Token(Token.TokenTypes.NUMBER,2,24,"311"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,3,0));
        tokens.add(new Token(Token.TokenTypes.SET,3,7));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,3,18,"charFactor"));
        tokens.add(new Token(Token.TokenTypes.TO,3,21));
        tokens.add(new Token(Token.TokenTypes.CHARACTERLITERAL,3,22,"a"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,4,0));
        tokens.add(new Token(Token.TokenTypes.SET,4,7));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,4,17,"strFactor"));
        tokens.add(new Token(Token.TokenTypes.TO,4,20));
        tokens.add(new Token(Token.TokenTypes.STRINGLITERAL,4,34,"Hello World"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,5,0));
        tokens.add(new Token(Token.TokenTypes.SET,5,7));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,5,18,"boolFactor"));
        tokens.add(new Token(Token.TokenTypes.TO,5,21));
        tokens.add(new Token(Token.TokenTypes.TRUE,5,26));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,6,0));
        tokens.add(new Token(Token.TokenTypes.SET,6,7));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,6,17,"varFactor"));
        tokens.add(new Token(Token.TokenTypes.TO,6,20));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,6,27,"numVar"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,7,0));
        tokens.add(new Token(Token.TokenTypes.DEDENT,7,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,7,0));

        var ast = new PlainEnglishParser(tokens).program().orElseThrow();
        Assertions.assertEquals("FactorTests",ast.method.get(0).name);
        Assertions.assertFalse(ast.method.get(0).with);
        Assertions.assertTrue(ast.method.get(0).className.isEmpty());
        Assertions.assertTrue(ast.method.get(0).statementblock.statement.get(0).set.isPresent());
        Assertions.assertEquals("numFactor",ast.method.get(0).statementblock.statement.get(0).set.get().variablereference.name);
        Assertions.assertEquals(false,ast.method.get(0).statementblock.statement.get(0).set.get().variablereference.of);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(0).set.get().variablereference.$object.isPresent());
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(0).set.get().expression.term.get(0).factor.get(0).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(0).set.get().expression.term.get(0).factor.get(0).$false);
        Assertions.assertEquals("311",ast.method.get(0).statementblock.statement.get(0).set.get().expression.term.get(0).factor.get(0).number.get());
        Assertions.assertTrue(ast.method.get(0).statementblock.statement.get(1).set.isPresent());
        Assertions.assertEquals("charFactor",ast.method.get(0).statementblock.statement.get(1).set.get().variablereference.name);
        Assertions.assertEquals(false,ast.method.get(0).statementblock.statement.get(1).set.get().variablereference.of);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(1).set.get().variablereference.$object.isPresent());
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(0).factor.get(0).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(0).factor.get(0).$false);
        Assertions.assertEquals("a",ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(0).factor.get(0).characterliteral.get());
        Assertions.assertTrue(ast.method.get(0).statementblock.statement.get(2).set.isPresent());
        Assertions.assertEquals("strFactor",ast.method.get(0).statementblock.statement.get(2).set.get().variablereference.name);
        Assertions.assertEquals(false,ast.method.get(0).statementblock.statement.get(2).set.get().variablereference.of);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().variablereference.$object.isPresent());
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).$false);
        Assertions.assertEquals("Hello World",ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).stringliteral.get());
        Assertions.assertTrue(ast.method.get(0).statementblock.statement.get(3).set.isPresent());
        Assertions.assertEquals("boolFactor",ast.method.get(0).statementblock.statement.get(3).set.get().variablereference.name);
        Assertions.assertEquals(false,ast.method.get(0).statementblock.statement.get(3).set.get().variablereference.of);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(3).set.get().variablereference.$object.isPresent());
        Assertions.assertTrue(ast.method.get(0).statementblock.statement.get(3).set.get().expression.term.get(0).factor.get(0).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(3).set.get().expression.term.get(0).factor.get(0).$false);
        Assertions.assertTrue(ast.method.get(0).statementblock.statement.get(4).set.isPresent());
        Assertions.assertEquals("varFactor",ast.method.get(0).statementblock.statement.get(4).set.get().variablereference.name);
        Assertions.assertEquals(false,ast.method.get(0).statementblock.statement.get(4).set.get().variablereference.of);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(4).set.get().variablereference.$object.isPresent());
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(4).set.get().expression.term.get(0).factor.get(0).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(4).set.get().expression.term.get(0).factor.get(0).$false);
        Assertions.assertEquals("numVar",ast.method.get(0).statementblock.statement.get(4).set.get().expression.term.get(0).factor.get(0).variablereference.get().name);
        Assertions.assertEquals(false,ast.method.get(0).statementblock.statement.get(4).set.get().expression.term.get(0).factor.get(0).variablereference.get().of);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(4).set.get().expression.term.get(0).factor.get(0).variablereference.get().$object.isPresent());
    }

    @Test
    public void TestTerm() throws Exception {
        var code = "To TermTests\n" +
                "    Set mulTerm to \"Hello\" + \" World\"\n" +
                "    Set divTerm to 7.4 / 2\n" +
                "    Set modTerm to 100 % 4\n" +
                "";

        var tokens = new LinkedList<Token>();
        tokens.add(new Token(Token.TokenTypes.TO,1,2));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,1,12,"TermTests"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,2,0));
        tokens.add(new Token(Token.TokenTypes.INDENT,2,4));
        tokens.add(new Token(Token.TokenTypes.SET,2,7));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,2,15,"mulTerm"));
        tokens.add(new Token(Token.TokenTypes.TO,2,18));
        tokens.add(new Token(Token.TokenTypes.STRINGLITERAL,2,26,"Hello"));
        tokens.add(new Token(Token.TokenTypes.PLUS,2,28));
        tokens.add(new Token(Token.TokenTypes.STRINGLITERAL,2,37," World"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,3,0));
        tokens.add(new Token(Token.TokenTypes.SET,3,7));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,3,15,"divTerm"));
        tokens.add(new Token(Token.TokenTypes.TO,3,18));
        tokens.add(new Token(Token.TokenTypes.NUMBER,3,22,"7.4"));
        tokens.add(new Token(Token.TokenTypes.SLASH,3,24));
        tokens.add(new Token(Token.TokenTypes.NUMBER,3,26,"2"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,4,0));
        tokens.add(new Token(Token.TokenTypes.SET,4,7));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,4,15,"modTerm"));
        tokens.add(new Token(Token.TokenTypes.TO,4,18));
        tokens.add(new Token(Token.TokenTypes.NUMBER,4,22,"100"));
        tokens.add(new Token(Token.TokenTypes.PERCENT,4,24));
        tokens.add(new Token(Token.TokenTypes.NUMBER,4,26,"4"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,5,0));
        tokens.add(new Token(Token.TokenTypes.DEDENT,5,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,5,0));
        var ast = new PlainEnglishParser(tokens).program().orElseThrow();
        Assertions.assertEquals("TermTests",ast.method.get(0).name);
        Assertions.assertFalse(ast.method.get(0).with);
        Assertions.assertTrue(ast.method.get(0).className.isEmpty());
        Assertions.assertTrue(ast.method.get(0).statementblock.statement.get(0).set.isPresent());
        Assertions.assertEquals("mulTerm",ast.method.get(0).statementblock.statement.get(0).set.get().variablereference.name);
        Assertions.assertEquals(false,ast.method.get(0).statementblock.statement.get(0).set.get().variablereference.of);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(0).set.get().variablereference.$object.isPresent());
        Assertions.assertEquals(PlainEnglish.AST.plusORhyphen.plus,ast.method.get(0).statementblock.statement.get(0).set.get().expression.theplusORhyphen.get(0));
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(0).set.get().expression.term.get(0).factor.get(0).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(0).set.get().expression.term.get(0).factor.get(0).$false);
        Assertions.assertEquals("Hello",ast.method.get(0).statementblock.statement.get(0).set.get().expression.term.get(0).factor.get(0).stringliteral.get());
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(0).set.get().expression.term.get(1).factor.get(0).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(0).set.get().expression.term.get(1).factor.get(0).$false);
        Assertions.assertEquals(" World",ast.method.get(0).statementblock.statement.get(0).set.get().expression.term.get(1).factor.get(0).stringliteral.get());
        Assertions.assertTrue(ast.method.get(0).statementblock.statement.get(1).set.isPresent());
        Assertions.assertEquals("divTerm",ast.method.get(0).statementblock.statement.get(1).set.get().variablereference.name);
        Assertions.assertEquals(false,ast.method.get(0).statementblock.statement.get(1).set.get().variablereference.of);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(1).set.get().variablereference.$object.isPresent());
        Assertions.assertEquals(PlainEnglish.AST.asteriskORslashORpercent.slash,ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(0).theasteriskORslashORpercent.get(0));
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(0).factor.get(0).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(0).factor.get(0).$false);
        Assertions.assertEquals("7.4",ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(0).factor.get(0).number.get());
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(0).factor.get(1).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(0).factor.get(1).$false);
        Assertions.assertEquals("2",ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(0).factor.get(1).number.get());
        Assertions.assertTrue(ast.method.get(0).statementblock.statement.get(2).set.isPresent());
        Assertions.assertEquals("modTerm",ast.method.get(0).statementblock.statement.get(2).set.get().variablereference.name);
        Assertions.assertEquals(false,ast.method.get(0).statementblock.statement.get(2).set.get().variablereference.of);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().variablereference.$object.isPresent());
        Assertions.assertEquals(PlainEnglish.AST.asteriskORslashORpercent.percent,ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).theasteriskORslashORpercent.get(0));
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).$false);
        Assertions.assertEquals("100",ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).number.get());
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(1).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(1).$false);
        Assertions.assertEquals("4",ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(1).number.get());
    }

    @Test
    public void TestExpression() throws Exception {
        var code = "To ExpressionTests\n" +
                "    Set longExpr to b + 2 * 3\n" +
                "    Set parenExpr to 1 + (m * n) - (4 / 5)\n" +
                "    Set multiExpr to (9 + (8 - x) * (y % 7)) / z\n" +
                "";

        var tokens = new LinkedList<Token>();
        tokens.add(new Token(Token.TokenTypes.TO,1,2));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,1,18,"ExpressionTests"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,2,0));
        tokens.add(new Token(Token.TokenTypes.INDENT,2,4));
        tokens.add(new Token(Token.TokenTypes.SET,2,7));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,2,16,"longExpr"));
        tokens.add(new Token(Token.TokenTypes.TO,2,19));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,2,21,"b"));
        tokens.add(new Token(Token.TokenTypes.PLUS,2,23));
        tokens.add(new Token(Token.TokenTypes.NUMBER,2,25,"2"));
        tokens.add(new Token(Token.TokenTypes.ASTERISK,2,27));
        tokens.add(new Token(Token.TokenTypes.NUMBER,2,29,"3"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,3,0));
        tokens.add(new Token(Token.TokenTypes.SET,3,7));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,3,17,"parenExpr"));
        tokens.add(new Token(Token.TokenTypes.TO,3,20));
        tokens.add(new Token(Token.TokenTypes.NUMBER,3,22,"1"));
        tokens.add(new Token(Token.TokenTypes.PLUS,3,24));
        tokens.add(new Token(Token.TokenTypes.OPENPAREN,3,26));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,3,27,"m"));
        tokens.add(new Token(Token.TokenTypes.ASTERISK,3,29));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,3,31,"n"));
        tokens.add(new Token(Token.TokenTypes.CLOSEPAREN,3,32));
        tokens.add(new Token(Token.TokenTypes.HYPHEN,3,34));
        tokens.add(new Token(Token.TokenTypes.OPENPAREN,3,36));
        tokens.add(new Token(Token.TokenTypes.NUMBER,3,37,"4"));
        tokens.add(new Token(Token.TokenTypes.SLASH,3,39));
        tokens.add(new Token(Token.TokenTypes.NUMBER,3,41,"5"));
        tokens.add(new Token(Token.TokenTypes.CLOSEPAREN,3,42));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,4,0));
        tokens.add(new Token(Token.TokenTypes.SET,4,7));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,4,17,"multiExpr"));
        tokens.add(new Token(Token.TokenTypes.TO,4,20));
        tokens.add(new Token(Token.TokenTypes.OPENPAREN,4,22));
        tokens.add(new Token(Token.TokenTypes.NUMBER,4,23,"9"));
        tokens.add(new Token(Token.TokenTypes.PLUS,4,25));
        tokens.add(new Token(Token.TokenTypes.OPENPAREN,4,27));
        tokens.add(new Token(Token.TokenTypes.NUMBER,4,28,"8"));
        tokens.add(new Token(Token.TokenTypes.HYPHEN,4,30));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,4,32,"x"));
        tokens.add(new Token(Token.TokenTypes.CLOSEPAREN,4,33));
        tokens.add(new Token(Token.TokenTypes.ASTERISK,4,35));
        tokens.add(new Token(Token.TokenTypes.OPENPAREN,4,37));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,4,38,"y"));
        tokens.add(new Token(Token.TokenTypes.PERCENT,4,40));
        tokens.add(new Token(Token.TokenTypes.NUMBER,4,42,"7"));
        tokens.add(new Token(Token.TokenTypes.CLOSEPAREN,4,43));
        tokens.add(new Token(Token.TokenTypes.CLOSEPAREN,4,44));
        tokens.add(new Token(Token.TokenTypes.SLASH,4,46));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,4,48,"z"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,5,0));
        tokens.add(new Token(Token.TokenTypes.DEDENT,5,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,5,0));
        var ast = new PlainEnglishParser(tokens).program().orElseThrow();
        Assertions.assertEquals("ExpressionTests",ast.method.get(0).name);
        Assertions.assertFalse(ast.method.get(0).with);
        Assertions.assertTrue(ast.method.get(0).className.isEmpty());
        Assertions.assertTrue(ast.method.get(0).statementblock.statement.get(0).set.isPresent());
        Assertions.assertEquals("longExpr",ast.method.get(0).statementblock.statement.get(0).set.get().variablereference.name);
        Assertions.assertEquals(false,ast.method.get(0).statementblock.statement.get(0).set.get().variablereference.of);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(0).set.get().variablereference.$object.isPresent());
        Assertions.assertEquals(PlainEnglish.AST.plusORhyphen.plus,ast.method.get(0).statementblock.statement.get(0).set.get().expression.theplusORhyphen.get(0));
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(0).set.get().expression.term.get(0).factor.get(0).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(0).set.get().expression.term.get(0).factor.get(0).$false);
        Assertions.assertEquals("b",ast.method.get(0).statementblock.statement.get(0).set.get().expression.term.get(0).factor.get(0).variablereference.get().name);
        Assertions.assertEquals(false,ast.method.get(0).statementblock.statement.get(0).set.get().expression.term.get(0).factor.get(0).variablereference.get().of);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(0).set.get().expression.term.get(0).factor.get(0).variablereference.get().$object.isPresent());
        Assertions.assertEquals(PlainEnglish.AST.asteriskORslashORpercent.asterisk,ast.method.get(0).statementblock.statement.get(0).set.get().expression.term.get(1).theasteriskORslashORpercent.get(0));
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(0).set.get().expression.term.get(1).factor.get(0).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(0).set.get().expression.term.get(1).factor.get(0).$false);
        Assertions.assertEquals("2",ast.method.get(0).statementblock.statement.get(0).set.get().expression.term.get(1).factor.get(0).number.get());
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(0).set.get().expression.term.get(1).factor.get(1).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(0).set.get().expression.term.get(1).factor.get(1).$false);
        Assertions.assertEquals("3",ast.method.get(0).statementblock.statement.get(0).set.get().expression.term.get(1).factor.get(1).number.get());
        Assertions.assertTrue(ast.method.get(0).statementblock.statement.get(1).set.isPresent());
        Assertions.assertEquals("parenExpr",ast.method.get(0).statementblock.statement.get(1).set.get().variablereference.name);
        Assertions.assertEquals(false,ast.method.get(0).statementblock.statement.get(1).set.get().variablereference.of);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(1).set.get().variablereference.$object.isPresent());
        Assertions.assertEquals(PlainEnglish.AST.plusORhyphen.plus,ast.method.get(0).statementblock.statement.get(1).set.get().expression.theplusORhyphen.get(0));
        Assertions.assertEquals(PlainEnglish.AST.plusORhyphen.hyphen,ast.method.get(0).statementblock.statement.get(1).set.get().expression.theplusORhyphen.get(1));
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(0).factor.get(0).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(0).factor.get(0).$false);
        Assertions.assertEquals("1",ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(0).factor.get(0).number.get());
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(1).factor.get(0).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(1).factor.get(0).$false);
        Assertions.assertEquals(PlainEnglish.AST.asteriskORslashORpercent.asterisk,ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(1).factor.get(0).expression.get().term.get(0).theasteriskORslashORpercent.get(0));
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(1).factor.get(0).expression.get().term.get(0).factor.get(0).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(1).factor.get(0).expression.get().term.get(0).factor.get(0).$false);
        Assertions.assertEquals("m",ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(1).factor.get(0).expression.get().term.get(0).factor.get(0).variablereference.get().name);
        Assertions.assertEquals(false,ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(1).factor.get(0).expression.get().term.get(0).factor.get(0).variablereference.get().of);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(1).factor.get(0).expression.get().term.get(0).factor.get(0).variablereference.get().$object.isPresent());
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(1).factor.get(0).expression.get().term.get(0).factor.get(1).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(1).factor.get(0).expression.get().term.get(0).factor.get(1).$false);
        Assertions.assertEquals("n",ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(1).factor.get(0).expression.get().term.get(0).factor.get(1).variablereference.get().name);
        Assertions.assertEquals(false,ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(1).factor.get(0).expression.get().term.get(0).factor.get(1).variablereference.get().of);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(1).factor.get(0).expression.get().term.get(0).factor.get(1).variablereference.get().$object.isPresent());
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(2).factor.get(0).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(2).factor.get(0).$false);
        Assertions.assertEquals(PlainEnglish.AST.asteriskORslashORpercent.slash,ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(2).factor.get(0).expression.get().term.get(0).theasteriskORslashORpercent.get(0));
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(2).factor.get(0).expression.get().term.get(0).factor.get(0).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(2).factor.get(0).expression.get().term.get(0).factor.get(0).$false);
        Assertions.assertEquals("4",ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(2).factor.get(0).expression.get().term.get(0).factor.get(0).number.get());
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(2).factor.get(0).expression.get().term.get(0).factor.get(1).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(2).factor.get(0).expression.get().term.get(0).factor.get(1).$false);
        Assertions.assertEquals("5",ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(2).factor.get(0).expression.get().term.get(0).factor.get(1).number.get());
        Assertions.assertTrue(ast.method.get(0).statementblock.statement.get(2).set.isPresent());
        Assertions.assertEquals("multiExpr",ast.method.get(0).statementblock.statement.get(2).set.get().variablereference.name);
        Assertions.assertEquals(false,ast.method.get(0).statementblock.statement.get(2).set.get().variablereference.of);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().variablereference.$object.isPresent());
        Assertions.assertEquals(PlainEnglish.AST.asteriskORslashORpercent.slash,ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).theasteriskORslashORpercent.get(0));
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).$false);
        Assertions.assertEquals(PlainEnglish.AST.plusORhyphen.plus,ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).expression.get().theplusORhyphen.get(0));
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).expression.get().term.get(0).factor.get(0).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).expression.get().term.get(0).factor.get(0).$false);
        Assertions.assertEquals("9",ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).expression.get().term.get(0).factor.get(0).number.get());
        Assertions.assertEquals(PlainEnglish.AST.asteriskORslashORpercent.asterisk,ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).expression.get().term.get(1).theasteriskORslashORpercent.get(0));
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).expression.get().term.get(1).factor.get(0).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).expression.get().term.get(1).factor.get(0).$false);
        Assertions.assertEquals(PlainEnglish.AST.plusORhyphen.hyphen,ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).expression.get().term.get(1).factor.get(0).expression.get().theplusORhyphen.get(0));
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).expression.get().term.get(1).factor.get(0).expression.get().term.get(0).factor.get(0).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).expression.get().term.get(1).factor.get(0).expression.get().term.get(0).factor.get(0).$false);
        Assertions.assertEquals("8",ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).expression.get().term.get(1).factor.get(0).expression.get().term.get(0).factor.get(0).number.get());
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).expression.get().term.get(1).factor.get(0).expression.get().term.get(1).factor.get(0).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).expression.get().term.get(1).factor.get(0).expression.get().term.get(1).factor.get(0).$false);
        Assertions.assertEquals("x",ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).expression.get().term.get(1).factor.get(0).expression.get().term.get(1).factor.get(0).variablereference.get().name);
        Assertions.assertEquals(false,ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).expression.get().term.get(1).factor.get(0).expression.get().term.get(1).factor.get(0).variablereference.get().of);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).expression.get().term.get(1).factor.get(0).expression.get().term.get(1).factor.get(0).variablereference.get().$object.isPresent());
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).expression.get().term.get(1).factor.get(1).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).expression.get().term.get(1).factor.get(1).$false);
        Assertions.assertEquals(PlainEnglish.AST.asteriskORslashORpercent.percent,ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).expression.get().term.get(1).factor.get(1).expression.get().term.get(0).theasteriskORslashORpercent.get(0));
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).expression.get().term.get(1).factor.get(1).expression.get().term.get(0).factor.get(0).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).expression.get().term.get(1).factor.get(1).expression.get().term.get(0).factor.get(0).$false);
        Assertions.assertEquals("y",ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).expression.get().term.get(1).factor.get(1).expression.get().term.get(0).factor.get(0).variablereference.get().name);
        Assertions.assertEquals(false,ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).expression.get().term.get(1).factor.get(1).expression.get().term.get(0).factor.get(0).variablereference.get().of);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).expression.get().term.get(1).factor.get(1).expression.get().term.get(0).factor.get(0).variablereference.get().$object.isPresent());
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).expression.get().term.get(1).factor.get(1).expression.get().term.get(0).factor.get(1).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).expression.get().term.get(1).factor.get(1).expression.get().term.get(0).factor.get(1).$false);
        Assertions.assertEquals("7",ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(0).expression.get().term.get(1).factor.get(1).expression.get().term.get(0).factor.get(1).number.get());
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(1).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(1).$false);
        Assertions.assertEquals("z",ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(1).variablereference.get().name);
        Assertions.assertEquals(false,ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(1).variablereference.get().of);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(2).set.get().expression.term.get(0).factor.get(1).variablereference.get().$object.isPresent());
    }
}
