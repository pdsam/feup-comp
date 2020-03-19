public abstract class BinOpExpression extends Expression {
    protected Expression right;
    protected Expression left;

    public BinOpExpression(int id) {
        super(id);
    }

    public BinOpExpression(MyGrammar p, int id) {
        super(p, id);
    }
}