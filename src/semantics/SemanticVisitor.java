package semantics;

import parser.*;
import symbolTable.*;
import symbolTable.descriptor.*;

import java.util.ArrayList;
import java.util.List;

public class SemanticVisitor implements MyGrammarVisitor {
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
    public Object visit(ASTImport node, Object data) {
        SymbolTableDoc st = (SymbolTableDoc) data;
        List<String> params = new ArrayList<>();

        if(node.parameters != null) {
            for(String param : node.parameters) {
                if(!param.equals("void"))
                    params.add(param);
            }
        }

        MethodDescriptor desc = new MethodDescriptor(node.methodName,node.returnType,params,node.className,node.isStatic);

        if(desc.getName() == null)
            return null;

        try{
            st.put(desc);

            System.out.println("Registering " + node.methodName + ": " + desc);
        } catch(Exception e){
            System.err.println(e.getMessage());
        }

        return null;
    }

    @Override
    // Receives SymbolTableDoc as argument
    public Object visit(ASTClass node, Object data) {
        SymbolTableClass st = node.getStClass();
        st.setParent((SymbolTableDoc)data);

        // Grand children are the methods declared inside the class
        for(Node child : node.children){
            if(child instanceof ASTParameterList)
                continue;

            SimpleNode simpleChild = (SimpleNode) child;
            if(simpleChild.children != null) {
                for(Node grandChild : simpleChild.children){
                    try {
                        registerMethod((ASTMethod) grandChild, st);
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            }
        }

        node.childrenAccept(this, st);
        return null;
    }

    private void registerMethod(ASTMethod node, SymbolTable table) throws UnknownDeclaration, AlreadyDeclared {
        MethodDescriptor desc = new MethodDescriptor(node.identifier, node.type, node.isStatic);
        SimpleNode paramList = (SimpleNode) node.children[0]; // parameter list is the first child of the method node
        List<String> parameters = new ArrayList<>();

        for(int i = 0; i < paramList.children.length; i++){
            ASTParameter param = (ASTParameter) paramList.children[i];
            parameters.add(param.type);
        }

        desc.setParameters(parameters);
        table.put(desc);
    }

    @Override
    public Object visit(ASTParameter node, Object data) {
        VarDescriptor var = new VarDescriptor(node.identifier, node.type);
        SymbolTable st = (SymbolTable) data;
        try{
            st.put(var);
        }
        catch(Exception e){
            System.err.println(e.getMessage());
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
        }
        catch(Exception e){
            System.err.println(e.getMessage());
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
            System.err.println(e.getMessage());
        }

        if(var == null)
            node.type = "null";
        else
            node.type = var.getType();
        return null;
    }

    @Override
    public Object visit(ASTFunctionCall node, Object data) {
        node.childrenAccept(this, data);
        SymbolTable st = (SymbolTable) data;
        MethodDescriptor var = null;

        try {
            var = st.method_lookup(node.identifier, node.arguments.list);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        if(var == null) {
            System.out.println(node.identifier + ": null");
            node.type = "null";
        }
        else {
            node.type = var.getReturnType();
            System.out.println(node.identifier + ": " + var);
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
        node.childrenAccept(this, data);
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
        //TODO: check if types match
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTBranch node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTWhileLoop node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTArrayAssignment node, Object data) {
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
        ASTVarReference var = (ASTVarReference) node.arrayRef;
        SymbolTable st = (SymbolTable) data;

        try {
            st.variable_lookup(var.identifier);
        } catch (UnknownDeclaration unknownDeclaration) {
            System.err.println(unknownDeclaration.getMessage());
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
        return null;
    }

    @Override
    public Object visit(ASTNegation node, Object data) {
        node.childrenAccept(this, data);

        if(node.child.type != "boolean") {
            System.err.println("! operator must be applied to a boolean expression.");
            //TODO: numerrors++
        }

        node.type = "boolean";
        return null;
    }

    private void checkOperandsTypes(BinOpExpression expr, String type) {
        System.out.println("Left type: " + expr.left.type);
//        if(!expr.left.type.equals(type)) {
//            System.err.println("Left side of && operator must be of type boolean.");
//            //TODO: numerrors++
//        } else if(!expr.right.type.equals(type)) {
//            System.err.println("Right side of && operator must be of type boolean.");
//            //TODO: numerrors++
//        }
    }

    @Override
    public Object visit(ASTAnd node, Object data) {
//        node.childrenAccept(this, data);
//        checkOperandsTypes(node, "boolean");
//        node.type = "boolean";
        return null;
    }

    @Override
    public Object visit(ASTLessThan node, Object data) {
//        node.childrenAccept(this, data);
//        checkOperandsTypes(node, "int");
//        node.type = "boolean";
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
//        node.childrenAccept(this, data);
//        checkOperandsTypes(node, "int");
//        node.type = "int";
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
//        node.childrenAccept(this, data);
//        checkOperandsTypes(node, "int");
//        node.type = "int";
        return null;
    }
}
