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

    @Override
    public MethodDescriptor method_lookup(String id, ArrayList<String> parameters) throws UnknownDeclaration {
        if(parent != null) return this.parent.method_lookup(id, parameters);

        throw new UnknownDeclaration("Method not found because parent was null");
    }

    @Override
    public VarDescriptor variable_lookup(String id) throws UnknownDeclaration, InvalidDescriptor {
        VarDescriptor varDescriptor = variables.get(id);

        if(varDescriptor != null)
            return varDescriptor;

        if(parent != null) return this.parent.variable_lookup(id);

        throw new UnknownDeclaration("Id passed doesn't match any variable");
    }

    public void put(Descriptor descriptor) throws AlreadyDeclared, InvalidDescriptor {
        if(descriptor instanceof VarDescriptor) {
            if(variables.put(descriptor.getName(), (VarDescriptor) descriptor) != null){
                throw new AlreadyDeclared("Variable already");
            }
            return;
        }

        throw new InvalidDescriptor("Methods can only have var descriptions");
    }
}