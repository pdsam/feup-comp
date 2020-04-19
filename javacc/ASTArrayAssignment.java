/* Generated By:JJTree: Do not edit this line. ASTArrayAssignment.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public class ASTArrayAssignment extends Statement {
  protected Expression arrayRef;
  protected Expression value;

  public ASTArrayAssignment(int id) {
    super(id);
  }

  public ASTArrayAssignment(MyGrammar p, int id) {
    super(p, id);
  }


  @Override
  public void dump(String prefix) {
    System.out.println(toString(prefix));
    System.out.printf("%s Reference:\n", prefix);
    arrayRef.dump(prefix + "  ");
    System.out.printf("%s value:\n", prefix);
    value.dump(prefix + "  ");
  }

  @Override
  public Object jjtAccept(MyGrammarVisitor visitor, Object data) {
    visitor.visit(this, data);

    arrayRef.jjtAccept(visitor, data);
    value.jjtAccept(visitor, data);

    return data;
  }
}
/* JavaCC - OriginalChecksum=5abeb09cb988ad85b4f2f3f7e6845ab2 (do not edit this line) */
