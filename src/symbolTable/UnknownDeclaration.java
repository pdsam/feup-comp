package symbolTable;

public class UnknownDeclaration extends Exception {

        UnknownDeclaration()
        {
            super();
        }

        UnknownDeclaration(String message)
        {
            super(message);
        }
}