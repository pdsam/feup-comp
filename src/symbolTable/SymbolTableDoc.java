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
    private HashMap<String, VarDescriptor> classes = new HashMap<>();

    @Override
    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }

    @Override
    public MethodDescriptor method_lookup(String id, List<String> parameters) throws UnknownDeclaration {
        ArrayList<MethodDescriptor> overloads = imports.get(id);

        if(overloads != null) {
            for (MethodDescriptor descriptor : overloads) {
                if (descriptor.getParameters().equals(parameters))
                    return descriptor;
            }
        }

        if(parent != null) return this.parent.method_lookup(id, parameters);

        throw new UnknownDeclaration("Method \'" + id + "\' not defined.");
    }

    @Override
    public VarDescriptor variable_lookup(String id) throws UnknownDeclaration {
        VarDescriptor varDescriptor = classes.get(id);

        if(varDescriptor != null)
            return varDescriptor;

        if(parent != null) return this.parent.variable_lookup(id);

        throw new UnknownDeclaration("Variable \'" + id + "\' not defined.");
    }

    @Override
    public void put(Descriptor descriptor) throws AlreadyDeclared, UnknownDeclaration {
        String id = descriptor.getName();
        if(descriptor instanceof MethodDescriptor) {
            MethodDescriptor mtd = (MethodDescriptor) descriptor;
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
        } else if(descriptor instanceof VarDescriptor) {
            if(classes.get(id) == null)
                classes.put(id, (VarDescriptor) descriptor);

            return;
        }

        throw new UnknownDeclaration("Variables cannot be defined outside a class or method.");
    }
}