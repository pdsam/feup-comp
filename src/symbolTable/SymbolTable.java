package symbolTable;

import symbolTable.descriptor.Descriptor;
import symbolTable.descriptor.MethodDescriptor;
import symbolTable.descriptor.VarDescriptor;

import java.util.List;

public interface SymbolTable {
    void setParent(SymbolTable parent);
    boolean isValidType(String type) throws InvalidType;
    MethodDescriptor method_lookup(String id, List<String> parameters, String className) throws UnknownDeclaration;
    VarDescriptor variable_lookup(String id) throws UnknownDeclaration;
    void put(Descriptor descriptor) throws AlreadyDeclared, UnknownDeclaration;
}