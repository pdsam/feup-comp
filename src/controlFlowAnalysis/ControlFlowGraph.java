package controlFlowAnalysis;

import java.util.ArrayList;
import java.util.List;

public class ControlFlowGraph {
    private List<ControlFlowNode> nodeSet;
    private final String methodName;

    public ControlFlowGraph(String methodName) {
        this(new ArrayList<>(), methodName);
    }

    public ControlFlowGraph(ArrayList<ControlFlowNode> nodeSet, String methodName) {
        this.nodeSet = nodeSet;
        this.methodName = methodName;
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
