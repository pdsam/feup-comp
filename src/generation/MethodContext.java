package generation;

import symbolTable.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class MethodContext {
    private int currentLabel;
    private int currentIfLabel;
    public SymbolTable st;

    public MethodContext(SymbolTable st) {
        this.currentLabel = 0;
        this.currentIfLabel = 0;
        this.st = st;
    }

    public String generateLabel() {
        String label = String.format("label_%d", currentLabel);
        currentLabel++;
        return label;
    }

    public String[] generateIfLabel() {
        String label1 = String.format("else_%d", currentIfLabel );
        String label2 = String.format("endif_%d", currentIfLabel );
        this.currentIfLabel++;
        return new String[]{label1, label2};
    }
}
