package interpreter;

import java.util.*;

public class Lexer {
    private final String input;
    private int pos = 0;
    private final List<Token> tokens = new ArrayList<>();

    private static final Set<String> KEYWORDS = Set.of("if", "else", "while", "true", "false", "print");
    private static final Set<String> OPERATORS = Set.of("+", "-", "*", "/", "%", "==", "!=", "<", ">", "<=", ">=", "&&", "||", "!");
    private static final Set<String> SYMBOLS = Set.of("=", ";", "(", ")", "{", "}");

    public Lexer(String input) {
        this.input = input;
    }

    public List<Token> tokenize() {
        while (!isAtEnd()) {
            char c = peek();
            if (Character.isWhitespace(c)) {
                advance();
            } else if (c == '"') {
                tokenizeString();
            } else if (Character.isDigit(c)) {
                tokenizeNumber();
            } else if (Character.isLetter(c) || c == '_') {
                tokenizeIdentifierOrKeyword();
            } else {
                tokenizeOperatorOrSymbol();
            }
        }
        tokens.add(new Token(Token.Type.EOF, ""));
        return tokens;
    }

    private void tokenizeString() {
        advance(); // Skip opening quote
        int start = pos;
        while (!isAtEnd() && peek() != '"') {
            advance();
        }

        if (isAtEnd()) {
            throw new RuntimeException("Unterminated string literal");
        }

        String str = input.substring(start, pos); // Extract string contents
        advance(); // Skip closing quote
        tokens.add(new Token(Token.Type.STRING, str));
    }

    private void tokenizeNumber() {
        int start = pos;
        while (!isAtEnd() && (Character.isDigit(peek()) || peek() == '.')) advance();
        tokens.add(new Token(Token.Type.NUMBER, input.substring(start, pos)));
    }

    private void tokenizeIdentifierOrKeyword() {
        int start = pos;
        while (!isAtEnd() && (Character.isLetterOrDigit(peek()) || peek() == '_')) advance();
        String text = input.substring(start, pos);
        Token.Type type = KEYWORDS.contains(text) ? Token.Type.KEYWORD : Token.Type.IDENTIFIER;
        tokens.add(new Token(type, text));
    }

    private void tokenizeOperatorOrSymbol() {
        int start = pos;
        for (int len = 3; len > 0; len--) {
            if (pos + len <= input.length()) {
                String text = input.substring(pos, pos + len);
                if (OPERATORS.contains(text) || SYMBOLS.contains(text)) {
                    tokens.add(new Token(OPERATORS.contains(text) ? Token.Type.OPERATOR : Token.Type.SYMBOL, text));
                    pos += len;
                    return;
                }
            }
        }
        throw new RuntimeException("Unexpected character: " + advance());
    }

    private boolean isAtEnd() {
        return pos >= input.length();
    }

    private char peek() {
        return input.charAt(pos);
    }

    private char advance() {
        return input.charAt(pos++);
    }
}
