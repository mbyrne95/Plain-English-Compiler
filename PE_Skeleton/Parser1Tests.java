package PlainEnglish;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.LinkedList;

public class Parser1Tests {

    @Test
    public void SimpleTypeDef() throws Exception {
        var code = "A square is\n" +
                "    number length\n" +
                "";

        var tokens = new LinkedList<Token>();
        tokens.add(new Token(Token.TokenTypes.A,1,1));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,1,8,"square"));
        tokens.add(new Token(Token.TokenTypes.IS,1,11));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,2,0));
        tokens.add(new Token(Token.TokenTypes.INDENT,2,4));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,2,10,"number"));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,2,17,"length"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,3,0));
        tokens.add(new Token(Token.TokenTypes.DEDENT,3,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,3,0));
        var ast = new PlainEnglishParser(tokens).program().orElseThrow();
        Assertions.assertEquals("square",ast.typedef.get(0).name);
        Assertions.assertEquals("number",ast.typedef.get(0).field.get(0).type);
        Assertions.assertEquals("length",ast.typedef.get(0).field.get(0).name);
    }

    @Test
    public void MultiLineTypeDef() throws Exception {
        var code = "A Student is\n" +
                "\n" +
                "    string firstname\n" +
                "    \n" +
                "    string lastname\n" +
                "    \n" +
                "    number id\n" +
                "    \n" +
                "    \n" +
                "";

        var tokens = new LinkedList<Token>();
        tokens.add(new Token(Token.TokenTypes.A,1,1));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,1,9,"Student"));
        tokens.add(new Token(Token.TokenTypes.IS,1,12));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,2,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,3,0));
        tokens.add(new Token(Token.TokenTypes.INDENT,3,4));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,3,10,"string"));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,3,20,"firstname"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,4,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,5,0));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,5,10,"string"));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,5,19,"lastname"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,6,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,7,0));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,7,10,"number"));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,7,13,"id"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,8,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,9,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,10,0));
        tokens.add(new Token(Token.TokenTypes.DEDENT,10,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,10,0));
        var ast = new PlainEnglishParser(tokens).program().orElseThrow();
        Assertions.assertEquals("Student",ast.typedef.get(0).name);
        Assertions.assertEquals("string",ast.typedef.get(0).field.get(0).type);
        Assertions.assertEquals("firstname",ast.typedef.get(0).field.get(0).name);
        Assertions.assertEquals("string",ast.typedef.get(0).field.get(1).type);
        Assertions.assertEquals("lastname",ast.typedef.get(0).field.get(1).name);
        Assertions.assertEquals("number",ast.typedef.get(0).field.get(2).type);
        Assertions.assertEquals("id",ast.typedef.get(0).field.get(2).name);
    }

    @Test
    public void MultipleTypeDef() throws Exception {
        var code = "\n" +
                "\n" +
                "A MathExpression is\n" +
                "\n" +
                "    string leftExpression\n" +
                "    \n" +
                "    \n" +
                "    string mathOp\n" +
                "    \n" +
                "    \n" +
                "    string rightExpression\n" +
                "    \n" +
                "    \n" +
                "    \n" +
                "    \n" +
                "A BoolExpression is\n" +
                "\n" +
                "    string leftExpression\n" +
                "    \n" +
                "    \n" +
                "    string boolOp\n" +
                "    \n" +
                "    \n" +
                "    string rightExpression\n" +
                "    \n" +
                "\n" +
                "";

        var tokens = new LinkedList<Token>();
        tokens.add(new Token(Token.TokenTypes.NEWLINE,2,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,3,0));
        tokens.add(new Token(Token.TokenTypes.A,3,1));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,3,16,"MathExpression"));
        tokens.add(new Token(Token.TokenTypes.IS,3,19));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,4,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,5,0));
        tokens.add(new Token(Token.TokenTypes.INDENT,5,4));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,5,10,"string"));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,5,25,"leftExpression"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,6,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,7,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,8,0));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,8,10,"string"));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,8,17,"mathOp"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,9,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,10,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,11,0));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,11,10,"string"));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,11,26,"rightExpression"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,12,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,13,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,14,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,15,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,16,0));
        tokens.add(new Token(Token.TokenTypes.DEDENT,16,0));
        tokens.add(new Token(Token.TokenTypes.A,16,1));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,16,16,"BoolExpression"));
        tokens.add(new Token(Token.TokenTypes.IS,16,19));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,17,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,18,0));
        tokens.add(new Token(Token.TokenTypes.INDENT,18,4));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,18,10,"string"));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,18,25,"leftExpression"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,19,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,20,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,21,0));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,21,10,"string"));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,21,17,"boolOp"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,22,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,23,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,24,0));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,24,10,"string"));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,24,26,"rightExpression"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,25,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,26,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,27,0));
        tokens.add(new Token(Token.TokenTypes.DEDENT,27,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,27,0));
        var ast = new PlainEnglishParser(tokens).program().orElseThrow();
        Assertions.assertEquals("MathExpression",ast.typedef.get(0).name);
        Assertions.assertEquals("string",ast.typedef.get(0).field.get(0).type);
        Assertions.assertEquals("leftExpression",ast.typedef.get(0).field.get(0).name);
        Assertions.assertEquals("string",ast.typedef.get(0).field.get(1).type);
        Assertions.assertEquals("mathOp",ast.typedef.get(0).field.get(1).name);
        Assertions.assertEquals("string",ast.typedef.get(0).field.get(2).type);
        Assertions.assertEquals("rightExpression",ast.typedef.get(0).field.get(2).name);
        Assertions.assertEquals("BoolExpression",ast.typedef.get(1).name);
        Assertions.assertEquals("string",ast.typedef.get(1).field.get(0).type);
        Assertions.assertEquals("leftExpression",ast.typedef.get(1).field.get(0).name);
        Assertions.assertEquals("string",ast.typedef.get(1).field.get(1).type);
        Assertions.assertEquals("boolOp",ast.typedef.get(1).field.get(1).name);
        Assertions.assertEquals("string",ast.typedef.get(1).field.get(2).type);
        Assertions.assertEquals("rightExpression",ast.typedef.get(1).field.get(2).name);
    }

    @Test
    public void TestPE_CodeParser() throws Exception {
        var code = "An ErrorDef is\n" +
                "    type\n" +
                "";

        var tokens = new LinkedList<Token>();
        tokens.add(new Token(Token.TokenTypes.AN,1,2));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,1,11,"ErrorDef"));
        tokens.add(new Token(Token.TokenTypes.IS,1,14));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,2,0));
        tokens.add(new Token(Token.TokenTypes.INDENT,2,4));
        tokens.add(new Token(Token.TokenTypes.IDENTIFIER,2,8,"type"));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,3,0));
        tokens.add(new Token(Token.TokenTypes.DEDENT,3,0));
        tokens.add(new Token(Token.TokenTypes.NEWLINE,3,0));

        Assertions.assertThrowsExactly(SyntaxErrorException.class, () -> new PlainEnglishParser(tokens).program());
    }

}
