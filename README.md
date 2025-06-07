# 🧠 Interpreter-Java

A mini interpreter built from scratch in Java. This project implements a simple language featuring expressions, variables, blocks, conditionals, and loops, using classical interpreter design techniques.

---

## 🛠 Project Structure

The project is organized into the following key components:

### 🔣 Abstract Syntax Tree (AST)
- `Expr`: Base interface for all expressions.
- `BinaryExpr`, `UnaryExpr`, `LiteralExpr`, `VariableExpr`: Expression types.
- `Stmt`: Base interface for all statements.
- `AssignStmt`, `PrintStmt`, `IfStmt`, `WhileStmt`, `BlockStmt`: Statement implementations.

### ⚙️ Core Interpreter
- `Lexer`: Converts source code into tokens.
- `Parser`: Parses tokens into an AST.
- `Environment`: Handles variable scopes and bindings.

---

## ✅ Supported Features

- Variable declarations and assignments
- Arithmetic and logical expressions
- `if`, `while`, and block statements
- `print` statements
- Nested environments and variable scoping

---

## 🚀 Sample Code

```java
print 3 + 2 * (4 - 1);  // Output: 9

var x = 5;
if (x > 3) {
    print "Greater than 3";
}

while (x > 0) {
    print x;
    x = x - 1;
}
