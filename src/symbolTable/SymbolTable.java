package symbolTable;

import java.util.ArrayList;

import symbolTable.descriptor.Descriptor;
import symbolTable.descriptor.MethodDescriptor;
import symbolTable.descriptor.VarDescriptor;

public interface SymbolTable {
    void setParent(SymbolTable parent);
    MethodDescriptor method_lookup(String id, ArrayList<String> parameters) throws UnknownDeclaration, InvalidDescriptor;
    VarDescriptor variable_lookup(String id) throws UnknownDeclaration, InvalidDescriptor;
    void put(Descriptor descriptor) throws AlreadyDeclared, UnknownDeclaration, InvalidDescriptor;
}