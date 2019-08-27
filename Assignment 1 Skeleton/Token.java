import java.util.Optional;

public class Token {

	public enum TokenType {

		NUMBER, PLUS, MINUS, TIMES, DIVIDE, NONE;

		public boolean isNumber() {
			switch (this) {
			case NUMBER:
				return true;
			default:
				return false;
			}
		}

	}

	private TokenType type;
	private double value;

	public Token() {
		this.type = TokenType.NONE;
		this.value = Double.NaN;
	}

	public Token(double value) {
		this.type = TokenType.NUMBER;
		this.value = value;
	}

	public Token(int value) {
		this.type = TokenType.NUMBER;
		this.value = value;
	}

	public Token(TokenType type) {
		this.type = type;
		this.value = Double.NaN;
	}

	public boolean isNumber() {
		return type.isNumber();
	}

	public TokenType getType() {
		return this.type;
	}

	public Optional<Double> getValue() {
		return this.isNumber() ? Optional.of(value) : Optional.empty();
	}

	public static TokenType typeOf(char symbol) {
		switch (symbol) {
		case '+':
			return TokenType.PLUS;
		case '-':
			return TokenType.MINUS;
		case '*':
			return TokenType.TIMES;
		case '/':
			return TokenType.DIVIDE;
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			return TokenType.NUMBER;
		default:
			return TokenType.NONE;
		}
	}

	@Override
	public String toString() {
		switch (this.type) {
		case NUMBER:
			return getValue().get().toString();
		case PLUS:
			return "+";
		case MINUS:
			return "-";
		case TIMES:
			return "*";
		case DIVIDE:
			return "/";
		default:
			return "Error converting token to String.";
		}
	}

	@Override
	public boolean equals(Object other) {

		if (this == other)
			return true;

		if (!(other instanceof Token))
			return false;

		return this.equals((Token) other);
	}

	public boolean equals(Token other) {

		if (!this.type.equals(other.type))
			return false;

		if (this.isNumber() && this.getValue().isPresent())
			return other.getValue().isPresent() && Math.abs(this.getValue().get() - other.getValue().get()) < 0.00001;

		if (this.isNumber() && !this.getValue().isPresent())
			return !other.getValue().isPresent();

		return true;

	}

}
