package symbolTable;

import symbolTable.descriptor.Descriptor;
import symbolTable.descriptor.MethodDescriptor;
import symbolTable.descriptor.VarDescriptor;
import symbolTable.descriptor.VarType;
import symbolTable.exception.AlreadyDeclaredException;
import symbolTable.exception.SemanticException;
import symbolTable.exception.UnknownDeclarationException;
import symbolTable.exception.UnknownTypeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SymbolTableClass extends SymbolTable {

    private String superClass;
    private HashMap<String, VarDescriptor> fields_table = new HashMap<>();
    private HashMap<String, ArrayList<MethodDescriptor>> methods_table = new HashMap<>();


    public void setSuperClass(String className) { this.superClass = className; }


    @Override
    public boolean isValidType(String type) {
        if(parent != null) return parent.isValidType(type);

        return false;
    }

    // Lookup bottom up traverses the symbol table hierarchy until it finds the method descriptor 
    @Override
    public MethodDescriptor method_lookup(String id, List<String> parameters, String className) throws SemanticException {
        ArrayList<MethodDescriptor> overloads = methods_table.get(id);

        if(debug) {
            System.out.println("Method lookup in class' ST: " + id);
        }

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

           
    // Lookup bottom up traverses the symbol table hierarchy until it finds the variable descriptor    
    @Override
    public VarDescriptor variable_lookup(String id) throws SemanticException {
        VarDescriptor varDescriptor = fields_table.get(id);

        if(debug) {
            System.out.println("Variable lookup in class' ST: " + id);
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
            System.out.println("Putting descriptor in class' ST: " + id);
        }

        if(descriptor instanceof MethodDescriptor) {
            MethodDescriptor mtd = (MethodDescriptor) descriptor;
            ArrayList<MethodDescriptor> overloads = methods_table.get(id);

            if(debug) {
                System.out.println("Registering method '" + id + "': " + mtd.getClassName());
            }

            if(!isValidType(mtd.getReturnType()) && !mtd.getReturnType().equals("void"))
                throw new UnknownTypeException();

            if(overloads != null){ //a method with that id exists already
                for(MethodDescriptor methodDescriptor : overloads){
                    if(methodDescriptor.getParameters().equals(mtd.getParameters()) &&
                            methodDescriptor.getClassName().equals(mtd.getClassName()))
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
                var.setVarType(VarType.FIELD);
                var.setClassName(getClassName());
                fields_table.put(id, (VarDescriptor) descriptor);
            } else
                throw new AlreadyDeclaredException("Variable '" + id + "' already declared.");
        }
    }
}