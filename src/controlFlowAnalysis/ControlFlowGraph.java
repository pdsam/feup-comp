package controlFlowAnalysis;

import java.util.ArrayList;
import java.util.List;

public class ControlFlowGraph {
    private List<ControlFlowNode> nodeSet;

    public ControlFlowGraph() {
        this(new ArrayList<>());
    }

    public ControlFlowGraph(List<ControlFlowNode> nodeSet) {
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
}
