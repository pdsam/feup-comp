/* Generated By:JJTree: Do not edit this line. ASTDocument.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTDocument extends SimpleNode {
  public ASTDocument(int id) {
    super(id);
  }

  public ASTDocument(MyGrammar p, int id) {
    super(p, id);
  }


  @Override
  public Object jjtAccept(MyGrammarVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=371d209ba49e7dd487b59a18ad511b9e (do not edit this line) */
