/* Generated By:JJTree: Do not edit this line. ASTAnd.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTAnd extends BinOpExpression {
  public ASTAnd(int id) {
    super(id);
  }

  public ASTAnd(MyGrammar p, int id) {
    super(p, id);
  }

  @Override
  public Object jjtAccept(MyGrammarVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=6d17c8b9226d3f3e295ca5d1059b16bf (do not edit this line) */
