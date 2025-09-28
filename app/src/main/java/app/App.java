package app;

import calculator.Calculator;

public class App {
  public static void main(String[] args) {
    Calculator calculator = new Calculator();
    String[] examples = {
      "2 + 5",           // should print 7.0
      "3 + 6 * 5",       // should print 33.0
      "4 * (2 + 3)",     // should print 20.0
      "(7 + 9) / 8",     // should print 2.0
      "2^2^3"            // should print 256.0
    };
    for (String expr : examples) {
      double result = calculator.evaluate(expr);
      System.out.printf("%s = %s%n", expr, result);
    }
  }
}