package PlainEnglish;

import PlainEnglish.AST.*;

import java.util.LinkedList;
import java.util.Optional;

//TODO: clean up comments :D
public class PlainEnglishParser  {
    public PlainEnglishParser(LinkedList<Token> tokensIn) {
        tm = new TokenManager(tokensIn);
    }

    private final TokenManager tm;

    //Program = (NEWLINE | TypeDef | Method)+
    public Optional<Program> program() throws SyntaxErrorException {
        Program p = new Program();

        // iterate through the token until the list is empty
        while (!tm.isDone()) {
            // skip empty lines if we are at indent 0 - matchandremove newline token, if the returned optional is .present(), continue
            if (tm.matchAndRemove(Token.TokenTypes.NEWLINE).isPresent())
                continue;

            // check for type definition
            Optional<TypeDef> t = typeDef();
            if (t.isPresent()){
                p.typedef.add(t.get());
                continue;
            }

            // check for method
            Optional<Method> m = method();
            if (m.isPresent()){
                p.method.add(m.get());
                continue;
            }

            // if we didn't get the above just assume it was an unexpected token
            throw new SyntaxErrorException("unexpected token", tm.getCurrentLine(), tm.getCurrentColumn());
        }
        return Optional.of(p);
    }

    //typedef requires name and field
    //TypeDef = {ignore}("A" | "An") {name}IDENTIFIER "is" NEWLINE+ INDENT Field+ DEDENT {ignore}NEWLINE*
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

        //get rid of newlines
        while(tm.peek(0).isPresent() && tm.peek(0).get().Type == Token.TokenTypes.NEWLINE)
            tm.matchAndRemove(Token.TokenTypes.NEWLINE);

        return Optional.of(t);
    }

    //field requires name and type
    /*
    "A Student is\n" +
                "\n" +
                "    string firstname\n" + <- field
     */
    //Field = {type}IDENTIFIER {name}IDENTIFIER NEWLINE+
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
        if (tm.matchAndRemove(Token.TokenTypes.NEWLINE).isEmpty()) {
            if (tm.isDone())
                return;
            if (tm.peek(0).isPresent() && tm.peek(0).get().Type == Token.TokenTypes.DEDENT)
                return;
            throw new SyntaxErrorException("Could not find expected newline", tm.getCurrentLine(), tm.getCurrentColumn());
        }
        // check if the currentToken is present and value is newline - remove while that is the case
        while ( tm.peek(0).isPresent() && tm.peek(0).get().Type == Token.TokenTypes.NEWLINE )
            tm.matchAndRemove(Token.TokenTypes.NEWLINE);
    }

    /*
        public String name;
        public Optional<String> className = Optional.empty();
        public boolean with;
        public LinkedList<Parameter> parameter = new LinkedList<>();
        public StatementBlock statementblock;
     */
    //Method = "To" {name}IDENTIFIER ({ignore}"a" {className}IDENTIFIER)? ("with" Parameter ("," Parameter)*)? NEWLINE+ StatementBlock
    private Optional<Method> method() throws SyntaxErrorException {
        // check for the mandatory to, return empty if it's not there
        if (tm.matchAndRemove(Token.TokenTypes.TO).isEmpty())
            return Optional.empty();

        //get the name identifier - throw exception if not found
        Optional<Token> name = tm.matchAndRemove(Token.TokenTypes.IDENTIFIER);
        if (name.isEmpty())
            throw new SyntaxErrorException("no method name", tm.getCurrentLine(), tm.getCurrentColumn());

        Method m = new Method();
        m.name = name.get().Value.orElse("");

        //get optional {ignore}"a" {className}IDENTIFIER - throw if A but no className
        if (tm.matchAndRemove(Token.TokenTypes.A).isPresent()) {
            Optional<Token> className = tm.matchAndRemove(Token.TokenTypes.IDENTIFIER);
            if (className.isEmpty())
                throw new SyntaxErrorException("expected a classname", tm.getCurrentLine(), tm.getCurrentColumn());
            // needs to be optional String
            String n = className.get().Value.orElse("");
            m.className = Optional.of(n);
        }

        //get optional "with" Parameter ("," Parameter)*
        if (tm.matchAndRemove(Token.TokenTypes.WITH).isPresent()) {
            m.with = true;
            m.parameter.add(parameter());
            while (tm.matchAndRemove(Token.TokenTypes.COMMA).isPresent())
                m.parameter.add(parameter());
        }
        // NEWLINE+ StatementBlock
        requireNewLine();
        m.statementblock = statementBlock();
        return Optional.of(m);
    }

    /*
        public String paramType;
        public boolean named;
        public Optional<String> nameOverride = Optional.empty();
     */
    //Parameter = {paramType}IDENTIFIER ("named" {nameOverride}IDENTIFIER)?
    private Parameter parameter() throws SyntaxErrorException {
        Parameter p = new Parameter();
        Optional<Token> type = tm.matchAndRemove(Token.TokenTypes.IDENTIFIER);
        if (type.isEmpty())
            throw new SyntaxErrorException("couldn't find identifier parameter", tm.getCurrentLine(), tm.getCurrentColumn());
        p.paramType = type.get().Value.orElse("");

        //check if it's NAMED
        if (tm.matchAndRemove(Token.TokenTypes.NAMED).isPresent()) {
            p.named = true;
            Optional<Token> name = tm.matchAndRemove(Token.TokenTypes.IDENTIFIER);
            // throw if NAMED but no identifier after
            if (name.isEmpty())
                throw new SyntaxErrorException("expected identifier after NAMED", tm.getCurrentLine(), tm.getCurrentColumn());
            // optional<string>
            String nameString = name.get().Value.orElse("");
            p.nameOverride = Optional.of(nameString);
        } else if (tm.peek(0).isPresent() && tm.peek(0).get().Type == Token.TokenTypes.IDENTIFIER) {
            // an identifier follows the type but 'named' was missing - give a helpful error
            throw new SyntaxErrorException(
                    "expected 'named' before parameter name for type '" + p.paramType + "'",
                    tm.getCurrentLine(), tm.getCurrentColumn());
        }
        return p;
    }

    // linked list of statements
    // StatementBlock = INDENT Statement+ DEDENT
    private StatementBlock statementBlock() throws SyntaxErrorException {
        StatementBlock sb = new StatementBlock();
        if (tm.matchAndRemove(Token.TokenTypes.INDENT).isEmpty())
            throw new SyntaxErrorException("expected indent in statement block", tm.getCurrentLine(), tm.getCurrentColumn());

        // iterate while we DONT have a dedent
        while (tm.matchAndRemove(Token.TokenTypes.DEDENT).isEmpty()) {
            // if we get a newline it doesn't matter just continue
            if (tm.matchAndRemove(Token.TokenTypes.NEWLINE).isPresent())
                continue;

            // otherwise build the statement
            Optional<Statement> s = statement();
            if (s.isPresent())
                sb.statement.add(s.get());
            else
                // we didn't get a statement even though we got a newline+didn't dedent
                throw new SyntaxErrorException("expected statement in statement block",tm.getCurrentLine(), tm.getCurrentColumn());
        }
        return sb;
    }

    /*
        public Optional<If> $if = Optional.empty();
        public Optional<Loop> loop = Optional.empty();
        public Optional<Set> set = Optional.empty();
        public Optional<Make> make = Optional.empty();
        public Optional<FunctionCall> functioncall = Optional.empty();
     */
    //Statement = If  | Loop | Set | Make | FunctionCall
    private Optional<Statement> statement() throws SyntaxErrorException {
        Optional<If> ifToken = ifStatement();
        if (ifToken.isPresent()) {
            Statement s = new Statement();
            s.$if = ifToken;
            return Optional.of(s);
        }
        Optional<Loop> loopToken = loop();
        if (loopToken.isPresent()) {
            Statement s = new Statement();
            s.loop = loopToken;
            return Optional.of(s);
        }
        Optional<Set> setToken = set();
        if (setToken.isPresent()) {
            Statement s = new Statement();
            s.set = setToken;
            return Optional.of(s);
        }
        Optional<Make> makeToken = make();
        if (makeToken.isPresent()) {
            Statement s = new Statement();
            s.make = makeToken;
            return Optional.of(s);
        }
        Optional<FunctionCall> functionToken = functionCall();
        if (functionToken.isPresent()) {
            Statement s = new Statement();
            s.functioncall = functionToken;
            return Optional.of(s);
        }

        // if we got here just return an empty, we didn't get a real statement
        return Optional.empty();
    }

    /*
        public VariableReference variablereference;
        public Expression expression;
     */
    //Set = "Set" VariableReference "to" Expression NEWLINE+
    private Optional<Set> set() throws SyntaxErrorException {
        // first check set
        if (tm.matchAndRemove(Token.TokenTypes.SET).isEmpty())
            return Optional.empty();

        // then get variable references
        // stubbed
        Optional<VariableReference> v = variableReference();
        if (v.isEmpty())
            throw new SyntaxErrorException("expected variable reference after set", tm.getCurrentLine(), tm.getCurrentColumn());

        if (tm.matchAndRemove(Token.TokenTypes.TO).isEmpty())
            throw new SyntaxErrorException("expected to after variable reference", tm.getCurrentLine(), tm.getCurrentColumn());

        //expression - stubbed
        Optional<Expression> e = expression();
        if (e.isEmpty())
            throw new SyntaxErrorException("expected expression after to in set", tm.getCurrentLine(), tm.getCurrentColumn());

        // make set
        Set s = new Set();
        s.variablereference = v.get();
        s.expression = e.get();
        requireNewLine();
        return Optional.of(s);
    }

    /*
        public String name;
        public boolean of;
        public Optional<String> $object = Optional.empty();
     */
    //VariableReference = {name}IDENTIFIER ("of" {object}IDENTIFIER)?
    private Optional<VariableReference> variableReference() throws SyntaxErrorException {
        Optional<Token> name = tm.matchAndRemove(Token.TokenTypes.IDENTIFIER);
        if (name.isEmpty())
            return Optional.empty(); // let factor deal with the issue

        VariableReference v = new VariableReference();
        v.name = name.get().Value.orElse("");
        // if there isn't an of, we can just return
        if (tm.matchAndRemove(Token.TokenTypes.OF).isEmpty()) {
            v.of = false;
            return Optional.of(v);
        }
        v.of = true;
        Optional<Token> o = tm.matchAndRemove(Token.TokenTypes.IDENTIFIER);
        if (o.isEmpty())
            throw new SyntaxErrorException("expected identifier after OF", tm.getCurrentLine(), tm.getCurrentColumn());
        v.$object = Optional.of(o.get().Value.orElse(""));
        return Optional.of(v);
    }

    /*
        public String type;
        public String name;
     */
    //Make = "Make" {type}IDENTIFIER "named" {name}IDENTIFIER NEWLINE+
    private Optional<Make> make() throws SyntaxErrorException {
        // make
        if (tm.matchAndRemove(Token.TokenTypes.MAKE).isEmpty())
            return Optional.empty();

        //identifier
        Optional<Token> type = tm.matchAndRemove(Token.TokenTypes.IDENTIFIER);
        if (type.isEmpty())
            throw new SyntaxErrorException("expected identifier after make", tm.getCurrentLine(), tm.getCurrentColumn());

        // named
        if (tm.matchAndRemove(Token.TokenTypes.NAMED).isEmpty())
            throw new SyntaxErrorException("expected named after identifier in make", tm.getCurrentLine(), tm.getCurrentColumn());

        // identifier
        Optional<Token> name = tm.matchAndRemove(Token.TokenTypes.IDENTIFIER);
        if (name.isEmpty())
            throw new SyntaxErrorException("expected identifier after named", tm.getCurrentLine(), tm.getCurrentColumn());

        requireNewLine();

        Make m = new Make();
        m.type = type.get().Value.orElse("");
        m.name = name.get().Value.orElse("");
        return Optional.of(m);
    }


    /*
        public BoolExpTerm boolexpterm;
        public StatementBlock statementblock;
        public boolean $else;
        public Optional<StatementBlock> falseCase= Optional.empty();
     */
    //If = "If" BoolExpTerm NEWLINE+ StatementBlock ("else" NEWLINE {falseCase}StatementBlock)?
    private Optional<If> ifStatement() throws SyntaxErrorException {
        // check the if
        If i = new If();
        if (tm.matchAndRemove(Token.TokenTypes.IF).isEmpty())
            return Optional.empty();

        //get the condition (bool exp term)
        Optional<BoolExpTerm> condition = boolExpTerm();
        if (condition.isEmpty())
            throw new SyntaxErrorException("expected boolean exp term after IF", tm.getCurrentLine(), tm.getCurrentColumn());
        i.boolexpterm = condition.get();

        // newline, then statement block
        requireNewLine();
        i.statementblock = statementBlock();

        // optional else statement new lne statement block
        if (tm.matchAndRemove(Token.TokenTypes.ELSE).isPresent()) {
            i.$else = true;
            requireNewLine();
            i.falseCase = Optional.of(statementBlock());
        }

        return Optional.of(i);
    }

    /*
        public BoolExpTerm boolexpterm;
        public StatementBlock statementblock;
     */
    //Loop = "Loop" BoolExpTerm NEWLINE+ StatementBlock
    private Optional<Loop> loop() throws SyntaxErrorException {
        // same as if
        Loop l = new Loop();
        if (tm.matchAndRemove(Token.TokenTypes.LOOP).isEmpty())
            return Optional.empty();
        Optional<BoolExpTerm> boolExpTerm = boolExpTerm();
        if (boolExpTerm.isEmpty())
            throw new SyntaxErrorException("expected boolean exp term after LOOP", tm.getCurrentLine(), tm.getCurrentColumn());
        l.boolexpterm = boolExpTerm.get();
        requireNewLine();
        l.statementblock = statementBlock();

        return Optional.of(l);
    }

    /*
        public String name;
        public Optional<String> obj= Optional.empty();
        public LinkedList<Expression> parameter= new LinkedList<>();
     */
    // FunctionCall = {name}IDENTIFIER {obj}IDENTIFIER? ({ignore}"with" {parameter}Expression ("," {parameter}Expression)* )? NEWLINE+
    private Optional<FunctionCall> functionCall() throws SyntaxErrorException {
        FunctionCall f = new FunctionCall();
        Optional<Token> id1 = tm.matchAndRemove(Token.TokenTypes.IDENTIFIER);
        if (id1.isEmpty())
            return Optional.empty();
        f.name = id1.get().Value.orElse("");

        //get optional second identifier
        if (tm.peek(0).get().Type == Token.TokenTypes.IDENTIFIER) {
            Optional<Token> id2 = tm.matchAndRemove(Token.TokenTypes.IDENTIFIER);
            String str = id2.get().Value.orElse("");
            f.obj = Optional.of(str);
        }
        // with expression (, expression)* newline
        if (tm.matchAndRemove(Token.TokenTypes.WITH).isPresent()) {
            Optional<Expression> e = expression();
            if (e.isEmpty())
                throw new SyntaxErrorException("expected expression after WITH", tm.getCurrentLine(), tm.getCurrentColumn());
            f.parameter.add(e.get());
            // loop while we have comma
            while(tm.matchAndRemove(Token.TokenTypes.COMMA).isPresent()){
                Optional<Expression> ex = expression();
                if (ex.isEmpty())
                    throw new SyntaxErrorException("expected expression after COMMA", tm.getCurrentLine(), tm.getCurrentColumn());
                f.parameter.add(ex.get());
            }
        }
        requireNewLine();
        return Optional.of(f);
    }

    /*
        public LinkedList<Term> term= new LinkedList<>();
        public LinkedList<plusORhyphen> theplusORhyphen= new LinkedList<>();
    */
    //Expression = Term ( ("+"|"-") Term )*
    private Optional<Expression> expression() throws SyntaxErrorException {
        Expression e = new Expression();
        e.term.add(term().get());

        // similar to term - loop get + or - and a term
        while ( tm.peek(0).isPresent() &&
                (tm.peek(0).get().Type == Token.TokenTypes.PLUS ||
                tm.peek(0).get().Type == Token.TokenTypes.HYPHEN) ) {
            plusORhyphen i;
            if (tm.matchAndRemove(Token.TokenTypes.PLUS).isPresent()) {
                i = plusORhyphen.plus;
            }
            else{
                tm.matchAndRemove(Token.TokenTypes.HYPHEN); //we still need to consume tokem
                i = plusORhyphen.hyphen;

            }
            e.theplusORhyphen.add(i);
            e.term.add(term().get());
        }

        return Optional.of(e);
    }

    /*
        public LinkedList<Factor> factor = new LinkedList<>();
        public LinkedList<asteriskORslashORpercent> theasteriskORslashORpercent = new LinkedList<>();
     */
    // Term = Factor ( ("*"|"/"|"%") Factor )*
    private Optional<Term> term() throws SyntaxErrorException {
        Term t = new Term();
        t.factor.add(factor().get());

        // we need to loop and get an asterisk/slash/percent and a factor
        // get the symbol, add it to the list, get the factor, add it to ist
        while ( tm.peek(0).isPresent() && (
                tm.peek(0).get().Type == Token.TokenTypes.ASTERISK ||
                tm.peek(0).get().Type == Token.TokenTypes.SLASH ||
                tm.peek(0).get().Type == Token.TokenTypes.PERCENT )) {
            asteriskORslashORpercent i;
            if (tm.matchAndRemove(Token.TokenTypes.ASTERISK).isPresent())
                i = asteriskORslashORpercent.asterisk;
            else if (tm.matchAndRemove(Token.TokenTypes.SLASH).isPresent())
                i = asteriskORslashORpercent.slash;
            else{
                tm.matchAndRemove(Token.TokenTypes.PERCENT); // still need to consume token
                i = asteriskORslashORpercent.percent;
            }

            t.theasteriskORslashORpercent.add(i);
            t.factor.add(factor().get());
        }
        return Optional.of(t);
    }

    /*
        public Optional<String> number = Optional.empty();
        public Optional<VariableReference> variablereference= Optional.empty();
        public boolean $true;
        public boolean $false;
        public Optional<String> stringliteral= Optional.empty();
        public Optional<String> characterliteral= Optional.empty();
        public Optional<Expression> expression= Optional.empty();
     */
    //Factor = NUMBER | VariableReference | "true" | "false" | STRINGLITERAL | CHARACTERLITERAL | {ignore}"(" Expression {ignore}")"
    private Optional<Factor> factor() throws SyntaxErrorException {
        Factor f = new Factor();
        // get number first
        Optional<Token> t = tm.matchAndRemove(Token.TokenTypes.NUMBER);
        if (t.isPresent()){
            // convert to optional string and assign to number
            Optional<String> str = Optional.of(t.get().Value.orElse(""));
            f.number = str;
            return Optional.of(f);
        }

        //var reference
        // t = tm.matchAndRemove(Token.TokenTypes.IDENTIFIER);
        // can't consume identifier - variable reference needs it
        if (tm.peek(0).isPresent() && tm.peek(0).get().Type == Token.TokenTypes.IDENTIFIER) {
            f.variablereference = variableReference();
            return Optional.of(f);
        }

        // check true/false (deceptively, sest false to true if it's the one)
        if (tm.matchAndRemove(Token.TokenTypes.TRUE).isPresent()) {
            f.$true = true;
            return Optional.of(f);
        }
        if (tm.matchAndRemove(Token.TokenTypes.FALSE).isPresent()) {
            f.$false = true;
            return Optional.of(f);
        }

        // check for string literal
        t = tm.matchAndRemove(Token.TokenTypes.STRINGLITERAL);
        if (t.isPresent()) {
            Optional<String> str = Optional.of(t.get().Value.orElse(""));
            f.stringliteral = str;
            return Optional.of(f);
        }

        // char literal
        t = tm.matchAndRemove(Token.TokenTypes.CHARACTERLITERAL);
        if (t.isPresent()) {
            Optional<String> str = Optional.of(t.get().Value.orElse(""));
            f.characterliteral = str;
            return Optional.of(f);
        }

        // open paren expression close paren
        if (tm.matchAndRemove(Token.TokenTypes.OPENPAREN).isPresent()) {
            f.expression = expression();
            // throw syntax if there isn't a close to follow
            if (tm.matchAndRemove(Token.TokenTypes.CLOSEPAREN).isEmpty())
                throw new SyntaxErrorException("expected )",tm.getCurrentLine(),tm.getCurrentColumn());
            return Optional.of(f);
        }

        // if we get all the way here we should actually throw a syntax error
        // can't just return optional empty because other methods are expecting an actual return
        throw new SyntaxErrorException("expected a char or string or number or bool or expression...", tm.getCurrentLine(), tm.getCurrentColumn());
    }

    /*
        public Optional<BoolExpFactor> boolexpfactor = Optional.empty();
        public LinkedList<andORor> theandORor = new LinkedList<>();
        public LinkedList<BoolExpTerm> boolexpterm = new LinkedList<>();
        public boolean not;
        public Optional<BoolExpTerm> notTerm = Optional.empty();
     */
    //BoolExpTerm =  BoolExpFactor (("and"|"or") BoolExpTerm)* | "not" {notTerm}BoolExpTerm
    private Optional<BoolExpTerm> boolExpTerm() throws SyntaxErrorException {
        // boolexpTerm = (asdfadf) (AND a)  || boolexpTerm = NOT (asdasdf)
        // check not first
        BoolExpTerm b = new BoolExpTerm();
        if(tm.matchAndRemove(Token.TokenTypes.NOT).isEmpty()) {
            b.not = false;
            b.boolexpfactor = boolExpFactor();

            // check for and/ors, repeat while they exist
            while (tm.peek(0).isPresent() &&
                    (tm.peek(0).get().Type == Token.TokenTypes.AND ||
                    tm.peek(0).get().Type == Token.TokenTypes.OR)){
                // eat the token and assign
                if (tm.matchAndRemove(Token.TokenTypes.AND).isPresent())
                    b.theandORor.add(andORor.and);
                else if (tm.matchAndRemove(Token.TokenTypes.OR).isPresent())
                    b.theandORor.add(andORor.or);
                Optional<BoolExpTerm> bet = boolExpTerm();
                if (bet.isEmpty())
                    throw new SyntaxErrorException("expected boolean term after conditional",tm.getCurrentLine(),tm.getCurrentColumn());
                b.boolexpterm.add(bet.get());
            }
            return Optional.of(b);
        }
        // if not is present, then just not + expterm
        b.not = true;
        Optional<BoolExpTerm> recurse = boolExpTerm();
        if (recurse.isEmpty())
            throw new SyntaxErrorException("expected boolean expression after NOT",tm.getCurrentLine(),tm.getCurrentColumn());
        b.notTerm = recurse;
        return Optional.of(b);
    }

    /*
        public Optional<Expression> lhs = Optional.empty();
        public Optional<compareOps> thecompareOps = Optional.empty();
        public Optional<Expression> rhs = Optional.empty();
        public Optional<VariableReference> variablereference = Optional.empty();
     */
    // BoolExpFactor = ({lhs}Expression {compareOps}( "==" | "!=" | "<=" | ">=" | ">" | "<" ) {rhs}Expression) | VariableReference
    private Optional<BoolExpFactor> boolExpFactor() throws SyntaxErrorException {
        BoolExpFactor b = new BoolExpFactor();

        // get expression, expression() throws if we get empty
        Optional<Expression> lhs = expression();

        // try to get one of the compare operators
        if (tm.matchAndRemove(Token.TokenTypes.DOUBLEEQUAL).isPresent())
            b.thecompareOps = Optional.of(compareOps.doubleequal);
        else if (tm.matchAndRemove(Token.TokenTypes.LESSTHAN).isPresent())
            b.thecompareOps = Optional.of(compareOps.lessthan);
        else if (tm.matchAndRemove(Token.TokenTypes.LESSTHANEQUAL).isPresent())
            b.thecompareOps = Optional.of(compareOps.lessthanequal);
        else if (tm.matchAndRemove(Token.TokenTypes.GREATERTHAN).isPresent())
            b.thecompareOps = Optional.of(compareOps.greaterthan);
        else if (tm.matchAndRemove(Token.TokenTypes.GREATERTHANEQUAL).isPresent())
            b.thecompareOps = Optional.of(compareOps.greaterthanequal);
        else if (tm.matchAndRemove(Token.TokenTypes.NOTEQUAL).isPresent())
            b.thecompareOps = Optional.of(compareOps.notequal);

        // if the compare op is empty, then this is just a variable ref and we need to
        // get lhs's variable ref
        if (b.thecompareOps.isEmpty()) {
            Optional<VariableReference> vr = lhs.get().term.get(0).factor.get(0).variablereference;
            b.variablereference = vr;
            return Optional.of(b);
        }

        b.lhs = lhs;
        b.rhs = expression();
        if (b.rhs.isEmpty())
            throw new SyntaxErrorException("expected expression on right side of comparator", tm.getCurrentLine(), tm.getCurrentColumn());

        return Optional.of(b);
    }
}
