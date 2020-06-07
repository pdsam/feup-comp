package controlFlowAnalysis;

public class AllocationException extends Exception {
    public AllocationException(int required, int actual) {
        super(String.format("%d registers are not enough to compile the program. At least %d are needed.", required, actual));
    }
}
