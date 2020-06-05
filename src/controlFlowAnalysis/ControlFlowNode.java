package controlFlowAnalysis;

import parser.SimpleNode;
import symbolTable.descriptor.VarDescriptor;

import java.util.ArrayList;

public class ControlFlowNode {
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

    public void setDef(ArrayList<VarDescriptor> def) {
        this.def = def;
    }

    public ArrayList<VarDescriptor> getUse() {
        return use;
    }

    public void setUse(ArrayList<VarDescriptor> use) {
        this.use = use;
    }
}
