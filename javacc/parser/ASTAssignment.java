/* Generated By:JJTree: Do not edit this line. ASTAssignment.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTAssignment extends Statement {
  public Expression varReference;
  public Expression value;

  public ASTAssignment(int id) {
    super(id);
  }

  public ASTAssignment(MyGrammar p, int id) {
    super(p, id);
  }

  @Override
  public void dump(String prefix) {
    System.out.println(toString(prefix));
    varReference.dump(prefix + "  ");
    value.dump(prefix + "  ");
  }

  @Override
  public Object jjtAccept(MyGrammarVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=62e1e838c675001b7573c6604b2957d0 (do not edit this line) */
