/* Generated By:JJTree: Do not edit this line. ASTImportList.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTImportList extends SimpleNode {
  public ASTImportList(int id) {
    super(id);
  }

  public ASTImportList(MyGrammar p, int id) {
    super(p, id);
  }


  @Override
  public Object jjtAccept(MyGrammarVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=33f733f9c9d0f3c6a7b0669236e00916 (do not edit this line) */
