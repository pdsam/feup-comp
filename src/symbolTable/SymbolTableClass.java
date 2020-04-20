package symbolTable;

import java.util.ArrayList;
import java.util.HashMap;

import symbolTable.SymbolTable;

public class SymbolTableClass extends SymbolTable {
    private SymbolTable parent = null;
    private HashMap<String, VarDescriptor> fields_table = new HashMap<String, VarDescriptor>();
    private HashMap<String, ArrayList<Descriptor>> methods_table = new HashMap<String, ArrayList<Descriptor>>();

    void setupParent(SymbolTable parent) {
        this.parent = parent;
    }

    MethodDescriptor method_lookup(String id, ArrayList<String> parameters){

    }

    VarDescriptor variable_lookup(String id){

    }

    void put(Descriptor descriptor){

    }
}