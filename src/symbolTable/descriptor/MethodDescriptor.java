package symbolTable.descriptor;

import java.util.ArrayList;

public class MethodDescriptor extends Descriptor {
    private final String returnType;
    private ArrayList<String> parameters = new ArrayList<>();
    private boolean isStatic;

    public MethodDescriptor(String name, String returnType, boolean isStatic) {
        super(name);
        this.returnType = returnType;
        this.isStatic = isStatic;
    }

    public MethodDescriptor(String name, String returnType, ArrayList<String> parameters, boolean isStatic) {
        super(name);
        this.returnType = returnType;
        this.parameters = parameters;
        this.isStatic = isStatic;
    }

    public int getNumParameters() { return parameters.size(); }

    public String getReturnType() { return returnType; }

    public ArrayList<String> getParameters() { return parameters; }
}

