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
        if(!interferences.contains(node))
            interferences.add(node);
    }

    public void removeInterference(VarNode node) { interferences.remove(node); }

    public List<VarNode> getInterferences() {
        return interferences;
    }

    public int numInterferences() { return interferences.size(); }

    @Override
    public String toString() {
        String msg = "\tVarNode '" + descriptor.getName() + "':\n";

        msg += "\t\tInterferences: ";

        for(VarNode interference : interferences) {
            msg += interference.descriptor.getName() + "; ";
        }


        return msg + '\n';
    }
}
