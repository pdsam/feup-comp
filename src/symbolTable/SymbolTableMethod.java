package symbolTable;

import symbolTable.descriptor.Descriptor;
import symbolTable.descriptor.MethodDescriptor;
import symbolTable.descriptor.VarDescriptor;
import symbolTable.descriptor.VarType;
import symbolTable.exception.AlreadyDeclaredException;
import symbolTable.exception.SemanticException;
import symbolTable.exception.UnknownTypeException;
import symbolTable.exception.UnknownDeclarationException;

import java.util.HashMap;
import java.util.List;

//TODO access string array in main arguments
public class SymbolTableMethod extends SymbolTable {

    private HashMap<String, VarDescriptor> variables = new HashMap<>();
    private boolean isStaticContext;
    private int currentVarIndex;

    public void setStaticContext(boolean staticContext) {
        isStaticContext = staticContext;
        currentVarIndex = isStaticContext ? 0 : 1;
    }

    public boolean isStaticContext() {
        return isStaticContext;
    }

    public int getLocalsCount() {
        return variables.size();
    }

    @Override
    public String getClassName() {
        if(parent != null)
            return parent.getClassName();

        return null;
    }

    @Override
    public boolean isValidType(String type) {
        if(parent != null) return parent.isValidType(type);

        return false;
    }

    @Override
    public MethodDescriptor method_lookup(String id, List<String> parameters, String className) throws SemanticException {
        if(debug) {
            System.out.println("Method lookup in method's ST: " + id);
        }

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

    // Lookup bottom up traverses the hierarchy until it finds the descriptor
    @Override
    public VarDescriptor variable_lookup(String id) throws SemanticException {
        VarDescriptor varDescriptor = variables.get(id);

        if(debug) {
            System.out.println("Variable lookup in method's ST: " + id);
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
            System.out.println("Putting descriptor in method's ST: " + id);
        }

        if(descriptor instanceof VarDescriptor) {

            VarDescriptor var = (VarDescriptor) descriptor;

            if(debug) {
                System.out.println("Registering variable '" + id + "': " + var);
            }

            if(!isValidType(var.getType()))
                throw new UnknownTypeException();

            if(variables.get(id) == null) {
                var.setStackOffset(currentVarIndex);
                if (var.getVarType() == null) {
                    var.setVarType(VarType.LOCAL);
                }
                currentVarIndex++;
                variables.put(id, (VarDescriptor) descriptor);
                return;
            }
            
            throw new AlreadyDeclaredException("Variable '" + id + "' already declared.");

        }

        throw new UnknownDeclarationException("Methods cannot be defined inside other methods.");
    }
}