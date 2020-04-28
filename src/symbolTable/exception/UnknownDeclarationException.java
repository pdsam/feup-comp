package symbolTable.exception;

public class UnknownDeclarationException extends SemanticException {
    public UnknownDeclarationException(String message)
    {
        super(message);
    }
}