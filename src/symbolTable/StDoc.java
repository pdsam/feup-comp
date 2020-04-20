package symbolTable;

import java.util.ArrayList;
import java.util.HashMap;

public class StDoc extends SymbolTable {

    //List with MethodDescriptors
    private HashMap<String, Descriptor> imports = new HashMap<String, Descriptor>();

    @Override
    public ArrayList<Descriptor> method_lookup(String id) {
        ArrayList<Descriptor> methods = new ArrayList<>();
        methods.add(this.imports.get(id));
        return methods;
    }

    @Override
    public void put_method(String id, MethodDescriptor descriptor) {
        this.imports.put(id, descriptor);
    }

    @Override
    public Descriptor variables_lookup(String id) {
        return null;
    }

    @Override
    public void put_variables(String id, VarDescriptor descriptor) {    }

}