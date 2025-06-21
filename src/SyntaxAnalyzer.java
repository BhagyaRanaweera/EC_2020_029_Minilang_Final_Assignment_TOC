// === SyntaxAnalyzer.java (Updated in SimpleParser style) ===
import java.util.List;

public class SyntaxAnalyzer {

    private final List<LexicalAnalyzer.Token> tokens;
    private int currentIndex = 0;

    public SyntaxAnalyzer(List<LexicalAnalyzer.Token> tokens) {
        this.tokens = tokens;
    }

    public void parse() {
        while (!isAtEnd()) {
            parseStatement();
        }
        System.out.println("✅ Syntax Analysis: Passed.");
    }

    private void parseStatement() {
        if (match(LexicalAnalyzer.TokenType.INT)) {
            parseDeclaration();
        } else if (check(LexicalAnalyzer.TokenType.IDENTIFIER)) {
            parseAssignment();
        } else if (match(LexicalAnalyzer.TokenType.IF)) {
            parseIfStatement();
        } else if (match(LexicalAnalyzer.TokenType.WHILE)) {
            parseWhileStatement();
        } else if (match(LexicalAnalyzer.TokenType.PRINT)) {
            parsePrintStatement();
        } else {
            error("Expected a statement.");
        }
    }

    private void parseDeclaration() {
        consume(LexicalAnalyzer.TokenType.IDENTIFIER, "Expected variable name after 'int'.");
        consume(LexicalAnalyzer.TokenType.SEMICOLON, "Expected ';' after declaration.");
    }

    private void parseAssignment() {
        consume(LexicalAnalyzer.TokenType.IDENTIFIER, "Expected variable name.");
        consume(LexicalAnalyzer.TokenType.ASSIGN, "Expected '=' in assignment.");
        parseExpression();
        consume(LexicalAnalyzer.TokenType.SEMICOLON, "Expected ';' after assignment.");
    }

    private void parseIfStatement() {
        consume(LexicalAnalyzer.TokenType.LPAREN, "Expected '(' after 'if'.");
        parseExpression();
        consume(LexicalAnalyzer.TokenType.RPAREN, "Expected ')' after condition.");
        parseBlock();
        if (match(LexicalAnalyzer.TokenType.ELSE)) {
            parseBlock();
        }
    }

    private void parseWhileStatement() {
        consume(LexicalAnalyzer.TokenType.LPAREN, "Expected '(' after 'while'.");
        parseExpression();
        consume(LexicalAnalyzer.TokenType.RPAREN, "Expected ')' after condition.");
        parseBlock();
    }

    private void parsePrintStatement() {
        consume(LexicalAnalyzer.TokenType.LPAREN, "Expected '(' after 'print'.");
        parseExpression();
        consume(LexicalAnalyzer.TokenType.RPAREN, "Expected ')' after expression.");
        consume(LexicalAnalyzer.TokenType.SEMICOLON, "Expected ';' after print statement.");
    }

    private void parseBlock() {
        consume(LexicalAnalyzer.TokenType.LBRACE, "Expected '{' to start block.");
        while (!check(LexicalAnalyzer.TokenType.RBRACE) && !isAtEnd()) {
            parseStatement();
        }
        consume(LexicalAnalyzer.TokenType.RBRACE, "Expected '}' to close block.");
    }

    private void parseExpression() {
        parseArithmetic();
        if (match(LexicalAnalyzer.TokenType.GREATER) || match(LexicalAnalyzer.TokenType.LESS) ||
                match(LexicalAnalyzer.TokenType.EQUAL) || match(LexicalAnalyzer.TokenType.NOTEQUAL)) {
            parseArithmetic();
        }
    }

    private void parseArithmetic() {
        parseTerm();
        while (match(LexicalAnalyzer.TokenType.PLUS) || match(LexicalAnalyzer.TokenType.MINUS)) {
            parseTerm();
        }
    }

    private void parseTerm() {
        parseFactor();
        while (match(LexicalAnalyzer.TokenType.MULT) || match(LexicalAnalyzer.TokenType.DIV)) {
            parseFactor();
        }
    }

    private void parseFactor() {
        if (match(LexicalAnalyzer.TokenType.IDENTIFIER) || match(LexicalAnalyzer.TokenType.NUMBER)) {
            return;
        } else if (match(LexicalAnalyzer.TokenType.LPAREN)) {
            parseExpression();
            consume(LexicalAnalyzer.TokenType.RPAREN, "Expected ')' after expression.");
        } else {
            error("Expected number, variable, or expression.");
        }
    }

    private boolean match(LexicalAnalyzer.TokenType type) {
        if (check(type)) {
            advance();
            return true;
        }
        return false;
    }

    private void consume(LexicalAnalyzer.TokenType type, String errorMessage) {
        if (check(type)) {
            advance();
        } else {
            error(errorMessage);
        }
    }

    private boolean check(LexicalAnalyzer.TokenType type) {
        return !isAtEnd() && peek().type == type;
    }

    private LexicalAnalyzer.Token peek() {
        return tokens.get(currentIndex);
    }

    private LexicalAnalyzer.Token advance() {
        if (!isAtEnd()) currentIndex++;
        return previous();
    }

    private LexicalAnalyzer.Token previous() {
        return tokens.get(currentIndex - 1);
    }

    private boolean isAtEnd() {
        return currentIndex >= tokens.size();
    }

    private void error(String message) {
        System.err.println(" Syntax Error: " + message + " at token: " +
                (isAtEnd() ? "EOF" : peek()));
        System.exit(1);
    }
}
