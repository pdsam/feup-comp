package symbolTable;

import javacc.ASTMethod;
import javacc.SimpleNode;
import java.util.HashMap;


public class StMethod extends SymbolTable {
    private HashMap<String, VarDescriptor> parameters = new HashMap<String, VarDescriptor>();
    private HashMap<String, VarDescriptor> local_variables = new HashMap<String, VarDescriptor>();

    public StMethod(SimpleNode node, SymbolTable parent){
        super(node, parent);
    }

    public StMethod(SimpleNode node) {
        super(node);
    }

    @Override
    public Descriptor method_lookup(String id) {
        return this.parameters.get(id);
    }

    public void put_parameters(String id, VarDescriptor descriptor){
        this.parameters.put(id, descriptor);
    }

    @Override
    public Descriptor variables_lookup(String id) {
         return this.local_variables.get(id);
    }

    public void put(String id, VarDescriptor descriptor){
        this.local_variables.put(id, descriptor);
    }
    
}