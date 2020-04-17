/* Generated By:JJTree: Do not edit this line. ASTArrayAccess.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTArrayAccess extends Expression {
  protected Expression arrayRef;
  protected Expression index;

  public ASTArrayAccess(int id) {
    super(id);
  }

  public ASTArrayAccess(MyGrammar p, int id) {
    super(p, id);
  }

  @Override
  public void dump(String prefix) {
    System.out.println(toString(prefix));
    System.out.printf("%s Reference:\n", prefix);
    arrayRef.dump(prefix + "  ");
    System.out.printf("%s Index:\n", prefix);
    index.dump(prefix + "  ");
  }
}
/* JavaCC - OriginalChecksum=e3590b2cb8e57392b3ef9f40abdb8647 (do not edit this line) */