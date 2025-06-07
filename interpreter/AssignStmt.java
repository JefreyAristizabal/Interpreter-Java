package interpreter;

public class AssignStmt implements Stmt {
    private final String name;
    private final Expr expr;

    public AssignStmt(String name, Expr expr) {
        this.name = name;
        this.expr = expr;
    }

    @Override
    public void execute(Environment env) {
        Object value = expr.eval(env);
        env.set(name, value);
    }
}


