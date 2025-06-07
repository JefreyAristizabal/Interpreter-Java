package interpreter;

public class VariableExpr implements Expr {
    private final String name;

    public VariableExpr(String name) {
        this.name = name;
    }

    @Override
    public Object eval(Environment env) {
        return env.get(name);
    }
}


