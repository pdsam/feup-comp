package symbolTable.descriptor;

public class VarDescriptor implements Descriptor {
    private final String name;
    private final String type;
    private int stackOffset;
    private String className;
    private boolean initialized;
    private VarType varType;

    public VarDescriptor(String name, String type) {
        this.name = name;
        this.type = type;
        this.initialized = false;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getStackOffset() {
        return stackOffset;
    }

    public void setStackOffset(int stackOffset) {
        this.stackOffset = stackOffset;
    }

    public void initialize() {
        this.initialized = true;
    }

    public String getClassName() {
        return className;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public VarType getVarType() {
        return varType;
    }

    public void setVarType(VarType varType) {
        this.varType = varType;
    }

    @Override
    public String toString() {
        return "VarDescriptor{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", stackOffset=" + stackOffset +
                ", initialized='" + initialized + '\'' +
                '}';
    }
}
