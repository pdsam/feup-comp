/* Generated By:JJTree: Do not edit this line. ASTStatementList.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTStatementList extends SimpleNode {
  public ASTStatementList(int id) {
    super(id);
  }

  public ASTStatementList(MyGrammar p, int id) {
    super(p, id);
  }


  @Override
  public Object jjtAccept(MyGrammarVisitor visitor, Object data) {
    visitor.visit(this, data);

    childrenAccept(visitor, data);

    return data;
  }
}
/* JavaCC - OriginalChecksum=be726a777693971c70aa66b3285a4aef (do not edit this line) */
