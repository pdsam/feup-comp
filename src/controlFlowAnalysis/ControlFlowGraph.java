package controlFlowAnalysis;

import java.util.ArrayList;
import java.util.List;

public class ControlFlowGraph {
    private List<ControlFlowNode> nodeSet;

    public ControlFlowGraph() {
        this(new ArrayList<>());
    }

    public ControlFlowGraph(ArrayList<ControlFlowNode> nodeSet) {
        this.nodeSet = nodeSet;
    }

    public List<ControlFlowNode> getNodeSet() {
        return nodeSet;
    }

    public void addNode(ControlFlowNode node) {
        nodeSet.add(node);
    }

    public void addSuccessor(ControlFlowNode current, ControlFlowNode succ) {
        current.addSuccessor(succ);
        succ.addPredecessor(current);
    }

    @Override
    public String toString() {
        String msg = "Graph: \n";

        for(ControlFlowNode node : nodeSet){
            msg += node.toString() + '\n';
        }

        return msg;
    }
}
