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

    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    public String print(String methodName) {
        StringBuilder msg = new StringBuilder("Interference Graph for '" + methodName + "':\n");

        for(Map.Entry<String, VarNode> entry : nodes.entrySet()) {
            msg.append(entry.getValue().toString()).append('\n');
        }

        return msg.toString();
    }

    public String printAllocation(String methodName) {
        StringBuilder msg = new StringBuilder("\n========== Allocation for method " + methodName + " ============\n");

        for(Map.Entry<String, VarNode> entry : nodes.entrySet()) {
            VarDescriptor var = entry.getValue().getDescriptor();
            msg.append("Variable '").append(var.getName()).append("' allocated to register ").append(var.getStackOffset()).append('\n');
        }

        return msg.toString();
    }
}
