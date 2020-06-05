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

    public void addEdge(VarDescriptor varDescriptor1, VarDescriptor varDescriptor2) {
        VarNode node1 = lookup(varDescriptor1);
        VarNode node2 = lookup(varDescriptor2);

        if(node1 == null) {
            node1 =  new VarNode(varDescriptor1);
        }

        if(node2 == null) {
            node2 = new VarNode(varDescriptor2);
        }

        node1.addEdge(node2);
        node2.addEdge(node1);
    }
}
