/* Generated By:JJTree: Do not edit this line. ASTStaticMethodImport.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
import java.util.ArrayList;

public class ASTStaticMethodImport extends SimpleNode {
  public String returnType;
  public String importSequence;
  public int numParams;
  public ArrayList<String> parameters = new ArrayList<>();

  public ASTStaticMethodImport(int id) {
    super(id);
    add_to_symbol_table(id, returnType + " " + importSequence);
  }

  public ASTStaticMethodImport(MyGrammar p, int id) {
    super(p, id);
    add_to_symbol_table(id, returnType + " " + importSequence);
  }

}
/* JavaCC - OriginalChecksum=1c299e391173300adcf0368514144414 (do not edit this line) */
