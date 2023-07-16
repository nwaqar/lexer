/**
 * This class stores the token type and value.
 */
public class Token
{
    public enum TokenType {DEDENT, INDENT, IDENTIFIER, NUMBER, WHILE, STRINGLITERAL, CHARACTERLITERAL, VAR, DEFINE, FOR, FROM, TO, WRITE, INTEGER, REAL, STRING, CONSTANTS, VARIABLES, IF, ELSIF, ELSE, MOD, REPEAT, UNTIL, PLUS, MINUS, TIMES, DIVIDE, LEFT_PARENTHESIS, RIGHT_PARENTHESIS, PERIOD, EXCLAMATION, SEMICOLON, COLON, COMMA, SINGLE_QUOTE, DOUBLE_QUOTE, QUESTION_MARK, THEN, EQUAL, LESS_THAN, GREATER_THAN, LESS_THAN_EQUAL, GREATER_THAN_EQUAL, EQUALS, NOT_EQUALS, ASSIGN, TRUE, FALSE, ENDOFLINE }
    private TokenType tokenType;
    private String value;

    /**
     * This constructor initializes the value to an empty string.
     */
    public Token()
    {
        value = "";
    }

    /**
     * This method adds a character to the value.
     * @param character
     */
    public void addCharacter(char character)
    {
        value = value + character;
    }

    /**
     * This method checks if value is empty.
     * @return
     */
    public boolean isEmpty()
    {
        return value.equals("");
    }

    /**
     * This method sets the token type.
     * @param tokenType
     */
    public void setTokenType(TokenType tokenType)
    {
        this.tokenType = tokenType;
    }

    public TokenType getTokenType()
    {
        return tokenType;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * This method returns the token in the string format.
     * @return
     */
    @Override
    public String toString()
    {
        if (!isEmpty())
        {
            if (tokenType != null)
            {
                return tokenType.name() + "(" + value + ")";
            }

            return "(" + value + ")";
        }

        if (tokenType != null)
        {
            return tokenType.name();
        }

        return "";
    }
}
