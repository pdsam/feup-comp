package parser;

import controlFlowAnalysis.ControlFlowNode;

public class Statement extends SimpleNode {
    public ControlFlowNode cfNode;

    public Statement(int id) {
        super(id);
    }

    public Statement(MyGrammar p, int id) {
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