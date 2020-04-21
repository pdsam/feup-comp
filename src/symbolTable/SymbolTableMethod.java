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

        throw new UnknownDeclaration("Method \'" + id + "\' not defined.");
    }

    @Override
    public VarDescriptor variable_lookup(String id) throws UnknownDeclaration {
        VarDescriptor varDescriptor = variables.get(id);

        if(varDescriptor != null)
            return varDescriptor;

        if(parent != null) return this.parent.variable_lookup(id);

        throw new UnknownDeclaration("Variable \'" + id + "\' not defined.");
    }

    @Override
    public void put(Descriptor descriptor) throws AlreadyDeclared, UnknownDeclaration {
        if(descriptor instanceof VarDescriptor) {
            String id = descriptor.getName();
            if(variables.put(id, (VarDescriptor) descriptor) != null){
                throw new AlreadyDeclared("Variable \'" + id + "\' already defined");
            }
            return;
        }

        throw new UnknownDeclaration("Methods cannot be defined inside other methods.");
    }
}