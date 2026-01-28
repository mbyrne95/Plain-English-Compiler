package PlainEnglish;

import PlainEnglish.AST.Parameter;

import java.util.HashMap;

public class PrintBuiltInMethod extends BuiltInMethod {
    public PrintBuiltInMethod() {
        this.name = "Print";
        var p1 = new Parameter();
        p1.paramType = "thing1";
        this.parameter.add(p1);

        var p2 = new Parameter();
        p2.paramType = "thing2";
        this.parameter.add(p2);

        var p3 = new Parameter();
        p3.paramType = "thing3";
        this.parameter.add(p3);

        var p4 = new Parameter();
        p4.paramType = "thing4";
        this.parameter.add(p4);

        var p5 = new Parameter();
        p5.paramType = "thing4";
        this.parameter.add(p5);
    }

    public void Execute(HashMap<String, InterpreterDataType> context) {
        for (var item : context.values()) {
            System.out.println(item);
        }
    }
}
