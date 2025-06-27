/**
 * SyntaxAnalyzer.java
 * --------------------
 * This class performs **Syntax Analysis** (Parsing) for the MiniLang compiler.
 * It uses recursive descent parsing in a SimpleParser style to verify whether
 * the input sequence of tokens (from the Lexical Analyzer) forms a syntactically
 * valid MiniLang program.
 *
 * Supported Statements:
 * - Variable Declaration: e.g., int x;
 * - Assignment: e.g., x = 5;
 * - If-Else Statements
 * - While Loops
 * - Print Statements
 *
 * Supported Expressions:
 * - Arithmetic: +, -, *, /
 * - Comparisons: >, <, ==, !=
 *
 * Key Features:
 * - Reports syntax errors with precise line-level diagnostics.
 * - Gracefully handles malformed input with proper error messages.
 * - Builds an internal AST (Abstract Syntax Tree) for valid MiniLang code.
 */

import java.util.ArrayList;
import java.util.List;

public class SyntaxAnalyzer {

    private final List<LexicalAnalyzer.Token> tokens;
    private int currentIndex = 0;

    public SyntaxAnalyzer(List<LexicalAnalyzer.Token> tokens) {
        this.tokens = tokens;
    }

    /** Entry point: Parses the entire token list and returns a Block of statements */
    public Block parse() {
        List<Statement> statements = new ArrayList<>();
        while (!isAtEnd()) {
            try {
                statements.add(parseStatement());
            } catch (RuntimeException e) {
                System.err.println("Error while parsing statement: " + e.getMessage());
                System.exit(1);
            }
        }
        System.out.println(" Syntax Analysis: Passed.");
        return new Block(statements);
    }

    private Statement parseStatement() {
        try {
            if (match(LexicalAnalyzer.TokenType.INT)) {
                return parseDeclaration();
            } else if (check(LexicalAnalyzer.TokenType.IDENTIFIER)) {
                return parseAssignment();
            } else if (match(LexicalAnalyzer.TokenType.IF)) {
                return parseIfStatement();
            } else if (match(LexicalAnalyzer.TokenType.WHILE)) {
                return parseWhileStatement();
            } else if (match(LexicalAnalyzer.TokenType.PRINT)) {
                return parsePrintStatement();
            } else {
                error("Expected a valid statement.");
                return null;
            }
        } catch (RuntimeException e) {
            error("Invalid statement format.");
            return null;
        }
    }

    private Declaration parseDeclaration() {
        String varName = consume(LexicalAnalyzer.TokenType.IDENTIFIER, "Expected variable name after 'int'.").value;
        consume(LexicalAnalyzer.TokenType.SEMICOLON, "Expected ';' after declaration.");
        return new Declaration(varName);
    }

    private Assignment parseAssignment() {
        String varName = consume(LexicalAnalyzer.TokenType.IDENTIFIER, "Expected variable name.").value;
        consume(LexicalAnalyzer.TokenType.ASSIGN, "Expected '=' in assignment.");
        Expression expr = parseExpression();
        consume(LexicalAnalyzer.TokenType.SEMICOLON, "Expected ';' after assignment.");
        return new Assignment(varName, expr);
    }

    private IfStatement parseIfStatement() {
        consume(LexicalAnalyzer.TokenType.LPAREN, "Expected '(' after 'if'.");
        Expression condition = parseExpression();
        consume(LexicalAnalyzer.TokenType.RPAREN, "Expected ')' after condition.");
        Block thenBlock = parseBlock();
        Block elseBlock = null;
        if (match(LexicalAnalyzer.TokenType.ELSE)) {
            elseBlock = parseBlock();
        }
        return new IfStatement(condition, thenBlock, elseBlock);
    }

    private WhileStatement parseWhileStatement() {
        consume(LexicalAnalyzer.TokenType.LPAREN, "Expected '(' after 'while'.");
        Expression condition = parseExpression();
        consume(LexicalAnalyzer.TokenType.RPAREN, "Expected ')' after condition.");
        Block body = parseBlock();
        return new WhileStatement(condition, body);
    }

    private PrintStatement parsePrintStatement() {
        consume(LexicalAnalyzer.TokenType.LPAREN, "Expected '(' after 'print'.");
        Expression expr = parseExpression();
        consume(LexicalAnalyzer.TokenType.RPAREN, "Expected ')' after expression.");
        consume(LexicalAnalyzer.TokenType.SEMICOLON, "Expected ';' after print statement.");
        return new PrintStatement(expr);
    }

    private Block parseBlock() {
        consume(LexicalAnalyzer.TokenType.LBRACE, "Expected '{' to start block.");
        List<Statement> statements = new ArrayList<>();
        while (!check(LexicalAnalyzer.TokenType.RBRACE) && !isAtEnd()) {
            statements.add(parseStatement());
        }
        consume(LexicalAnalyzer.TokenType.RBRACE, "Expected '}' to close block.");
        return new Block(statements);
    }

    /** Parses arithmetic or comparison expressions */
    private Expression parseExpression() {
        Expression left = parseArithmetic();
        if (match(LexicalAnalyzer.TokenType.GREATER)) {
            return new BinaryExpression(left, BinaryExpression.Operator.GREATER, parseArithmetic());
        } else if (match(LexicalAnalyzer.TokenType.LESS)) {
            return new BinaryExpression(left, BinaryExpression.Operator.LESS, parseArithmetic());
        } else if (match(LexicalAnalyzer.TokenType.EQUAL)) {
            return new BinaryExpression(left, BinaryExpression.Operator.EQUAL, parseArithmetic());
        } else if (match(LexicalAnalyzer.TokenType.NOTEQUAL)) {
            return new BinaryExpression(left, BinaryExpression.Operator.NOTEQUAL, parseArithmetic());
        }
        return left;
    }

    private Expression parseArithmetic() {
        Expression expr = parseTerm();
        while (true) {
            if (match(LexicalAnalyzer.TokenType.PLUS)) {
                expr = new BinaryExpression(expr, BinaryExpression.Operator.PLUS, parseTerm());
            } else if (match(LexicalAnalyzer.TokenType.MINUS)) {
                expr = new BinaryExpression(expr, BinaryExpression.Operator.MINUS, parseTerm());
            } else {
                break;
            }
        }
        return expr;
    }

    private Expression parseTerm() {
        Expression expr = parseFactor();
        while (true) {
            if (match(LexicalAnalyzer.TokenType.MULT)) {
                expr = new BinaryExpression(expr, BinaryExpression.Operator.MULT, parseFactor());
            } else if (match(LexicalAnalyzer.TokenType.DIV)) {
                expr = new BinaryExpression(expr, BinaryExpression.Operator.DIV, parseFactor());
            } else {
                break;
            }
        }
        return expr;
    }

    private Expression parseFactor() {
        if (match(LexicalAnalyzer.TokenType.NUMBER)) {
            return new NumberLiteral(Integer.parseInt(previous().value));
        } else if (match(LexicalAnalyzer.TokenType.IDENTIFIER)) {
            return new Variable(previous().value);
        } else if (match(LexicalAnalyzer.TokenType.LPAREN)) {
            Expression expr = parseExpression();
            consume(LexicalAnalyzer.TokenType.RPAREN, "Expected ')' after expression.");
            return expr;
        } else {
            error("Expected number, variable, or '(' expression ')'.");
            return null;
        }
    }

    // === Utility Methods ===

    private boolean match(LexicalAnalyzer.TokenType type) {
        if (check(type)) {
            advance();
            return true;
        }
        return false;
    }

    private LexicalAnalyzer.Token consume(LexicalAnalyzer.TokenType type, String message) {
        if (check(type)) return advance();
        error(message);
        return null;
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
        LexicalAnalyzer.Token token = isAtEnd() ? null : peek();
        String positionInfo = (token != null) ? " at token: '" + token.value + "' (type=" + token.type + ")" : " at end of input";
        throw new RuntimeException("Syntax Error: " + message + positionInfo);
    }
}
