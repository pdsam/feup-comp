package symbolTable;

public class InvalidDescriptor extends Exception {

    InvalidDescriptor() {    }

    InvalidDescriptor(String error) {
        super(error);
    }
}
