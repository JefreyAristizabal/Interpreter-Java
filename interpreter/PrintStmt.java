package interpreter;

public class PrintStmt implements Stmt {
    private final Expr expr;

    public PrintStmt(Expr expr) {
        this.expr = expr;
    }

    @Override
    public void execute(Environment env) {
        Object value = expr.eval(env);
        System.out.println(value);
    }
}



