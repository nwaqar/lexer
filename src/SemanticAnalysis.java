import java.util.List;

public class SemanticAnalysis
{
    /**
     * Checks assignments within each program function
     * @param programNode
     * @throws Exception
     */
    public void checkAssignments(ProgramNode programNode) throws Exception
    {
        for (FunctionNode functionNode : programNode.getFunctionNodes().values())
        {
            checkAssignmentsInBlock(functionNode.getStatements(), functionNode.getVariables());
        }
    }

    /**
     * Either checks assignment or assignments within a nested block
     * @param statements
     * @param variableNodes
     * @throws Exception
     */
    private void checkAssignmentsInBlock(List<StatementBaseNode> statements, List<VariableNode> variableNodes) throws Exception
    {
        for (Node statement : statements)
        {
            if (statement instanceof AssignmentNode)
            {
                checkAssignment((AssignmentNode) statement, variableNodes);
            }
            else if (statement instanceof IfNode)
            {
                checkAssignmentsInBlock(((IfNode) statement).getStatements(), variableNodes);
            }
            else if (statement instanceof WhileNode)
            {
                checkAssignmentsInBlock(((WhileNode) statement).getStatements(), variableNodes);
            }
            else if (statement instanceof ForNode)
            {
                checkAssignmentsInBlock(((ForNode) statement).getStatements(), variableNodes);
            }
            else if (statement instanceof RepeatNode)
            {
                checkAssignmentsInBlock(((RepeatNode) statement).getStatements(),variableNodes);
            }
        }
    }

    /**
     * Compares variable node token type and the value node. Throw an exception if they don't match
     * @param assignmentNode
     * @param variableNodes
     * @throws Exception
     */
    private void checkAssignment(AssignmentNode assignmentNode, List<VariableNode> variableNodes) throws Exception
    {
        VariableReferenceNode variableReferenceNode = assignmentNode.getTarget();
        Node value = assignmentNode.getValue();

        // Compare variable node and the assignment value provided. Throw error if they don't match
        for (VariableNode variableNode : variableNodes)
        {
            if (variableNode.getName().equals(variableReferenceNode.getName()))
            {
                if (variableNode.getType() == Token.TokenType.INTEGER)
                {
                    if (!(value instanceof IntegerNode))
                    {
                        throw new Exception("Value (" + value.toString() + ") does not match variable type (Integer)");
                    }
                }
                else if (variableNode.getType() == Token.TokenType.REAL)
                {
                    if (!(value instanceof RealNode))
                    {
                        throw new Exception("Value (" + value.toString() + ") does not match variable type (Real)");
                    }
                }
                else if (variableNode.getType() == Token.TokenType.STRING)
                {
                    if (!(value instanceof StringNode))
                    {
                        throw new Exception("Value (" + value.toString() + ") does not match variable type (String)");
                    }
                }

                break;
            }
        }
    }
}
