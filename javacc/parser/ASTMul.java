/* Generated By:JJTree: Do not edit this line. ASTMul.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTMul extends BinOpExpression {
  public ASTMul(int id) {
    super(id);
  }

  public ASTMul(MyGrammar p, int id) {
    super(p, id);
  }

  @Override
  public Object jjtAccept(MyGrammarVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=06343e5bb98c3dccb24a6cb9c40c953b (do not edit this line) */
