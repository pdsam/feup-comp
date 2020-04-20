package symbolTable;

import java.util.ArrayList;
import java.util.HashMap;

public class  SymbolTableDoc extends SymbolTable {
    private SymbolTable parent;
    private HashMap<String, Descriptor> imports = new HashMap<String, Descriptor>();

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