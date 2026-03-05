package PlainEnglish;

import PlainEnglish.AST.*;

import java.util.LinkedList;
import java.util.Optional;

public class PlainEnglishParser  {
    public PlainEnglishParser(LinkedList<Token> tokensIn) {
        tm = new TokenManager(tokensIn);
    }

    private final TokenManager tm;

    public Optional<Program> program() throws SyntaxErrorException {
        Program p = new Program();

        // iterate through the token until the list is empty
        while (!tm.isDone()) {
            // skip empty lines if we are at indent 0 - matchandremove newline token, if the returned optional is .present(), continue
            if (tm.matchAndRemove(Token.TokenTypes.NEWLINE).isPresent())
                continue;

            Optional<TypeDef> t = typeDef();
            if (t.isPresent()){
                p.typedef.add(t.get());
                continue;
            }

        }
        return Optional.of(p);
    }

    //typedef requires name and field
    private Optional<TypeDef> typeDef() throws SyntaxErrorException {
        // if we don't have A IDENTIFIER or AN IDENTIFIER, we don't have a typedef and just return empty
        // DM: if !(a or b) == if !a and !b
        if ( (!tm.nextTwoTokensMatch(Token.TokenTypes.A, Token.TokenTypes.IDENTIFIER)) && (!tm.nextTwoTokensMatch(Token.TokenTypes.AN, Token.TokenTypes.IDENTIFIER)))
            return Optional.empty();

        // additionally need an IS, if we don't have an IS we're not in a typedef
        if ( (tm.peek(2).isPresent()) && (tm.peek(2).get().Type != Token.TokenTypes.IS) )
            return Optional.empty();

        // if we got here, we can remove the A or AN, as well as IDENTIFIER and IS
        tm.matchAndRemove(Token.TokenTypes.A);
        tm.matchAndRemove(Token.TokenTypes.AN);
        Optional<Token> name = tm.matchAndRemove(Token.TokenTypes.IDENTIFIER);
        if (name.isEmpty())
            throw new SyntaxErrorException("No name in typedef", tm.getCurrentLine(), tm.getCurrentColumn());
        tm.matchAndRemove(Token.TokenTypes.IS);

        TypeDef t = new TypeDef();
        t.name = name.get().Value.orElse("");

        // require new line, we finished the declaration now move on to the members
        requireNewLine();
        if (tm.matchAndRemove(Token.TokenTypes.INDENT).isEmpty())
            throw new SyntaxErrorException("Missing indent level", tm.getCurrentLine(), tm.getCurrentColumn());

        // loop while we don't have a dedent, finding a dedent means field is over
        while (tm.matchAndRemove(Token.TokenTypes.DEDENT).isEmpty()) {
            //skip newlines
            if (tm.matchAndRemove(Token.TokenTypes.NEWLINE).isPresent())
                continue;

            Optional<Field> f = field();
            if (f.isPresent())
                t.field.add(f.get());
            else
                throw new SyntaxErrorException("No field in typedef", tm.getCurrentLine(), tm.getCurrentColumn());
        }

        return Optional.of(t);
    }

    //field requires name and type
    /*
    "A Student is\n" +
                "\n" +
                "    string firstname\n" + <- field
     */
    private Optional<Field> field() throws SyntaxErrorException {
        Optional<Token> type = tm.matchAndRemove(Token.TokenTypes.IDENTIFIER); // string, number, etc
        if (type.isEmpty()) // we misidentified and need to just return
                return Optional.empty();

        Optional<Token> name = tm.matchAndRemove(Token.TokenTypes.IDENTIFIER);
        if (name.isEmpty()) //if we failed to get a name here, it's an actual syntax error
            throw new SyntaxErrorException("No name after type in field", tm.getCurrentLine(), tm.getCurrentColumn());

        requireNewLine();

        Field f = new Field();
        f.type = type.get().Value.orElse("");
        f.name = name.get().Value.orElse("");

        return Optional.of(f);
    }

    // helper to eat newline tokens
    private void requireNewLine() throws SyntaxErrorException {
        // if there isn't at least one (REQUIRE new line), throw
        if (tm.matchAndRemove(Token.TokenTypes.NEWLINE).isEmpty())
            throw new SyntaxErrorException("Could not find expected newline", tm.getCurrentLine(), tm.getCurrentColumn());

        // check if the currentToken is present and value is newline - remove while that is the case
        while ( tm.peek(0).isPresent() && tm.peek(0).get().Type == Token.TokenTypes.NEWLINE )
            tm.matchAndRemove(Token.TokenTypes.NEWLINE);
    }
}

