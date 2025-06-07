import java.util.*;

// === Token Types ===
enum TokenType {
    NUMBER, IDENTIFIER, PLUS, MINUS, STAR, SLASH,
    ASSIGN, LPAREN, RPAREN, EOF
}

// === Token Class ===
class Token {
    TokenType type;
    String text;

    Token(TokenType type, String text) {
        this.type = type;
        this.text = text;
    }
}

// === Lexer ===
class Lexer {
    private final String input;
    private int pos = 0;

    Lexer(String input) {
        this.input = input;
    }

    List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (pos < input.length()) {
            char current = input.charAt(pos);
            if (Character.isWhitespace(current)) {
                pos++;
            } else if (Character.isDigit(current)) {
                tokens.add(number());
            } else if (Character.isLetter(current)) {
                tokens.add(identifier());
            } else {
                switch (current) {
                    case '+': tokens.add(new Token(TokenType.PLUS, "+")); break;
                    case '-': tokens.add(new Token(TokenType.MINUS, "-")); break;
                    case '*': tokens.add(new Token(TokenType.STAR, "*")); break;
                    case '/': tokens.add(new Token(TokenType.SLASH, "/")); break;
                    case '=': tokens.add(new Token(TokenType.ASSIGN, "=")); break;
                    case '(': tokens.add(new Token(TokenType.LPAREN, "(")); break;
                    case ')': tokens.add(new Token(TokenType.RPAREN, ")")); break;
                    default: throw new RuntimeException("Unknown character: " + current);
                }
                pos++;
            }
        }
        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }

    private Token number() {
        int start = pos;
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) pos++;
        return new Token(TokenType.NUMBER, input.substring(start, pos));
    }

    private Token identifier() {
        int start = pos;
        while (pos < input.length() && Character.isLetter(input.charAt(pos))) pos++;
        return new Token(TokenType.IDENTIFIER, input.substring(start, pos));
    }
}

// === AST Nodes ===
abstract class Expr {
    abstract int eval(Map<String, Integer> env);
}

class NumberExpr extends Expr {
    int value;
    NumberExpr(int value) { this.value = value; }
    int eval(Map<String, Integer> env) { return value; }
}

class VariableExpr extends Expr {
    String name;
    VariableExpr(String name) { this.name = name; }
    int eval(Map<String, Integer> env) {
        if (!env.containsKey(name)) throw new RuntimeException("Undefined variable: " + name);
        return env.get(name);
    }
}

class BinaryExpr extends Expr {
    Expr left, right;
    TokenType op;
    BinaryExpr(Expr left, TokenType op, Expr right) {
        this.left = left; this.op = op; this.right = right;
    }
    int eval(Map<String, Integer> env) {
        int a = left.eval(env);
        int b = right.eval(env);
        return switch (op) {
            case PLUS -> a + b;
            case MINUS -> a - b;
            case STAR -> a * b;
            case SLASH -> a / b;
            default -> throw new RuntimeException("Unknown binary operator");
        };
    }
}

class AssignmentExpr extends Expr {
    String name;
    Expr value;
    AssignmentExpr(String name, Expr value) {
        this.name = name; this.value = value;
    }
    int eval(Map<String, Integer> env) {
        int v = value.eval(env);
        env.put(name, v);
        return v;
    }
}

// === Parser ===
class Parser {
    private final List<Token> tokens;
    private int pos = 0;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    Expr parse() {
        return expression();
    }

    private Expr expression() {
        if (lookahead().type == TokenType.IDENTIFIER && lookahead(1).type == TokenType.ASSIGN) {
            String name = consume(TokenType.IDENTIFIER).text;
            consume(TokenType.ASSIGN);
            return new AssignmentExpr(name, expression());
        }
        return addSub();
    }

    private Expr addSub() {
        Expr expr = mulDiv();
        while (match(TokenType.PLUS, TokenType.MINUS)) {
            Token op = previous();
            Expr right = mulDiv();
            expr = new BinaryExpr(expr, op.type, right);
        }
        return expr;
    }

    private Expr mulDiv() {
        Expr expr = primary();
        while (match(TokenType.STAR, TokenType.SLASH)) {
            Token op = previous();
            Expr right = primary();
            expr = new BinaryExpr(expr, op.type, right);
        }
        return expr;
    }

    private Expr primary() {
        if (match(TokenType.NUMBER)) {
            return new NumberExpr(Integer.parseInt(previous().text));
        }
        if (match(TokenType.IDENTIFIER)) {
            return new VariableExpr(previous().text);
        }
        if (match(TokenType.LPAREN)) {
            Expr expr = expression();
            consume(TokenType.RPAREN);
            return expr;
        }
        throw new RuntimeException("Unexpected token: " + lookahead().text);
    }

    private Token consume(TokenType type) {
        if (lookahead().type == type) return tokens.get(pos++);
        throw new RuntimeException("Expected token: " + type);
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (lookahead().type == type) {
                pos++;
                return true;
            }
        }
        return false;
    }

    private Token lookahead() {
        return lookahead(0);
    }

    private Token lookahead(int offset) {
        return pos + offset < tokens.size() ? tokens.get(pos + offset) : new Token(TokenType.EOF, "");
    }

    private Token previous() {
        return tokens.get(pos - 1);
    }
}

// === Main Interpreter ===
public class MiniInterpreter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> env = new HashMap<>();
        System.out.println("MiniInterpreter (type 'exit' to quit):");
        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine();
            if (line.equals("exit")) break;
            try {
                Lexer lexer = new Lexer(line);
                List<Token> tokens = lexer.tokenize();
                Parser parser = new Parser(tokens);
                Expr expr = parser.parse();
                int result = expr.eval(env);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
