package symbolTable;

import java.util.ArrayList;
import java.util.HashMap;

import symbolTable.descriptor.Descriptor;
import symbolTable.descriptor.MethodDescriptor;

public class  SymbolTableDoc extends SymbolTable {
    private SymbolTable parent;
    private HashMap<String, ArrayList<MethodDescriptor>> imports = new HashMap<String, ArrayList<MethodDescriptor>>();

    void setupParent(SymbolTable parent) {
        this.parent = parent;
    }

    MethodDescriptor method_lookup(String id, ArrayList<String> parameters) { // throws UnknownDeclaration {
        ArrayList<MethodDescriptor> overloads = imports.get(id);

        if(overloads == null)
            return null;
        
        for(MethodDescriptor descriptor : overloads){
            if(MethodDescriptor.getParameters().equals(parameters))
                return descriptor;
        }

        return null;
    }

    VarDescriptor variable_lookup(String id) { // throws UnknownDeclaration {
        return null;
    }

    void put(Descriptor descriptor) { // throws AlreadyDeclared {
        if(descriptor instanceof MethodDescriptor) {
            MethodDescriptor mtd = (MethodDescriptor) descriptor;
            String id = descriptor.getName();
            ArrayList<MethodDescriptor> overloads = imports.get(id);

            if(overloads != null){
                for(MethodDescriptor descriptor : overloads){
                    if(MethodDescriptor.getParameters().equals(parameters))
                        return; // lancar excecao
                }

                overloads.add(mtd);
            }else{
                ArrayList<MethodDescriptor> entry = new ArrayList<>();
                entry.add(mtd);
                imports.put(id, entry);
            }

            return;
        }

        //lanca excecao
    }
}