/* Generated By:JJTree: Do not edit this line. ASTArgument.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTArgument extends SimpleNode {
  protected String type;
  protected String identifier;

  public ASTArgument(int id) {
    super(id);
  }

  public ASTArgument(MyGrammar p, int id) {
    super(p, id);
  }

  @Override
  public void dump(String prefix) {
    System.out.printf("%s %s: %s\n", prefix, identifier, type);
  }
}
/* JavaCC - OriginalChecksum=c430fbd62544d7d522d0a25eec5becdf (do not edit this line) */
