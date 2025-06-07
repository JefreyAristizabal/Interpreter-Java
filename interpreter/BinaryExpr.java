package interpreter;

public class BinaryExpr implements Expr {
    private final Expr left;
    private final String operator;
    private final Expr right;

    public BinaryExpr(Expr left, String operator, Expr right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public Object eval(Environment env) {
        Object leftVal = left.eval(env);
        Object rightVal = right.eval(env);

        if (leftVal instanceof Double && rightVal instanceof Double) {
            double a = (Double) leftVal;
            double b = (Double) rightVal;

            switch (operator) {
                case "+": return a + b;
                case "-": return a - b;
                case "*": return a * b;
                case "/":
                    if (b == 0) throw new RuntimeException("Division by zero");
                    return a / b;
                case "%": return a % b;
                case "==": return a == b;
                case "!=": return a != b;
                case "<": return a < b;
                case ">": return a > b;
                case "<=": return a <= b;
                case ">=": return a >= b;
            }
        } else if (leftVal instanceof Boolean && rightVal instanceof Boolean) {
            boolean a = (Boolean) leftVal;
            boolean b = (Boolean) rightVal;

            switch (operator) {
                case "&&": return a && b;
                case "||": return a || b;
                case "==": return a == b;
                case "!=": return a != b;
            }
        } else if (leftVal instanceof String || rightVal instanceof String) {
            if (operator.equals("+")) {
                return String.valueOf(leftVal) + String.valueOf(rightVal);
            }
        }

        throw new RuntimeException("Type mismatch in binary expression: "
                + leftVal + " (" + (leftVal != null ? leftVal.getClass().getSimpleName() : "null") + ") "
                + operator + " "
                + rightVal + " (" + (rightVal != null ? rightVal.getClass().getSimpleName() : "null") + ")");
    }
}
