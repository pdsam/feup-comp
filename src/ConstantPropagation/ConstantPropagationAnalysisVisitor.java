package ConstantPropagation;

import parser.*;
import ConstantPropagation.*;
import symbolTable.descriptor.VarDescriptor;
import java.util.Iterator;
import java.util.Map;

public class ConstantPropagationAnalysisVisitor implements MyGrammarVisitor {

    private void logDebug(String msg) {
        System.out.println("Debug: " + msg);
    }

    @Override
    public Object visit(SimpleNode node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTDocument node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTImportList node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTImport node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTClass node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTVarDeclarationsList node, Object data) {
        node.childrenAccept(this, data);
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
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTMethod node, Object data) {
        ConstantState state = new ConstantState();
        node.childrenAccept(this, state);
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
    public Object visit(ASTMainMethod node, Object data) {
        ConstantState state = new ConstantState();
        node.childrenAccept(this, state);
        return null;
    }

    @Override
    public Object visit(ASTParameter node, Object data) {
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
        node.value.jjtAccept(this, data);

        ASTVarReference aux = (ASTVarReference) node.varReference;
        if (node.value instanceof ASTIntegerLiteral || node.value instanceof ASTBooleanLiteral) {
            aux.value = node.value;

        }

        else {
            aux.value = null;
        }
        if (data != null) {
            ConstantState state = (ConstantState) data;
            state.add(aux.desc, aux.value);

        }

        return null;
    }

    @Override
    public Object visit(ASTBranch node, Object data) {
        ConstantState state = (ConstantState) data;

        ConstantState thenState = new ConstantState();
        ConstantState elseState = new ConstantState();

        node.thenStatement.jjtAccept(this, thenState);
        node.elseStatement.jjtAccept(this, elseState);

        // compare if they both modify the same variables, or the previous ones.

        Map<VarDescriptor, Expression> first = thenState.getVarstate();
        Map<VarDescriptor, Expression> second = elseState.getVarstate();

        Iterator<VarDescriptor> keythenItr = first.keySet().iterator();
        while (keythenItr.hasNext()) {
            VarDescriptor keyTemp = keythenItr.next();
            if (first.get(keyTemp).equals(second.get(keyTemp))) { // If same key, same value mapped
                state.add(keyTemp, first.get(keyTemp)); // add key value to map
            }
        }

        return null;
    }

    @Override
    public Object visit(ASTWhileLoop node, Object data) {
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

        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTSelfReference node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTArrayCreation node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTConstructorCall node, Object data) {
        node.childrenAccept(this, data);
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