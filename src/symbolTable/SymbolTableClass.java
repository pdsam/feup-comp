package symbolTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import symbolTable.descriptor.Descriptor;
import symbolTable.descriptor.MethodDescriptor;
import symbolTable.descriptor.VarDescriptor;
import symbolTable.exception.AlreadyDeclaredException;
import symbolTable.exception.SemanticException;
import symbolTable.exception.UnknownTypeException;
import symbolTable.exception.UnknownDeclarationException;

public class SymbolTableClass implements SymbolTable {
    private SymbolTable parent = null;
    private String superClass;
    private HashMap<String, VarDescriptor> fields_table = new HashMap<>();
    private HashMap<String, ArrayList<MethodDescriptor>> methods_table = new HashMap<>();

    @Override
    public String getClassName() {
        if(parent != null)
            return parent.getClassName();

        return null;
    }

    public void setSuperClass(String className) { this.superClass = className; }

    @Override
    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }

    @Override
    public boolean isValidType(String type) {
        if(parent != null) return parent.isValidType(type);

        return false;
    }

    // Lookup bottom up traverses the symbol table hierarchy until it finds the method descriptor 
    @Override
    public MethodDescriptor method_lookup(String id, List<String> parameters, String className) throws SemanticException {
        ArrayList<MethodDescriptor> overloads = methods_table.get(id);

        if(overloads != null){
            for(MethodDescriptor descriptor : overloads){
                if(descriptor.getParameters().equals(parameters) && descriptor.getClassName().equals(className) ){
                    if(debug) {
                        System.out.println("Method found: " + descriptor);
                    }
                    return descriptor;
                }
            }
        }

        // Variables is not in this scope, iteratively looks in the ancestors
        if(parent != null)
            return this.parent.method_lookup(id, parameters, className);

        throw new UnknownDeclarationException("Method '" + id + "' not defined.");
    }

           
    // Lookup bottom up traverses the symbol table hierarchy until it finds the variable descriptor    
    @Override
    public VarDescriptor variable_lookup(String id) throws SemanticException {
        VarDescriptor varDescriptor = fields_table.get(id);

        if(debug) {
            System.out.println("Variable lookup: " + id);
        }

        if(varDescriptor != null) {
            if(debug) {
                System.out.println("Variable found: " + varDescriptor);
            }
            return varDescriptor;
        }

        // Variables is not in this scope, iteratively looks in the ancestors
        if(parent != null) 
            return this.parent.variable_lookup(id);

        throw new UnknownDeclarationException("Variable '" + id + "' not defined.");
    }

    @Override
    public void put(Descriptor descriptor) throws SemanticException {
        String id = descriptor.getName();

        if(debug) {
            System.out.println("Putting descriptor: " + id);
        }

        if(descriptor instanceof MethodDescriptor) {
            MethodDescriptor mtd = (MethodDescriptor) descriptor;
            ArrayList<MethodDescriptor> overloads = methods_table.get(id);

            if(debug) {
                System.out.println("Registering method '" + id + "': " + mtd.getClassName());
            }

            if(!isValidType(mtd.getReturnType()) && !mtd.getReturnType().equals("void"))
                throw new UnknownTypeException();

            if(overloads != null){
                for(MethodDescriptor methodDescriptor : overloads){
                    if(methodDescriptor.getParameters().equals(((MethodDescriptor) descriptor).getParameters()) &&
                            methodDescriptor.getClassName().equals( ((MethodDescriptor) descriptor).getClassName()))
                        throw new AlreadyDeclaredException("Method '" + id + "' already defined.\nConflict: " + methodDescriptor);
                }

                overloads.add(mtd);
            } else {
                ArrayList<MethodDescriptor> entry = new ArrayList<>();
                entry.add(mtd);
                methods_table.put(id, entry);
            }

        } else if(descriptor instanceof VarDescriptor) {
            VarDescriptor var = (VarDescriptor) descriptor;

            if(debug) {
                System.out.println("Registering class field '" + id + "': " + descriptor);
            }

            if(!isValidType(var.getType()))
                throw new UnknownTypeException();

            if(fields_table.get(id) == null) {
                var.setField(true);
                var.setClassName(getClassName());
                fields_table.put(id, (VarDescriptor) descriptor);
            } else
                throw new AlreadyDeclaredException("Variable '" + id + "' already declared.");
        }

        if(debug ){
            System.out.println("Descriptor isn't neither a var neither a method");
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("SymbolTableClass{ superClass='" + superClass + '\'');

        result.append(", fields=[ \n");
        for(Map.Entry<String, VarDescriptor> entry : fields_table.entrySet()) {
            result.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        result.append("], methods=[ \n");
        for(Map.Entry<String, ArrayList<MethodDescriptor>> entry : methods_table.entrySet()) {
            for(MethodDescriptor mtd : entry.getValue()){
                result.append(entry.getKey()).append(": ").append(mtd).append("\n");
            }
        }

        result.append("]}");

        return result.toString();
    }
}