package PlainEnglish;

import PlainEnglish.AST.Method;

import java.util.HashMap;

public abstract class BuiltInMethod extends Method {
    public abstract void Execute(HashMap<String, InterpreterDataType> context);
}