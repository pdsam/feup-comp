/* Generated By:JJTree: Do not edit this line. ASTImport.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

import java.util.List;
import java.util.ArrayList;

public class ASTImport extends SimpleNode {
  public boolean isStatic = false;
  public String className;
  public String methodName = null;
  public String returnType = "void";

  public List<String> parameters = new ArrayList<String>();

  public ASTImport(int id) {
    super(id);
  }

  public ASTImport(MyGrammar p, int id) {
    super(p, id);
  }


  @Override
  public Object jjtAccept(MyGrammarVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=adfce7cfaebda5f64600e3ae32ea0dbf (do not edit this line) */
