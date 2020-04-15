
public class StMethod extends SymbolTable {

    public HashMap<String, Descriptor> parameters = new HashMap<String, Descriptor>();
    public HashMap<String, Descriptor> vars = new HashMap<String, Descriptor>();

    StMethod(SimpleNode node, SymbolTable parent){
        super(node, parent);
    }
    
    public Descriptor lookup(String id) {
        return this.vars.get(id);
    }
    
    public void put(String id, Descriptor descriptor){
        this.vars.put(id, descriptor);
    }

    public Descriptor lookup_parameters(String id) {
        return this.parameters.get(id);
    }
    
    public void put_parameters(String id, Descriptor descriptor){
        this.parameters.put(id, descriptor);
    }


    
}