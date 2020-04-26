package symbolTable;

import symbolTable.descriptor.Descriptor;
import symbolTable.descriptor.MethodDescriptor;
import symbolTable.descriptor.VarDescriptor;
import symbolTable.exception.SemanticException;

import java.util.List;

public interface SymbolTable {
    void setParent(SymbolTable parent);
    boolean isValidType(String type);
    MethodDescriptor method_lookup(String id, List<String> parameters, String className) throws SemanticException;
    VarDescriptor variable_lookup(String id) throws SemanticException;
    void put(Descriptor descriptor) throws SemanticException;
}