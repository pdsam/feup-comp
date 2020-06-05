package controlFlowAnalysis;

import parser.SimpleNode;
import symbolTable.descriptor.VarDescriptor;

import java.util.ArrayList;
import java.util.List;

public class ControlFlowNode {
    private List<ControlFlowNode> successors;
    private List<ControlFlowNode> predecessors;

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
        this.def.add(def);
    }

    public void addUse(VarDescriptor use) {
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
        successors.add(succ);
    }

    public void addPredecessor(ControlFlowNode pred) {
        predecessors.add(pred);
    }
}
