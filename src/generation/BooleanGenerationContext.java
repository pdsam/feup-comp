package generation;

public class BooleanGenerationContext {
    public MethodContext methodcontext;
    public String successLabel;
    public String failLabel;

    public BooleanGenerationContext(MethodContext methodcontext, String successLabel, String failLabel) {
        this.methodcontext = methodcontext;
        this.successLabel = successLabel;
        this.failLabel = failLabel;
    }
}
