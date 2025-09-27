package calculator;

import java.util.ArrayList;
import java.util.List;

import stack.LinkedStack;

public class Calculator {
    // (1) Get the string from the main function
    // (2) Pass the string into the shunting yard fxn
    // (3) Split the string (TODO: Find how to do so in java docs)
    // (4) Evaluate RPN with a method Postfix(array)
    // (5) Return result to main function

	// Evaluate an infix expression and return the result as a double
	public double evaluate(String infix) {
		List<String> tokens = tokenize(infix);
		List<String> postfix = infixToPostfix(tokens);
		return evaluatePostfix(postfix);
	}

	// Tokenize the input string into numbers, operators and parentheses.
	private List<String> tokenize(String s) {
		List<String> tokens = new ArrayList<>();
		int i = 0;
		int n = s.length();

		while (i < n) {
			char c = s.charAt(i);
			if (Character.isWhitespace(c)) { i++; continue; }

			// Parentheses
			if (c == '(' || c == ')') {
				tokens.add(String.valueOf(c));
				i++;
				continue;
			}

			// Operator characters
			if (isOperatorChar(c)) {
				// Determine if this is a unary sign for a number
				boolean unary = (c == '+' || c == '-') &&
								(tokens.isEmpty() || tokens.get(tokens.size()-1).equals("(") || isOperator(tokens.get(tokens.size()-1)));
				if (unary) {
					// parse signed number
					int j = i + 1;
					boolean foundDigit = false;
					StringBuilder sb = new StringBuilder();
					sb.append(c); // sign
					// allow decimals and digits
					while (j < n) {
						char cj = s.charAt(j);
						if (Character.isDigit(cj) || cj == '.') {
							foundDigit = true;
							sb.append(cj);
							j++;
						} else break;
					}
					if (!foundDigit)
						throw new IllegalArgumentException("Invalid unary sign at position " + i);
					tokens.add(sb.toString());
					i = j;
				} else {
					// binary operator
					tokens.add(String.valueOf(c));
					i++;
				}
				continue;
			}

			// Number starting with digit or dot
			if (Character.isDigit(c) || c == '.') {
				int j = i;
				StringBuilder sb = new StringBuilder();
				while (j < n) {
					char cj = s.charAt(j);
					if (Character.isDigit(cj) || cj == '.') {
						sb.append(cj);
						j++;
					} else break;
				}
				tokens.add(sb.toString());
				i = j;
				continue;
			}

			// Unknown character
			throw new IllegalArgumentException("Unexpected character '" + c + "' at position " + i);
		}
		return tokens;
	}

	// Infix to postfix using the provided LinkedStack implementation.
	private List<String> infixToPostfix(List<String> tokens) {
		List<String> output = new ArrayList<>();
		LinkedStack<String> ops = new LinkedStack<>();

		for (String token : tokens) {
			if (token.isEmpty()) continue;
			if (isNumber(token)) {
				output.add(token);
			} else if (isOperator(token)) {
				while (!ops.isEmpty() && isOperator(ops.top())) {
					String top = ops.top();
					int precTop = precedence(top);
					int precTok = precedence(token);
					// '^' is right-associative
					if (precTop > precTok || (precTop == precTok && !token.equals("^"))) {
						output.add(ops.pop());
					} else break;
				}
				ops.push(token);
			} else if (token.equals("(")) {
				ops.push(token);
			} else if (token.equals(")")) {
				while (!ops.isEmpty() && !ops.top().equals("(")) {
					output.add(ops.pop());
				}
				if (ops.isEmpty() || !ops.top().equals("("))
					throw new IllegalArgumentException("Mismatched parentheses");
				ops.pop(); // pop "("
			} else {
				throw new IllegalArgumentException("Unknown token: " + token);
			}
		}
		while (!ops.isEmpty()) {
			String t = ops.pop();
			if (t.equals("(") || t.equals(")")) throw new IllegalArgumentException("Mismatched parentheses");
			output.add(t);
		}
		return output;
	}

	// Evaluate postfix expression
	private double evaluatePostfix(List<String> postfix) {
		LinkedStack<Double> st = new LinkedStack<>();
		for (String token : postfix) {
			if (isNumber(token)) {
				st.push(Double.valueOf(token));
			} else if (isOperator(token)) {
				Double b = st.pop();
				Double a = st.pop();
				if (a == null || b == null) throw new IllegalArgumentException("Malformed expression");
				double res;
				switch (token) {
					case "+":
						res = a + b; break;
					case "-":
						res = a - b; break;
					case "*":
						res = a * b; break;
					case "/":
						res = a / b; break;
					case "^":
						res = Math.pow(a, b); break;
					default:
						throw new IllegalArgumentException("Unsupported operator: " + token);
				}
				st.push(res);
			} else {
				throw new IllegalArgumentException("Unknown token in postfix: " + token);
			}
		}
		Double result = st.pop();
		if (!st.isEmpty()) throw new IllegalArgumentException("Malformed expression");
		return result == null ? 0.0 : result;
	}

	private boolean isNumber(String s) {
		// a simple check: starts with digit or '.' or a sign followed by digit
		if (s == null || s.isEmpty()) return false;
		char c = s.charAt(0);
		return Character.isDigit(c) || c == '.' || ((c == '+' || c == '-') && s.length() > 1 && (Character.isDigit(s.charAt(1)) || s.charAt(1) == '.'));
	}

	private boolean isOperator(String s) {
		return "+-*/^".contains(s) && s.length() == 1;
	}

	private boolean isOperatorChar(char c) {
		return "+-*/^".indexOf(c) >= 0;
	}

	private int precedence(String op) {
		switch (op) {
			case "+":
			case "-":
				return 1;
			case "*":
			case "/":
				return 2;
			case "^":
				return 3;
		}
		return -1;
	}
}
