package symbolTable.descriptor;

public class VarDescriptor extends Descriptor {
    private final String type;
    private int stackOffset;
    private String value;

    public VarDescriptor(String name, String type) {
        super(name);
        this.type = type;
        value = null;
    }

    public VarDescriptor(String name, String type, String value) {
        super(name);
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
}
