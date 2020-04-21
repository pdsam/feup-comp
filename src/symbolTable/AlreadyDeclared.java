package symbolTable;

public class AlreadyDeclared extends Exception {
    AlreadyDeclared(String message) {
        super(message);
    }
}