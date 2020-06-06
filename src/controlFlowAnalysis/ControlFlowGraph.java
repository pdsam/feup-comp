package controlFlowAnalysis;

import java.util.ArrayList;
import java.util.List;

public class ControlFlowGraph {
    private List<ControlFlowNode> nodeSet;
    private final String methodName;
    private final int initialStackOffset;

    public ControlFlowGraph(String methodName, int initialStackOffset) {
        this(new ArrayList<>(), methodName, initialStackOffset);
    }

    public ControlFlowGraph(ArrayList<ControlFlowNode> nodeSet, String methodName, int initialStackOffset) {
        this.nodeSet = nodeSet;
        this.methodName = methodName;
        this.initialStackOffset = initialStackOffset;
    }

    public int getInitialStackOffset() {
        return initialStackOffset;
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
        String msg= "Method's " + methodName + " graph: \n";

        for(ControlFlowNode node : nodeSet){
            msg += node.toString() + '\n';
        }

        return msg;
    }
}
