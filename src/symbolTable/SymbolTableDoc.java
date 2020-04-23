package symbolTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import symbolTable.descriptor.Descriptor;
import symbolTable.descriptor.MethodDescriptor;
import symbolTable.descriptor.VarDescriptor;

public class  SymbolTableDoc implements SymbolTable {
    private SymbolTable parent = null;
    private HashMap<String, ArrayList<MethodDescriptor>> imports = new HashMap<>();

    @Override
    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }

    @Override
    public MethodDescriptor method_lookup(String id, List<String> parameters) throws UnknownDeclaration {
        ArrayList<MethodDescriptor> overloads = imports.get(id);

        if(overloads == null) {
            for (MethodDescriptor descriptor : overloads) {
                if (descriptor.getParameters().equals(parameters))
                    return descriptor;
            }
        }

        throw new UnknownDeclaration("Method \'" + id + "\' not defined.");
    }

    @Override
    public VarDescriptor variable_lookup(String id) throws UnknownDeclaration {
        throw new UnknownDeclaration("Variable \'" + id + "\' not defined.");
    }

    @Override
    public void put(Descriptor descriptor) throws AlreadyDeclared, UnknownDeclaration {
        if(descriptor instanceof MethodDescriptor) {
            MethodDescriptor mtd = (MethodDescriptor) descriptor;
            String id = descriptor.getName();
            ArrayList<MethodDescriptor> overloads = imports.get(id);

            if(overloads != null){ //a method with that id exists already
                for(MethodDescriptor methodDescriptor : overloads){
                    if(methodDescriptor.getParameters().equals(((MethodDescriptor) descriptor).getParameters() ))
                        throw new AlreadyDeclared("Method \'" + id + "\' already defined.\nConflict: " + methodDescriptor);
                }

                overloads.add(mtd);
            }else{
                ArrayList<MethodDescriptor> entry = new ArrayList<>();
                entry.add(mtd);
                imports.put(id, entry);
            }

            return;
        }

        throw new UnknownDeclaration("Variables cannot be defined outside a class or method.");
    }
}