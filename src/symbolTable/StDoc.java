package symbolTable;

import javacc.SimpleNode;
import java.util.HashMap;

public class StDoc extends SymbolTable {
    private HashMap<String, MethodDescriptor> imports = new HashMap<String, MethodDescriptor>();

    public StDoc(SimpleNode node, SymbolTable parent){
        super(node, parent);
    }

    public MethodDescriptor lookup(String id) {
        return this.imports.get(id);
    }
    
    public void put(String id, MethodDescriptor descriptor){
        this.imports.put(id, descriptor);
    }

    @Override
    public Descriptor method_lookup(String id) {
        return this.imports.get(id);
    }

    @Override
    public void put_method(String id, MethodDescriptor descriptor) {
    {
        this.imports.put(id, descriptor);
    }
    
}