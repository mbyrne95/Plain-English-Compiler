package PlainEnglish;

import PlainEnglish.AST.*;

import java.util.HashMap;
import java.util.Stack;

public class Interpreter {
    private final Program program;

    // every time a method is entered, push a hashmap with every variable
    Stack<HashMap<String, InterpreterDataType>> variables = new Stack<>();
    HashMap<String, TypeDef> typedefs = new HashMap<>();
    HashMap<String, Method> methods = new HashMap<>();

    //Program = (NEWLINE | TypeDef | Method)+
    public Interpreter(Program prog) {
        program = prog;
    }

    //todo:this
    public void Start() {
    }

    // executes body of a method
    //Method = "To" {name}IDENTIFIER ({ignore}"a" {className}IDENTIFIER)? ("with" Parameter ("," Parameter)*)? NEWLINE+ StatementBlock
    void DoMethod(Method m) {
        DoStatementBlock(m.statementblock);
    }

    // iterates through each statement, separated out for
    //StatementBlock = INDENT Statement+ DEDENT
    private void DoStatementBlock(StatementBlock sb) {
        for (Statement s : sb.statement)
            DoStatement(s);
    }

    // p much just sends statement to the respective function
    // Statement = If  | Loop | Set | Make | FunctionCall
    void DoStatement(Statement s) {
        if (s.$if.isPresent())
            DoIf(s.$if.get());
        else if (s.loop.isPresent())
            DoLoop(s.loop.get());
        else if (s.set.isPresent())
            DoSet(s.set.get());
        else if (s.make.isPresent())
            DoMake(s.make.get());
        else if (s.functioncall.isPresent())
            DoFunctionCall(s.functioncall.get());
        else
            throw new RuntimeException("Unrecognized statement.");
    }

    //todo: this
    //FunctionCall = {name}IDENTIFIER {obj}IDENTIFIER? ({ignore}"with" {parameter}Expression ("," {parameter}Expression)* )? NEWLINE+
    void DoFunctionCall(FunctionCall f) {}

    // checks do bool term for result of boolean expression, does appropriate stmnt block depending on
    //If = "If" BoolExpTerm NEWLINE+ StatementBlock ("else" NEWLINE {falseCase}StatementBlock)?
    void DoIf(If i) {
        // if the result of the boolean expression is true, do the corresponding statemnt block
        if (DoBoolTerm(i.boolexpterm))
            DoStatementBlock(i.statementblock);
        // otherwise, check for false case and do its statemnt block
        else if (i.falseCase.isPresent())
            DoStatementBlock(i.falseCase.get());
    }

    // similar to if, checks bool expression results and does stmnt while
    // Loop = "Loop" BoolExpTerm NEWLINE+ StatementBlock
    void DoLoop(Loop l) {
        while (DoBoolTerm(l.boolexpterm))
            DoStatementBlock(l.statementblock);
    }

    // checks and returns value of the boolean expression
    // BoolExpTerm = BoolExpFactor (("and"|"or") BoolExpTerm)* | "not" {notTerm}BoolExpTerm
    boolean DoBoolTerm(BoolExpTerm b) {
        //check the 'not' flag - if it + the not term is present then return inverse doboolterm on notTerm
        if (b.not && b.notTerm.isPresent())
            return !DoBoolTerm(b.notTerm.get());
        boolean result = false;
        if (b.boolexpfactor.isPresent())
            result = DoBoolFactor(b.boolexpfactor.get());
        // iterate while we have and/or
        for (int i = 0; i < b.theandORor.size(); i++) {
            // get the right expression, then and or or with current result
            boolean r = DoBoolTerm(b.boolexpterm.get(i));
            if (b.theandORor.get(i) == andORor.and)
                result = result && r;
            else
                result = result || r;
        }
        return result;
    }

    // // BoolExpFactor = ({lhs}Expression {compareOps}("=="|"!="|"<="|">="|">"|"<") {rhs}Expression) | VariableReference
    boolean DoBoolFactor(BoolExpFactor b) {
        // first we gotta check if everything is present, if it is, we need to evaluate based on the compare ops
        if (b.lhs.isPresent() && b.thecompareOps.isPresent() && b.rhs.isPresent()){
            
        }
    }

    //todo: write method that compares datatypes based on the compareOp
    private boolean CompareBooleanFactor(){
        return false;
    }

    //Set = "Set" VariableReference "to" Expression NEWLINE+
    void DoSet (Set s) {}

    //Expression = Term ( ("+"|"-") Term )*
    InterpreterDataType DoExpression(Expression e) {
        //set result equal to the first term, for now
        InterpreterDataType result = DoTerm(e.term.get(0));
        //while we still have a plus or hyphen, iterate
        for (int i = 0; i < e.theplusORhyphen.size(); i++) {
            InterpreterDataType right = DoTerm(e.term.get(i+1));
            // for now, just throw if we aren't working with numbers
            // todo: determine if we need to handle more sophisticated typechecking
            if (!(result instanceof NumberInterpreterDataType) || !(right instanceof NumberInterpreterDataType))
                throw new RuntimeException("Cannot use +/-  on non-numeric types");
            NumberInterpreterDataType plusResult = new NumberInterpreterDataType();
            float resultVal = ((NumberInterpreterDataType) result).value;
            float rightVal = ((NumberInterpreterDataType) right).value;

            // plusOrHypen is a +
            if (e.theplusORhyphen.get(i) == plusORhyphen.plus) {
                plusResult.value = resultVal + rightVal;
            }
            else {
                plusResult.value = resultVal - rightVal;
            }
            result = plusResult;
        }
        return result;
    }

    //Term = Factor ( ("*"|"/"|"%") Factor )*
    //    public LinkedList<Factor> factor = new LinkedList<>();
    //    public LinkedList<asteriskORslashORpercent> theasteriskORslashORpercent = new LinkedList<>();
    InterpreterDataType DoTerm(Term t) {
        // create the result, for now, it should just be the first factor
        InterpreterDataType result = DoFactor(t.factor.get(0));
        for (int i = 0; i < t.theasteriskORslashORpercent.size(); i++) {    // iterate while we have more operands
            InterpreterDataType right = DoFactor(t.factor.get(i+1));    // get right val + convert to float
            // todo: determine if we need to handle more sophisticated typechecking
            if (!(result instanceof NumberInterpreterDataType) || !(right instanceof NumberInterpreterDataType))
                throw new RuntimeException("Cannot use +/-  on non-numeric types");
            float rightVal = ((NumberInterpreterDataType) right).value;
            float leftVal = ((NumberInterpreterDataType) result).value;
            NumberInterpreterDataType operationResult = new NumberInterpreterDataType();
            // check the operand and perform the operation
            if (t.theasteriskORslashORpercent.get(i) == asteriskORslashORpercent.asterisk)
                operationResult.value = rightVal * leftVal;
            else if (t.theasteriskORslashORpercent.get(i) == asteriskORslashORpercent.slash)
                operationResult.value = leftVal / rightVal;
            else
                operationResult.value = leftVal % rightVal;
            result = operationResult;
        }
        return result;
    }

    //Factor = NUMBER | VariableReference | "true" | "false" | STRINGLITERAL | CHARACTERLITERAL | {ignore}"(" Expression {ignore}")"
    //  public Optional<String> number = Optional.empty();
    //  public Optional<VariableReference> variablereference= Optional.empty();
    //  public boolean $true;
    //  public boolean $false;
    //  public Optional<String> stringliteral= Optional.empty();
    //  public Optional<String> characterliteral= Optional.empty();
    //  public Optional<Expression> expression= Optional.empty();
    InterpreterDataType DoFactor(Factor f) {
        // basically just convert into an interpreter data type based on what member the factor hsa
        if (f.number.isPresent()){
            NumberInterpreterDataType n = new NumberInterpreterDataType();
            float result = Float.parseFloat(f.number.get());
            n.value = result;
            return n;
        }
        // variable reference
        if (f.variablereference.isPresent()){
            //    public String name;
            //    public boolean of;
            //    public Optional<String> $object = Optional.empty();
            VariableReference v = f.variablereference.get();
            // if of is set, we need to look up the object and get its members
            if (v.of) {
                // iterate through the variable stack, see if we have an object of that name
                for (int i = variables.size(); i >= 0; i--) { // top to bottom so we check current first
                    String vrObjectName = v.$object.get();
                    if (variables.get(i).containsKey(vrObjectName)) {
                        InterpreterDataType j = variables.get(i).get(vrObjectName);
                        // type check to make sure that the object is actually an object
                        if (j instanceof ObjectInterpreterDataType o)
                            return o.fields.get(v.name);
                        throw new RuntimeException(vrObjectName + " not an object");
                    }
                }
                // if we iterate over the entire stack and it doesn't exist, its undefined and an exception
                throw new RuntimeException(v.$object.get()+" undefined");
            }
            else {
                // otherwise, just search the stack for v,name
                for (int i = variables.size(); i >= 0; i--) {
                    if (variables.get(i).containsKey(v.name))
                        return variables.get(i).get(v.name);
                }
                throw new RuntimeException(v.$object.get()+" undefined");
            }
        }
        // $true or $true
        if (f.$true) {
            BooleanInterpreterDataType b = new BooleanInterpreterDataType();
            b.value = true;
            return b;
        }
        if (f.$false) {
            BooleanInterpreterDataType b = new BooleanInterpreterDataType();
            b.value = false;
            return b;
        }
        // string literal
        if (f.stringliteral.isPresent()) {
            StringInterpreterDataType s = new StringInterpreterDataType();
            s.value = f.stringliteral.get();
            return s;
        }
        // char literal
        if (f.characterliteral.isPresent()) {
            StringInterpreterDataType s = new StringInterpreterDataType();
            s.value = f.characterliteral.get();
            return s;
        }
        // expression
        if (f.expression.isPresent())
            return DoExpression(f.expression.get());
        throw new RuntimeException("undetermined factor");
    }

    //Make = "Make" {type}IDENTIFIER "named" {name}IDENTIFIER NEWLINE+
    //    public String type;
    //    public String name;
    void DoMake (Make m) {
        if (m.type.toLowerCase().equals("number")){
            NumberInterpreterDataType n = new NumberInterpreterDataType();
            n.value = 0;
            variables.peek().put(m.name, n);
        }
        else if (m.type.toLowerCase().equals("string")) {
            StringInterpreterDataType s = new StringInterpreterDataType();
            s.value = "";
            variables.peek().put(m.name, s);
        }
        else if (m.type.toLowerCase().equals("boolean")) {
            BooleanInterpreterDataType b = new BooleanInterpreterDataType();
            b.value = false;
            variables.peek().put(m.name, b);
        }
        // otherwise, it could be an existing object - check typedefs for it
        else {
            if (typedefs.get(m.type) == null)
                throw new RuntimeException("undefined type");
            // otherwise, we need to make a blank object
            ObjectInterpreterDataType o = new ObjectInterpreterDataType();
            o.type = m.type;
            // then iterate through its fields and fill with default values
            TypeDef t = typedefs.get(m.type);
            for (int i = 0; i < t.field.size(); i++) {
                Field f = t.field.get(i);
                // repeat the above checks, filling in field with the same default values
                if (f.type.toLowerCase().equals("number")){
                    NumberInterpreterDataType n = new NumberInterpreterDataType();
                    n.value = 0;
                    o.fields.put(f.name, n);
                }
                else if (f.type.toLowerCase().equals("string")) {
                    StringInterpreterDataType s = new StringInterpreterDataType();
                    s.value = "";
                    o.fields.put(f.name, s);
                }
                else if (f.type.toLowerCase().equals("boolean")) {
                    BooleanInterpreterDataType b = new BooleanInterpreterDataType();
                    b.value = false;
                    o.fields.put(f.name, b);
                }
            }
            variables.peek().put(m.name,o);
        }
    }
}
