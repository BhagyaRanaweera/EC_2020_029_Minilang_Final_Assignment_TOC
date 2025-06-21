// === IntermediateCodeGenerator.java ===

import java.util.List;

public class IntermediateCodeGenerator {
    private final List<LexicalAnalyzer.Token> tokens;
    private int tempCount = 0;

    public IntermediateCodeGenerator(List<LexicalAnalyzer.Token> tokens) {
        this.tokens = tokens;
    }

    public void generate() {
        for (int i = 0; i < tokens.size(); i++) {
            LexicalAnalyzer.Token t = tokens.get(i);
            if (t.type == LexicalAnalyzer.TokenType.IDENTIFIER &&
                    i + 2 < tokens.size() && tokens.get(i + 1).type == LexicalAnalyzer.TokenType.ASSIGN) {
                String left = t.value;
                String right1 = tokens.get(i + 2).value;
                String result = left + " = " + right1;
                if (i + 3 < tokens.size() && isOperator(tokens.get(i + 3).type)) {
                    String op = tokens.get(i + 3).value;
                    String right2 = tokens.get(i + 4).value;
                    String temp = newTemp();
                    System.out.println(temp + " = " + right1 + " " + op + " " + right2);
                    System.out.println(left + " = " + temp);
                    i += 4;
                } else {
                    System.out.println(result);
                    i += 2;
                }
            }
        }
    }

    private String newTemp() {
        return "t" + (++tempCount);
    }

    private boolean isOperator(LexicalAnalyzer.TokenType t) {
        return t == LexicalAnalyzer.TokenType.PLUS ||
                t == LexicalAnalyzer.TokenType.MINUS ||
                t == LexicalAnalyzer.TokenType.MULT ||
                t == LexicalAnalyzer.TokenType.DIV;
    }
}

