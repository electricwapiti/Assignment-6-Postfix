package org.example;

import org.junit.jupiter.api.Test;

import calculator.Calculator;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {
  @Test
  void evaluatesSimpleAddition() {
    Calculator calculator = new Calculator();
    assertEquals(7.0, calculator.evaluate("2 + 5"), 1e-9, "2 + 5 should equal 7.0");
  }
}
