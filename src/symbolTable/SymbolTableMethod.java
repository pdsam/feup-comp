package symbolTable;

import symbolTable.descriptor.Descriptor;
import symbolTable.descriptor.MethodDescriptor;
import symbolTable.descriptor.VarDescriptor;

import java.util.ArrayList;
import java.util.HashMap;


public class SymbolTableMethod implements SymbolTable {
    private SymbolTable parent;
    private HashMap<String, VarDescriptor> variables = new HashMap<>();
    

    @Override
    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }

    public MethodDescriptor method_lookup(String id, ArrayList<String> parameters){
        return null;
    }

    public VarDescriptor variable_lookup(String id) throws UnknownDeclaration {
        VarDescriptor varDescriptor = variables.get(id);
        if(varDescriptor == null)
            throw new UnknownDeclaration("Id passed doesn't match any variable");
        return varDescriptor;
    }

    public void put(Descriptor descriptor) throws AlreadyDeclared, InvalidDescriptor {
        if(descriptor instanceof VarDescriptor) {
            if(variables.put(descriptor.getName(), (VarDescriptor) descriptor) != null){
                throw new AlreadyDeclared("Variable already");
            }
        }

        throw new InvalidDescriptor("Methods can only have var descriptions");
    }
}