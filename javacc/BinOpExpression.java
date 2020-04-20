public abstract class BinOpExpression extends Expression {
    protected Expression right;
    protected Expression left;

    public BinOpExpression(int id) {
        super(id);
    }

    public BinOpExpression(MyGrammar p, int id) {
        super(p, id);
    }

    public void dump(String prefix) {
        System.out.println(toString(prefix));
        left.dump(prefix + " ");
        right.dump(prefix + " ");
    }

    @Override
    public Object jjtAccept(MyGrammarVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}