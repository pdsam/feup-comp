package semantics;

import parser.*;
import symbolTable.*;
import symbolTable.descriptor.*;

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
    public Object visit(ASTImportList node, Object data) {
        node.childrenAccept(this, data);

        return null;
    }

    @Override
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
    public Object visit(ASTClass node, Object data) {
        SymbolTableClass st = node.getStClass();
        st.setParent((SymbolTableDoc)data);
        node.childrenAccept(this,st);
        return null;
    }

    @Override
    public Object visit(ASTVarDeclarationsList node, Object data) {
        node.childrenAccept(this,data);
        return null;
    }

    @Override
    public Object visit(ASTMethodList node, Object data) {
        node.childrenAccept(this,data);
        return null;
    }

    @Override
    public Object visit(ASTMainContainer node, Object data) {
        node.childrenAccept(this,data);
        return null;
    }

    @Override
    public Object visit(ASTVar node, Object data) {
        VarDescriptor var = new VarDescriptor(node.identifier, node.type);
        try{
            SymbolTable st=(SymbolTable)data;
            st.put(var);
        }
        catch(Exception e){
            System.out.println("handle erros at ASTVAR");
        }

        return null;
    }

    @Override
    public Object visit(ASTMethod node, Object data) {
        SymbolTable st = node.getStMethod();
        st.setParent((SymbolTableClass)data);

        

        


        return null;
    }

    @Override
    public Object visit(ASTStatementList node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTReturnStatement node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTMainMethod node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTParameter node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTParameterList node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTScopedStatementList node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTAssignment node, Object data) {
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
    public Object visit(ASTVarReference node, Object data) {
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
