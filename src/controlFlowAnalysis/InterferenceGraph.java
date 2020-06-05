package controlFlowAnalysis;

import symbolTable.descriptor.VarDescriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InterferenceGraph {
    private HashMap<String, VarNode> nodes = new HashMap<>();

    public void addNode(VarNode varNode) {
        nodes.put(varNode.getDescriptor().getName(), varNode);
    }

    public List<VarNode> getNodes() {
        return new ArrayList<>(nodes.values());
    }

    public VarNode lookup(VarDescriptor varDescriptor) {
        return nodes.get(varDescriptor.getName());
    }

    public void addEdge(VarDescriptor node1, VarDescriptor node2) {
        //node1.addEdge(node2);
        //node2.addEdge(node1);
    }
}
