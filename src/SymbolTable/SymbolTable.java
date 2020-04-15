
import java.util.HashMap;
import parser.SimpleNode;

abstract class SymbolTable {
    
    private final SymbolTable parent;
    private final SimpleNode node;

    public SymbolTable(SimpleNode node, SymbolTable parent) {
        this.node = node;
        this.parent = parent;
    }

}