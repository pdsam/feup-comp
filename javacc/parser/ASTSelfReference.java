/* Generated By:JJTree: Do not edit this line. ASTSelfReference.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTSelfReference extends Expression {
  public ASTSelfReference(int id) {
    super(id);
  }

  public ASTSelfReference(MyGrammar p, int id) {
    super(p, id);
  }

  @Override
  public void dump(String prefix) {
    System.out.println(toString(prefix));
  }

  @Override
  public Object jjtAccept(MyGrammarVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=1b12a8a5fdf2dfa9880d4385718a69db (do not edit this line) */
