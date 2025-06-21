import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MiniLang Lexical Analyzer
 * --------------------------
 * Updated with comment handling and improved keyword recognition.
 */
public class LexicalAnalyzer {

    public enum TokenType {
        INT, IF, ELSE, WHILE, PRINT,       // Keywords
        IDENTIFIER, NUMBER,                // Identifiers and literals
        ASSIGN, SEMICOLON,                 // = ;
        PLUS, MINUS, MULT, DIV,            // Arithmetic operators
        GREATER, LESS, EQUAL, NOTEQUAL,    // Comparison operators
        LPAREN, RPAREN, LBRACE, RBRACE,    // Symbols
        COMMENT, WHITESPACE                // Skip types
    }

    public static class Token {
        public final TokenType type;
        public final String value;

        public Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString() {
            return "[" + type + " : " + value + "]";
        }
    }

    private static final LinkedHashMap<TokenType, String> tokenPatterns = new LinkedHashMap<>() {{
        put(TokenType.WHITESPACE, "[\\s]+");
        put(TokenType.COMMENT, "//.*");
        put(TokenType.INT, "\\bint\\b");
        put(TokenType.IF, "\\bif\\b");
        put(TokenType.ELSE, "\\belse\\b");
        put(TokenType.WHILE, "\\bwhile\\b");
        put(TokenType.PRINT, "\\bprint\\b");
        put(TokenType.IDENTIFIER, "[a-zA-Z_][a-zA-Z0-9_]*");
        put(TokenType.NUMBER, "\\b\\d+\\b");
        put(TokenType.ASSIGN, "=");
        put(TokenType.SEMICOLON, ";");
        put(TokenType.PLUS, "\\+");
        put(TokenType.MINUS, "-");
        put(TokenType.MULT, "\\*");
        put(TokenType.DIV, "/");
        put(TokenType.GREATER, ">");
        put(TokenType.LESS, "<");
        put(TokenType.EQUAL, "==");
        put(TokenType.NOTEQUAL, "!=");
        put(TokenType.LPAREN, "\\(");
        put(TokenType.RPAREN, "\\)");
        put(TokenType.LBRACE, "\\{");
        put(TokenType.RBRACE, "\\}");
    }};

    public static List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        StringBuilder masterPattern = new StringBuilder();
        for (String pattern : tokenPatterns.values()) {
            masterPattern.append("(" + pattern + ")|");
        }
        masterPattern.setLength(masterPattern.length() - 1); // remove last |
        Pattern combinedPattern = Pattern.compile(masterPattern.toString());

        Matcher matcher = combinedPattern.matcher(input);
        while (matcher.find()) {
            String match = matcher.group();
            for (Map.Entry<TokenType, String> entry : tokenPatterns.entrySet()) {
                if (match.matches(entry.getValue())) {
                    TokenType type = entry.getKey();
                    if (type != TokenType.WHITESPACE && type != TokenType.COMMENT) {
                        tokens.add(new Token(type, match));
                    }
                    break;
                }
            }
        }
        return tokens;
    }

    public static void main(String[] args) {
        String fileName = "input.minilang";
        StringBuilder code = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                code.append(line).append("\n");
            }

            System.out.println("==================== MiniLang Compiler ====================");
            System.out.println(" Ensure 'input.minilang' is in the current directory.");
            System.out.println("  Stages: Lexical ➤ Syntax ➤ Semantic ➤ Intermediate Code Generation");
            System.out.println("===========================================================\n");

            // Lexical Analysis
            System.out.println(" Lexical Analysis:");
            List<Token> tokens = tokenize(code.toString());
            for (Token token : tokens) {
                System.out.println(token);
            }

            // Syntax Analysis
            System.out.println("\n📐 Syntax Analysis:");
            SyntaxAnalyzer syntax = new SyntaxAnalyzer(tokens);
            syntax.parse();

        } catch (FileNotFoundException e) {
            System.err.println(" File not found: " + fileName);
        } catch (IOException e) {
            System.err.println(" I/O Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println(" Unexpected Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
