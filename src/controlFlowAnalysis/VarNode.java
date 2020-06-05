package controlFlowAnalysis;

import symbolTable.descriptor.VarDescriptor;

public class VarNode {
    private VarDescriptor descriptor;

    public VarNode(VarDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public VarDescriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(VarDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public boolean lookup( VarDescriptor varDescriptor) {
        return descriptor.equals(varDescriptor);
    }
}
