/* Generated By:JJTree: Do not edit this line. ASTArrayLength.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTArrayLength extends Expression {
  public Expression arrayRef;

  public ASTArrayLength(int id) {
    super(id);
    this.type = "int";
  }

  public ASTArrayLength(MyGrammar p, int id) {
    super(p, id);
    this.type = "int";
  }

  @Override
  public void dump(String prefix) {
    System.out.println(toString(prefix));
    System.out.printf("%s Array Reference:\n", prefix);
    arrayRef.dump(prefix + "  ");
  }

  @Override
  public Object jjtAccept(MyGrammarVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=1f8775333fd9ffeb990d54271f0f6278 (do not edit this line) */
