import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SemanticAnalyzer {

    private final Set<String> declaredVariables = new HashSet<>();

    public void analyze(List<LexicalAnalyzer.Token> tokens) {
        for (int i = 0; i < tokens.size(); i++) {
            LexicalAnalyzer.Token token = tokens.get(i);

            // Handle variable declarations (e.g., int x;)
            if (token.type == LexicalAnalyzer.TokenType.INT) {
                if (i + 1 < tokens.size()) {
                    LexicalAnalyzer.Token next = tokens.get(i + 1);
                    if (next.type == LexicalAnalyzer.TokenType.IDENTIFIER) {
                        String varName = next.value;
                        if (declaredVariables.contains(varName)) {
                            System.err.println(" Semantic Error: Variable '" + varName + "' already declared.");
                            System.exit(1);
                        }
                        declaredVariables.add(varName);
                        i++; // Skip the identifier
                    } else {
                        System.err.println(" Semantic Error: Expected variable name after 'int'.");
                        System.exit(1);
                    }
                }
            }

            // Check variable usage
            else if (token.type == LexicalAnalyzer.TokenType.IDENTIFIER) {
                String varName = token.value;
                if (!declaredVariables.contains(varName)) {
                    System.err.println(" Semantic Error: Variable '" + varName + "' used before declaration.");
                    System.exit(1);
                }
            }
        }
        System.out.println("✅ Semantic Analysis: Passed.");
    }
}
