package symbolTable;

import symbolTable.descriptor.Descriptor;
import symbolTable.descriptor.MethodDescriptor;
import symbolTable.descriptor.VarDescriptor;

import java.util.ArrayList;

public interface SymbolTable {
    MethodDescriptor method_lookup(String id, ArrayList<String> parameters);
    VarDescriptor variable_lookup(String id);
    void put(Descriptor descriptor);
}