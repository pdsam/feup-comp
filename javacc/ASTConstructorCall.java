/* Generated By:JJTree: Do not edit this line. ASTConstructorCall.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTConstructorCall extends Expression {
  protected String identifier;

  public ASTConstructorCall(int id) {
    super(id);
  }

  public ASTConstructorCall(MyGrammar p, int id) {
    super(p, id);
  }

  @Override
  public void dump(String prefix) {
    System.out.printf("%s : new %s()\n", toString(prefix), identifier);
  }

  @Override
  public Object jjtAccept(MyGrammarVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=b08e45add43ed768d356944cbfdc00cb (do not edit this line) */
