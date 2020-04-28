package symbolTable.exception;

public class UnknownTypeException extends SemanticException {
    public UnknownTypeException() {
        super("Invalid type");
    }
}
