/* Generated By:JJTree: Do not edit this line. ASTMethod.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

import symbolTable.SymbolTableMethod;
import symbolTable.descriptor.MethodDescriptor;

public
class ASTMethod extends SimpleNode {
  public boolean isStatic = false;
  public String identifier;
  public String type;
  public MethodDescriptor descriptor;
  protected SymbolTableMethod symbol_table = new SymbolTableMethod();

  public ASTMethod(int id) {
    super(id);
  }

  public ASTMethod(MyGrammar p, int id) {
    super(p, id);
  }

  @Override
  public void dump(String prefix) {
    System.out.printf("%sMethod: %s -> %s\n", prefix, identifier, type);

    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
        SimpleNode n = (SimpleNode)children[i];
        if (n != null) {
          n.dump(prefix + "  ");
        }
      }
    }
  }

  public SymbolTableMethod getStMethod() { return symbol_table; }

  @Override
  public Object jjtAccept(MyGrammarVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=e520198b9ad9d425775d48b35053f06f (do not edit this line) */
