package symbolTable.descriptor;

import java.util.ArrayList;
import java.util.List;

public class MethodDescriptor implements Descriptor {
    private final String name;
    private final String returnType;
    private List<String> parameters;
    private boolean isStatic;
    private String className = null;


    public MethodDescriptor(String name, String returnType, List<String> parameters, boolean isStatic,String className) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
        this.isStatic = isStatic;
        this.className = className; 
    }

    public MethodDescriptor(String name, String returnType, boolean isStatic) {
        this.name = name;
        this.returnType = returnType;
        this.isStatic = isStatic;
        this.parameters = new ArrayList<>();
    }

    public MethodDescriptor(String name, String returnType, List<String> parameters, boolean isStatic) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
        this.isStatic = isStatic;
    }

   

    public int getNumParameters() { return parameters.size(); }

    public String getReturnType() { return returnType; }

    public List<String> getParameters() { return parameters; }

    @Override
    public String getName() {
        return name;
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

