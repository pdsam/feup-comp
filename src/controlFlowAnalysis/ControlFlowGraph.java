package controlFlowAnalysis;

import java.util.ArrayList;
import java.util.List;

public class ControlFlowGraph {
    private ArrayList<ControlFlowNode> nodeSet;

    public ControlFlowGraph() {
        this(new ArrayList<>());
    }

    public ControlFlowGraph(ArrayList<ControlFlowNode> nodeSet) {
        this.nodeSet = nodeSet;
    }

    public ArrayList<ControlFlowNode> getNodeSet() {
        return nodeSet;
    }

    public void addNode(ControlFlowNode node) {
        nodeSet.add(node);
    }

    public void addSuccessor(ControlFlowNode current, ControlFlowNode succ) {
        current.addSuccessor(succ);
        succ.addPredecessor(current);
    }
}
