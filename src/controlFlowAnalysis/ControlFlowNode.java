package controlFlowAnalysis;

import parser.SimpleNode;
import symbolTable.descriptor.VarDescriptor;

import java.util.ArrayList;
import java.util.List;

public class ControlFlowNode {
    private List<ControlFlowNode> successors = new ArrayList<>();
    private List<ControlFlowNode> predecessors = new ArrayList<>();

    private SimpleNode node;
    private ArrayList<VarDescriptor> in = new ArrayList<>();
    private ArrayList<VarDescriptor> out = new ArrayList<>();
    private ArrayList<VarDescriptor> def = new ArrayList<>();
    private ArrayList<VarDescriptor> use = new ArrayList<>();


    public ArrayList<VarDescriptor> getIn() {
        return in;
    }

    public void setIn(ArrayList<VarDescriptor> in) {
        this.in = in;
    }

    public ArrayList<VarDescriptor> getOut() {
        return out;
    }

    public void setOut(ArrayList<VarDescriptor> out) {
        this.out = out;
    }

    public ArrayList<VarDescriptor> getDef() {
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

    public ArrayList<VarDescriptor> getUse() {
        return use;
    }

    public void setUse(ArrayList<VarDescriptor> use) {
        this.use = use;
    }

    public List<ControlFlowNode> getSuccessors() {
        return successors;
    }

    public List<ControlFlowNode> getPredecessors() {
        return predecessors;
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
}
