package interpreter;

public class IfStmt implements Stmt {
    private final Expr condition;
    private final Stmt thenBranch;
    private final Stmt elseBranch;

    public IfStmt(Expr condition, Stmt thenBranch, Stmt elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    public void execute(Environment env) {
        Object cond = condition.eval(env);
        if (!(cond instanceof Boolean)) {
            throw new RuntimeException("Condition must evaluate to a boolean.");
        }

        if ((Boolean) cond) {
            thenBranch.execute(env);
        } else if (elseBranch != null) {
            elseBranch.execute(env);
        }
    }
}
