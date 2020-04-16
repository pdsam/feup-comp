package symbolTable;

import javacc.SimpleNode;
import java.util.HashMap;

public class StClass extends SymbolTable {
    private HashMap<String, VarDescriptor> fields_table = new HashMap<String, VarDescriptor>();
    private HashMap<String, MethodDescriptor> methods_table = new HashMap<String, MethodDescriptor>();

    public StClass(SimpleNode node, SymbolTable parent){
        super(node, parent);
    }

    public StClass(SimpleNode node) {
        super(node);
    }

    @Override
    public Descriptor method_lookup(String id) {
        return this.methods_table.get(id);
    }

    @Override
    public void put_method(String id, MethodDescriptor descriptor){
        this.methods_table.put(id, descriptor);
    }

    @Override
    public Descriptor variables_lookup(String id) {
        return this.fields_table.get(id);
    }

    @Override
    public void put_variables(String id, VarDescriptor descriptor){
        this.fields_table.put(id, descriptor);
    }


    
}