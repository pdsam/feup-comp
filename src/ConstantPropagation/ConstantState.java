package ConstantPropagation;

import symbolTable.SymbolTable;
import symbolTable.descriptor.VarDescriptor;
import java.util.HashMap;
import java.util.Map;
import parser.*;

public class ConstantState {

    private Map<VarDescriptor, Expression>  varState = new HashMap<>();

    public void add(VarDescriptor var, Expression value){

        //do logic to see if redefinition

        varState.put(var, value);
    }


}