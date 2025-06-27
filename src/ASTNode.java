import java.util.List;

// Base AST node interfaces/classes
interface ASTNode { }

interface Statement extends ASTNode { }

interface Expression extends ASTNode { }

// Statements
class Declaration implements Statement {
    public final String varName;
    public Declaration(String varName) {
        this.varName = varName;
    }
}

class Assignment implements Statement {
    public final String varName;
    public final Expression expression;
    public Assignment(String varName, Expression expression) {
        this.varName = varName;
        this.expression = expression;
    }
}

class IfStatement implements Statement {
    public final Expression condition;
    public final Block thenBlock;
    public final Block elseBlock; // can be null
    public IfStatement(Expression condition, Block thenBlock, Block elseBlock) {
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }
}

class WhileStatement implements Statement {
    public final Expression condition;
    public final Block body;
    public WhileStatement(Expression condition, Block body) {
        this.condition = condition;
        this.body = body;
    }
}

class PrintStatement implements Statement {
    public final Expression expression;
    public PrintStatement(Expression expression) {
        this.expression = expression;
    }
}

class Block implements Statement {
    public final List<Statement> statements;
    public Block(List<Statement> statements) {
        this.statements = statements;
    }
}

// Expressions
class BinaryExpression implements Expression {
    public enum Operator {
        PLUS, MINUS, MULT, DIV, GREATER, LESS, EQUAL, NOTEQUAL
    }
    public final Expression left;
    public final Operator operator;
    public final Expression right;

    public BinaryExpression(Expression left, Operator operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
}

class Variable implements Expression {
    public final String name;
    public Variable(String name) {
        this.name = name;
    }
}

class NumberLiteral implements Expression {
    public final int value;
    public NumberLiteral(int value) {
        this.value = value;
    }
}
