/* Generated By:JJTree: Do not edit this line. ASTIntegerLiteral.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTIntegerLiteral extends Expression {
  public int val;

  public ASTIntegerLiteral(int id) {
    super(id);
    this.type = "int";
  }

  public ASTIntegerLiteral(MyGrammar p, int id) {
    super(p, id);
    this.type = "int";
  }


  /** Accept the visitor. **/
  public Object jjtAccept(MyGrammarVisitor visitor, Object data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=6ed65c8664b8bc938ae2bde3fcd785ba (do not edit this line) */
