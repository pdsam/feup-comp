package symbolTable;

import java.util.ArrayList;

abstract class SymbolTable {

    public ArrayList<Descriptor> lookup(String id) {
        Descriptor varDescriptor = variables_lookup(id);

        if( varDescriptor != null){
            ArrayList<Descriptor> descriptor = new ArrayList<>();
            descriptor.add(varDescriptor);
            return descriptor;
        }

        return method_lookup(id);
    }

    public abstract ArrayList<Descriptor> method_lookup(String id);

    public abstract void put_method(String id, MethodDescriptor descriptor);

    public abstract Descriptor variables_lookup(String id);

    public abstract void put_variables(String id, VarDescriptor descriptor);

}