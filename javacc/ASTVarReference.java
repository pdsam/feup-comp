/* Generated By:JJTree: Do not edit this line. ASTVarReference.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTVarReference extends Expression {
  protected String identifier;

  public ASTVarReference(int id) {
    super(id);
  }

  public ASTVarReference(MyGrammar p, int id) {
    super(p, id);
  }

  @Override
  public void dump(String prefix) {
    System.out.printf("%s [ %s ]\n", toString(prefix), identifier);
  }
}
/* JavaCC - OriginalChecksum=1b26da79c32f646f480044bf94dafc7c (do not edit this line) */
