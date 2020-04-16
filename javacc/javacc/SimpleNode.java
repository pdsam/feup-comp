package javacc;

import jdk.tools.jaotc.binformat.SymbolTable;
import symbolTable.Descriptor;
import symbolTable.MethodDescriptor;
import symbolTable.StDoc;
import symbolTable.VarDescriptor;

/* Generated By:JJTree: Do not edit this line. SimpleNode.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class SimpleNode implements Node {

  protected SimpleNode parent;
  protected Node[] children;
  protected int id;
  protected Object value;
  protected MyGrammar parser;
  protected StDoc imports_symbol_table;

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

  public boolean add_to_symbol_table(String id, Descriptor descriptor) {
    SimpleNode node = parent;

    while (node != null) {

      if (node instanceof ASTClass) {

        if(descriptor instanceof MethodDescriptor ) {
          if(((ASTClass) node).symbol_table.method_lookup(id) != null)
            return false;

          ((ASTClass) node).symbol_table.put_method(id, (MethodDescriptor) descriptor);

        } else {
          if( ((ASTClass) node).symbol_table.variables_lookup(id) != null)
            return false;

          ((ASTClass) node).symbol_table.put_variables(id, (VarDescriptor) descriptor);
        }

        return true;
      }


      if (node instanceof ASTMethod) {

        if(descriptor instanceof  VarDescriptor) {
          if( ((ASTMethod) node).symbol_table.variables_lookup(id) != null)
            return false;

          ((ASTMethod) node).symbol_table.put_variables(id, (VarDescriptor) descriptor);
        }
     /*   if(descriptor instanceof MethodDescriptor ) {
          if(((ASTMethod) node).symbol_table.method_lookup(id) != null)
            return false;

          ((ASTMethod) node).symbol_table.p
                  put_method(id, (MethodDescriptor) descriptor);

        } else {

        } */

        return true;
      }

      node = node.parent;
    }

    return false;
  }

}

/* JavaCC - OriginalChecksum=93ad6855db0df46c9c1b9ebbb09966cc (do not edit this line) */
