/* Generated By:JJTree: Do not edit this line. ASTClass.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

import symbolTable.SymbolTableClass;

public
class ASTClass extends SimpleNode {
  public String identifier;
  public String parent = null;
  protected SymbolTableClass symbol_table = new SymbolTableClass();
  
  public ASTClass(int id) {
    super(id);
  }

  public ASTClass(MyGrammar p, int id) {
    super(p, id);
  }

  @Override
  public void dump(String prefix) {
    System.out.printf("%s : %s\n", toString(prefix), identifier);

    if (parent != null) {
      System.out.printf("%s Extends: %s\n", prefix, parent);
    }
    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
        SimpleNode n = (SimpleNode)children[i];
        if (n != null) {
          n.dump(prefix + "  ");
        }
      }
    }
  }

  public SymbolTableClass getStClass(){
    return this.symbol_table;
  }

  @Override
  public Object jjtAccept(MyGrammarVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=e5d622066819fa5dbc374d37ea6811bc (do not edit this line) */
