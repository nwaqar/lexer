import java.util.List;

public class IfNode extends StatementBaseNode
{
    private List<Token> tokens;
    private BooleanCompareNode conditions;
    private List<StatementBaseNode> statements;
    private IfNode nextIf;

    public IfNode(List<Token> tokens) {
        this.tokens = tokens;
    }

    public IfNode(BooleanCompareNode conditions, List<StatementBaseNode> elseStatements, IfNode nextIf)
    {
        super();

        this.conditions = conditions;
        this.statements = elseStatements;
        this.nextIf = nextIf;
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

    public IfNode getNextIf() {
        return nextIf;
    }

    public void setNextIf(IfNode nextIf) {
        this.nextIf = nextIf;
    }

    @Override
    public String toString() {
        return "IfNode{" +
                "tokens=" + tokens +
                ", conditions=" + conditions +
                ", statements=" + statements +
                ", nextIf=" + nextIf +
                '}';
    }
}