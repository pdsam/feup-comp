package semantics;

import symbolTable.SymbolTable;
import symbolTable.descriptor.VarDescriptor;

import java.util.HashMap;
import java.util.Map;

public class FlowState {
    private SymbolTable st;
    private Map<VarDescriptor, VarState> vars = new HashMap<>();

    public FlowState(SymbolTable st) {
        this.st = st;
    }

    public void clone (FlowState fs) {
        vars.clear();

        fs.getVars().forEach((var, state) -> {
            vars.put(var, state);
        });
    }

    public SymbolTable getSt() {
        return st;
    }

    public Map<VarDescriptor, VarState> getVars() {
        return vars;
    }

    public VarState state_lookup(VarDescriptor var) {
        return vars.get(var);
    }
}
