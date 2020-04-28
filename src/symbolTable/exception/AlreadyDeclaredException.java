package symbolTable.exception;

public class AlreadyDeclaredException extends SemanticException {
    public AlreadyDeclaredException(String message) {
        super(message);
    }
}