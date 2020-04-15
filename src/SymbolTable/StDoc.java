
public class StDoc extends SymbolTable {

    //import hashMap
    public HashMap<String, Descriptor> imports = new HashMap<String, Descriptor>();

    StDoc(SimpleNode node, SymbolTable parent){
        super(node, parent);
    }

    public Descriptor lookup(String id) {
        return this.imports.get(id);
    }
    
    public void put(String id, Descriptor descriptor){
        this.imports.put(id, descriptor);
    }
}