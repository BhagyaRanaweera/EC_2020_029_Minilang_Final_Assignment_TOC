// === IntermediateCodeGenerator.java ===
/**
 * IntermediateCodeGenerator.java
 * -------------------------------
 * This class performs **Intermediate Code Generation** for the MiniLang compiler.
 * It translates high-level assignment expressions into a lower-level,
 * three-address code (TAC) format suitable for further compilation or interpretation.
 *
 *  Intermediate Code Generation: Translate to 3-address code
 *
 * Main Responsibilities:
 * - Detects assignment statements like: `x = a + b;`
 * - Converts expressions into simplified instructions using temporary variables.
 * - Generates intermediate representation (TAC) with helpful error diagnostics.
 *
 *  Example Translation:
 *   Input:    x = a + b;
 *   Output:   t1 = a + b
 *             x = t1
 *
 *  Error Handling:
 * - Detects missing semicolons.
 * - Detects unexpected tokens in expressions.
 * - Detects assignment without right-hand expression.
 *
 *  Limitations:
 * - No operator precedence (evaluates left to right).
 * - Handles basic binary expressions and assignments only.
 */

import java.util.List;

public class IntermediateCodeGenerator {
    private final List<LexicalAnalyzer.Token> tokens;
    private int tempCount = 0;
    private int index = 0;

    public IntermediateCodeGenerator(List<LexicalAnalyzer.Token> tokens) {
        this.tokens = tokens;
    }

    public void generate() {
        while (index < tokens.size()) {
            LexicalAnalyzer.Token token = tokens.get(index);

            // Check for assignment: IDENTIFIER = ...
            if (token.type == LexicalAnalyzer.TokenType.IDENTIFIER) {
                if (index + 1 < tokens.size() && tokens.get(index + 1).type == LexicalAnalyzer.TokenType.ASSIGN) {
                    String lhs = token.value;
                    index += 2; // Skip IDENTIFIER and ASSIGN

                    if (index >= tokens.size()) {
                        System.err.println(" Error: Missing expression after '=' at token " + token.value);
                        System.exit(1);
                    }

                    String rhs = parseExpression();

                    // Final assignment
                    System.out.println(lhs + " = " + rhs);

                    // Expect semicolon
                    if (index < tokens.size() && tokens.get(index).type == LexicalAnalyzer.TokenType.SEMICOLON) {
                        index++; // Skip semicolon
                    } else {
                        System.err.println(" Error: Expected ';' after assignment to variable '" + lhs + "'.");
                        System.exit(1);
                    }
                } else {
                    index++; // Not an assignment, skip
                }
            } else {
                index++; // Skip non-assignment tokens
            }
        }
    }

    // Parses expressions like: a + b - c (left to right)
    private String parseExpression() {
        String left = parseTerm();

        while (index < tokens.size() && isOperator(tokens.get(index).type)) {
            String op = tokens.get(index).value;
            index++;

            if (index >= tokens.size()) {
                System.err.println(" Error: Missing operand after operator '" + op + "'.");
                System.exit(1);
            }

            String right = parseTerm();

            String temp = newTemp();
            System.out.println(temp + " = " + left + " " + op + " " + right);
            left = temp;
        }

        return left;
    }

    // Parse a single identifier or number
    private String parseTerm() {
        if (index >= tokens.size()) {
            System.err.println(" Error: Unexpected end of expression.");
            System.exit(1);
        }

        LexicalAnalyzer.Token token = tokens.get(index);

        if (token.type == LexicalAnalyzer.TokenType.IDENTIFIER ||
                token.type == LexicalAnalyzer.TokenType.NUMBER) {
            index++;
            return token.value;
        } else {
            System.err.println(" Error: Expected identifier or number, but found '" + token.value + "'.");
            System.exit(1);
        }

        return null; // Unreachable
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
