/* Generated By:JJTree: Do not edit this line. ASTParameter.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTParameter extends SimpleNode {
  public String type;
  public String identifier;

  public ASTParameter(int id) {
    super(id);
  }

  public ASTParameter(MyGrammar p, int id) {
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
/* JavaCC - OriginalChecksum=c430fbd62544d7d522d0a25eec5becdf (do not edit this line) */
