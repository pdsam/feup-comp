package symbolTable;

import java.util.HashMap;

public class StClass extends SymbolTable {
    private HashMap<String, VarDescriptor> fields_table = new HashMap<String, VarDescriptor>();
    private HashMap<String, MethodDescriptor> methods_table = new HashMap<String, MethodDescriptor>();

    public StClass(SimpleNode node, SymbolTable parent){
        super(node, parent);
    }
    
    public VarDescriptor lookup_field(String id) {
        return this.fields_table.get(id);
    }
    
    public void put_field(String id, VarDescriptor descriptor){
        this.fields_table.put(id, descriptor);
    }

    public MethodDescriptor lookup_method(String id) {
        return this.methods_table.get(id);
    }
    
    public void put_method(String id, MethodDescriptor descriptor){
        this.methods_table.put(id, descriptor);
    }
    
}