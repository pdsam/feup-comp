package ConstantPropagation;

import parser.*;
import symbolTable.descriptor.VarDescriptor;

import java.util.Map;
import java.util.Objects;

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
        node.children[1].jjtAccept(this, data);
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
        node.children[1].jjtAccept(this, data); // Method declarations
        node.children[2].jjtAccept(this, data); // Main declaration
        node.children[3].jjtAccept(this, data); // Method declarations
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
        node.children[2].jjtAccept(this, state); // Generate code for statements

        node.children[3].jjtAccept(this, state); // Generate code for return statement
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
        node.children[2].jjtAccept(this, state);
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

        node.condition.jjtAccept(this, data);

        ConstantState state = (ConstantState) data;

        ConstantState thenState = new ConstantState();
        thenState.clone(state);
        ConstantState elseState = new ConstantState();
        elseState.clone(state);

        node.thenStatement.jjtAccept(this, thenState);
        node.elseStatement.jjtAccept(this, elseState);

        // compare if they both modify the same variables, or the previous ones.

        Map<VarDescriptor, Object> first = thenState.getVarstate();
        Map<VarDescriptor, Object> second = elseState.getVarstate();

        for (VarDescriptor keyTemp : first.keySet()) {
            if (first.get(keyTemp) != null) {
                if (first.get(keyTemp).equals(second.get(keyTemp))) { // If same key, same value mapped
                    state.add(keyTemp, first.get(keyTemp)); // add key value to map
                    second.remove(keyTemp);
                } else {
                    state.add(keyTemp, null);
                    second.remove(keyTemp);
                }
            } else {
                state.add(keyTemp, null);
                second.remove(keyTemp);

            }
        }
        for (VarDescriptor keyTemp : second.keySet()) {
            if (second.get(keyTemp) != null) {
                if (!second.get(keyTemp).equals(first.get(keyTemp))) {
                    state.add(keyTemp, null);
                }
                else{
                    //should never enter here
                    state.add(keyTemp, second.get(keyTemp));
                }
            } else {
                state.add(keyTemp, null);
            }
        }

        return null;
    }

    @Override
    public Object visit(ASTWhileLoop node, Object data) {

        ConstantState state = (ConstantState) data;

        node.condition.jjtAccept(this, state);

        ConstantState bodyState = new ConstantState();
        bodyState.clone(state);

        node.body.jjtAccept(this, bodyState);

        boolean redo = false;
        Map<VarDescriptor, Object> outsideVars = state.getVarstate();
        for (VarDescriptor keyTemp : outsideVars.keySet()) {
            if (!Objects.equals(outsideVars.get(keyTemp), bodyState.getVarstate().get(keyTemp))) {
                redo = true;
                bodyState.add(keyTemp, null);
                state.add(keyTemp, null);
            }
        }

        if (redo) {
            ConstantState conditionState = new ConstantState();
            conditionState.clone(bodyState);
            node.condition.jjtAccept(this, conditionState);

            node.body.jjtAccept(this, bodyState);
        }

        return null;
    }

    @Override
    public Object visit(ASTArrayAssignment node, Object data) {
        node.arrayRef.index.jjtAccept(this, data);
        node.value.jjtAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTExprStatement node, Object data) {
        node.expression.jjtAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTArrayAccess node, Object data) {
        node.index.jjtAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTIntegerLiteral node, Object data) {
        node.foldedValue = node.val;
        return null;
    }

    @Override
    public Object visit(ASTBooleanLiteral node, Object data) {
        node.foldedValue = node.val;
        return null;
    }

    @Override
    public Object visit(ASTVarReference node, Object data) {
        node.foldedValue = null;
        ConstantState state = (ConstantState) data;
        node.value = state.getVarstate().get(node.desc);

        node.foldedValue = state.getVarstate().get(node.desc);
        if (node.value instanceof ASTIntegerLiteral) {
            node.foldedValue = ((ASTIntegerLiteral) node.value).val;
        }
        if (node.value instanceof ASTBooleanLiteral) {
            node.foldedValue = ((ASTBooleanLiteral) node.value).val;
        }
        return null;
    }

    @Override
    public Object visit(ASTSelfReference node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTArrayCreation node, Object data) {
        node.size.jjtAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTConstructorCall node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTNegation node, Object data) {
        node.foldedValue = null;
        node.child.jjtAccept(this, data);

        if (node.child.foldedValue != null) {
            node.foldedValue = !(boolean) node.child.foldedValue;
        }
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
        node.foldedValue = null;
        node.left.jjtAccept(this, data);
        node.right.jjtAccept(this, data);

        if (node.left.foldedValue != null && node.right.foldedValue != null) {
            node.foldedValue = (boolean) node.left.foldedValue && (boolean) node.right.foldedValue;
        }
        return null;
    }

    @Override
    public Object visit(ASTLessThan node, Object data) {
        node.foldedValue = null;
        node.left.jjtAccept(this, data);
        node.right.jjtAccept(this, data);

        if (node.left.foldedValue != null && node.right.foldedValue != null) {
            node.foldedValue = (int) node.left.foldedValue < (int) node.right.foldedValue;
        }

        return null;
    }

    @Override
    public Object visit(ASTSum node, Object data) {
        node.foldedValue = null;
        node.left.jjtAccept(this, data);
        node.right.jjtAccept(this, data);

        if (node.left.foldedValue != null && node.right.foldedValue != null) {
            node.foldedValue = (int) node.left.foldedValue + (int) node.right.foldedValue;
        }
        return null;
    }

    @Override
    public Object visit(ASTSub node, Object data) {
        node.foldedValue = null;
        node.left.jjtAccept(this, data);
        node.right.jjtAccept(this, data);

        if (node.left.foldedValue != null && node.right.foldedValue != null) {
            node.foldedValue = (int) node.left.foldedValue - (int) node.right.foldedValue;
        }
        return null;
    }

    @Override
    public Object visit(ASTMul node, Object data) {
        node.foldedValue = null;
        node.left.jjtAccept(this, data);
        node.right.jjtAccept(this, data);

        if (node.left.foldedValue != null && node.right.foldedValue != null) {
            node.foldedValue = (int) node.left.foldedValue * (int) node.right.foldedValue;
        }
        return null;
    }

    @Override
    public Object visit(ASTDiv node, Object data) {
        node.foldedValue = null;
        node.left.jjtAccept(this, data);
        node.right.jjtAccept(this, data);

        if (node.left.foldedValue != null && node.right.foldedValue != null) {
            node.foldedValue = (int) node.left.foldedValue / (int) node.right.foldedValue;
        }
        return null;
    }
}