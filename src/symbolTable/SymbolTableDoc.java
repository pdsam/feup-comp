package symbolTable;

import symbolTable.descriptor.Descriptor;
import symbolTable.descriptor.MethodDescriptor;
import symbolTable.descriptor.VarDescriptor;
import symbolTable.exception.AlreadyDeclaredException;
import symbolTable.exception.SemanticException;
import symbolTable.exception.UnknownDeclarationException;
import symbolTable.exception.UnknownTypeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SymbolTableDoc extends SymbolTable {

    private HashMap<String, ArrayList<MethodDescriptor>> imports = new HashMap<>();
    private HashMap<String, VarDescriptor> classes = new HashMap<>();
    private ArrayList<String> validTypes = new ArrayList<>(Arrays.asList("int", "boolean", "array", "String[]"));
    private String className;

    public ArrayList<MethodDescriptor> getClassMethods(String className) {
        ArrayList<MethodDescriptor> descriptors = new ArrayList<>();

        for(HashMap.Entry<String, ArrayList<MethodDescriptor>> entry : imports.entrySet()){
            for(MethodDescriptor method : entry.getValue()) {
                if(method.getClassName().equals(className)) {
                    if(debug) {
                        System.out.println("Adding method to descriptor");
                    }
                    descriptors.add(method);
                }
            }
        }

        return descriptors;
    }

    public void setClassName(String className) {
        if(debug) {
            System.out.println("Registering document class: " + className);
        }

        this.className = className;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public boolean isValidType(String type) {
        //if type isn't valid a error message will be send afterwords
        return validTypes.contains(type) || classes.containsKey(type);
    }

    @Override
    public MethodDescriptor method_lookup(String id, List<String> parameters, String className) throws SemanticException {
        ArrayList<MethodDescriptor> overloads = imports.get(id);

        if(debug) {
            System.out.println("Method lookup in document's ST: " + id);
        }

        if(overloads != null) {
            for (MethodDescriptor descriptor : overloads) {
                //checks if the number and type of arguments matches 
                //and confirms if the class used to invoke the method is the same as the descriptor class
                if (descriptor.getParameters().equals(parameters) &&
                        descriptor.getClassName().equals(className)) {
                    if(debug) {
                        System.out.println("Method '" + id + "' found: " + descriptor);
                    }
                    return descriptor;
                }

            }
        }

        if(parent != null) return this.parent.method_lookup(id, parameters,className);

        String errorMessage = "Method '" + id +"(";

        for(int i = 0; i < parameters.size(); i++) {
            if(i == parameters.size() - 1)
                errorMessage += parameters.get(i);
            else
                errorMessage += parameters.get(i) + ", ";
        }

        errorMessage += ")' not defined.";

        throw new UnknownDeclarationException(errorMessage);
    }

    @Override
    public VarDescriptor variable_lookup(String id) throws SemanticException {
        VarDescriptor varDescriptor = classes.get(id);

        if(debug) {
            System.out.println("Variable lookup in document's ST: " + id);
        }

        if(varDescriptor != null) {
            if(debug) {
                System.out.println("Variable '" + id + "' found: " + varDescriptor);
            }
            return varDescriptor;
        }

        if(parent != null) 
            return this.parent.variable_lookup(id);

        throw new UnknownDeclarationException("Variable '" + id + "' not defined.");
    }

    @Override
    public void put(Descriptor descriptor) throws SemanticException {
        String id = descriptor.getName();

        if(debug) {
            System.out.println("Putting descriptor in document's ST: " + id);
        }

        if(descriptor instanceof MethodDescriptor) {
            MethodDescriptor mtd = (MethodDescriptor) descriptor;
            ArrayList<MethodDescriptor> overloads = imports.get(id);

            if(debug) {
                System.out.println("Registering import method '" + id + "': " + mtd.getClassName());
            }

            if(!isValidType(mtd.getReturnType()) && !mtd.getReturnType().equals("void"))
                throw new UnknownTypeException();

            if(overloads != null){ //a method with that id exists already
                for(MethodDescriptor methodDescriptor : overloads){ 
                    //check if a function in that scope and with the same parameters (in number and type of arguments) exists
                    if(methodDescriptor.getParameters().equals(mtd.getParameters() ) &&
                        methodDescriptor.getClassName().equals(mtd.getClassName()) ) {
                        throw new AlreadyDeclaredException("Method '" + id + "' already defined.\nConflict: " + methodDescriptor);
                    }

                }

                overloads.add(mtd);
            } else {
                ArrayList<MethodDescriptor> entry = new ArrayList<>();
                entry.add(mtd);
                imports.put(id, entry);
            }

        } else if(descriptor instanceof VarDescriptor) {
            VarDescriptor var = (VarDescriptor) descriptor;

            if(debug) {
                System.out.println("Registering class '" + id + "': " + var);
            }

            classes.putIfAbsent(id, var);
        }

    }
}