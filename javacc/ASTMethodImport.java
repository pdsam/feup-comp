/* Generated By:JJTree: Do not edit this line. ASTMethodImport.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
import java.util.ArrayList;

public class ASTMethodImport extends ASTImport {
  public String returnType;
  public String importSequence;
  public int numParams;
  public ArrayList<String> parameters = new ArrayList<>();

  public ASTMethodImport(int id) {
    super(id);
  }

  public ASTMethodImport(MyGrammar p, int id) {
    super(p, id);
  }

}
/* JavaCC - OriginalChecksum=721b526f9ba26e0137474d2c4ef1d531 (do not edit this line) */
