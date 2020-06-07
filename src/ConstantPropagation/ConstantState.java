package ConstantPropagation;

import symbolTable.SymbolTable;
import symbolTable.descriptor.VarDescriptor;
import java.util.HashMap;
import java.util.Map;
import parser.*;

public class ConstantState {
    
    private Map<VarDescriptor, Object>  varState = new HashMap<>();
    
    ConstantState(){}

    ConstantState(Map<VarDescriptor, Object> original){
        this.varState = original;
    }
    
    public void add(VarDescriptor var, Object value){

        varState.put(var, value);

    }

    public Map<VarDescriptor, Object> getVarstate(){
        return varState;
    }

}