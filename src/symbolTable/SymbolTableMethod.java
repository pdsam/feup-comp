package symbolTable;

import java.util.ArrayList;
import java.util.HashMap;


public class SymbolTableMethod implements SymbolTable {
    private SymbolTable parent;
    private HashMap<String, VarDescriptor> variables = new HashMap<String, VarDescriptor>();
    
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