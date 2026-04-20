package PlainEnglish;

import PlainEnglish.AST.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String source = new String(Files.readAllBytes(Paths.get("path_to_ple_program")));
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.lex();
        PlainEnglishParser parser = new PlainEnglishParser(new LinkedList<>(tokens));
        Program program = parser.program().get();
        Interpreter interpreter = new Interpreter(program);
        interpreter.Start();
    }
}
