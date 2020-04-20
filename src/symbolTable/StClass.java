package symbolTable;

import java.util.ArrayList;
import java.util.HashMap;

public class StClass extends SymbolTable {
    private HashMap<String, VarDescriptor> fields_table = new HashMap<String, VarDescriptor>();
    //List with MethodDescriptors
    private HashMap<String, ArrayList<Descriptor>> methods_table = new HashMap<String, ArrayList<Descriptor>>();

    public ArrayList<Descriptor> method_lookup(String id) {
        return this.methods_table.get(id);
    }

    public void put_method(String id, MethodDescriptor descriptor){
        ArrayList<Descriptor> methods = this.method_lookup(id);
        if(methods == null) {
            ArrayList<Descriptor> new_list_descriptors = new ArrayList<>();
            new_list_descriptors.add(descriptor);
            this.methods_table.put(id, new_list_descriptors);
        } else {
            methods.add(descriptor);
            this.methods_table.replace(id, methods);
        }

    }

    public Descriptor variables_lookup(String id) {
        return this.fields_table.get(id);
    }

    public void put_variables(String id, VarDescriptor descriptor){
        this.fields_table.put(id, descriptor);
    }
    
}