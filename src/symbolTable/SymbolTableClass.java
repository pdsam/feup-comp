package symbolTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import symbolTable.descriptor.Descriptor;
import symbolTable.descriptor.MethodDescriptor;
import symbolTable.descriptor.VarDescriptor;

public class SymbolTableClass implements SymbolTable {
    private SymbolTable parent = null;
    private String className;
    private String superClass;
    private HashMap<String, VarDescriptor> fields_table = new HashMap<>();
    private HashMap<String, ArrayList<MethodDescriptor>> methods_table = new HashMap<>();

    public void setClassName(String className) {
        this.className = className;
    }

    public void setSuperClass(String className) { this.superClass = className; }

    @Override
    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }

    @Override
    public boolean isValidType(String type) throws InvalidType {
        if(parent != null) return parent.isValidType(type);

        throw new InvalidType();
    }

    @Override
    public MethodDescriptor method_lookup(String id, List<String> parameters, String className) throws UnknownDeclaration {
        ArrayList<MethodDescriptor> overloads = methods_table.get(id);
        if(className.equals(this.className))
            className = "this"; //Every method from this class is registered with 'this'

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

    @Override
    public void put(Descriptor descriptor) throws AlreadyDeclared {
        String id = descriptor.getName();

        if(descriptor instanceof MethodDescriptor) {
            MethodDescriptor mtd = (MethodDescriptor) descriptor;
            ArrayList<MethodDescriptor> overloads = methods_table.get(id);

            if(overloads != null){
                for(MethodDescriptor methodDescriptor : overloads){
                    if(methodDescriptor.getParameters().equals(((MethodDescriptor) descriptor).getParameters()) &&
                            methodDescriptor.getClassName().equals( ((MethodDescriptor) descriptor).getClassName()))
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

    @Override
    public String toString() {
        String result = "SymbolTableClass{ className='" + className + "', superClass='" + superClass + '\'';

        result += ", fields=[ \n";
        for(Map.Entry<String, VarDescriptor> entry : fields_table.entrySet()) {
            result += entry.getKey() + ": " + entry.getValue() + "\n";
        }

        result += "], methods=[ \n";
        for(Map.Entry<String, ArrayList<MethodDescriptor>> entry : methods_table.entrySet()) {
            for(MethodDescriptor mtd : entry.getValue()){
                result += entry.getKey() + ": " + mtd + "\n";
            }
        }

        result += "]}";

        return result;
    }
}