package interpreter;

public class Token {
    public enum Type {
        NUMBER,
        IDENTIFIER,
        KEYWORD,
        OPERATOR,
        SYMBOL,
        STRING,
        EOF
    }

    public final Type type;
    public final String text;

    public Token(Type type, String text) {
        this.type = type;
        this.text = text;
    }

    @Override
    public String toString() {
        return type + "('" + text + "')";
    }
}
