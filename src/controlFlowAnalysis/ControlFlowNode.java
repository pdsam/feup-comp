package controlFlowAnalysis;

import parser.SimpleNode;
import symbolTable.descriptor.VarDescriptor;

import java.util.ArrayList;
import java.util.List;

public class ControlFlowNode {
    private List<ControlFlowNode> successors = new ArrayList<>();
    private List<ControlFlowNode> predecessors = new ArrayList<>();

    private SimpleNode node;
    private List<VarDescriptor> in = new ArrayList<>();
    private List<VarDescriptor> out = new ArrayList<>();
    private List<VarDescriptor> def = new ArrayList<>();
    private List<VarDescriptor> use = new ArrayList<>();

    public List<VarDescriptor> getIn() {
        return in;
    }

    public void setIn(List<VarDescriptor> in) {
        this.in = in;
    }

    public List<VarDescriptor> getOut() {
        return out;
    }

    public void setOut(List<VarDescriptor> out) {
        this.out = out;
    }

    public List<VarDescriptor> getDef() {
        return def;
    }

    public void addDef(VarDescriptor def) {
        if(!this.def.contains(def))
            this.def.add(def);
    }

    public void addUse(VarDescriptor use) {
        if(!this.use.contains(use))
            this.use.add(use);
    }

    public List<VarDescriptor> getUse() {
        return use;
    }

    public List<ControlFlowNode> getSuccessors() {
        return successors;
    }

    public void addSuccessor(ControlFlowNode succ) {
        //check if suc is not on the list
        if(!this.successors.contains(succ))
            successors.add(succ);
    }

    public void addPredecessor(ControlFlowNode pred) {
        //check if pred is not on the list
        if(!this.predecessors.contains(pred))
            predecessors.add(pred);
    }

    public SimpleNode getNode() {
        return node;
    }

    public void setNode(SimpleNode node) {
        this.node = node;
    }

    @Override
    public String toString() {
        String msg = "";
        msg += "Line " + node.line + " has " + successors.size() + " successors and " + predecessors.size() + " predecessors\n";

        msg+= "\tPred: ";
        for(ControlFlowNode descriptor: predecessors) {
            msg+= descriptor.getNode().line + "; ";
        }

        msg+= "\n\tSucc: ";
        for(ControlFlowNode descriptor: successors) {
            msg+= descriptor.getNode().line + "; ";
        }

        msg+= "\n\tIn: ";
        for(VarDescriptor descriptor: in) {
            msg+= descriptor.getName() + "; ";
        }

        msg+= "\n\tOut: ";
        for(VarDescriptor descriptor: out) {
            msg+= descriptor.getName() + "; ";
        }

        msg+= "\n\tUse: ";
        for(VarDescriptor descriptor: use) {
            msg+= descriptor.getName() + "; ";
        }

        msg+= "\n\tDef: ";
        for(VarDescriptor descriptor: def) {
            msg+= descriptor.getName() + "; ";
        }

        return msg;
    }

}
