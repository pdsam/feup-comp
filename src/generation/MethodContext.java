package generation;

import symbolTable.SymbolTable;

public class MethodContext {
    private int currentLabel;
    public SymbolTable st;

    public MethodContext(SymbolTable st) {
        this.currentLabel = 0;
        this.st = st;
    }

    public String generateLabel() {
        String label = String.format("label_%d\n", currentLabel);
        currentLabel++;
        return label;
    }
}
