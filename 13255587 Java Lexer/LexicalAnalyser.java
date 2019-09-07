import java.util.ArrayList;
import java.util.List;

//////
//Lexical Analyser - 1.0
//By Matthew Mallos - 13255587
//////
public class LexicalAnalyser
{
    public static List<Token> analyse(final String input) throws NumberException, ExpressionException {
        final List<Token> result = new ArrayList<Token>(); //Stores token
        String s = "";                                     //String to store multi-digit values
        final Token token = new Token();                   
        double value;                                      //Assign multi-digit values 
        int state = 0;                                     //Variable to keep track of state
        int temp = 0;                                      //Ensures the DFA doesn't try to read/assign values beyond where it is at

        //System.out.println(input);                       //Used to see what expressions the LA failed on
        for (int i = 0; i <input.length(); i++) { //iterate through the input
            switch (state) { //Empty state, beginning
                case   0:  //Accepts 0-9, WS
                switch (input.charAt(i)) {
                    case ' ':
                    state = 0;
                    break;
                    
                    case '0':
                    s = s + input.charAt(i);
                    value = Double.valueOf(String.valueOf(input.charAt(i)));
                    result.add(new Token(Token.typeOf(input.charAt(i))));
                    result.get(i).setValue(value);
                    temp = i;
                    state = 3;
                    break;
                    
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                    s = s + input.charAt(i);
                    value = Double.valueOf(String.valueOf(input.charAt(i)));
                    result.add(new Token(Token.typeOf(input.charAt(i))));
                    result.get(0).setValue(value);
                    temp = i;
                    state = 1;
                    break;
                    
                    case '.':
                    throw new NumberException();
                    case '+':
                    case '-':
                    case '*':
                    case '/':
                    default:
                    throw new ExpressionException();
                }
                break;

                case 1: //NUM STATE, Accepts 0-9, WS, Operator
                switch (input.charAt(i)) {
                    case '+':
                    case '-':
                    case '*':
                    case '/':
                    result.add(new Token(Token.typeOf(input.charAt(i))));
                    state = 2;
                    temp = result.size();
                    break;
                    
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
                    s = s + input.charAt(i);
                    value = Double.parseDouble(s);
                    result.get(temp).setValue(value);
                    break;
                    
                    case ' ':
                    state = 5;
                    break;
                    
                    case '.':
                    throw new NumberException();
                    
                    default:
                    throw new ExpressionException();
                }
                break;

                case 2: //OPERATOR STATE, accepts 0-9,  WS
                switch (input.charAt(i)) {
                    case '0':
                    state = 3;
                    result.add(new Token(Token.typeOf(input.charAt(i))));
                    s ="" + input.charAt(i);
                    result.get(temp).setValue(0);
                    temp = i;
                    break;
                    
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                    s ="" + input.charAt(i);
                    value = Double.valueOf(String.valueOf(input.charAt(i)));
                    result.add(new Token(Token.typeOf(input.charAt(i))));
                    result.get(temp).setValue(value);
                    state = 1;
                    break;
                    
                    case ' ':
                    state = 6;
                    break;
                    
                    case '+' :
                    case '-' :
                    case '*' :
                    case '/' :
                    throw new ExpressionException();
                    case '.' :
                    throw new NumberException();
                    
                    default:
                    throw new ExpressionException();
                }
                break;

                case 3: //Expression starts with zero STATE, accepts operators, decimal, WS
                switch (input.charAt(i)) {
                    case '+':
                    case '-':
                    case '*':
                    case '/':
                    result.add(new Token(Token.typeOf(input.charAt(i))));
                    state = 2;
                    temp = i+1;
                    break;
                    
                    case '.':
                    state = 4;
                    break;
                    
                    case ' ':
                    state = 7;
                    break;
                    
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
                    throw new NumberException();
                    
                    default:
                    throw new ExpressionException();
                }
                break;

                case 4: //Just entered a decimal after a 0
                s = s + "."; //refreshes the string, adds decimal
                switch (input.charAt(i)) {
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
                    s = s + input.charAt(i);
                    value = Double.parseDouble(s);
                    result.get(temp).setValue(value);
                    state = 1;
                    break;
                    
                    default: throw new ExpressionException();
                }

                break;

                case 5: //WHITESPACE AFTER NUM, only accepts operators
                switch (input.charAt(i)) {
                    case '+' :
                    case '-' :
                    case '*' :
                    case '/' :
                    result.add(new Token(Token.typeOf(input.charAt(i))));
                    state = 2;
                    temp = result.size();
                    break;
                    
                    case ' ' :
                    break;
                    
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
                    case '.':
                    throw new ExpressionException();
                    
                    default:
                    throw new ExpressionException();

                }
                break;

                case 6: //WHITESPACE AFTER OPERATOR, Accepts 0-9, WS
                switch (input.charAt(i)) {
                    case '0':
                    result.add(new Token(Token.typeOf(input.charAt(i))));
                    s ="" + input.charAt(i);
                    result.get(temp).setValue(0);
                    temp = i;
                    state = 3;
                    break;
                    
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                    s ="" + input.charAt(i);
                    value = Double.valueOf(String.valueOf(input.charAt(i)));
                    result.add(new Token(Token.typeOf(input.charAt(i))));
                    result.get(temp).setValue(value);
                    state = 1;
                    break;
                    
                    case ' ' :
                    break;

                    case '+' :
                    case '-' :
                    case '*' :
                    case '/' :
                    case '.':
                    throw new ExpressionException();
                    
                    default:
                    throw new ExpressionException();
                }
                break;

                case 7: //0 and then WHITESPACE, Accepts operator, WS
                switch (input.charAt(i)) {
                    case '+':
                    case '-':
                    case '*':
                    case '/':
                    result.add(new Token(Token.typeOf(input.charAt(i))));
                    state = 2;
                    temp = i+1;
                    break;
                    
                    case ' ':
                    break;
                    
                    case '.':
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
                    throw new NumberException();
                    
                    default:
                    throw new ExpressionException();
                }
                break;
            }
        } 

        if (state == 4) { //A last second catch clause for invalid state endings (Represents sink State)
            throw new NumberException();
        }
        else if (state == 0 || state == 2 || state == 5 || state == 7 || state == 6) {
            throw new ExpressionException();
        }
        return result; //Returns list of tokens created
    }
}