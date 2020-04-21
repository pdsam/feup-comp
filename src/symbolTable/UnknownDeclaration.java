package symbolTable;

public class UnknownDeclaration extends Exception {
        UnknownDeclaration(String message)
        {
            super(message);
        }
}