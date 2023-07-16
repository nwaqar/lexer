import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class reads a line and extracts all tokens.
 */
public class Lexer
{
    private int lineNumber = 0;
    private int lastIndentationCount = 0;
    private HashMap<String, Token.TokenType> knownWords = new HashMap<>();
    private Token token;
    private List<Token> tokens = new ArrayList<>();
    private enum StateType {STATE_START, STATE_IDENTIFIER, STATE_DOUBLE, STATE_NUMBER, STATE_STRING, STATE_COMMENT}
    private StateType state = StateType.STATE_START;

    /**
     * This constructor initializes the known words list with the Shank keywords.
     */
    public Lexer()
    {
        knownWords.put("while", Token.TokenType.WHILE);
        knownWords.put("var", Token.TokenType.VAR);
        knownWords.put("define", Token.TokenType.DEFINE);
        knownWords.put("for", Token.TokenType.FOR);
        knownWords.put("from", Token.TokenType.FROM);
        knownWords.put("to", Token.TokenType.TO);
        knownWords.put("write", Token.TokenType.WRITE);
        knownWords.put("integer", Token.TokenType.INTEGER);
        knownWords.put("real", Token.TokenType.REAL);
        knownWords.put("string", Token.TokenType.STRING);
        knownWords.put("constants", Token.TokenType.CONSTANTS);
        knownWords.put("variables", Token.TokenType.VARIABLES);
        knownWords.put("if", Token.TokenType.IF);
        knownWords.put("elsif", Token.TokenType.ELSIF);
        knownWords.put("else", Token.TokenType.ELSE);
        knownWords.put("mod", Token.TokenType.MOD);
        knownWords.put("repeat", Token.TokenType.REPEAT);
        knownWords.put("until", Token.TokenType.UNTIL);
        knownWords.put("=", Token.TokenType.EQUAL);
        knownWords.put("<", Token.TokenType.LESS_THAN);
        knownWords.put("<=", Token.TokenType.LESS_THAN_EQUAL);
        knownWords.put(">", Token.TokenType.GREATER_THAN);
        knownWords.put(">=", Token.TokenType.GREATER_THAN_EQUAL);
        knownWords.put("+", Token.TokenType.PLUS);
        knownWords.put("-", Token.TokenType.MINUS);
        knownWords.put("*", Token.TokenType.TIMES);
        knownWords.put("/", Token.TokenType.DIVIDE);
        knownWords.put("(", Token.TokenType.LEFT_PARENTHESIS);
        knownWords.put(")", Token.TokenType.RIGHT_PARENTHESIS);
        knownWords.put("\\.", Token.TokenType.PERIOD);
        knownWords.put("!", Token.TokenType.EXCLAMATION);
        knownWords.put(";", Token.TokenType.SEMICOLON);
        knownWords.put(":", Token.TokenType.COLON);
        knownWords.put(":=", Token.TokenType.ASSIGN);
        knownWords.put(",", Token.TokenType.COMMA);
        knownWords.put("?", Token.TokenType.QUESTION_MARK);
        knownWords.put("true", Token.TokenType.TRUE);
        knownWords.put("false", Token.TokenType.FALSE);
        knownWords.put("then", Token.TokenType.THEN);
    }

    /**
     * This method extracts all tokens from the line.
     * @param line
     * @throws Exception
     */
    public void lex(String line) throws Exception
    {
        lineNumber++;

        if (state != StateType.STATE_COMMENT)
        {
            state = StateType.STATE_START;
        }

        token = new Token();

        boolean countingIndentations = true;
        int indentationCount = 0;
        int spaceCount = 0;

        for (int i = 0; i < line.length(); i++)
        {
            char character = line.charAt(i);

            // Handle indentations
            if (countingIndentations)
            {
                if (character == ' ' || character == '\t')
                {
                    if (character == ' ')
                    {
                        spaceCount++;

                        if (spaceCount == 4)
                        {
                            indentationCount++;
                            spaceCount = 0;
                        }
                    }
                    else
                    {
                        indentationCount++;
                    }

                    continue;
                }
                else
                {
                    countingIndentations = false;

                    if (indentationCount > lastIndentationCount)
                    {
                        int indentTokenCount = indentationCount - lastIndentationCount;

                        for (int j = 0; j < indentTokenCount; j++)
                        {
                            token.setTokenType(Token.TokenType.INDENT);

                            addToken(token);
                            token = new Token();
                        }
                    }
                    else if (indentationCount < lastIndentationCount)
                    {
                        int dedentTokenCount = lastIndentationCount - indentationCount;

                        for (int j = 0; j < dedentTokenCount; j++)
                        {
                            token.setTokenType(Token.TokenType.DEDENT);

                            addToken(token);
                            token = new Token();
                        }
                    }

                    lastIndentationCount = indentationCount;
                }
            }

            // Handle comments
            if (state == StateType.STATE_START && character == '{')
            {
                state = StateType.STATE_COMMENT;
                continue;
            }
            else if (state == StateType.STATE_COMMENT && character != '}')
            {
                continue;
            }
            else if (character == '}')
            {
                state = StateType.STATE_START;
            }

            // Handling everything else
            if (Character.isLetter(character) || Character.isDigit(character) || isValidSpecialCharacter(character))
            {
                if (Character.isLetter(character))
                {
                    if (state == StateType.STATE_START)
                    {
                        state = StateType.STATE_IDENTIFIER;

                        token.addCharacter(character);
                        token.setTokenType(Token.TokenType.IDENTIFIER);
                    }
                    else if (state == StateType.STATE_IDENTIFIER)
                    {
                        token.addCharacter(character);
                    }
                    else if (state == StateType.STATE_DOUBLE)
                    {
                        state = StateType.STATE_START;

                        addToken(token);
                        token = new Token();
                    }
                    else if (state == StateType.STATE_NUMBER)
                    {
                        state = StateType.STATE_START;

                        addToken(token);
                        token = new Token();
                    }
                    else if (state == StateType.STATE_STRING)
                    {
                        token.addCharacter(character);
                    }
                }
                else if (Character.isDigit(character))
                {
                    if (state == StateType.STATE_START)
                    {
                        state = StateType.STATE_NUMBER;

                        token.addCharacter(character);
                        token.setTokenType(Token.TokenType.NUMBER);
                    }
                    else if (state == StateType.STATE_IDENTIFIER)
                    {
                        token.addCharacter(character);
                    }
                    else if (state == StateType.STATE_DOUBLE)
                    {
                        token.addCharacter(character);
                        token.setTokenType(Token.TokenType.NUMBER);
                    }
                    else if (state == StateType.STATE_NUMBER)
                    {
                        token.addCharacter(character);
                    }
                    else if (state == StateType.STATE_STRING)
                    {
                        token.addCharacter(character);
                    }
                }
                else if (character == '+' || character == '-' || character == '*' || character == '/' || character == '>' || character == '<')
                {
                    if (state == StateType.STATE_START || state == StateType.STATE_NUMBER)
                    {
                        if (state == StateType.STATE_NUMBER)
                        {
                            addToken(token);
                            token = new Token();
                        }

                        if (character == '+')
                        {
                            token.setTokenType(Token.TokenType.PLUS);
                        }
                        else if (character == '-')
                        {
                            token.setTokenType(Token.TokenType.MINUS);
                        }
                        else if (character == '*')
                        {
                            token.setTokenType(Token.TokenType.TIMES);
                        }
                        else if (character == '>')
                        {
                            token.setTokenType(Token.TokenType.GREATER_THAN);
                        }
                        else if (character == '<')
                        {
                            token.setTokenType(Token.TokenType.LESS_THAN);
                        }
                        else
                        {
                            token.setTokenType(Token.TokenType.DIVIDE);
                        }

                        addToken(token);
                        token = new Token();

                        state = StateType.STATE_START;
                    }
                }
                else if (character == '.')
                {
                    if (state == StateType.STATE_START)
                    {
                        state = StateType.STATE_DOUBLE;

                        token.addCharacter(character);
                        token.setTokenType(Token.TokenType.PERIOD);
                    }
                    else if (state == StateType.STATE_IDENTIFIER)
                    {
                        addToken(token);
                        token = new Token();
                    }
                    else if (state == StateType.STATE_DOUBLE)
                    {
                        addToken(token);
                        token = new Token();

                        state = StateType.STATE_START;
                    }
                    else if (state == StateType.STATE_NUMBER)
                    {
                        state = StateType.STATE_DOUBLE;

                        token.addCharacter(character);
                    }
                    else if (state == StateType.STATE_STRING)
                    {
                        token.addCharacter(character);
                    }
                }
                else if (character == '"')
                {
                    if (state == StateType.STATE_START)
                    {
                        state = StateType.STATE_STRING;

                        token.setTokenType(Token.TokenType.STRINGLITERAL);
                    }
                    else if (state == StateType.STATE_STRING)
                    {
                        state = StateType.STATE_START;

                        addToken(token);
                        token = new Token();
                    }
                }
                else if (character == '\'')
                {
                    if (state == StateType.STATE_START)
                    {
                        state = StateType.STATE_STRING;

                        token.setTokenType(Token.TokenType.CHARACTERLITERAL);
                    }
                    else if (state == StateType.STATE_STRING)
                    {
                        state = StateType.STATE_START;

                        addToken(token);
                        token = new Token();
                    }
                }
                else if (character == '(' || character == ')' || character == ':' || character == ',' || character == '=' || character == ';')
                {
                    if (state != StateType.STATE_COMMENT)
                    {
                        if (!token.isEmpty())
                        {
                            addToken(token);
                            token = new Token();
                        }

                        token.addCharacter(character);

                        addToken(token);
                        token = new Token();

                        state = StateType.STATE_START;
                    }
                }
                else
                {
                    if (state != StateType.STATE_START && state != StateType.STATE_STRING)
                    {
                        state = StateType.STATE_START;

                        addToken(token);
                        token = new Token();
                    }
                    else if (state == StateType.STATE_STRING && character == ' ')
                    {
                        token.addCharacter(character);
                    }
                }
            }
            else
            {
                throw new SyntaxErrorException("Line " + lineNumber + ": An invalid character '" + character + "' was encountered for the input \"" + line + "\"");
            }
        }

//        if (lastIndentationCount > 0 && line.equals(""))
//        {
//            for (int i = 0; i < lastIndentationCount; i++)
//            {
//                token.setTokenType(Token.TokenType.DEDENT);
//
//                addToken(token);
//                token = new Token();
//            }
//
//            lastIndentationCount = 0;
//        }

        if (!token.isEmpty())
        {
            addToken(token);
        }

        token = new Token();
        token.setTokenType(Token.TokenType.ENDOFLINE);

        addToken(token);
    }

    // add "quotes" in state machine and {braces}.
    /**
     * This method refreshes the token list.
     */
    public void clearTokens() {
        this.tokens = new ArrayList<>();
    }

    /**
     * This methods returns all tokens in the string format.
     * @return
     */
    @Override
    public String toString() {
        StringBuilder lexerString = new StringBuilder();

        for (Token token : tokens)
        {
            lexerString.append(token.toString()).append(" ");
        }

        return lexerString + "\n";
    }

    public List<Token> getTokens() {
        return tokens;
    }

    /**
     * This method checks if the character is valid.
     * @param character
     * @return
     */
    private boolean isValidSpecialCharacter(char character)
    {
        Character[] validCharacters = {'.', ' ', '+', '-', '/', '*', '"', '\'', '{', '}', '!', '(', ')', ',', ':', '=', '>', '<', ';'};

        for (char c : validCharacters)
        {
            if (character == c)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * This method checks if the token value is a known word before adding the token to the token list.
     * @param token
     */
    private void addToken(Token token)
    {
        if (!token.isEmpty())
        {
            for (String key : knownWords.keySet())
            {
                if (token.getValue().equalsIgnoreCase(key))
                {
                    token.setTokenType(knownWords.get(key));
                    token.setValue("");
                    break;
                }
            }
        }

        if (token.getTokenType() != null)
        {
            tokens.add(token);
        }
    }
}
