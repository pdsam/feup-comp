package symbolTable;

import java.util.ArrayList;
import java.util.HashMap;

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
    public MethodDescriptor method_lookup(String id, ArrayList<String> parameters) throws UnknownDeclaration {
        ArrayList<MethodDescriptor> overloads = imports.get(id);

        if(overloads == null){
            throw new UnknownDeclaration("Id doesn't correspond to a method");
        }

        for(MethodDescriptor descriptor : overloads){
            if(descriptor.getParameters().equals(parameters))
                return descriptor;
        }

        throw new UnknownDeclaration("Any of the methods with that id has that list of parameters");
    }

    @Override
    public VarDescriptor variable_lookup(String id) throws InvalidDescriptor {
        throw new InvalidDescriptor("Imports only contain methods descriptors");
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
                        throw new AlreadyDeclared();
                }

                overloads.add(mtd);
            }else{
                ArrayList<MethodDescriptor> entry = new ArrayList<>();
                entry.add(mtd);
                imports.put(id, entry);
            }

            return;
        }

        throw new UnknownDeclaration("Invalid descriptor type");
    }
}