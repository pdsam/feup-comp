/* Generated By:JJTree: Do not edit this line. ASTLiteral.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTLiteral extends Expression {
  protected String type;
  protected Object val;

  public ASTLiteral(int id) {
    super(id);
  }

  public ASTLiteral(MyGrammar p, int id) {
    super(p, id);
  }

  @Override
  public void dump(String prefix) {
    System.out.println(toString(prefix));
    System.out.printf("%s Type: %s\n", prefix, type);
    System.out.printf("%s Value: %s\n", prefix, val);
  }

  @Override
  public Object jjtAccept(MyGrammarVisitor visitor, Object data) {
    visitor.visit(this, data);

    return data;
  }

}
/* JavaCC - OriginalChecksum=0d41266a3c855e938b1bcdfe22a9ce61 (do not edit this line) */
