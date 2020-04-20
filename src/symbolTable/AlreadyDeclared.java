package symbolTable;

public class AlreadyDeclared extends Exception {

    AlreadyDeclared() {
        super();
    }

    AlreadyDeclared(String message) {
        super(message);
    }
}