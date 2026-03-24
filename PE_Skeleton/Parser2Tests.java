package PlainEnglish;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

public class Parser2Tests {

    @Test
    public void NoParam() throws Exception {
        var code = "To Run\n" +
                "    Set num to 1\n" +
                "";

        var tokens = new LinkedList<Token>();
        tokens.add(new Token(Token.TokenTypes.TO,1,2));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,1,6,"Run"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,2,0));
        tokens.add(new Token(Token.TokenTypes.INDENT,2,4));
        tokens.add(new Token(Token.TokenTypes.SET,2,7));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,2,11,"num"));
        tokens.add(new Token(Token.TokenTypes.TO,2,14));
        tokens.add(new Token(Token.TokenTypes.NUMBER,2,16,"1"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,3,0));
        tokens.add(new Token(Token.TokenTypes.DEDENT,3,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,3,0));
        var ast = new PlainEnglishParser(tokens).program().orElseThrow();

        Assertions.assertEquals("Run",ast.method.get(0).name);
        Assertions.assertFalse(ast.method.get(0).with);
        Assertions.assertTrue(ast.method.get(0).className.isEmpty());
        Assertions.assertTrue(ast.method.get(0).statementblock.statement.get(0).set.isPresent());
        Assertions.assertEquals("num",ast.method.get(0).statementblock.statement.get(0).set.get().variablereference.name);
        Assertions.assertEquals(false,ast.method.get(0).statementblock.statement.get(0).set.get().variablereference.of);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(0).set.get().variablereference.$object.isPresent());
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(0).set.get().expression.term.get(0).factor.get(0).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(0).set.get().expression.term.get(0).factor.get(0).$false);
        Assertions.assertEquals("1",ast.method.get(0).statementblock.statement.get(0).set.get().expression.term.get(0).factor.get(0).number.get());
    }

    @Test
    public void SingleParam() throws Exception {
        var code = "To Create a string\n" +
                "    Make string named str\n" +
                "";

        var tokens = new LinkedList<Token>();
        tokens.add(new Token(Token.TokenTypes.TO,1,2));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,1,9,"Create"));
        tokens.add(new Token(Token.TokenTypes.A,1,11));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,1,18,"string"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,2,0));
        tokens.add(new Token(Token.TokenTypes.INDENT,2,4));
        tokens.add(new Token(Token.TokenTypes.MAKE,2,8));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,2,15,"string"));
        tokens.add(new Token(Token.TokenTypes.NAMED,2,21));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,2,25,"str"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,3,0));
        tokens.add(new Token(Token.TokenTypes.DEDENT,3,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,3,0));
        var ast = new PlainEnglishParser(tokens).program().orElseThrow();

        Assertions.assertEquals("Create",ast.method.get(0).name);
        Assertions.assertFalse(ast.method.get(0).with);
        Assertions.assertEquals("string",ast.method.get(0).className.get());
        Assertions.assertTrue(ast.method.get(0).statementblock.statement.get(0).make.isPresent());
        Assertions.assertEquals("string",ast.method.get(0).statementblock.statement.get(0).make.get().type);
        Assertions.assertEquals("str",ast.method.get(0).statementblock.statement.get(0).make.get().name);
    }

    @Test
    public void NamedParam() throws Exception {
        var code = "To Create with number named value\n" +
                "    Make number named num\n" +
                "    Set num to 5.0\n" +
                "";

        var tokens = new LinkedList<Token>();
        tokens.add(new Token(Token.TokenTypes.TO,1,2));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,1,9,"Create"));
        tokens.add(new Token(Token.TokenTypes.WITH,1,14));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,1,21,"number"));
        tokens.add(new Token(Token.TokenTypes.NAMED,1,27));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,1,33,"value"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,2,0));
        tokens.add(new Token(Token.TokenTypes.INDENT,2,4));
        tokens.add(new Token(Token.TokenTypes.MAKE,2,8));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,2,15,"number"));
        tokens.add(new Token(Token.TokenTypes.NAMED,2,21));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,2,25,"num"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,3,0));
        tokens.add(new Token(Token.TokenTypes.SET,3,7));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,3,11,"num"));
        tokens.add(new Token(Token.TokenTypes.TO,3,14));
        tokens.add(new Token(Token.TokenTypes.NUMBER,3,18,"5.0"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,4,0));
        tokens.add(new Token(Token.TokenTypes.DEDENT,4,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,4,0));
        var ast = new PlainEnglishParser(tokens).program().orElseThrow();

        Assertions.assertEquals("Create",ast.method.get(0).name);
        Assertions.assertTrue(ast.method.get(0).with);
        Assertions.assertTrue(ast.method.get(0).className.isEmpty());
        Assertions.assertEquals("number",ast.method.get(0).parameter.get(0).paramType);
        Assertions.assertTrue(ast.method.get(0).parameter.get(0).named);
        Assertions.assertEquals("value",ast.method.get(0).parameter.get(0).nameOverride.get());
        Assertions.assertTrue(ast.method.get(0).statementblock.statement.get(0).make.isPresent());
        Assertions.assertEquals("number",ast.method.get(0).statementblock.statement.get(0).make.get().type);
        Assertions.assertEquals("num",ast.method.get(0).statementblock.statement.get(0).make.get().name);
        Assertions.assertTrue(ast.method.get(0).statementblock.statement.get(1).set.isPresent());
        Assertions.assertEquals("num",ast.method.get(0).statementblock.statement.get(1).set.get().variablereference.name);
        Assertions.assertEquals(false,ast.method.get(0).statementblock.statement.get(1).set.get().variablereference.of);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(1).set.get().variablereference.$object.isPresent());
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(0).factor.get(0).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(0).factor.get(0).$false);
        Assertions.assertEquals("5.0",ast.method.get(0).statementblock.statement.get(1).set.get().expression.term.get(0).factor.get(0).number.get());
    }

    @Test
    public void MultiParam() throws Exception {
        var code = "To Assign a Student with number named sid, string named sname\n" +
                "    Set id to 12345\n" +
                "";

        var tokens = new LinkedList<Token>();
        tokens.add(new Token(Token.TokenTypes.TO,1,2));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,1,9,"Assign"));
        tokens.add(new Token(Token.TokenTypes.A,1,11));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,1,19,"Student"));
        tokens.add(new Token(Token.TokenTypes.WITH,1,24));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,1,31,"number"));
        tokens.add(new Token(Token.TokenTypes.NAMED,1,37));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,1,41,"sid"));
        tokens.add(new Token(Token.TokenTypes.COMMA,1,42));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,1,49,"string"));
        tokens.add(new Token(Token.TokenTypes.NAMED,1,55));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,1,61,"sname"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,2,0));
        tokens.add(new Token(Token.TokenTypes.INDENT,2,4));
        tokens.add(new Token(Token.TokenTypes.SET,2,7));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,2,10,"id"));
        tokens.add(new Token(Token.TokenTypes.TO,2,13));
        tokens.add(new Token(Token.TokenTypes.NUMBER,2,19,"12345"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,3,0));
        tokens.add(new Token(Token.TokenTypes.DEDENT,3,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,3,0));
        var ast = new PlainEnglishParser(tokens).program().orElseThrow();
        
        Assertions.assertEquals("Assign",ast.method.get(0).name);
        Assertions.assertTrue(ast.method.get(0).with);
        Assertions.assertEquals("Student",ast.method.get(0).className.get());
        Assertions.assertEquals("number",ast.method.get(0).parameter.get(0).paramType);
        Assertions.assertTrue(ast.method.get(0).parameter.get(0).named);
        Assertions.assertEquals("sid",ast.method.get(0).parameter.get(0).nameOverride.get());
        Assertions.assertEquals("string",ast.method.get(0).parameter.get(1).paramType);
        Assertions.assertTrue(ast.method.get(0).parameter.get(1).named);
        Assertions.assertEquals("sname",ast.method.get(0).parameter.get(1).nameOverride.get());
        Assertions.assertTrue(ast.method.get(0).statementblock.statement.get(0).set.isPresent());
        Assertions.assertEquals("id",ast.method.get(0).statementblock.statement.get(0).set.get().variablereference.name);
        Assertions.assertEquals(false,ast.method.get(0).statementblock.statement.get(0).set.get().variablereference.of);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(0).set.get().variablereference.$object.isPresent());
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(0).set.get().expression.term.get(0).factor.get(0).$true);
        Assertions.assertFalse(ast.method.get(0).statementblock.statement.get(0).set.get().expression.term.get(0).factor.get(0).$false);
        Assertions.assertEquals("12345",ast.method.get(0).statementblock.statement.get(0).set.get().expression.term.get(0).factor.get(0).number.get());
    }

}
