package symbolTable;

import java.util.HashMap;

public class StMethod extends SymbolTable {
    private HashMap<String, VarDescriptor> parameters = new HashMap<String, VarDescriptor>();
    private HashMap<String, VarDescriptor> local = new HashMap<String, VarDescriptor>();

    public StMethod(SimpleNode node, SymbolTable parent){
        super(node, parent);
    }
    
    public VarDescriptor lookup(String id) {
        return this.local.get(id);
    }
    
    public void put(String id, VarDescriptor descriptor){
        this.local.put(id, descriptor);
    }

    public VarDescriptor lookup_parameters(String id) {
        return this.parameters.get(id);
    }
    
    public void put_parameters(String id, VarDescriptor descriptor){
        this.parameters.put(id, descriptor);
    }


    
}