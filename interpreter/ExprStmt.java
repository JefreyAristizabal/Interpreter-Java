package interpreter;

public class ExprStmt implements Stmt {
    private final Expr expr;

    public ExprStmt(Expr expr) {
        this.expr = expr;
    }

    @Override
    public void execute(Environment env) {
        expr.eval(env);
    }
}
