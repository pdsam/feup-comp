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

    public void addEdge(VarNode node) {
        interferences.add(node);
    }

    public void removeInterference(VarNode node) { interferences.remove(node); }

    public List<VarNode> getInterferences() {
        return interferences;
    }

    public int numInterferences() { return interferences.size(); }
}
