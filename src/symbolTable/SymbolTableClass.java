package symbolTable;

import java.util.ArrayList;
import java.util.HashMap;

import symbolTable.descriptor.Descriptor;
import symbolTable.descriptor.MethodDescriptor;
import symbolTable.descriptor.VarDescriptor;

public class SymbolTableClass implements SymbolTable {
    private SymbolTable parent = null;
    private HashMap<String, VarDescriptor> fields_table = new HashMap<>();
    private HashMap<String, ArrayList<MethodDescriptor>> methods_table = new HashMap<>();

    @Override
    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }

    public MethodDescriptor method_lookup(String id, ArrayList<String> parameters) throws UnknownDeclaration {
        ArrayList<MethodDescriptor> overloads = methods_table.get(id);

        if(overloads != null){
            for(MethodDescriptor descriptor : overloads){
                if(descriptor.getParameters().equals(parameters))
                    return descriptor;
            }
        }

        if(parent != null) return this.parent.method_lookup(id, parameters);

        throw new UnknownDeclaration("Any of the methods with that id has that list of parameters");
    }

    @Override
    public VarDescriptor variable_lookup(String id) throws UnknownDeclaration, InvalidDescriptor {
        VarDescriptor varDescriptor = fields_table.get(id);
        if(varDescriptor != null)
            return varDescriptor;

        if(parent != null) return this.parent.variable_lookup(id);

        throw new UnknownDeclaration("Id passed doesn't match any variable");
    }

    public void put(Descriptor descriptor) throws AlreadyDeclared, UnknownDeclaration {
        String id = descriptor.getName();

        if(descriptor instanceof MethodDescriptor) {
            MethodDescriptor mtd = (MethodDescriptor) descriptor;
            ArrayList<MethodDescriptor> overloads = methods_table.get(id);

            if(overloads != null){
                for(MethodDescriptor methodDescriptor : overloads){
                    if(methodDescriptor.getParameters().equals(((MethodDescriptor) descriptor).getParameters() ))
                        throw new AlreadyDeclared("Function with the same name and arguments already exits");
                }

                overloads.add(mtd);
            }else{
                ArrayList<MethodDescriptor> entry = new ArrayList<>();
                entry.add(mtd);
                methods_table.put(id, entry);
            }

            return;
        } else if(descriptor instanceof VarDescriptor) {
            if(fields_table.put(id, (VarDescriptor) descriptor) != null) {
                throw new AlreadyDeclared("Variable already declared");
            }

            return;
        }

        throw new UnknownDeclaration("Invalid descriptor type");
    }
}