import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Parser
{
    private List<Token> tokens;
    private HashMap<String, VariableNode> variableNodes = new HashMap<>();

    /**
     * Parses an expression until the end of line
     * @param tokens
     * @return
     */
    public ProgramNode parse(List<Token> tokens) throws Exception
    {
        this.tokens = tokens;

        // Begins parsing program
        ProgramNode programNode = new ProgramNode();

        while (tokens.size() > 0)
        {
            Token token = peek(0);

            if (token.getTokenType() != Token.TokenType.DEFINE)
            {
                pop();
                continue;
            }

            // Parse function
            FunctionNode functionNode = function();

            if (functionNode == null)
            {
                break;
            }

            // Add function to program
            programNode.addFunctionNode(functionNode);
        }

        // For each function and for each statement within a function parse them as expressions
        for (String functionName : programNode.getFunctionNodes().keySet())
        {
            FunctionNode functionNode = programNode.getFunctionNode(functionName);

            // Save variable references in the class
            for (VariableNode variableNode : functionNode.getVariables())
            {
                this.variableNodes.put(variableNode.getName(), variableNode);
            }

            // Get all function body tokens
            this.tokens = new ArrayList<>();

            for (Node statementNode : functionNode.getStatements())
            {
                AssignmentNode assignmentNode = (AssignmentNode) statementNode;

                this.tokens.addAll(assignmentNode.getTokens());

                Token token = new Token();
                token.setTokenType(Token.TokenType.ENDOFLINE);

                this.tokens.add(token);
            }

            // Parse entire function body
            List<Node> statementNodes = handleFunctionTokens();
            List<StatementBaseNode> statements = new ArrayList<>();

            // Get specific type nodes based on instance type
            for (Node node : statementNodes)
            {
                if (node instanceof IfNode)
                {
                    IfNode ifNode = (IfNode) node;
                    statements.add(ifNode);
                }
                else if (node instanceof WhileNode)
                {
                    WhileNode whileNode = (WhileNode) node;
                    statements.add(whileNode);
                }
                else if (node instanceof ForNode)
                {
                    ForNode forNode = (ForNode) node;
                    statements.add(forNode);
                }
                else if (node instanceof RepeatNode)
                {
                    RepeatNode repeatNode = (RepeatNode) node;
                    statements.add(repeatNode);
                }
                else if (node instanceof FunctionCallNode)
                {
                    FunctionCallNode functionCallNode = (FunctionCallNode) node;
                    statements.add(functionCallNode);
                }
                else if (node instanceof AssignmentNode)
                {
                    AssignmentNode assignmentNode = (AssignmentNode) node;
                    statements.add(assignmentNode);
                }
            }

            // add statements of a function to the functions list
            functionNode.setStatements(statements);
        }

        return programNode;
    }

    /**
     * Parses all tokens within a function body
     * @return
     * @throws Exception
     */
    private List<Node> handleFunctionTokens() throws Exception
    {
        List<Node> statementNodes = new ArrayList<>();

        // Parses each statement depending on how it starts
        while (this.tokens.size() > 0)
        {
            if (peek(0).getTokenType() == Token.TokenType.ENDOFLINE)
            {
                pop();
            }

            if (this.tokens.size() == 0)
            {
                break;
            }

            if (peek(0).getTokenType() == Token.TokenType.IF)
            {
                statementNodes.add(parseIf());
            }
            else if (peek(0).getTokenType() == Token.TokenType.WHILE)
            {
                statementNodes.add(parseWhile());
            }
            else if (peek(0).getTokenType() == Token.TokenType.FOR)
            {
                statementNodes.add(parseFor());
            }
            else if (peek(0).getTokenType() == Token.TokenType.REPEAT)
            {
                statementNodes.add(parseRepeat());
            }
            // Could be a function call or an assignment
            else if (peek(0).getTokenType() == Token.TokenType.IDENTIFIER || peek(0).getTokenType() == Token.TokenType.WRITE)
            {
                // identifier :=
                if (this.tokens.size() > 3 && peek(1).getTokenType() == Token.TokenType.COLON && peek(2).getTokenType() == Token.TokenType.EQUAL)
                {
                    statementNodes.add(assignment());
                }
                else
                {
                    statementNodes.add(parseFunctionCall());
                }
            }
        }

        return statementNodes;
    }

    /**
     * Parses if, elsif, and else blocks within a function
     * @return
     * @throws Exception
     */
    private IfNode parseIf() throws Exception
    {
        pop(); // remove if

        List<Token> remainingTokens = new ArrayList<>();

        while (peek(0).getTokenType() != Token.TokenType.THEN)
        {
            remainingTokens.add(pop());
        }

        List<Token> temporaryTokens = this.tokens;
        this.tokens = remainingTokens;

        BooleanCompareNode condition = (BooleanCompareNode) boolCompare();

        this.tokens = temporaryTokens;

        pop(); // remove then
        pop(); // end of line

        List<StatementBaseNode> statements = parseStatements();

        IfNode nextIf = null;

        if (peek(0).getTokenType() == Token.TokenType.ELSIF)
        {
            nextIf = parseIf();
        }
        else if (peek(0).getTokenType() == Token.TokenType.ELSE)
        {
            pop(); // remove else

            // Get statements within else
            List<StatementBaseNode> elseStatements = parseStatements();

            nextIf = new IfNode(null, elseStatements, null);
        }

        return new IfNode(condition, statements, nextIf);
    }

    /**
     * Parses while blocks within a function
     * @return
     * @throws Exception
     */
    private WhileNode parseWhile() throws Exception
    {
        pop();

        List<Token> remainingTokens = new ArrayList<>();

        while (peek(0).getTokenType() != Token.TokenType.ENDOFLINE)
        {
            remainingTokens.add(pop());
        }

        List<Token> temporaryTokens = this.tokens;
        this.tokens = remainingTokens;

        BooleanCompareNode condition = (BooleanCompareNode) boolCompare();

        this.tokens = temporaryTokens;

        expectEndOfLine(); // remove endofline
        expect(Token.TokenType.INDENT);

        // Parse statements within the block
        List<StatementBaseNode> statements = parseStatements();

        return new WhileNode(condition, statements);
    }

    /**
     * Parses for blocks within a function
     * @return
     * @throws Exception
     */
    public ForNode parseFor() throws Exception {
        pop(); // for

        Token identifierToken = pop();
        String identifier = identifierToken.getValue();

        pop(); // from

        Token token = pop(); // start value
        Node from = new IntegerNode(Integer.parseInt(token.getValue()));

        pop(); // to

        token = pop(); // end value
        Node to = new IntegerNode(Integer.parseInt(token.getValue()));

        pop(); // end of line

        expect(Token.TokenType.INDENT); // for body starts

        // for statements
        List<StatementBaseNode> statements = parseStatements();

        // create loop variable
        VariableReferenceNode loopVariable = new VariableReferenceNode(identifier, null);

        return new ForNode(loopVariable, from, to, statements);
    }

    /**
     * Parses for blocks within a function
     * @return
     * @throws Exception
     */
    public RepeatNode parseRepeat() throws Exception {
        pop(); // repeat
        pop(); // until

        List<Token> remainingTokens = new ArrayList<>();

        while (peek(0).getTokenType() != Token.TokenType.ENDOFLINE)
        {
            remainingTokens.add(pop());
        }

        List<Token> temporaryTokens = this.tokens;
        this.tokens = remainingTokens;

        BooleanCompareNode condition = (BooleanCompareNode) boolCompare();

        this.tokens = temporaryTokens;

        expectEndOfLine(); // remove endofline
        expect(Token.TokenType.INDENT);

        // Parse statements within the block
        List<StatementBaseNode> statements = parseStatements();

        return new RepeatNode(condition, statements);
    }

    /**
     * Parses function calls within a function
     * @return
     */
    public FunctionCallNode parseFunctionCall() {
        Token functionNameToken = pop();
        String functionName = functionNameToken.getValue();

        if (functionName.equals(""))
        {
            functionName = functionNameToken.getTokenType().name().toLowerCase();
        }

        List<ParameterNode> parameterNodes = new ArrayList<>();

        while (peek(0).getTokenType() != Token.TokenType.ENDOFLINE)
        {
            Token token = pop();

            if (token.getTokenType() != Token.TokenType.COMMA)
            {
                Node node = getNodeByTokenTypeAndValue(token.getTokenType(), token.getValue());
                parameterNodes.add(new ParameterNode(null, node));
            }
        }

        return new FunctionCallNode(functionName, parameterNodes);
    }

    /**
     * Parses statements within if, while, and for blocks
     * @return
     * @throws Exception
     */
    private List<StatementBaseNode> parseStatements() throws Exception
    {
        List<StatementBaseNode> statementNodes = new ArrayList<>();

        if (peek(0).getTokenType() == Token.TokenType.ENDOFLINE)
        {
            pop(); // end of line
        }

        if (peek(0).getTokenType() == Token.TokenType.INDENT)
        {
            pop(); // indent
        }

        // while within block
        while (this.tokens.size() > 0 && peek(0).getTokenType() != Token.TokenType.DEDENT)
        {
            StatementBaseNode statementNode = new StatementBaseNode();

            if (peek(0).getTokenType() == Token.TokenType.IF)
            {
                statementNode.setNode(parseIf());
            }
            else if (peek(0).getTokenType() == Token.TokenType.FOR)
            {
                statementNode.setNode(parseFor());
            }
            else if (peek(0).getTokenType() == Token.TokenType.WHILE)
            {
                statementNode.setNode(parseFor());
            }
            else if (peek(0).getTokenType() == Token.TokenType.IDENTIFIER || peek(0).getTokenType() == Token.TokenType.WRITE)
            {
                // identifier :=
                if (this.tokens.size() > 3 && peek(1).getTokenType() == Token.TokenType.COLON && peek(2).getTokenType() == Token.TokenType.EQUAL)
                {
                    statementNode.setNode(assignment());
                }
                else
                {
                    statementNode.setNode(parseFunctionCall());
                }

                statementNodes.add(statementNode);

                continue;
            }

            // while not end of line
            while (this.tokens.size() > 0 && peek(0).getTokenType() != Token.TokenType.DEDENT && peek(0).getTokenType() != Token.TokenType.ENDOFLINE)
            {
                statementNode.addToken(pop());

                if (this.tokens.size() == 0)
                {
                    break;
                }
            }

            statementNodes.add(statementNode);

            if (this.tokens.size() == 0)
            {
                break;
            }
            else if (peek(0).getTokenType().equals(Token.TokenType.ENDOFLINE))
            {
                pop();
            }
        }

        if (this.tokens.size() > 0)
        {
            pop(); // dedent
        }

        return statementNodes;
    }

    /**
     * Parses assignment statements
     * @return
     * @throws Exception
     */
    private AssignmentNode assignment() throws Exception
    {
        Token targetToken = pop(); // pop identifier
        String variableName = targetToken.getValue();

        pop(); // pop :
        pop(); // pop =

        // Get remaining tokens
        List<Token> remainingTokens = new ArrayList<>();

        while (peek(0).getTokenType() != Token.TokenType.ENDOFLINE)
        {
            remainingTokens.add(pop());
        }

        pop(); // end of line

        List<Token> temporaryTokens = this.tokens;
        this.tokens = remainingTokens;

        if (remainingTokens.size() == 0)
        {
            throw new Exception("Right side missing.");
        }

        VariableReferenceNode variableReferenceNode = new VariableReferenceNode(targetToken.getValue(), null);
        AssignmentNode assignmentNode = new AssignmentNode(variableReferenceNode, null, remainingTokens);

        // What's remaining is an expression or boolcompare
        boolean hasIdentifiers = false;
        boolean isBoolCompare = false;

        // Check if boolcompare
        for (Token token : this.tokens)
        {
            if (token.getTokenType() == Token.TokenType.LESS_THAN ||
                    token.getTokenType() == Token.TokenType.GREATER_THAN ||
                    token.getTokenType() == Token.TokenType.LESS_THAN_EQUAL ||
                    token.getTokenType() == Token.TokenType.GREATER_THAN_EQUAL ||
                    token.getTokenType() == Token.TokenType.EQUALS ||
                    token.getTokenType() == Token.TokenType.NOT_EQUALS)
            {
                isBoolCompare = true;
            }
            else if (token.getTokenType() == Token.TokenType.IDENTIFIER)
            {
                hasIdentifiers = true;
            }
        }

        // Replace identifiers with values
        if (hasIdentifiers)
        {
            for (Token token : this.tokens)
            {
                if (token.getTokenType() != Token.TokenType.IDENTIFIER)
                {
                    continue;
                }

                VariableNode variable = getVariableByName(token.getValue());

                if (variable == null)
                {
                    throw new Exception("This variable '" + token.getValue() + "' has not been defined in the function");
                }

                if (variable.getType() == Token.TokenType.INTEGER)
                {
                    // number
                    IntegerNode integerNode = (IntegerNode) variable.getValue();

                    token.setTokenType(Token.TokenType.NUMBER);
                    token.setValue(String.valueOf(integerNode.getValue()));
                }
                else if (variable.getType() == Token.TokenType.REAL)
                {
                    // number
                    RealNode realNode = (RealNode) variable.getValue();

                    token.setTokenType(Token.TokenType.NUMBER);
                    token.setValue(String.valueOf(realNode.getValue()));
                }
                else
                {
                    // string
                    token.setValue(variable.getValue().toString());
                }
            }
        }

        // Execute bool comparison
        if (isBoolCompare)
        {
            Node result = boolCompare();

            VariableNode variableNode = getVariableByName(variableName);
            variableNode.setValue(result);

            assignmentNode.setValue(result);

            this.tokens = temporaryTokens;

            return assignmentNode;
        }

        // Get expression
        Node result = expression();

        VariableNode variableNode = getVariableByName(variableName);
        variableNode.setValue(result);

        assignmentNode.setValue(result);

        this.tokens = temporaryTokens;

        return assignmentNode;
    }

    /**
     * Compares expressions from left and right, and creates a node
     * @return
     * @throws Exception
     */
    private Node boolCompare() throws Exception
    {
        Node left = expression();
        Token token = peek(0);

        if (token.getTokenType() == Token.TokenType.LESS_THAN ||
                token.getTokenType() == Token.TokenType.GREATER_THAN ||
                token.getTokenType() == Token.TokenType.LESS_THAN_EQUAL ||
                token.getTokenType() == Token.TokenType.GREATER_THAN_EQUAL ||
                token.getTokenType() == Token.TokenType.EQUALS ||
                token.getTokenType() == Token.TokenType.NOT_EQUALS)
        {
            token = pop();
            Node right = expression();
            return new BooleanCompareNode(token.getTokenType(), left, right);
        }
        else
        {
            return new BooleanCompareNode(token.getTokenType(), left, null);
        }
    }

    /**
     * Develops the function node
     * @return
     */
    private FunctionNode function()
    {
        // Checks if it's a valid function
        Token currentToken = pop();

        if (currentToken.getTokenType() != Token.TokenType.DEFINE) {
            return null;
        }

        currentToken = pop();

        if (currentToken.getTokenType() != Token.TokenType.IDENTIFIER) {
            return null;
        }

        String functionName = currentToken.getValue();

        currentToken = pop();

        if (currentToken.getTokenType() != Token.TokenType.LEFT_PARENTHESIS) {
            return null;
        }

        // Gets function parameters
        List<VariableNode> parameters = parameterDeclarations();

        currentToken = pop();

        if (currentToken.getTokenType() != Token.TokenType.ENDOFLINE) {
            return null;
        }

        // Gets all constants
        List<VariableNode> constants = constants();

        if (peek(0).getTokenType() == Token.TokenType.ENDOFLINE)
        {
            currentToken = pop(); // end of line
        }

        // Gets all variables
        List<VariableNode> variables = variables();

        currentToken = pop(); // end of line
        currentToken = pop();

        if (currentToken.getTokenType() != Token.TokenType.INDENT)
        {
            return null;
        }

        int indentCount = 1;

        // Gets all statements
        List<StatementBaseNode> statementNodes = new ArrayList<>();

        while (true)
        {
            List<Token> statementTokens = new ArrayList<>();
            currentToken = pop();

            if (currentToken.getTokenType() == Token.TokenType.INDENT)
            {
                indentCount++;
            }
            else if (currentToken.getTokenType() == Token.TokenType.DEDENT)
            {
                indentCount--;
            }

            // if program is complete
            if (tokens.size() == 0 || indentCount == 0)
            {
                break;
            }

            while (currentToken.getTokenType() != Token.TokenType.ENDOFLINE)
            {
                statementTokens.add(currentToken);
                currentToken = pop();

                if (currentToken.getTokenType() == Token.TokenType.INDENT)
                {
                    indentCount++;
                }
                else if (currentToken.getTokenType() == Token.TokenType.DEDENT)
                {
                    indentCount--;
                }
            }

            statementNodes.add(new AssignmentNode(null, null, statementTokens));

            // if program is complete
            if (tokens.size() == 0 || indentCount == 0)
            {
                break;
            }
        }

        return new FunctionNode(functionName, parameters, variables, constants, statementNodes);
    }

    /**
     * Gets variables defined in the function
     * @return
     */
    private List<VariableNode> variables()
    {
        List<VariableNode> variableNodes = new ArrayList<>();
        Token token = peek(0);

        if (token.getTokenType() != Token.TokenType.VARIABLES)
        {
            return variableNodes;
        }

        pop();

        // Pop all variables
        while (true)
        {
            String name = pop().getValue();
            VariableNode variableNode = new VariableNode(name, null, true, null, 0, 0);
            variableNodes.add(variableNode);

            token = pop(); // comma or colon

            if (token.getTokenType() == Token.TokenType.COMMA)
            {
                continue; // continue to next identifier
            }

            break; // colon reached
        }

        Token.TokenType type = pop().getTokenType();

        for (VariableNode variableNode : variableNodes)
        {
            Node node = getNodeByTokenTypeAndValue(type, null);

            variableNode.setType(type);
            variableNode.setValue(node);
        }

        return variableNodes;
    }

    /**
     * Gets constants from the function
     * @return
     */
    private List<VariableNode> constants()
    {
        List<VariableNode> variableNodeList = new ArrayList<>();
        Token token = peek(0);

        if (token.getTokenType() != Token.TokenType.CONSTANTS)
        {
            return variableNodeList;
        }

        pop();

        while (true)
        {
            String name = pop().getValue();
            pop(); // pop equal
            token = pop();

            Token.TokenType type = token.getTokenType();
            String value = token.getValue();
            Node node = getNodeByTokenTypeAndValue(type, value);

            VariableNode variableNode = new VariableNode(name, type, false, node, 0, 0);
            variableNodeList.add(variableNode);

            if (peek(0).getTokenType() == Token.TokenType.COMMA)
            {
                pop();
                continue;
            }

            break;
        }

        return variableNodeList;
    }

    /**
     * Get variables from the parameters
     * @return
     */
    private List<VariableNode> parameterDeclarations()
    {
        List<VariableNode> variableNodeList = new ArrayList<>();

        if (peek(0).getTokenType() == Token.TokenType.RIGHT_PARENTHESIS)
        {
            return variableNodeList;
        }

        while (true)
        {
            String name = pop().getValue();

            pop(); // pop colon

            Token.TokenType type = pop().getTokenType();

            Node node;

            if (type == Token.TokenType.INTEGER)
            {
                node = new IntegerNode(0);
            }
            else if (type == Token.TokenType.REAL)
            {
                node = new RealNode(0);
            }
            // add more types
            else
            {
                node = new StringNode("");
            }

            VariableNode variableNode = new VariableNode(name, type, true, node, 0, 0);

            variableNodeList.add(variableNode);

            if (pop().getTokenType() != Token.TokenType.SEMICOLON)
            {
                break;
            }
        }

        return variableNodeList;
    }

    /**
     * Parses a single expression consisting of one or more terms.
     * @return
     */
    private Node expression() throws Exception
    {
        Node left = term();

        if (left == null)
        {
            return null;
        }

        while (true)
        {
            // Check if operator of plus or minus
            Token operator = matchAndRemove(Token.TokenType.PLUS, Token.TokenType.MINUS);

            if (operator == null)
            {
                break;
            }

            // Get the term for the right node if last character was an operator
            Node right = term();

            if (right == null)
            {
                throw new RuntimeException("Missing right-hand side of binary operator");
            }

            // Create leaves on the left node
            if (operator.getTokenType() == Token.TokenType.PLUS)
            {
                left = new MathOpNode("+", left, right);
            }
            else
            {
                left = new MathOpNode("-", left, right);
            }
        }

        return left;
    }

    /**
     * Parses a single term consisting of a number, operator, and number
     * @return
     */
    private Node term() throws Exception
    {
        Node left = factor();

        if (left == null)
        {
            return null;
        }

        while (true)
        {
            // Check if operator of multiplication, division, or mod
            Token operator = matchAndRemove(Token.TokenType.TIMES, Token.TokenType.DIVIDE, Token.TokenType.MOD);

            if (operator == null)
            {
                break;
            }

            Node right = factor();

            if (right == null)
            {
                throw new RuntimeException("Missing right-hand side of binary operator");
            }

            if (operator.getTokenType() == Token.TokenType.TIMES)
            {
                left = new MathOpNode("*", left, right);
            }
            else if (operator.getTokenType() == Token.TokenType.DIVIDE)
            {
                left = new MathOpNode("/", left, right);
            }
            else
            {
                left = new MathOpNode("%", left, right);
            }
        }

        return left;
    }

    /**
     * Parses a single factor (number or expression)
     * @return
     */
    private Node factor() throws Exception
    {
        Token operator = matchAndRemove(Token.TokenType.MINUS);

        if (operator != null)
        {
            // Check if there is a number ahead of the minus to make it a negative number
            Token tokenAhead = peek(0);

            if (tokenAhead.getTokenType() == Token.TokenType.NUMBER)
            {
                if (tokenAhead.getValue().contains("\\."))
                {
                    pop();
                    return new RealNode(Float.parseFloat(tokenAhead.getValue()) * -1);
                }
                else
                {
                    pop();
                    return new IntegerNode(Integer.parseInt(tokenAhead.getValue()) * -1);
                }
            }

            Node operand = factor();

            if (operand == null)
            {
                throw new RuntimeException("Missing operand after unary minus");
            }

            return new MathOpNode("-", operand);
        }

        Token number = matchAndRemove(Token.TokenType.NUMBER);

        if (number != null)
        {
            return getNodeByTokenTypeAndValue(number.getTokenType(), number.getValue());
        }

        Token leftParenthesis = matchAndRemove(Token.TokenType.LEFT_PARENTHESIS);

        // If there is a left parenthesis, parse token within as a separate expression
        if (leftParenthesis != null)
        {
            Node expression = expression();

            if (expression == null)
            {
                throw new RuntimeException("Missing expression inside parentheses");
            }

            expect(Token.TokenType.RIGHT_PARENTHESIS);

            return expression;
        }

        // Parse variable (identifier)
        Token identifier = matchAndRemove(Token.TokenType.IDENTIFIER);

        if (identifier != null)
        {
            VariableNode variable = getVariableByName(identifier.getValue());

            if (variable == null)
            {
                throw new Exception("This variable '" + identifier.getValue() + "' has not been defined in the function");
            }

            return new VariableReferenceNode(variable.getName(), null);
        }

        // Parse boolean statements
        Token boolTokenTrue = matchAndRemove(Token.TokenType.TRUE);

        if (boolTokenTrue != null)
        {
            return new BoolNode(true);
        }

        Token boolTokenFalse = matchAndRemove(Token.TokenType.FALSE);

        if (boolTokenFalse != null)
        {
            return new BoolNode(false);
        }

        return null;
    }

    /**
     * Match the token type and remove the token
     * @param types
     * @return
     */
    private Token matchAndRemove(Token.TokenType... types)
    {
        for (Token.TokenType type : types)
        {
            if (tokens.size() > 0 && peek(0).getTokenType() == type)
            {
                return pop();
            }
        }

        return null;
    }

    /**
     * Check if the next type is expected or not
     * @param type
     */
    private void expect(Token.TokenType type)
    {
        Token token = matchAndRemove(type);

        if (token == null)
        {
            throw new RuntimeException("Expected " + type + " but found " + peek(0));
        }
    }

    /**
     * Check if end of line
     */
    private void expectEndOfLine()
    {
        if (tokens.size() > 0 && peek(0).getTokenType() == Token.TokenType.ENDOFLINE)
        {
            pop();
        }
    }

    /**
     * Get the token at given index but don't remove
     * @param index
     * @return
     */
    private Token peek(int index)
    {
        return tokens.get(index);
    }

    /**
     * Get and remove the first item in queue
     * @return
     */
    private Token pop()
    {
        return tokens.remove(0);
    }

    /**
     * Gets specific node by token type and value
     * @param type
     * @param value
     * @return
     */
    private Node getNodeByTokenTypeAndValue(Token.TokenType type, String value)
    {
        Node node;

        if (type == Token.TokenType.INTEGER)
        {
            node = new IntegerNode(0);
        }
        else if (type == Token.TokenType.REAL)
        {
            node = new RealNode(0);
        }
        else if (type == Token.TokenType.NUMBER)
        {
            if (value.contains("."))
            {
                return new RealNode(Float.parseFloat(value));
            }
            else
            {
                return new IntegerNode(Integer.parseInt(value));
            }
        }
        else
        {
            node = new StringNode(value);
        }

        return node;
    }

    /**
     * Gets variable by name from the function or throws an exception if name is not defined
     * @param name
     * @return
     * @throws Exception
     */
    private VariableNode getVariableByName(String name) throws Exception
    {
        for (String variableName : variableNodes.keySet())
        {
            if (variableName.equals(name))
            {
                return variableNodes.get(variableName);
            }
        }

        throw new Exception("Variable '" + name + "' is not defined in the function.");
    }
}
