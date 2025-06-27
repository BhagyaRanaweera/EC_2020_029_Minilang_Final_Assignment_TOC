/**
 * SemanticAnalyzer.java
 * ----------------------
 * This class performs **Semantic Analysis** for the MiniLang compiler.
 * It verifies the semantic and type correctness of the program after syntax analysis.
 *
 * Responsibilities:
 * - Tracks variable declarations using a symbol table.
 * - Detects duplicate variable declarations (e.g., `int x; int x;`).
 * - Ensures variables are not used before being declared (e.g., `y = 10;`).
 * - Checks type consistency in assignments (e.g., `int x; x = "hello";` triggers an error).
 * - Verifies type compatibility when assigning variables to other variables (e.g., `x = y;`).
 * error handling
 *
 * Output:
 * - Prints a success message if semantic checks pass.
 * - Otherwise, prints a detailed error with token context and terminates the program.
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SemanticAnalyzer {

    private final Map<String, String> symbolTable = new HashMap<>();

    public void analyze(List<LexicalAnalyzer.Token> tokens) {
        for (int i = 0; i < tokens.size(); i++) {
            LexicalAnalyzer.Token token = tokens.get(i);

            // Variable Declaration
            if (token.type == LexicalAnalyzer.TokenType.INT) {
                if (i + 1 >= tokens.size()) {
                    error("Expected variable name after 'int'.", token);
                }

                LexicalAnalyzer.Token next = tokens.get(i + 1);
                if (next.type != LexicalAnalyzer.TokenType.IDENTIFIER) {
                    error("Expected variable name after 'int'.", next);
                }

                String varName = next.value;
                if (symbolTable.containsKey(varName)) {
                    error("Variable '" + varName + "' already declared.", next);
                }

                symbolTable.put(varName, "int");
                i++; // Skip identifier
            }

            // Variable Usage or Assignment
            else if (token.type == LexicalAnalyzer.TokenType.IDENTIFIER) {
                String varName = token.value;

                if (!symbolTable.containsKey(varName)) {
                    error("Variable '" + varName + "' used before declaration.", token);
                }

                // Assignment
                if (i + 1 < tokens.size() && tokens.get(i + 1).type == LexicalAnalyzer.TokenType.ASSIGN) {
                    if (i + 2 >= tokens.size()) {
                        error("Expected value after '=' in assignment to '" + varName + "'.", tokens.get(i + 1));
                    }

                    LexicalAnalyzer.Token valueToken = tokens.get(i + 2);
                    String expectedType = symbolTable.get(varName);

                    switch (valueToken.type) {
                        case NUMBER:
                            if (!"int".equals(expectedType)) {
                                error("Cannot assign 'int' to variable '" + varName + "' of type '" + expectedType + "'.", valueToken);
                            }
                            break;

                        case IDENTIFIER:
                            String rightVar = valueToken.value;
                            if (!symbolTable.containsKey(rightVar)) {
                                error("Variable '" + rightVar + "' used before declaration.", valueToken);
                            }
                            if (!symbolTable.get(rightVar).equals(expectedType)) {
                                error("Type mismatch: Cannot assign '" + symbolTable.get(rightVar) +
                                        "' to '" + expectedType + "' in '" + varName + " = " + rightVar + "'.", valueToken);
                            }
                            break;

                        default:
                            error("Unsupported assignment value type: " + valueToken.type, valueToken);
                    }

                    i += 2; // Skip over '=' and assigned value
                }
            }
        }

        System.out.println(" Semantic Analysis with Type Checking: Passed.");
    }

    /** Utility: Print error message and exit */
    private void error(String message, LexicalAnalyzer.Token token) {
        System.err.println(" Semantic Error: " + message);
        System.err.println("   → Offending token: '" + token.value + "' (type=" + token.type + ")");
        System.exit(1);
    }
}
