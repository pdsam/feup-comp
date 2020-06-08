package ConstantPropagation;

import symbolTable.descriptor.VarDescriptor;

import java.util.HashMap;
import java.util.Map;

public class ConstantState {
    
    private Map<VarDescriptor, Object>  varState = new HashMap<>();

    public void add(VarDescriptor var, Object value){

        varState.put(var, value);

    }

    public Map<VarDescriptor, Object> getVarstate(){
        return varState;
    }

    public void clone(ConstantState state) {
        varState.clear();
        state.getVarstate().forEach((var, val) -> {
            varState.put(var, val);
        });
    }
}