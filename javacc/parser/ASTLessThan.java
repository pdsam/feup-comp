/* Generated By:JJTree: Do not edit this line. ASTLessThan.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTLessThan extends BinOpExpression {
  public ASTLessThan(int id) {
    super(id);
    this.type = "boolean";
  }

  public ASTLessThan(MyGrammar p, int id) {
    super(p, id);
    this.type = "boolean";
  }

  @Override
  public Object jjtAccept(MyGrammarVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }

}
/* JavaCC - OriginalChecksum=d40724c42ee8060b3dcae9a7303625b8 (do not edit this line) */
