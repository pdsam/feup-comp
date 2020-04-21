/* Generated By:JJTree: Do not edit this line. ASTScopedStatementList.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTScopedStatementList extends Statement {
  public ASTScopedStatementList(int id) {
    super(id);
  }

  public ASTScopedStatementList(MyGrammar p, int id) {
    super(p, id);
  }

  @Override
  public void dump(String prefix) {
    if (children != null) {
      System.out.println(toString(prefix));
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
/* JavaCC - OriginalChecksum=7280cde7b7b6bfdf79f4dfa881713faa (do not edit this line) */
