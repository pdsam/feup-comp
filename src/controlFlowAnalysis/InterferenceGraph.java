package controlFlowAnalysis;

import symbolTable.descriptor.VarDescriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void addEdge(VarDescriptor varDescriptor1, VarDescriptor varDescriptor2) {
        VarNode node1 = lookup(varDescriptor1);
        VarNode node2 = lookup(varDescriptor2);

        if(node1 == null) {
            node1 =  new VarNode(varDescriptor1);
            addNode(node1);
        }

        if(node2 == null && node2 != node1) {
            node2 = new VarNode(varDescriptor2);
            addNode(node2);
        }

        if(!varDescriptor1.getName().equals(varDescriptor2.getName())) {
            node1.addEdge(node2);
            node2.addEdge(node1);
        }
    }

    public String print(String methodName) {
        String msg = "Interference Graph for '" + methodName + "':\n";

        for(Map.Entry<String, VarNode> entry : nodes.entrySet()) {
            msg += entry.getValue().toString() + '\n';
        }

        return msg;
    }
}
