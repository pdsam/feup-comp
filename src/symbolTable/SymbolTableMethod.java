package symbolTable;

import symbolTable.descriptor.Descriptor;
import symbolTable.descriptor.MethodDescriptor;
import symbolTable.descriptor.VarDescriptor;
import symbolTable.exception.AlreadyDeclaredException;
import symbolTable.exception.SemanticException;
import symbolTable.exception.UnknownTypeException;
import symbolTable.exception.UnknownDeclarationException;

import java.util.HashMap;
import java.util.List;

//TODO access string array in main arguments
public class SymbolTableMethod implements SymbolTable {
    private SymbolTable parent;
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

    @Override
    public void setParent(SymbolTable parent) {
        this.parent = parent;
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
        if(parent != null) 
            return this.parent.method_lookup(id, parameters, className);

        throw new UnknownDeclarationException("Method \'" + id + "\' not defined.");
    }

    // Lookup bottom up traverses the hierarchy until it finds the descriptor
    @Override
    public VarDescriptor variable_lookup(String id) throws SemanticException {
        VarDescriptor varDescriptor = variables.get(id);

        if(varDescriptor != null) {
            if(debug) {
                System.out.println("Variable found: " + varDescriptor);
            }
            return varDescriptor;
        }

        // Variables is not in this scope, iteratively looks in the ancestors
        if(parent != null) 
            return this.parent.variable_lookup(id);

        throw new UnknownDeclarationException("Variable \'" + id + "\' not defined.");
    }

    @Override
    public void put(Descriptor descriptor) throws SemanticException {
        if(descriptor instanceof VarDescriptor) {
            String id = descriptor.getName();
            VarDescriptor var = (VarDescriptor) descriptor;

            if(debug) {
                System.out.println("Registering variable '" + id + "': " + var);
            }

            if(!isValidType(var.getType()))
                throw new UnknownTypeException();

            if(variables.get(id) == null) {
                var.setField(false);
                var.setStackOffset(currentVarIndex);
                currentVarIndex++;
                variables.put(id, (VarDescriptor) descriptor);

            } else {

                throw new AlreadyDeclaredException("Variable \'" + id + "\' already declared.");
            }
            return;
        }

        throw new UnknownDeclarationException("Methods cannot be defined inside other methods.");
    }
}