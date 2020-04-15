/* Generated By:JJTree: Do not edit this line. SimpleNode.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class SimpleNode implements Node {

  protected Node parent;
  protected Node[] children;
  protected int id;
  protected Object value;
  protected MyGrammar parser;

  public SimpleNode(int i) {
    id = i;
  }

  public SimpleNode(MyGrammar p, int i) {
    this(i);
    parser = p;
  }

  public void jjtOpen() {
  }

  public void jjtClose() {
  }

  public void jjtSetParent(Node n) { parent = n; }
  public Node jjtGetParent() { return parent; }

  public void jjtAddChild(Node n, int i) {
    if (children == null) {
      children = new Node[i + 1];
    } else if (i >= children.length) {
      Node c[] = new Node[i + 1];
      System.arraycopy(children, 0, c, 0, children.length);
      children = c;
    }
    children[i] = n;
  }

  public Node jjtGetChild(int i) {
    return children[i];
  }

  public int jjtGetNumChildren() {
    return (children == null) ? 0 : children.length;
  }

  public void jjtSetValue(Object value) { this.value = value; }
  public Object jjtGetValue() { return value; }

  /* You can override these two methods in subclasses of SimpleNode to
     customize the way the node appears when the tree is dumped.  If
     your output uses more than one line you should override
     toString(String), otherwise overriding toString() is probably all
     you need to do. */

  public String toString() {
    return MyGrammarTreeConstants.jjtNodeName[id];
  }
  public String toString(String prefix) { return prefix + toString(); }

  /* Override this method if you want to customize how the node dumps
     out its children. */

  public void dump(String prefix) {
    System.out.println(toString(prefix));
    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
        SimpleNode n = (SimpleNode)children[i];
        if (n != null) {
          n.dump(prefix + " ");
        }
      }
    }
  }

  public int getId() {
    return id;
  }
/*
  public boolean add_to_symbol_table(Node node)
  {
  
    Node parent = node.jjtGetParent();

    if(node instanceof ASTVar ) {

      String key = ((ASTVar) node).get_identifier();
        
      if(parent instanceof ASTMethod) {
        if( (ASTMethod) parent).attributes_symbol_table.get(key) != null )
          return false;

        (ASTMethod) parent).vars_symbol_table.put(key, value );

        return true;

      } else (node instanceof ASTClass ) {

          if( (ASTClass ) parent).vars_symbol_table.get(key) != null )
            return false;

          (ASTClass) parent).vars_symbol_table.put(key, value );

          return true;
        }

        throw new InvalidDeclaration();
        
      }

      if(node instanceof ASTParameters ){
        if( (ASTMethod) node).parameters_symbol_table.get(key) != null )
          return false;

        (ASTMethod) node).parameters_symbol_table.put(key, value );

        return true;
      }

/**  In progress
      if(node instanceof ASTParameters ){
        if( (ASTClass) node).attributes_symbol_table.get(key) != null )
          return false;

        (ASTClass) node)attributes_symbol_table.put(key, value );

        return true;
      }

      if(node instanceof ASTParameters ){
        if( (ASTClass) node).methods_symbol_table.get(key) != null )
          return false;

        (ASTClass) node)methods_symbol_table.put(key, value );

        return true;
      }

*/
/*/

      if(node instanceof ASTImport ) {

        if( ((ASTImport) node).symbol_table.get(id) != null)
          return false;

        ((ASTImport) node).symbol_table.put(id, type);

        return true;

      }

      if(node instanceof ASTMethod) {

        if( ((ASTMethod) node).symbol_table.get(id) != null)
          return false;

        ((ASTMethod) node).symbol_table.put(id, type);

        return true;
      } 

      if(node instanceof ASTMainMethod) {
        
        if( ((ASTMainMethod) node).symbol_table.get(id) != null)
          return false;

        ((ASTMainMethod) node).symbol_table.put(id, type);

        return true;
      }

      if(node instanceof ASTClass) {

        if( ((ASTClass) node).symbol_table.get(id) != null)
          return false;

        ((ASTClass) node).symbol_table.put(id, type);
        return true;
      }
    return false;
  } */
}

/* JavaCC - OriginalChecksum=93ad6855db0df46c9c1b9ebbb09966cc (do not edit this line) */
