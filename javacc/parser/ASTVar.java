/* Generated By:JJTree: Do not edit this line. ASTVar.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTVar extends SimpleNode {
  public String type;
  public String identifier;

  public ASTVar(int id) {
    super(id);
  }

  public ASTVar(MyGrammar p, int id) {
    super(p, id);
  }

  @Override
  public void dump(String prefix) {
    System.out.printf("%s %s: %s\n", prefix, identifier, type);
  }

  @Override
  public Object jjtAccept(MyGrammarVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=f4007b07e33e21399e121c30aa957674 (do not edit this line) */
