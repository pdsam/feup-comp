/* Generated By:JJTree: Do not edit this line. ASTDiv.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTDiv extends BinOpExpression {
  public ASTDiv(int id) {
    super(id);
  }

  public ASTDiv(MyGrammar p, int id) {
    super(p, id);
  }

  @Override
  public Object jjtAccept(MyGrammarVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=b61b5577a877251ec1d392b9477044b5 (do not edit this line) */
