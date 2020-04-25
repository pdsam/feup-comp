package symbolTable.descriptor;

import java.util.ArrayList;
import java.util.List;

public class MethodDescriptor implements Descriptor {
    private final String name;
    private final String returnType;
    private List<String> parameters;
    private final String className;
    private boolean isStatic;

    public MethodDescriptor(String name, String returnType, boolean isStatic) {
        this(name, returnType, new ArrayList<>(), "this", isStatic);
    }

    public MethodDescriptor(String name, String returnType, List<String> parameters, boolean isStatic) {
        this(name, returnType, parameters, "this", isStatic);
    }

    public MethodDescriptor(String name, String returnType, List<String> parameters, String className, boolean isStatic) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
        this.className = className;
        this.isStatic = isStatic;
    }

    public int getNumParameters() { return parameters.size(); }

    public String getReturnType() { return returnType; }

    public List<String> getParameters() { return parameters; }

    @Override
    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public String toString() {
        return "MethodDescriptor{" +
                "name='" + name + '\'' +
                ", returnType='" + returnType + '\'' +
                ", parameters=" + parameters +
                ", isStatic=" + isStatic +
                '}';
    }


}

