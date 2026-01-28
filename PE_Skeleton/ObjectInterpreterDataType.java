package PlainEnglish;

import java.util.HashMap;

public class ObjectInterpreterDataType extends InterpreterDataType {
    public String type;
    public HashMap<String, InterpreterDataType> fields = new HashMap<String, InterpreterDataType>();

    public String toString() {
        var sb = new StringBuilder();
        sb.append(type);
        for (var item : fields.keySet())
            sb.append("    ").append(item).append(" = ").append(fields.get(item));
        return sb.toString();
    }
}
