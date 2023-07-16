import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Maintains all interpretation related methods for Shank
 */
public class Interpreter
{
    private ProgramNode programNode;
    private HashMap<String, AssignmentNode> variables;
    private HashMap<String, InterpreterDataType> constants;

    public Interpreter()
    {
        this.variables = new HashMap<>();
        this.constants = new HashMap<>();
    }

    /**
     * Begins interpreting the code after parsing it.
     * @param programNode
     */
    public void interpret(ProgramNode programNode) throws Exception {
        this.programNode = programNode;

        // Get current function nodes
        List<FunctionNode> functions = new ArrayList<>(programNode.getFunctionNodes().values());

        // Add built-in functions to the program
        programNode.addFunctionNode(new Read());
        programNode.addFunctionNode(new Write());
        programNode.addFunctionNode(new Left());
        programNode.addFunctionNode(new Right());
        programNode.addFunctionNode(new Substring());
        programNode.addFunctionNode(new SquareRoot());
        programNode.addFunctionNode(new GetRandom());
        programNode.addFunctionNode(new IntegerToReal());
        programNode.addFunctionNode(new RealToInteger());
        programNode.addFunctionNode(new Start());
        programNode.addFunctionNode(new End());

        // Output parsed program
        System.out.println(programNode);
        System.out.println();

        // Execute program
        System.out.println("Program output:");

        // Interpret the last function which will call the other functions
        interpretFunction(functions.get(0), null);
    }

    /**
     * Executes statements within a function
     * @param functionNode
     */
    private void interpretFunction(FunctionNode functionNode, List<InterpreterDataType> interpreterDataTypes) throws Exception {
        if (interpreterDataTypes != null)
        {
            // Update function parameters with provided arguments
            List<VariableNode> parameters = functionNode.getParameters();

            for (int i = 0; i < parameters.size(); i++)
            {
                VariableNode parameter = parameters.get(i);
                InterpreterDataType argumentValue = interpreterDataTypes.get(i);

                if (argumentValue instanceof IntegerDataType)
                {
                    int value = Integer.parseInt(argumentValue.ToString());

                    variables.put(parameter.getName(), new AssignmentNode(new VariableReferenceNode(parameter.getName(), null), new IntegerNode(value), null));
                }
                else if (argumentValue instanceof RealDataType)
                {
                    float value = Float.parseFloat(argumentValue.ToString());

                    variables.put(parameter.getName(), new AssignmentNode(new VariableReferenceNode(parameter.getName(), null), new RealNode(value), null));
                }
                else if (argumentValue instanceof StringDataType)
                {
                    String value = argumentValue.ToString();

                    variables.put(parameter.getName(), new AssignmentNode(new VariableReferenceNode(parameter.getName(), null), new StringNode(value), null));
                }
            }
        }

        List<StatementBaseNode> functionStatements = functionNode.getStatements();
        List<StatementBaseNode> statements = functionStatements.stream()
                .map(node -> (StatementBaseNode) node)
                .collect(Collectors.toList());

        handleConstants(functionNode.getConstants());
        interpretBlock(statements);
    }

    /**
     * Handles function call within a function
     * @param functionCallNode
     */
    private void handleFunctionCall(FunctionCallNode functionCallNode) throws Exception
    {
        String functionName = functionCallNode.getName();
        FunctionNode functionNode = programNode.getFunctionNode(functionName);

        if (functionNode == null)
        {
            throw new Exception("Function (" + functionName + ") not found.");
        }

        // Check number of arguments or if function is variadic
        if (!functionNode.isVariadic() && functionNode.getParameters().size() != functionCallNode.getParameters().size())
        {
            throw new Exception("Incorrect arguments provided for function (" + functionName + ").");
        }

        List<InterpreterDataType> interpreterDataTypes = new ArrayList<>();

        // Save IDT for each parameter in the IDT list
        for (ParameterNode parameterNode : functionCallNode.getParameters())
        {
            Node value = parameterNode.getValue();

            if (value instanceof IntegerNode)
            {
                interpreterDataTypes.add(new IntegerDataType(((IntegerNode) value).getValue()));
            }
            else if (value instanceof RealNode)
            {
                interpreterDataTypes.add(new RealDataType(((RealNode) value).getValue()));
            }
            else if (value instanceof StringNode)
            {
                interpreterDataTypes.add(new StringDataType(((StringNode) value).getValue()));
            }
        }

        // Call the function
        if (functionName.equals("write"))
        {
            Write write = new Write();
            write.execute(interpreterDataTypes);
        }
        else
        {
            interpretFunction(functionNode, interpreterDataTypes);
        }
    }

    /**
     * Saves constants defined in the function
     * @param constants
     * @return
     */
    private InterpreterDataType handleConstants(List<VariableNode> constants)
    {
        for (VariableNode constant : constants)
        {
            if (constant.getValue() instanceof IntegerNode)
            {
                int value = ((IntegerNode) constant.getValue()).getValue();
                this.constants.put(constant.getName(), new IntegerDataType(value));
            }
            else if (constant.getValue() instanceof RealNode)
            {
                float value = ((RealNode) constant.getValue()).getValue();
                this.constants.put(constant.getName(), new RealDataType(value));
            }
            else
            {
                String value = ((StringNode) constant.getValue()).getValue();
                this.constants.put(constant.getName(), new StringDataType(value));
            }
        }

        return null;
    }

    /**
     * Executes statements if conditions meet for while-loop
     * @param whileNode
     */
    private void handleWhileNode(WhileNode whileNode) throws Exception {
        if (whileNode != null)
        {
            // While condition is true
            while (booleanCompare(whileNode.getConditions()))
            {
                List<StatementBaseNode> nodes = new ArrayList<>();

                // Collect block statements
                for (StatementBaseNode node : whileNode.getStatements())
                {
                    nodes.add((StatementBaseNode) node.getNode());
                }

                interpretBlock(nodes);
            }
        }
    }

    /**
     * Executes statements if conditions meet for for-loop
     * @param forNode
     */
    private void handleForNode(ForNode forNode) throws Exception {
        if (forNode != null)
        {
            for (int i = ((IntegerNode) forNode.getFrom()).getValue(); i <= ((IntegerNode) forNode.getTo()).getValue(); i++)
            {
                List<StatementBaseNode> nodes = new ArrayList<>();

                // Collect block statements
                for (StatementBaseNode node : forNode.getStatements())
                {
                    nodes.add((StatementBaseNode) node.getNode());
                }

                interpretBlock(nodes);
            }
        }
    }

    /**
     * Executes statements if conditions meet for repeat-loop
     * @param repeatNode
     */
    private void handleRepeatNode(RepeatNode repeatNode) throws Exception {
        if (repeatNode != null)
        {
            // Repeat until condition becomes true
            while (!booleanCompare(repeatNode.getConditions()))
            {
                List<StatementBaseNode> nodes = new ArrayList<>();

                // Collect block statements
                for (StatementBaseNode node : repeatNode.getStatements())
                {
                    nodes.add((StatementBaseNode) node.getNode());
                }

                interpretBlock(nodes);
            }
        }
    }


    /**
     * Executes statements if conditions meet or recursively calls the next if node.
     * @param ifNode
     */
    private void handleIfNode(IfNode ifNode) throws Exception {
        if (ifNode != null)
        {
            boolean boolValue = booleanCompare(ifNode.getConditions());

            // If condition evaluates to true
            if (boolValue)
            {
                List<StatementBaseNode> nodes = new ArrayList<>();

                // Collect block statements
                for (StatementBaseNode node : ifNode.getStatements())
                {
                    nodes.add((StatementBaseNode) node.getNode());
                }

                interpretBlock(nodes);
            }
            else
            {
                handleIfNode(ifNode.getNextIf());
            }
        }
    }

    /**
     * Executes statements within a code block
     * @param statements
     */
    private void interpretBlock(List<StatementBaseNode> statements) throws Exception {
        for (StatementBaseNode statement : statements)
        {
            // Save variable
            if (statement instanceof AssignmentNode)
            {
                VariableReferenceNode variableReferenceNode = ((AssignmentNode) statement).getTarget();
                Node value = ((AssignmentNode) statement).getValue();

                if (value instanceof MathOpNode)
                {
                    value.setLeft(new RealNode(value.evaluate()));

                    ((AssignmentNode) statement).setValue(value);
                }

                variables.put(variableReferenceNode.getName(), (AssignmentNode) statement);
            }
            // Handle function call
            else if (statement instanceof FunctionCallNode)
            {
                String functionName = ((FunctionCallNode) statement).getName();
                List<InterpreterDataType> interpreterDataTypes = new ArrayList<>();
                List<ParameterNode> parameterNodes = ((FunctionCallNode) statement).getParameters();

                // Handle parameter types
                for (ParameterNode parameterNode : parameterNodes)
                {
                    Node value = parameterNode.getValue();

                    if (value instanceof StringNode)
                    {
                        interpreterDataTypes.add(new StringDataType(((StringNode) value).getValue()));
                    }
                    else if (value instanceof IntegerNode)
                    {
                        interpreterDataTypes.add(new IntegerDataType(((IntegerNode) value).getValue()));
                    }
                    else if (value instanceof RealNode)
                    {
                        interpreterDataTypes.add(new RealDataType(((RealNode) value).getValue()));
                    }
                }

                if (functionName.equals("write"))
                {
                    Write write = new Write();
                    write.execute(interpreterDataTypes);
                }
                else
                {
                    handleFunctionCall((FunctionCallNode) statement);
                }
            }
            // Handle if node
            else if (statement instanceof IfNode)
            {
                handleIfNode((IfNode) statement);
            }
            // Handle while node
            else if (statement instanceof WhileNode)
            {
                handleWhileNode((WhileNode) statement);
            }
            // Handle for node
            else if (statement instanceof ForNode)
            {
                handleForNode((ForNode) statement);
            }
            // Handle repeat node
            else if (statement instanceof RepeatNode)
            {
                handleRepeatNode((RepeatNode) statement);
            }
        }
    }

    /**
     * Evaluates boolean conditions
     * @param conditions
     * @return
     */
    private boolean booleanCompare(BooleanCompareNode conditions)
    {
        if (conditions == null)
        {
            return true;
        }

        // Get numeric values
        float leftValue = getVariableValue(conditions.getLeft());
        float rightValue = getVariableValue(conditions.getRight());

        // Compare sides
        if (conditions.getCompareType() == Token.TokenType.EQUAL)
        {
            return leftValue == rightValue;
        }
        else if (conditions.getCompareType() == Token.TokenType.GREATER_THAN)
        {
            return leftValue > rightValue;
        }
        else if (conditions.getCompareType() == Token.TokenType.GREATER_THAN_EQUAL)
        {
            return leftValue >= rightValue;
        }
        else if (conditions.getCompareType() == Token.TokenType.LESS_THAN)
        {
            return leftValue < rightValue;
        }
        else if (conditions.getCompareType() == Token.TokenType.LESS_THAN_EQUAL)
        {
            return leftValue <= rightValue;
        }

        return true;
    }

    /**
     * Gets value from a variable
     * @param node
     * @return
     */
    private float getVariableValue(Node node)
    {
        float value = 0;

        if (node instanceof VariableReferenceNode)
        {
            String variableName = ((VariableReferenceNode) node).getName();

            if (variables.get(variableName).getValue() instanceof IntegerNode)
            {
                value = ((IntegerNode) variables.get(variableName).getValue()).getValue();
            }
            else if (variables.get(variableName).getValue() instanceof RealNode)
            {
                value = ((RealNode) variables.get(variableName).getValue()).getValue();
            }
            else if (variables.get(variableName).getValue() instanceof MathOpNode)
            {
                // handles MathOpNode
                value = ((RealNode)(((MathOpNode) variables.get(variableName).getValue()).getLeft())).getValue();
            }
        }
        else
        {
            if (node instanceof IntegerNode)
            {
                value = ((IntegerNode) node).getValue();
            }
            else if (node instanceof RealNode)
            {
                value = ((RealNode) node).getValue();
            }
        }

        return value;
    }
}
