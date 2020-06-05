package controlFlowAnalysis;

import parser.SimpleNode;

import java.util.ArrayList;

public class DataFlowNode {
    private SimpleNode node;
    private ArrayList<String> in = new ArrayList<>();
    private ArrayList<String> out = new ArrayList<>();
    private ArrayList<String> def = new ArrayList<>();
    private ArrayList<String> use = new ArrayList<>();

    public ArrayList<String> getIn() {
        return in;
    }

    public void setIn(ArrayList<String> in) {
        this.in = in;
    }

    public ArrayList<String> getOut() {
        return out;
    }

    public void setOut(ArrayList<String> out) {
        this.out = out;
    }

}
