# MiniLang Analyzer

A compiler front-end for a simple CJAVA-like language, MiniLang, implementing:

- ✅ Lexical Analysis  
- ✅ Syntax Analysis  
- ✅ Semantic Analysis  
- ✅ Intermediate Code Generation  
##  Description

MiniLang Analyzer is a Java-based compiler component project that simulates the early phases of a compiler. It parses a simplified language with features like:

- Variable declaration (e.g., `int y;`)  
- Arithmetic operations (e.g., `y= a + b * 2;`)  
- Conditional statements (e.g., `if (...) { ... } else { ... }`)
- Loops(while,for)
- Print statements (e.g., `print(y);`)

This project is designed for learning compiler design principles, especially for BECS  – Theory of Compilers at the University of Kelaniya.





📁 File Structure
EC_2020_029_MINILANG/
├── input.minilang # Sample source code input file
├── LexicalAnalyzer.java # Performs tokenization of source code
├── SyntaxAnalyzer.java # Parses tokens and verifies syntax
├── SemanticAnalyzer.java # Checks semantic rules like variable declaration and usage
├── IntermediateCodeGenerator.java # Generates intermediate 3-address code
├── Main.java # Entry point coordinating all phases
└── README.md # This documentation file
🧪 Sample MiniLang Code
int a;
int b;
a = 5;
b = 3;
if (a > b) {
    print(a);
} else {
    print(b);
}
while (a > 0) {
    a = a - 1;
    print(a);
}

print(n);
🔧 How to Compile and Run
1. Clone or Download the Project
git clone https://github.com/BhagyaRanaweera/EC_2020_029_Minilang_Final_Assignment_TOC
cd EC_2020_029_Minilang_Final_Assignment_TOC
2. Ensure Java is Installed
Check with:

java -version
javac -version
Install JDK if not installed.

3. Compile All Java Files
javac Main.java SimpleLexer.java SimpleParser.java SemanticAnalyzer.java IntermediateCodeGenerator.java
4. Add Code to input.minilang
Example:

int a;
int b;
a = 5;
b = 3;
if (a > b) {
    print(a);
} else {
    print(b);
}
while (a > 0) {
    a = a - 1;
    print(a);
}

5. Run the Main Program
java Main
✅ Example Output
📘 Performing Lexical Analysis...
(KEYWORD, int)
(IDENTIFIER, x)
...
📗 Performing Syntax Analysis...
✅ Syntax Analysis: Passed.

📙 Performing Semantic Analysis...
✅ Semantic Analysis: Passed.

📒 Performing Intermediate Code Generation...
a = 5
b = 3
t1 = a - 1
a = t1

✅ All analyses and code generation completed successfully.
⚠️ Common Issues
Issue	Fix
FileNotFoundException	Ensure input.minilang exists in project directory
javac or java not recognized	Install Java SDK 24.1 and set environment variables
Semantic error on variable usage	Declare variable before using it
Missing semicolon or brace	Review code formatting, use proper syntax
🧠 Educational Use
This analyzer is part of:

Course: BECS 44673 – Theory of Compilers
University: University of Kelaniya
Student ID: EC/2020/029


🙌 Acknowledgements
University of Kelaniya
Theory of Compilers Module
Java Compiler Design examples
