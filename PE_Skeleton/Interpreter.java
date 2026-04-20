package PlainEnglish;

import PlainEnglish.AST.*;

import java.util.HashMap;
import java.util.LinkedList;
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

    public void Start() {
        variables.push(new HashMap<>());
        //iterate through program and get the methods and typedefs
        for (Method m : program.method)
            methods.put(m.name, m);
        for (TypeDef t : program.typedef)
            typedefs.put(t.name, t);

        // manually put print, since it's a builtin
        methods.put("Print", new PrintBuiltInMethod());

        DoMethod(methods.get("Run"));
    }

    // executes body of a method
    //Method = "To" {name}IDENTIFIER ({ignore}"a" {className}IDENTIFIER)? ("with" Parameter ("," Parameter)*)? NEWLINE+ StatementBlock
    void DoMethod(Method m) {
        for (Statement s : m.statementblock.statement)
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

    //FunctionCall = {name}IDENTIFIER {obj}IDENTIFIER? ({ignore}"with" {parameter}Expression ("," {parameter}Expression)* )? NEWLINE+
    void DoFunctionCall(FunctionCall f) {
        //    public String name;
        //    public Optional<String> obj= Optional.empty();
        //    public LinkedList<Expression> parameter= new LinkedList<>();
        // check if the method exists in the hashmap, throw if it doesn't
        Method method = methods.get(f.name);
        if (method == null)
            throw new RuntimeException("unrecognized method call");

        // get all the parameters from expression into a datatype
        LinkedList<InterpreterDataType> values = new LinkedList<>();
        for (int i = 0; i < f.parameter.size(); i++)
            values.add(DoExpression(f.parameter.get(i)));

        // check if the method is a built in - if it is (print built in) it wont have statement block
        if (method instanceof BuiltInMethod builtin) {
            HashMap<String, InterpreterDataType> builtinParams = new HashMap<>();
            for (int i = 0; i < method.parameter.size(); i++) { // match parameter values and names and put in the local ma
                if (i >= values.size()) // out of bounds safety check
                    break;
                // if the parameter has a name override, use that
                if (method.parameter.get(i).nameOverride.isPresent())
                    builtinParams.put(method.parameter.get(i).nameOverride.get(), values.get(i));
                else    // otherwise use the param type string
                    builtinParams.put(method.parameter.get(i).paramType, values.get(i));
            }
            builtin.Execute(builtinParams); // execute with the builtinParams
            return;
        }

        HashMap<String, InterpreterDataType> paramsAndValues = new HashMap<>();
        // if object is present, search variable stack for it, then copy the members into params
        if (f.obj.isPresent()) {
            String fObjName = f.obj.get();
            for (int i = variables.size() - 1; i >= 0; i--) {
                if (variables.get(i).containsKey(fObjName)) {
                    InterpreterDataType foo = variables.get(i).get(fObjName);
                    // verify that it is an object, then move members
                    if ( foo instanceof  ObjectInterpreterDataType objDataType )
                        paramsAndValues.putAll(objDataType.fields);
                    // else break
                    break;
                }
            }
        }
        // copy param names and values, push to variable stack, do method
        for (int i = 0; i < method.parameter.size(); i++ ) {
            if (i >= values.size()) //oob safety
                break;
            if (method.parameter.get(i).nameOverride.isPresent())
                paramsAndValues.put(method.parameter.get(i).nameOverride.get(), values.get(i));
            else    // otherwise use the param type string
                paramsAndValues.put(method.parameter.get(i).paramType, values.get(i));
        }
        variables.push(paramsAndValues); // push so it goes to top
        DoMethod(method);

        // paramsAndValues in variables has updated values - need to update the f.obj fields with these values
        if (f.obj.isPresent()){
            for (int i = variables.size() - 1; i >= 0; i--) {
                if (variables.get(i).containsKey(f.obj.get())) {
                    // found obj in stack, verify that it's an object
                    InterpreterDataType foo = variables.get(i).get(f.obj.get());
                    if (foo instanceof ObjectInterpreterDataType objDataType){
                        // for every field in the object, if params contains that field, replace kvp
                        for (String field : objDataType.fields.keySet()) {
                            if (paramsAndValues.containsKey(field))
                                objDataType.fields.put(field, paramsAndValues.get(field));
                        }
                    }
                    // if it's  not an instance of object we need to break
                    break;
                }
            }
        }

        // similar to above - iterate through the parameters to update in the main scope
        for (int i = 0; i < f.parameter.size(); i++){
            if (i >= method.parameter.size()) // oob safety check (theoretically these should never get hit)
                break;
            // only looking for a variable reference, nothing else - if the parameter has more than 1 term we skip
            Expression e = f.parameter.get(i);
            // expression has linked list of terms
            if (e.term.size() > 1)
                continue;
            Term t = e.term.get(0);
            // term has linked list of factor
            if (t.factor.size() > 1)
                continue;
            // factor has a variable reference
            if (!t.factor.get(0).variablereference.isPresent())
                continue;
            // this is the callers variable name
            VariableReference vr = t.factor.get(0).variablereference.get();
            String val;

            if (method.parameter.get(i).nameOverride.isPresent())
                val = method.parameter.get(i).nameOverride.get();
            else
                val = method.parameter.get(i).paramType;

            // if we go variables.size - 1, we get the current scope
            // size - 2 is the scope of whatever called the method - this is the scope that needs updated
            variables.get(variables.size() - 2).put(vr.name, paramsAndValues.get(val));

        }
        variables.pop();
    }

    // checks do bool term for result of boolean expression, does appropriate stmnt block depending on
    //If = "If" BoolExpTerm NEWLINE+ StatementBlock ("else" NEWLINE {falseCase}StatementBlock)?
    void DoIf(If i) {
        // if the result of the boolean expression is true, do the corresponding statemnt block
        if (DoBoolTerm(i.boolexpterm))
            for (Statement s : i.statementblock.statement)
                DoStatement(s);
        // otherwise, check for false case and do its statemnt block
        else if (i.falseCase.isPresent())
            for (Statement s : i.falseCase.get().statement)
                DoStatement(s);
    }

    // similar to if, checks bool expression results and does stmnt while
    // Loop = "Loop" BoolExpTerm NEWLINE+ StatementBlock
    void DoLoop(Loop l) {
        while (DoBoolTerm(l.boolexpterm))
            for (Statement s : l.statementblock.statement)
                DoStatement(s);
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

    // BoolExpFactor = ({lhs}Expression {compareOps}("=="|"!="|"<="|">="|">"|"<") {rhs}Expression) | VariableReference
    boolean DoBoolFactor(BoolExpFactor b) {
        // first we gotta check if everything is present, if it is, we need to evaluate based on the compare ops
        if (b.lhs.isPresent() && b.thecompareOps.isPresent() && b.rhs.isPresent()){
            // do the actual expression to get an interpretertype
            InterpreterDataType leftExp = DoExpression(b.lhs.get());
            InterpreterDataType rightExp = DoExpression(b.rhs.get());

            // handle number case
            if (leftExp instanceof NumberInterpreterDataType && rightExp instanceof NumberInterpreterDataType) {
                float leftVal = ((NumberInterpreterDataType) leftExp).value;
                float rightVal = ((NumberInterpreterDataType) rightExp).value;
                //    doubleequal,
                //    notequal,
                //    lessthanequal,
                //    greaterthanequal,
                //    greaterthan,
                //    lessthan
                compareOps op = b.thecompareOps.get();
                if (op == compareOps.doubleequal)
                    return (leftVal == rightVal);
                if (op == compareOps.notequal)
                    return (leftVal != rightVal);
                if (op == compareOps.lessthanequal)
                    return (leftVal <= rightVal);
                if (op == compareOps.greaterthanequal)
                    return (leftVal >= rightVal);
                if (op == compareOps.greaterthan)
                    return (leftVal > rightVal);
                if (op == compareOps.lessthan)
                    return (leftVal < rightVal);
            }
            // examples don't handle string comparisons, for now just throw if we get here
            throw new RuntimeException("comparison between non-numbers");
        }
        // handle variable reference case
        else if (b.variablereference.isPresent()) {
            // need to a) look up var in the stack and get the value and
            // b) verify that it's actually a boolean
            for (int i = variables.size() - 1; i >= 0; i--) { // top to bottom so we check current first
                String vrObjectName = b.variablereference.get().$object.get();
                if (variables.get(i).containsKey(vrObjectName)) {
                    InterpreterDataType j = variables.get(i).get(vrObjectName);
                    if (j instanceof BooleanInterpreterDataType bar)
                        return bar.value;
                    // otherwise, it's not a bool and we throw
                    throw new RuntimeException("expected boolean type var");
                }
            }
        }
        // otherwise, broken boolean expression factor
        throw new RuntimeException("incorrect boolean expression factor");
    }

    //Set = "Set" VariableReference "to" Expression NEWLINE+
    void DoSet (Set s) {
        //just get the return from do expression, and put var ref name on stack with expression value
        InterpreterDataType v = DoExpression(s.expression);
        // peek first to get top of the stack i.e. current method vars
        variables.peek().put(s.variablereference.name, v);
    }

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
                for (int i = variables.size() - 1; i >= 0; i--) { // top to bottom so we check current first
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
                for (int i = variables.size() - 1; i >= 0; i--) {
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
