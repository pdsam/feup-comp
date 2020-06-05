package controlFlowAnalysis;

import parser.*;
import symbolTable.SymbolTable;
import symbolTable.SymbolTableDoc;
import symbolTable.SymbolTableMethod;

public class ControlFlowVisitor implements MyGrammarVisitor {
    @Override
    public Object visit(SimpleNode node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTDocument node, Object data) {
        node.childrenAccept(this, data);

        return null;
    }

    @Override
    public Object visit(ASTImportList node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTImport node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTClass node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTVarDeclarationsList node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTMethodList node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTMainContainer node, Object data) {
        node.childrenAccept(this, data);

        return null;
    }

    @Override
    public Object visit(ASTVar node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTMethod node, Object data) {
        SymbolTable st = node.getStMethod();
        ControlFlowData cfdata = new ControlFlowData(st);

        //All nodes of the CFG will be filled with succ[], pred[], use[] and def[]
        node.childrenAccept(this, cfdata);

        //Afterwards, we can run the Liveness algorithm
        //TODO: insert algorithm

        return null;
    }

    @Override
    public Object visit(ASTStatementList node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTReturnStatement node, Object data) {
        //TODO: this is a statement
        return null;
    }

    @Override
    public Object visit(ASTMainMethod node, Object data) {
        //TODO: Method
        return null;
    }

    @Override
    public Object visit(ASTParameter node, Object data) {
        //TODO: def var

        return null;
    }

    @Override
    public Object visit(ASTParameterList node, Object data) {
        //TODO: this is a statement
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
        //TODO: def var
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTBranch node, Object data) {
        //TODO: statement

        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTWhileLoop node, Object data) {
        //TODO: statement

        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTArrayAssignment node, Object data) {
        //TODO: statement

        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTExprStatement node, Object data) {
        //TODO: statement

        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTArrayAccess node, Object data) {
        //TODO: use var

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
    public Object visit(ASTVarReference node, Object data) {
        //TODO: use var
        return null;
    }

    @Override
    public Object visit(ASTSelfReference node, Object data) {
        //TODO: user var
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
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTArrayLength node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTFunctionCall node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTArguments node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTAnd node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTLessThan node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTSum node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTSub node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTMul node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTDiv node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }
}
