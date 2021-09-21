import java.util.ArrayList;
import java.util.Scanner;


public class Parser {
    public static void main(String[] args) {
        test();
    }

    
    static class Lexeme {
        private String type;
        private String value;
        
        public Lexeme(String type, String value) {
            this.type = type;
            this.value = value;
        }
        
        @Override
        public String toString() {
            return value + "\t" + type;
        }
        
        public String getValue() {
            return value;
        }
        
        public String getType() {
            return type;
        }
        
        public int priority() {
            int priority = 0;
            if (value.equals("+") || value.equals("-")) {
                priority = 1;
            } else if (value.equals("*") || value.equals("/")) {
                priority = 2;
            }
            return priority;
        }
    }


    public static ArrayList<Lexeme> lexer(String expression) throws Exception {
        ArrayList<Lexeme> result = new ArrayList<>();
        for (int i = 0; i < expression.length();) {
            char ei = expression.charAt(i);
            if (ei == '+' || ei == '-' || ei == '*' || ei == '/') {
                result.add(new Lexeme("op", String.valueOf(ei)));
                i++;
            } else if (Character.isDigit(ei)) {
                String num = "";
                while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                    num += expression.charAt(i);
                    i++;
                }
                result.add(new Lexeme("num", num));
            } else if (Character.isWhitespace(ei)) {
                i++;
            } else {
                throw new Exception("Parsing error.");
            }
        }
        return result;
    }


    public static ArrayList<Lexeme> postfix(ArrayList<Lexeme> lexemes) {
        ArrayList<Lexeme> result = new ArrayList<>();
        ArrayList<Lexeme> stack = new ArrayList<>();
        
        for (Lexeme t : lexemes) {
            if (t.getType() == "num") {
                result.add(t);
            } else if (t.getType() == "op") {
                while (!stack.isEmpty() && stack.get(stack.size()-1).priority() >= t.priority()) {
                    Lexeme op = stack.remove(stack.size()-1);
                    result.add(op);
                }
                stack.add(t);
            }
        }
        while (!stack.isEmpty()) {
            Lexeme op = stack.remove(stack.size()-1);
            result.add(op);
        }
        return result;
    }
    
    
    public static double evaluate(ArrayList<Lexeme> lexemes) {
        double result = 0.0;
        double left = 0.0;
        double right = 0.0;
        
        ArrayList<Double> stack = new ArrayList<>();
        
        for (Lexeme t : lexemes) {
            if (t.getType() == "num") {
                stack.add((double) Integer.parseInt(t.getValue()));
            } else if (t.getType() == "op") {
                right = stack.remove(stack.size()-1);
                left = stack.remove(stack.size()-1);
                switch (t.getValue()) {
                    case "+":
                        result = left + right;
                        break;
                    case "-":
                        result = left - right;
                        break;
                    case "*":
                        result = left * right;
                        break;
                    case "/":
                        result = left / right;
                        break;
                }
                stack.add(result);
            }
        }
        result = stack.isEmpty() ? 0 : stack.get(0);
        return result;
    }
    
    public static void test() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Expression: ");
        String expression = scanner.nextLine();
        try {
            System.out.println("Result: " + evaluate(postfix(lexer(expression))));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
