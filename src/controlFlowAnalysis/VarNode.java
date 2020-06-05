package controlFlowAnalysis;

import symbolTable.descriptor.VarDescriptor;

import java.util.ArrayList;
import java.util.List;

public class VarNode {
    private VarDescriptor descriptor;
    private List<VarNode> interferences = new ArrayList<>();

    public VarNode(VarDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public VarDescriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(VarDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public boolean lookup( VarDescriptor varDescriptor) {
        return descriptor.equals(varDescriptor);
    }

    public void addEdge(VarNode node) {
        interferences.add(node);
    }

    public int numInterferences() { return interferences.size(); }
}
