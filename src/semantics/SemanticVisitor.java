package semantics;

import parser.*;
import symbolTable.*;
import symbolTable.descriptor.*;
import symbolTable.exception.AlreadyDeclaredException;
import symbolTable.exception.UnknownDeclarationException;
import symbolTable.exception.UnknownTypeException;

import java.util.ArrayList;
import java.util.List;

public class SemanticVisitor implements MyGrammarVisitor {
    private boolean debug = false;

    private void logError(SimpleNode node, String msg) {
        System.err.println("Error at line: "+node.line+", column: " +node.column+". "+ msg);
        this.increment();
    }
    private int numerrors = 0;
    private void increment(){
        numerrors++;
        if(numerrors > 10){
            System.out.println("Max number of errors reached, Semantic analyser exiting.");
            //System.exit(1); 
        }
    }

    @Override
    public Object visit(SimpleNode node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTDocument node, Object data) {
        node.childrenAccept(this, node.getStDoc());

        return null;
    }

    @Override
    // Receives SymbolTableDoc as argument
    public Object visit(ASTImportList node, Object data) {
        node.childrenAccept(this, data);

        return null;
    }

    @Override
    // Receives SymbolTableDoc as argument
    //TODO: check valid types
    public Object visit(ASTImport node, Object data) {
        SymbolTableDoc st = (SymbolTableDoc) data;
        List<String> params = new ArrayList<>();

        if (node.parameters != null) {
            for (String param : node.parameters) {
                if (!param.equals("void"))
                    params.add(param);
            }
        }

        VarDescriptor var = new VarDescriptor(node.className, node.className);
        MethodDescriptor mtd = new MethodDescriptor(node.methodName, node.returnType, params, node.className, node.isStatic);

        // If the import is only of a class then there is no method to register
        //then the visitor stops here
        if (mtd.getName() == null)
            return null;

        try {
            st.put(mtd);
        } catch(UnknownTypeException e) {
            logError(node, e.getMessage() + " '" + mtd.getReturnType() + "' as return for method " + mtd.getName());
        } catch (Exception e) {
            logError(node, e.getMessage());
        }

        try {
            // The following line register a new class as a valid type
            //so no type checking is done whatsoever
            st.put(var);
        } catch(Exception e){
            logError(node, e.getMessage());
        }

        return null;
    }

    @Override
    // Receives SymbolTableDoc as argument
    public Object visit(ASTClass node, Object data) {
        SymbolTableDoc parentST = (SymbolTableDoc) data;
        SymbolTableClass st = node.getStClass();
        st.setParent(parentST);
        st.setClassName(node.identifier);

        //Registering this own class, which means there is a new type
        //so no type checking is needed
        VarDescriptor var = new VarDescriptor(node.identifier, node.identifier);
        try {
            parentST.put(var);
        } catch(Exception e){
            logError(node, e.getMessage());
        }

        // Grand children are the methods declared inside the class
        for(Node child : node.children){
            if(child instanceof ASTVarDeclarationsList)
                continue;

            SimpleNode simpleChild = (SimpleNode) child;
            if(simpleChild.children != null) {
                for(Node grandChild : simpleChild.children){
                    try {
                        registerMethodNode((ASTMethod) grandChild, st);
                    } catch (Exception e) {
                        logError(node, e.getMessage());
                    }
                }
            }
        }

        // Register methods if this class extends another
        if(node.parent != null) {
            st.setSuperClass(node.parent);
            for(MethodDescriptor mtd : parentST.getClassMethods(node.parent)){
                try {
                    // Every method inside the class is referenced to 'this'
                    //and they have already been type checked
                    mtd.setClassName("this");
                    st.put(mtd);
                } catch (Exception ignore) {
                    //There will be an exception if the method is already declared
                    //which means that the class is overriding the methods of the superclass
                }
            }
        }

//        System.out.println("Class ST: " + st);
        node.childrenAccept(this, st);
        return null;
    }

    private void registerMethodNode(ASTMethod node, SymbolTable classTable) throws UnknownDeclarationException, AlreadyDeclaredException {
        MethodDescriptor mtd = new MethodDescriptor(node.identifier, node.type, node.isStatic);
        SimpleNode paramList = (SimpleNode) node.jjtGetChild(0); // parameter list is the first child of the method node
        List<String> parameters = new ArrayList<>();

        for(int i = 0; i < paramList.children.length; i++){
            ASTParameter param = (ASTParameter) paramList.children[i];
            parameters.add(param.type);
        }

        mtd.setParameters(parameters);
        try {
            classTable.put(mtd);
        } catch(UnknownTypeException e) {
            logError(node, e.getMessage() + " '" + mtd.getReturnType() + "' as return for method " + mtd.getName());
        } catch (symbolTable.exception.SemanticException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object visit(ASTParameter node, Object data) {
        VarDescriptor var = new VarDescriptor(node.identifier, node.type);
        SymbolTable st = (SymbolTable) data;

        try{
            st.put(var);
        } catch(UnknownTypeException e) {
            logError(node, e.getMessage() + " '" + var.getType() + "' as return for parameter " + var.getName());
        } catch(Exception e){
            logError(node, e.getMessage());
        }
        return null;
    }

    @Override
    // Receives SymbolTableClass or SymbolTableMethod as argument
    public Object visit(ASTVar node, Object data) {
        VarDescriptor var = new VarDescriptor(node.identifier, node.type);
        SymbolTable st = (SymbolTable) data;
        try{
            st.put(var);
        } catch(UnknownTypeException e) {
            logError(node, e.getMessage() + " '" + var.getType() + "' as return for variable " + var.getName());
        } catch(Exception e){
            logError(node, e.getMessage());
        }

        return null;
    }

    @Override
    public Object visit(ASTVarReference node, Object data) {
        SymbolTable st = (SymbolTable) data;
        VarDescriptor var = null;

        try {
            var = st.variable_lookup(node.identifier);
        } catch (Exception e) {
            logError(node, e.getMessage());
        }

        if(var == null) {
            node.type = "null";
        } else {
//            System.out.println("Referencing " + node.identifier + ": " + var);
            node.type = var.getType();
            node.desc = var;
        }
        return null;
    }

    @Override
    public Object visit(ASTFunctionCall node, Object data) {
        node.childrenAccept(this, data);
        SymbolTable st = (SymbolTable) data;
        MethodDescriptor var = null;

        try {
            var = st.method_lookup(node.identifier, node.arguments.list, node.ownerRef.type);
        } catch (Exception e) {
            logError(node, e.getMessage());
        }

        if(var == null) {
            System.out.println(node.identifier + ": null");
            node.type = "null";
        }
        else {
            node.type = var.getReturnType();
            node.desc = var;

        }
        return null;
    }

    @Override
    public Object visit(ASTArguments node, Object data) {
        node.childrenAccept(this, data);

        if(node.children == null)
            return null;

        for(Node child : node.children) {
            Expression exp = (Expression) child;
            node.list.add(exp.type);
        }

        return null;
    }

    @Override
    // Receives SymbolTableClass as argument
    public Object visit(ASTMethod node, Object data) {
        SymbolTable st = node.getStMethod();
        st.setParent((SymbolTable)data);
        node.childrenAccept(this, st);
        return null;
    }

    @Override
    public Object visit(ASTMainMethod node, Object data) {
        SymbolTable st = node.getStMethod();
        st.setParent((SymbolTable)data);
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    // Receives SymbolTableClass as argument
    public Object visit(ASTMainContainer node, Object data) {
        node.childrenAccept(this,data);
        return null;
    }

    @Override
    // Receives SymbolTableClass as argument
    public Object visit(ASTVarDeclarationsList node, Object data) {
        node.childrenAccept(this,data);
        return null;
    }

    @Override
    // Receives SymbolTableClass as argument
    public Object visit(ASTMethodList node, Object data) {
        node.childrenAccept(this,data);
        return null;
    }

    @Override
    public Object visit(ASTStatementList node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTReturnStatement node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTParameterList node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTScopedStatementList node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTAssignment node, Object data) {
        node.childrenAccept(this, data);

        if(!node.varReference.type.equals(node.value.type)){
            ASTVarReference var = (ASTVarReference) node.varReference;
            logError(node, "Types do not match: " + var.identifier + " is of type " + var.type);
        }

        return null;
    }

    @Override
    public Object visit(ASTBranch node, Object data) {
        //TODO: check if condition is boolean
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTWhileLoop node, Object data) {
        //TODO: check if condition is boolean
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTArrayAssignment node, Object data) {
        //TODO: check if value being assigned is int
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTExprStatement node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTArrayAccess node, Object data) {
        //TODO: check if index is int
        ASTVarReference var = (ASTVarReference) node.arrayRef;
        SymbolTable st = (SymbolTable) data;

        try {
            st.variable_lookup(var.identifier);
        } catch (Exception e) {
            logError(node, e.getMessage());
        }

        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTIntegerLiteral node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTBooleanLiteral node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTSelfReference node, Object data) {
        node.type = "this";
        return null;
    }

    @Override
    public Object visit(ASTArrayCreation node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTConstructorCall node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTArrayLength node, Object data) {
        node.type = "int";
        return null;
    }

    @Override
    public Object visit(ASTNegation node, Object data) {
        node.childrenAccept(this, data);

        if(node.child.type != "boolean") {
            logError(node, "! operator must be applied to a boolean expression.");
        }

        node.type = "boolean";
        return null;
    }

    private void checkOperandsTypes(BinOpExpression expr, String type) {
//        System.out.println("Left type: " + expr.left.type);
//        System.out.println("Right type: " + expr.right.type);

        if(!expr.left.type.equals(type)) {
            logError(expr, "Left side of && operator must be of type boolean.");
        } else if(!expr.right.type.equals(type)) {
            logError(expr, "Right side of && operator must be of type boolean.");
        }
    }

    @Override
    public Object visit(ASTAnd node, Object data) {
        node.childrenAccept(this, data);
        checkOperandsTypes(node, "boolean");
        node.type = "boolean";
        return null;
    }

    @Override
    public Object visit(ASTLessThan node, Object data) {
        node.childrenAccept(this, data);
        checkOperandsTypes(node, "int");
        node.type = "boolean";
        return null;
    }

    @Override
    public Object visit(ASTSum node, Object data) {
        node.childrenAccept(this, data);
        checkOperandsTypes(node, "int");
        node.type = "int";
        return null;
    }

    @Override
    public Object visit(ASTSub node, Object data) {
        node.childrenAccept(this, data);
        checkOperandsTypes(node, "int");
        node.type = "int";
        return null;
    }

    @Override
    public Object visit(ASTMul node, Object data) {
        node.childrenAccept(this, data);
        checkOperandsTypes(node, "int");
        node.type = "int";
        return null;
    }

    @Override
    public Object visit(ASTDiv node, Object data) {
        node.childrenAccept(this, data);
        checkOperandsTypes(node, "int");
        node.type = "int";
        return null;
    }
}
