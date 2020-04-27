package symbolTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import symbolTable.descriptor.Descriptor;
import symbolTable.descriptor.MethodDescriptor;
import symbolTable.descriptor.VarDescriptor;
import symbolTable.exception.AlreadyDeclaredException;
import symbolTable.exception.SemanticException;
import symbolTable.exception.UnknownDeclarationException;
import symbolTable.exception.UnknownTypeException;

public class  SymbolTableDoc implements SymbolTable {
    private SymbolTable parent = null;
    private HashMap<String, ArrayList<MethodDescriptor>> imports = new HashMap<>();
    private HashMap<String, VarDescriptor> classes = new HashMap<>();
    private ArrayList<String> validTypes = new ArrayList<>(List.of("int", "boolean", "array", "String[]"));

    public ArrayList<MethodDescriptor> getClassMethods(String className) {
        ArrayList<MethodDescriptor> descriptors = new ArrayList<>();

        for(HashMap.Entry<String, ArrayList<MethodDescriptor>> entry : imports.entrySet()){
            for(MethodDescriptor method : entry.getValue()) {
                if(method.getClassName().equals(className))
                    descriptors.add(method);
            }
        }

        return descriptors;
    }

    @Override
    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }

    @Override
    public boolean isValidType(String type) {
        return validTypes.contains(type) || classes.containsKey(type);
    }

    @Override
    public MethodDescriptor method_lookup(String id, List<String> parameters, String className) throws SemanticException {
        ArrayList<MethodDescriptor> overloads = imports.get(id);

        if(overloads != null) {
            for (MethodDescriptor descriptor : overloads) {
                if (descriptor.getParameters().equals(parameters) &&
                        descriptor.getClassName().equals(className))
                    return descriptor;
            }
        }

        if(parent != null) return this.parent.method_lookup(id, parameters,className);

        throw new UnknownDeclarationException("Method \'" + id + "\' not defined.");
    }

    @Override
    public VarDescriptor variable_lookup(String id) throws SemanticException {
        VarDescriptor varDescriptor = classes.get(id);

        if(varDescriptor != null)
            return varDescriptor;

        if(parent != null) return this.parent.variable_lookup(id);

        throw new UnknownDeclarationException("Variable \'" + id + "\' not defined.");
    }

    @Override
    public void put(Descriptor descriptor) throws SemanticException {
        String id = descriptor.getName();
        if(descriptor instanceof MethodDescriptor) {
            MethodDescriptor mtd = (MethodDescriptor) descriptor;
            ArrayList<MethodDescriptor> overloads = imports.get(id);

            if(!isValidType(mtd.getReturnType()) && !mtd.getReturnType().equals("void"))
                throw new UnknownTypeException();

            if(overloads != null){ //a method with that id exists already
                for(MethodDescriptor methodDescriptor : overloads){
                    if(methodDescriptor.getParameters().equals(((MethodDescriptor) descriptor).getParameters() ) &&
                        methodDescriptor.getClassName().equals( ((MethodDescriptor) descriptor).getClassName()) ) {
                        throw new AlreadyDeclaredException("Method \'" + id + "\' already defined.\nConflict: " + methodDescriptor);
                    }

                }

                overloads.add(mtd);
            } else {
                ArrayList<MethodDescriptor> entry = new ArrayList<>();
                entry.add(mtd);
                imports.put(id, entry);
            }

            return;
        } else if(descriptor instanceof VarDescriptor) {
            if(classes.get(id) == null)
                classes.put(id, (VarDescriptor) descriptor);

            return;
        }

        throw new UnknownDeclarationException("Variables cannot be defined outside a class or method.");
    }
}