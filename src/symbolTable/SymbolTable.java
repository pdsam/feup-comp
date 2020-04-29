package symbolTable;

import symbolTable.descriptor.Descriptor;
import symbolTable.descriptor.MethodDescriptor;
import symbolTable.descriptor.VarDescriptor;
import symbolTable.exception.SemanticException;

import java.util.List;

public abstract class SymbolTable {
    protected SymbolTable parent = null;
    protected static boolean debug = false;

    public static void setDebug(boolean val) {
        debug = val;
    }

    public String getClassName() {
        if(parent != null)
            return parent.getClassName();

        return null;
    }

    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }

    abstract boolean isValidType(String type);
    abstract MethodDescriptor method_lookup(String id, List<String> parameters, String className) throws SemanticException;
    abstract VarDescriptor variable_lookup(String id) throws SemanticException;
    abstract void put(Descriptor descriptor) throws SemanticException;
}