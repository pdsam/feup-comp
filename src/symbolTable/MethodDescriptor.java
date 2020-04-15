package symbolTable;

import java.util.ArrayList;

public class MethodDescriptor {
    private final String name;
    private final String returnType;
    private final ArrayList<String> parameters;

    public MethodDescriptor(String name, String returnType) {
        this(name, returnType, new ArrayList<>());
    }

    public MethodDescriptor(String name, String returnType, ArrayList<String> parameters) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
    }

    public int getNumParameters() { return parameters.size(); }

    public String getName() { return name; }

    public String getReturnType() { return returnType; }

    public ArrayList<String> getParameters() { return parameters; }
}

