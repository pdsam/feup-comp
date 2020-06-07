package controlFlowAnalysis;

import symbolTable.descriptor.MethodDescriptor;

import java.util.ArrayList;
import java.util.List;

public class ControlFlowGraph {
    private List<ControlFlowNode> nodeSet;
    private final MethodDescriptor method;
    private final int initialStackOffset;

    public ControlFlowGraph(MethodDescriptor method, int initialStackOffset) {
        this(new ArrayList<>(), method, initialStackOffset);
    }

    public ControlFlowGraph(ArrayList<ControlFlowNode> nodeSet, MethodDescriptor method, int initialStackOffset) {
        this.nodeSet = nodeSet;
        this.method = method;
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

    public String getMethodName() {
        return method.getName();
    }

    public MethodDescriptor getMethodDescriptor() {
        return this.method;
    }

    @Override
    public String toString() {
        String msg= "Method's " + method + " graph: \n";

        for(ControlFlowNode node : nodeSet){
            msg += node.toString();
        }

        return msg;
    }
}
