import interpreter.*;

import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Evaluator evaluator = new Evaluator();

        System.out.println("Mini Intérprete (escribe 'exit;' para salir)");

        StringBuilder input = new StringBuilder();
        while (true) {
            System.out.print(">>> ");
            String line = scanner.nextLine();
            if (line.trim().equals("exit;")) break;

            input.append(line).append("\n");

            if (line.trim().endsWith(";") || line.trim().endsWith("}")) {
                try {
                    Lexer lexer = new Lexer(input.toString());
                    List<Token> tokens = lexer.tokenize();

                    Parser parser = new Parser(tokens);
                    List<Stmt> statements = parser.parse();

                    evaluator.execute(statements);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
                input.setLength(0); // limpiar
            }
        }
    }
}
