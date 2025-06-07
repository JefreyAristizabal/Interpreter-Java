package interpreter;

public class WhileStmt implements Stmt {
    private final Expr condition;
    private final Stmt body;

    public WhileStmt(Expr condition, Stmt body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public void execute(Environment env) {
        while (Boolean.TRUE.equals(condition.eval(env))) {
            body.execute(env);
        }
    }
}


