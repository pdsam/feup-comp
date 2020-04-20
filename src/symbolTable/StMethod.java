package symbolTable;

import java.util.ArrayList;
import java.util.HashMap;


public class StMethod extends SymbolTable {

   // private HashMap<String, VarDescriptor> parameters = new HashMap<String, VarDescriptor>();
    //parameters are variable in a certain way
    private HashMap<String, VarDescriptor> variables = new HashMap<String, VarDescriptor>();

    @Override
    public ArrayList<Descriptor> method_lookup(String id) {
        return null;
    }

    @Override
    public void put_method(String id, MethodDescriptor descriptor) {    }

    public Descriptor variables_lookup(String id) {
         return this.variables.get(id);
    }

    public void put_variables(String id, VarDescriptor descriptor) {
        this.variables.put(id, descriptor);
    }
    
}