package interpreter;

import java.util.List;

public class Evaluator {
    private final Environment env = new Environment();

    public void execute(List<Stmt> statements) {
        for (Stmt stmt : statements) {
            stmt.execute(env);
        }
    }
}
