/* Generated By:JJTree: Do not edit this line. ASTSum.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTSum extends BinOpExpression {
  public ASTSum(int id) {
    super(id);
    this.type = "int";
  }

  public ASTSum(MyGrammar p, int id) {
    super(p, id);
    this.type = "int";
  }

  @Override
  public Object jjtAccept(MyGrammarVisitor visitor, Object data) {
    visitor.visit(this, data);

    left.jjtAccept(visitor, data);
    right.jjtAccept(visitor, data);

    return data;
  }
}
/* JavaCC - OriginalChecksum=80e0c57c002571cb7dba1674ad4bb3f4 (do not edit this line) */
