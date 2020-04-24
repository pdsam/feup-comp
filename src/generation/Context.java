package generation;

import symbolTable.SymbolTable;

public class Context {
    private int currentLabel;
    public SymbolTable st;

    public Context(SymbolTable st) {
        this.currentLabel = 0;
        this.st = st;
    }

    public String generateLabel() {
        String label = String.format("label_%d\n", currentLabel);
        currentLabel++;
        return label;
    }
}
