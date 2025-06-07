package interpreter;

import java.util.List;

public class BlockStmt implements Stmt {
    private final List<Stmt> statements;

    public BlockStmt(List<Stmt> statements) {
        this.statements = statements;
    }

    @Override
    public void execute(Environment env) {
        env.enterScope();
        for (Stmt stmt : statements) {
            stmt.execute(env);
        }
        env.exitScope();
    }
}



