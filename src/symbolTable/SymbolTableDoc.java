package symbolTable;

import java.util.ArrayList;
import java.util.HashMap;

import symbolTable.descriptor.Descriptor;
import symbolTable.descriptor.MethodDescriptor;
import symbolTable.descriptor.VarDescriptor;

public class  SymbolTableDoc implements SymbolTable {
    private SymbolTable parent;
    private HashMap<String, ArrayList<MethodDescriptor>> imports = new HashMap<String, ArrayList<MethodDescriptor>>();


    @Override
    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }

    public MethodDescriptor method_lookup(String id, ArrayList<String> parameters) throws UnknownDeclaration { // throws UnknownDeclaration {
        ArrayList<MethodDescriptor> overloads = imports.get(id);

        if(overloads == null){
            throw new UnknownDeclaration("Id doesn't correspond to a method");
        }

        for(MethodDescriptor descriptor : overloads){
            if(descriptor.getParameters().equals(parameters))
                return descriptor;
        }

        return null;
    }

    public VarDescriptor variable_lookup(String id) throws InvalidDescriptor { // throws UnknownDeclaration {
        throw new InvalidDescriptor("Imports only contain methods descriptors");
    }

    public void put(Descriptor descriptor) throws AlreadyDeclared, UnknownDeclaration {
        if(descriptor instanceof MethodDescriptor) {
            MethodDescriptor mtd = (MethodDescriptor) descriptor;
            String id = descriptor.getName();
            ArrayList<MethodDescriptor> overloads = imports.get(id);

            if(overloads != null){
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