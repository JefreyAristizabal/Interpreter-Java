package interpreter;

public class LiteralExpr implements Expr {
    private final Object value;

    public LiteralExpr(Object value) {
        this.value = value;
    }

    @Override
    public Object eval(Environment env) {
        return value;
    }
}

