package controlFlowAnalysis;

import symbolTable.SymbolTable;

public class ControlFlowData {
    private SymbolTable symbolTable;
    private ControlFlowGraph cfg;
    private ControlFlowNode node;

    public ControlFlowData(SymbolTable symbolTable, String methodName) {
        this.symbolTable = symbolTable;
        this.cfg = new ControlFlowGraph(methodName);
    }

    public void setNode(ControlFlowNode node) {
        this.node = node;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public ControlFlowGraph getGraph() {
        return cfg;
    }

    public ControlFlowNode getNode() {
        return node;
    }
}
