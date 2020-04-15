package symbolTable;

abstract class SymbolTable {
    private final SymbolTable parent;
    private final SimpleNode node;

    public SymbolTable(SimpleNode node, SymbolTable parent) {
        this.node = node;
        this.parent = parent;
    }

//    public Descriptor parentLookup() {
//        if(parent == null)
//            return null;
//
//        return parent.lookup();
//    }
//
//    protected abstract Descriptor lookup();
}