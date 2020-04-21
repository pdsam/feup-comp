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
        MethodDescriptor des = new MethodDescriptor(node.methodName,node.returnType,node.parameters,node.className,node.isStatic);

        try{
            st.put(des);
        } catch(Exception e){
            System.out.println("do stuff at ASTIMPORT");
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
            for(Node grandChild : simpleChild.children){
                try {
                    registerMethod((ASTMethod) grandChild, st);
                } catch (Exception e) {
                    e.getMessage();
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
            System.out.println("handle erros at ASTVAR");
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
            System.out.println("handle erros at ASTVAR");
        }

        return null;
    }

    @Override
    public Object visit(ASTVarReference node, Object data) {
        SymbolTable st = (SymbolTable) data;
        VarDescriptor var;

        try {
            var = st.variable_lookup(node.identifier);
        } catch (UnknownDeclaration unknownDeclaration) {
            System.err.println(unknownDeclaration.getMessage());
        }

        return null; //Eventally retrn type
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
        return null;
    }

    @Override
    public Object visit(ASTWhileLoop node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTArrayAssignment node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTExprStatement node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTArrayAccess node, Object data) {
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
    public Object visit(ASTNegation node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTArrayLength node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTFunctionCall node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTArguments node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTAnd node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTLessThan node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTSum node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTSub node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTMul node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTDiv node, Object data) {
        return null;
    }
}
