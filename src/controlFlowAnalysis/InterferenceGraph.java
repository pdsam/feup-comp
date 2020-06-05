package controlFlowAnalysis;

import symbolTable.descriptor.VarDescriptor;

import java.util.HashMap;

public class InterferenceGraph {
    private HashMap<String, VarNode> nodes = new HashMap<>();

    public void addNode(VarNode varNode) {
        nodes.put(varNode.getDescriptor().getName(), varNode);
    }

    public VarNode lookup(VarDescriptor varDescriptor) {
        return nodes.get(varDescriptor.getName());
    }

    public void addEdge(VarDescriptor node1, VarDescriptor node2) {
        //node1.addEdge(node2);
        //node2.addEdge(node1);
    }
}
