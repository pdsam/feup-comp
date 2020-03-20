public abstract class Expression extends SimpleNode {
    protected String type;

    public Expression(int id) {
        super(id);
    }

    public Expression(MyGrammar p, int id) {
        super(p, id);
    }

}