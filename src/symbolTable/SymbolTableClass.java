package symbolTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import symbolTable.descriptor.Descriptor;
import symbolTable.descriptor.MethodDescriptor;
import symbolTable.descriptor.VarDescriptor;

public class SymbolTableClass implements SymbolTable {
    private SymbolTable parent = null;
    private String className;
    private HashMap<String, VarDescriptor> fields_table = new HashMap<>();
    private HashMap<String, ArrayList<MethodDescriptor>> methods_table = new HashMap<>();

    @Override
    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }

    public MethodDescriptor method_lookup(String id, List<String> parameters, String className) throws UnknownDeclaration {
        ArrayList<MethodDescriptor> overloads = methods_table.get(id);

        if(overloads != null){
            for(MethodDescriptor descriptor : overloads){
                if(descriptor.getParameters().equals(parameters) && descriptor.getClassName().equals(className) )
                    return descriptor;
            }
        }

        if(parent != null) return this.parent.method_lookup(id, parameters, className);

        throw new UnknownDeclaration("Method \'" + id + "\' not defined.");
    }

    @Override
    public VarDescriptor variable_lookup(String id) throws UnknownDeclaration {
        VarDescriptor varDescriptor = fields_table.get(id);

        if(varDescriptor != null)
            return varDescriptor;

        if(parent != null) return this.parent.variable_lookup(id);

        throw new UnknownDeclaration("Variable \'" + id + "\' not defined.");
    }

    public void put(Descriptor descriptor) throws AlreadyDeclared {
        String id = descriptor.getName();

        if(descriptor instanceof MethodDescriptor) {
            MethodDescriptor mtd = (MethodDescriptor) descriptor;
            ArrayList<MethodDescriptor> overloads = methods_table.get(id);

            if(overloads != null){
                for(MethodDescriptor methodDescriptor : overloads){
                    if(methodDescriptor.getParameters().equals(((MethodDescriptor) descriptor).getParameters() ))
                        throw new AlreadyDeclared("Method \'" + id + "\' already defined.\nConflict: " + methodDescriptor);
                }

                overloads.add(mtd);
            }else{
                ArrayList<MethodDescriptor> entry = new ArrayList<>();
                entry.add(mtd);
                methods_table.put(id, entry);
            }
        } else if(descriptor instanceof VarDescriptor) {
            if(fields_table.get(id) == null) {
                ((VarDescriptor) descriptor).setField(true);
                ((VarDescriptor) descriptor).setClassName(className);

                fields_table.put(id, (VarDescriptor) descriptor);
            } else
                throw new AlreadyDeclared("Variable \'" + id + "\' already declared.");
        }
    }

    public void setClassName(String className) {
        this.className = className;
    }
}