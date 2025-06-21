// === Main.java ===
import java.io.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("\n==================== MiniLang Compiler ====================");
        System.out.println(" Ensure 'input.minilang' is in the current directory.");
        System.out.println("  Stages: Lexical -> Syntax -> Semantic -> Intermediate Code Generation");
        System.out.println("===========================================================\n");

        String fileName = "input.minilang";
        StringBuilder codeBuffer = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                codeBuffer.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("File Read Error: " + e.getMessage());
            return;
        }

        try {
            System.out.println(" Lexical Analysis:");
            List<LexicalAnalyzer.Token> tokens = LexicalAnalyzer.tokenize(codeBuffer.toString());
            tokens.forEach(System.out::println);

            System.out.println("\n Syntax Analysis:");
            SyntaxAnalyzer parser = new SyntaxAnalyzer(tokens);
            parser.parse();

            System.out.println("\n Semantic Analysis:");
            SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
            semanticAnalyzer.analyze(tokens);

            System.out.println("\n  Intermediate Code Generation:");
            IntermediateCodeGenerator icg = new IntermediateCodeGenerator(tokens);
            icg.generate();

            System.out.println("\n Compilation completed successfully!");
        } catch (Exception e) {
            System.err.println(" Compilation Error: " + e.getMessage());
        }
    }
}