import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class LexicalAnalyserTest {

	private Random rand = new Random(System.currentTimeMillis());

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	private char digitToChar(int d) {
		switch (d) {
		case 0:
			return '0';
		case 1:
			return '1';
		case 2:
			return '2';
		case 3:
			return '3';
		case 4:
			return '4';
		case 5:
			return '5';
		case 6:
			return '6';
		case 7:
			return '7';
		case 8:
			return '8';
		case 9:
			return '9';
		default:
			return ' ';
		}
	}

	private char randomNonZeroDigit() {

		return digitToChar(rand.nextInt(9) + 1);

	}

	private char randomDigit() {

		return digitToChar(rand.nextInt(5));

	}

	private String randomDigitString(int length) {

		String number = "";

		for (int i = 0; i < length; i++) {
			if (i == 0)
				number += randomNonZeroDigit();
			else
				number += randomDigit();
		}

		return number;

	}

	private char randomOperator() {
		switch (rand.nextInt(4)) {
		case 0:
			return '+';
		case 1:
			return '-';
		case 2:
			return '*';
		case 3:
			return '/';
		default:
			return ' ';
		}
	}

	@Test
	public void testAnalyseSimpleNumber() {

		String testString = "1";

		List<Token> result;
		try {
			result = LexicalAnalyser.analyse(testString);
			assertEquals(1, result.size());
			assertTrue(result.get(0).isNumber());
			assertTrue(result.get(0).getValue().isPresent());
			assertEquals(1, result.get(0).getValue().get(), 0.000001);
		} catch (NumberException | ExpressionException e) {
			System.err.println(e.getMessage());
			fail("Exception thrown.");
		}

	}

	@Test
	public void testAnalyseMultiDigitInteger() {

		String testString = randomDigitString(rand.nextInt(5) + 1);

		List<Token> result;
		try {
			result = LexicalAnalyser.analyse(testString);
			assertEquals(1, result.size());
			assertTrue(result.get(0).isNumber());
			assertTrue(result.get(0).getValue().isPresent());
			assertEquals(Integer.parseInt(testString), result.get(0).getValue().get(), 0.000001);
		} catch (NumberException | ExpressionException e) {
			System.err.println(e.getMessage());
			fail("Exception thrown.");
		}

	}

	@Test
	public void testSmallDecimal() {

		String testString = "0.2";

		List<Token> result;
		try {
			result = LexicalAnalyser.analyse(testString);
			assertEquals(1, result.size());
			assertTrue(result.get(0).isNumber());
			assertTrue(result.get(0).getValue().isPresent());
			assertEquals(Double.parseDouble(testString), result.get(0).getValue().get(), 0.000001);
		} catch (NumberException | ExpressionException e) {
			System.err.println(e.getMessage());
			fail("Exception thrown.");
		}
	}

	@Test
	public void testLongDecimal() {

		String testString = "0." + randomDigitString(rand.nextInt(5) + 1);

		List<Token> result;
		try {
			result = LexicalAnalyser.analyse(testString);
			assertEquals(1, result.size());
			assertTrue(result.get(0).isNumber());
			assertTrue(result.get(0).getValue().isPresent());
			assertEquals(Double.parseDouble(testString), result.get(0).getValue().get(), 0.000001);
		} catch (NumberException | ExpressionException e) {
			System.err.println(e.getMessage());
			fail("Exception thrown.");
		}
	}

	@Test
	public void testDecimalNonZeroIntegerPart() throws NumberException, ExpressionException {

		String testString = randomDigitString(rand.nextInt(5) + 1) + "." + randomDigitString(rand.nextInt(5) + 1);

		exception.expect(NumberException.class);
		LexicalAnalyser.analyse(testString);

	}

	@Test
	public void testDecimalNoIntegerPart() throws NumberException, ExpressionException {

		String testString = "." + randomDigitString(rand.nextInt(5) + 1);

		exception.expect(NumberException.class);
		LexicalAnalyser.analyse(testString);

	}

	@Test
	public void testDecimalNoDecimalPart() throws NumberException, ExpressionException {

		String testString = "0.";

		exception.expect(NumberException.class);
		LexicalAnalyser.analyse(testString);

	}

	@Test
	public void testSimplePlus() {

		String testString = randomDigitString(rand.nextInt(5) + 1) + " + " + randomDigitString(rand.nextInt(5) + 1);

		List<Token> result;

		try {

			result = LexicalAnalyser.analyse(testString);
			assertEquals(3, result.size());
			assertTrue(result.get(0).isNumber());
			assertEquals(Token.TokenType.PLUS, result.get(1).getType());
			assertTrue(result.get(2).isNumber());
		} catch (NumberException | ExpressionException e) {
			System.err.println(e.getMessage());
			fail("Exception thrown.");
		}

	}

	@Test
	public void testSimpleMinus() {

		String testString = randomDigitString(rand.nextInt(5) + 1) + " - " + randomDigitString(rand.nextInt(5) + 1);

		List<Token> result;

		try {

			result = LexicalAnalyser.analyse(testString);
			assertEquals(3, result.size());
			assertTrue(result.get(0).isNumber());
			assertEquals(Token.TokenType.MINUS, result.get(1).getType());
			assertTrue(result.get(2).isNumber());
		} catch (NumberException | ExpressionException e) {
			System.err.println(e.getMessage());
			fail("Exception thrown.");
		}

	}

	@Test
	public void testSimpleTimes() {

		String testString = randomDigitString(rand.nextInt(5) + 1) + " * " + randomDigitString(rand.nextInt(5) + 1);

		List<Token> result;

		try {

			result = LexicalAnalyser.analyse(testString);
			assertEquals(3, result.size());
			assertTrue(result.get(0).isNumber());
			assertEquals(Token.TokenType.TIMES, result.get(1).getType());
			assertTrue(result.get(2).isNumber());
		} catch (NumberException | ExpressionException e) {
			System.err.println(e.getMessage());
			fail("Exception thrown.");
		}

	}

	@Test
	public void testSimpleDivide() {

		String testString = randomDigitString(rand.nextInt(5) + 1) + " / " + randomDigitString(rand.nextInt(5) + 1);

		List<Token> result;

		try {

			result = LexicalAnalyser.analyse(testString);
			assertEquals(3, result.size());
			assertTrue(result.get(0).isNumber());
			assertEquals(Token.TokenType.DIVIDE, result.get(1).getType());
			assertTrue(result.get(2).isNumber());
		} catch (NumberException | ExpressionException e) {
			System.err.println(e.getMessage());
			fail("Exception thrown.");
		}

	}

	@Test
	public void testRandomValidExpression() throws NumberException, ExpressionException {

		int numOperators = rand.nextInt(5);
		List<Token> tokenStream = new ArrayList<Token>();
		String number = randomDigitString(rand.nextInt(5) + 1);
		tokenStream.add(new Token(Integer.parseInt(number)));
		String testString = number;
		for (int i = 0; i < numOperators; i++) {
			char operator = randomOperator();
			switch (operator) {
			case '+':
				tokenStream.add(new Token(Token.TokenType.PLUS));
				break;
			case '-':
				tokenStream.add(new Token(Token.TokenType.MINUS));
				break;
			case '*':
				tokenStream.add(new Token(Token.TokenType.TIMES));
				break;
			case '/':
				tokenStream.add(new Token(Token.TokenType.DIVIDE));
				break;
			}
			number = randomDigitString(rand.nextInt(5) + 1);
			tokenStream.add(new Token(Integer.parseInt(number)));
			testString += operator + "" + number;
		}

		List<Token> result = LexicalAnalyser.analyse(testString);
		assertEquals(tokenStream.size(), result.size());
		for (int i = 0; i < result.size(); i++) {
			assertEquals(tokenStream.get(i), result.get(i));
		}
	}

	@Test
	public void testSimpleWrongExpression() throws NumberException, ExpressionException {
		String testString = " + +";

		exception.expect(ExpressionException.class);
		LexicalAnalyser.analyse(testString);
	}

	@Test
	public void testUnexpectedSymbol() throws NumberException, ExpressionException {

		String testString = "aeirgfhj";

		exception.expect(ExpressionException.class);
		LexicalAnalyser.analyse(testString);

	}

	@Test
	public void testRandomWrongExpression() throws NumberException, ExpressionException {

		int numOperators = rand.nextInt(5) + 1;
		int missingPosition = rand.nextInt(2 * numOperators + 1);
		List<Token> tokenStream = new ArrayList<Token>();
		String number = randomDigitString(rand.nextInt(5) + 1);
		tokenStream.add(new Token(Integer.parseInt(number)));
		String testString = missingPosition == 0 ? "" : number;
		for (int i = 0; i < numOperators; i++) {

			if (missingPosition != 2 * i + 1) {
				char operator = randomOperator();
				switch (operator) {
				case '+':
					tokenStream.add(new Token(Token.TokenType.PLUS));
					break;
				case '-':
					tokenStream.add(new Token(Token.TokenType.MINUS));
					break;
				case '*':
					tokenStream.add(new Token(Token.TokenType.TIMES));
					break;
				case '/':
					tokenStream.add(new Token(Token.TokenType.DIVIDE));
					break;
				}

				testString += " " + operator;
			}

			if (missingPosition != 2 * i + 2) {
				number = randomDigitString(rand.nextInt(5) + 1);
				tokenStream.add(new Token(Integer.parseInt(number)));
				testString += " " + number;
			}
		}
		
		exception.expect(ExpressionException.class);
		LexicalAnalyser.analyse(testString);
	}

}
