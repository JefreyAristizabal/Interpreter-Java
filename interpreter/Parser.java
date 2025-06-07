package interpreter;

import java.util.*;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(statement());
        }
        return statements;
    }

    private Stmt statement() {
        if (match("KEYWORD", "if")) return ifStatement();
        if (match("KEYWORD", "while")) return whileStatement();
        if (match("KEYWORD", "print")) return printStatement();
        if (check("IDENTIFIER") && checkNext("SYMBOL", "=")) return assignStatement();
        if (match("SYMBOL", "{")) {
            Stmt block = block();
            match("SYMBOL", ";");
            return block;
        }
        return expressionStatement();
    }

    private Stmt assignStatement() {
        Token name = advance(); // IDENTIFIER
        consume("SYMBOL", "=");
        Expr value = expression();
        consume("SYMBOL", ";");
        return new AssignStmt(name.text, value);
    }

    private Stmt printStatement() {
        Expr value = expression();
        consume("SYMBOL", ";");
        return new PrintStmt(value);
    }

    private Stmt ifStatement() {
        consume("SYMBOL", "(");
        Expr condition = expression();
        consume("SYMBOL", ")");
        Stmt thenBranch = statement();
        Stmt elseBranch = null;
        if (match("KEYWORD", "else")) {
            elseBranch = statement();
        }
        return new IfStmt(condition, thenBranch, elseBranch);
    }

    private Stmt whileStatement() {
        consume("SYMBOL", "(");
        Expr condition = expression();
        consume("SYMBOL", ")");
        Stmt body = statement();
        return new WhileStmt(condition, body);
    }

    private Stmt expressionStatement() {
        Expr expr = expression();
        consume("SYMBOL", ";");
        return new ExprStmt(expr);
    }

    private Expr expression() {
        return equality();
    }

    private Expr equality() {
        Expr expr = comparison();
        while (match("OPERATOR", "==", "!=")) {
            String operator = previous().text;
            Expr right = comparison();
            expr = new BinaryExpr(expr, operator, right);
        }
        return expr;
    }

    private Expr comparison() {
        Expr expr = term();
        while (match("OPERATOR", "<", "<=", ">", ">=")) {
            String operator = previous().text;
            Expr right = term();
            expr = new BinaryExpr(expr, operator, right);
        }
        return expr;
    }

    private Expr term() {
        Expr expr = factor();
        while (match("OPERATOR", "+", "-")) {
            String operator = previous().text;
            Expr right = factor();
            expr = new BinaryExpr(expr, operator, right);
        }
        return expr;
    }

    private Expr factor() {
        Expr expr = unary();
        while (match("OPERATOR", "*", "/", "%")) {
            String operator = previous().text;
            Expr right = unary();
            expr = new BinaryExpr(expr, operator, right);
        }
        return expr;
    }

    private Expr unary() {
        if (match("OPERATOR", "!", "-")) {
            String operator = previous().text;
            Expr right = unary();
            return new UnaryExpr(operator, right);
        }
        return primary();
    }

    private Expr primary() {
        if (match("NUMBER")) {
            return new LiteralExpr(Double.parseDouble(previous().text));
        }
        if (match("STRING")) {
            return new LiteralExpr(previous().text);
        }
        if (match("KEYWORD", "true")) {
            return new LiteralExpr(true);
        }
        if (match("KEYWORD", "false")) {
            return new LiteralExpr(false);
        }
        if (match("IDENTIFIER")) {
            return new VariableExpr(previous().text);
        }
        if (match("SYMBOL", "(")) {
            Expr expr = expression();
            consume("SYMBOL", ")");
            return expr;
        }
        throw error(peek(), "Expected expression.");
    }

    private boolean match(String type, String... texts) {
        if (check(type, texts)) {
            advance();
            return true;
        }
        return false;
    }

    private boolean check(String type, String... texts) {
        if (isAtEnd()) return false;
        Token token = peek();
        if (!token.type.name().equals(type)) return false;
        if (texts.length == 0) return true;
        for (String text : texts) {
            if (token.text.equals(text)) return true;
        }
        return false;
    }

    private boolean check(String type) {
        return !isAtEnd() && peek().type.name().equals(type);
    }

    private boolean checkNext(String type, String text) {
        if (current + 1 >= tokens.size()) return false;
        Token next = tokens.get(current + 1);
        return next.type.name().equals(type) && next.text.equals(text);
    }

    private Token consume(String type, String text) {
        if (check(type, text)) return advance();
        throw error(peek(), "Expected '" + text + "'");
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == Token.Type.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private RuntimeException error(Token token, String message) {
        System.err.println("Parse error at token " + token.text + ": " + message);
        return new RuntimeException(message);
    }

    private Stmt block() {
        List<Stmt> statements = new ArrayList<>();
        while (!check("SYMBOL", "}") && !isAtEnd()) {
            statements.add(statement());
        }
        consume("SYMBOL", "}");
        return new BlockStmt(statements);
    }

}
