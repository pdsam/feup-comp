package symbolTable;

import java.util.ArrayList;
import java.util.HashMap;

import symbolTable.descriptor.Descriptor;
import symbolTable.descriptor.MethodDescriptor;
import symbolTable.descriptor.VarDescriptor;

public class SymbolTableClass implements SymbolTable {
    private SymbolTable parent = null;
    private HashMap<String, VarDescriptor> fields_table = new HashMap<String, VarDescriptor>();
    private HashMap<String, ArrayList<MethodDescriptor>> methods_table = new HashMap<>();

    @Override
    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }

    public MethodDescriptor method_lookup(String id, ArrayList<String> parameters){
        ArrayList<MethodDescriptor> overloads = methods_table.get(id);

        if(overloads == null)
            return null;

        for(MethodDescriptor descriptor : overloads){
            if(descriptor.getParameters().equals(parameters))
                return descriptor;
        }

        return null;
    }

    public VarDescriptor variable_lookup(String id) throws UnknownDeclaration {
        VarDescriptor varDescriptor = fields_table.get(id);
        if(varDescriptor == null)
            throw new UnknownDeclaration("Id passed doesn't match any variable");
        return varDescriptor;
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