import java.util.List;

public class WhileNode extends StatementBaseNode
{
    private List<Token> tokens;
    private BooleanCompareNode conditions;
    private List<StatementBaseNode> statements;

    public WhileNode(List<Token> tokens) {
        this.tokens = tokens;
    }

    public WhileNode(BooleanCompareNode conditions, List<StatementBaseNode> statements) {
        super();

        this.conditions = conditions;
        this.statements = statements;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    public BooleanCompareNode getConditions() {
        return conditions;
    }

    public void setConditions(BooleanCompareNode conditions) {
        this.conditions = conditions;
    }

    public List<StatementBaseNode> getStatements() {
        return statements;
    }

    public void setStatements(List<StatementBaseNode> statements) {
        this.statements = statements;
    }

    @Override
    public String toString() {
        return "WhileNode{" +
                "tokens=" + tokens +
                ", conditions=" + conditions +
                ", statements=" + statements +
                '}';
    }
}