package interpreter;

public interface Expr {
    Object eval(Environment env);
}
