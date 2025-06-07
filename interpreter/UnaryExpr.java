package interpreter;

public class UnaryExpr implements Expr {
    private final String operator;
    private final Expr expr;

    public UnaryExpr(String operator, Expr expr) {
        this.operator = operator;
        this.expr = expr;
    }

    @Override
    public Object eval(Environment env) {
        Object value = expr.eval(env);

        if (value instanceof Integer && operator.equals("-")) {
            return -(Integer) value;
        }

        throw new RuntimeException("Invalid unary operation.");
    }
}
