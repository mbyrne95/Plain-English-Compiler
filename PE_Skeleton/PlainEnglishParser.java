package PlainEnglish;

import PlainEnglish.AST.*;

import java.util.LinkedList;

public class PlainEnglishParser  {
    public PlainEnglishParser(LinkedList<Token> tokensIn) {
        tm = new TokenManager(tokensIn);
    }

    private final TokenManager tm;
}

