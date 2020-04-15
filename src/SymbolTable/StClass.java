
public class StClass extends SymbolTable {

    public HashMap<String, Descriptor> fields_table = new HashMap<String, Descriptor>();
    public HashMap<String, Descriptor> methods_table = new HashMap<String, Descriptor>();

    StClass(SimpleNode node, SymbolTable parent){
        super(node, parent);
    }
    
    public Descriptor lookup_field(String id) {
        return this.fields_table.get(id);
    }
    
    public void put_field(String id, Descriptor descriptor){
        this.fields_table.put(id, descriptor);
    }

    public Descriptor lookup_method(String id) {
        return this.methods_table.get(id);
    }
    
    public void put_method(String id, Descriptor descriptor){
        this.methods_table.put(id, descriptor);
    }
    
}