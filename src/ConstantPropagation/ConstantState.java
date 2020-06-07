package ConstantPropagation;

import symbolTable.SymbolTable;
import symbolTable.descriptor.VarDescriptor;
import java.util.HashMap;
import java.util.Map;
import parser.*;

public class ConstantState {
    
    private Map<VarDescriptor, Expression>  varState = new HashMap<>();
    
    ConstantState(){}

    ConstantState(Map<VarDescriptor, Expression> original){
        this.varState = original;
    }
    
    public void add(VarDescriptor var, Expression value){

        varState.put(var, value);

    }

    public Map<VarDescriptor, Expression> getVarstate(){
        return varState;
    }

}