/* Generated By:JJTree: Do not edit this line. ASTBranch.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTBranch extends Statement {
  public Expression condition;
  public Statement thenStatement;
  public Statement elseStatement;

  public ASTBranch(int id) {
    super(id);
  }

  public ASTBranch(MyGrammar p, int id) {
    super(p, id);
  }

  @Override
  public void dump(String prefix) {
    System.out.println(toString(prefix));
    System.out.printf("%s  Condition:\n", prefix);
    condition.dump(prefix + "  ");
    System.out.printf("%s  Then:\n", prefix);
    thenStatement.dump(prefix + "  ");
    System.out.printf("%s  Else:\n", prefix);
    elseStatement.dump(prefix + "  ");
  }

  @Override
  public Object jjtAccept(MyGrammarVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=82bf9e9154f4c386dcef3ed1b685eed3 (do not edit this line) */
