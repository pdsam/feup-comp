package symbolTable;

import javacc.SimpleNode;

abstract class SymbolTable {
    private SymbolTable parent;
    private final SimpleNode node;

    public SymbolTable(SimpleNode node) {
        this.node = node;
        this.parent = null;
    }

    public SymbolTable(SimpleNode node, SymbolTable parent) {
        this.node = node;
        this.parent = parent;
    }

    public Descriptor lookup(String id) {
        Descriptor descriptor = variables_lookup(id);
        if( descriptor != null)
            return descriptor;
        return method_lookup(id);
    }

    public abstract Descriptor method_lookup(String id);

    public abstract void put_method(String id, MethodDescriptor descriptor);

    public abstract Descriptor variables_lookup(String id);

    public abstract void put_variables(String id, VarDescriptor descriptor);

}