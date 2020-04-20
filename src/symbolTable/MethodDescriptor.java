package symbolTable;

import java.util.ArrayList;

public class MethodDescriptor extends Descriptor {
    private final String returnType;
    private ArrayList<String> parameters = new ArrayList<>();

    public MethodDescriptor(String name, String returnType) {
        super(name);
        this.returnType = returnType;
    }

    public MethodDescriptor(String name, String returnType, ArrayList<String> parameters) {
        super(name);
        this.returnType = returnType;
        this.parameters = parameters;
    }

    public int getNumParameters() { return parameters.size(); }

    public String getReturnType() { return returnType; }

    public ArrayList<String> getParameters() { return parameters; }
}

