package symbolTable.descriptor;

public class VarDescriptor implements Descriptor {
    private final String name;
    private final String type;
    private int stackOffset;
    private String value;
    private String className;
    private boolean isField;

    public VarDescriptor(String name, String type) {
        this.name = name;
        this.type = type;
        value = null;
    }

    public VarDescriptor(String name, String type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public boolean isInitialized() {
        return this.value != null;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public boolean isField() {
        return isField;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setField(boolean field) {
        isField = field;
    }

    @Override
    public String toString() {
        return "VarDescriptor{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", stackOffset=" + stackOffset +
                ", value='" + value + '\'' +
                '}';
    }
}
