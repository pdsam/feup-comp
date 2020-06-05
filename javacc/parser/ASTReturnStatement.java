/* Generated By:JJTree: Do not edit this line. ASTReturnStatement.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTReturnStatement extends Statement {
  public Expression expr;

  public ASTReturnStatement(int id) {
    super(id);
  }

  public ASTReturnStatement(MyGrammar p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(MyGrammarVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }

  @Override
  public void dump(String prefix) {
    String pref = prefix + "Return: ";
    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
        SimpleNode n = (SimpleNode)children[i];
        if (n != null) {
          n.dump(pref);
        }
      }
    }
  }
}
/* JavaCC - OriginalChecksum=c4e044e89c3c71bb03995a44b8e87ebe (do not edit this line) */
