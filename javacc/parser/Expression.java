package parser;

public abstract class Expression extends SimpleNode {
    public String type;
    public Object foldedValue = null;

    public Expression(int id) {
        super(id);
    }

    public Expression(MyGrammar p, int id) {
        super(p, id);
    }

    @Override
    public void dump(String prefix) {
        if (children != null) {
            for (int i = 0; i < children.length; ++i) {
                SimpleNode n = (SimpleNode)children[i];
                if (n != null) {
                    n.dump(prefix + "  ");
                }
            }
        }
    }

    @Override
    public Object jjtAccept(MyGrammarVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}