package symbolTable;

import java.util.ArrayList;

import src.symbolTable.descriptor.Descriptor;
import src.symbolTable.descriptor.MethodDescriptor;
import src.symbolTable.descriptor.VarDescriptor;

public interface SymbolTable {
    void setParent(SymbolTable parent);
    MethodDescriptor method_lookup(String id, ArrayList<String> parameters);
    VarDescriptor variable_lookup(String id);
    void put(Descriptor descriptor);
}