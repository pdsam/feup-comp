package controlFlowAnalysis;

import symbolTable.descriptor.VarDescriptor;

import java.util.ArrayList;
import java.util.List;

public class InterferenceGraph {
    private List<VarNode> nodes = new ArrayList<>();

    public List<VarNode> getNodes() {
        return nodes;
    }

    public void addNode(VarNode varNode) {
        nodes.add(varNode);
    }

    public void setNodes(List<VarNode> nodes) {
        this.nodes = nodes;
    }

    public VarNode lookup(VarDescriptor varDescriptor) {
        for(VarNode node: nodes) {
            if(node.lookup(varDescriptor))
                return node;
        }
        return null;
    }
}
