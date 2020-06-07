package generation;

import jdk.jshell.execution.Util;
import parser.*;
import symbolTable.descriptor.VarType;
import utils.Utils;

public class StackLimitCalculatorVisitor implements MyGrammarVisitor {

    private boolean optimizeBooleanExpressions;

    public StackLimitCalculatorVisitor(boolean optimizeBooleanExpressions) {
        this.optimizeBooleanExpressions = optimizeBooleanExpressions;
    }

    private int getMaxOfChildren(SimpleNode node, Object data) {
        int max = 0;
        if (node.children != null) {
            for (int i = 0; i < node.children.length; i++) {
                int num = (int) node.children[i].jjtAccept(this, data);
                if (num > max) {
                    max = num;
                }
            }
        }
        return max;
    }

    @Override
    public Object visit(SimpleNode node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTDocument node, Object data) {
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
        return null;
    }

    @Override
    public Object visit(ASTVarDeclarationsList node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTMethodList node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTMainContainer node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTVar node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTMethod node, Object data) {
        return Integer.max((int) node.children[2].jjtAccept(this, data),
                (int) node.children[3].jjtAccept(this, data));
    }

    @Override
    public Object visit(ASTStatementList node, Object data) {
        return getMaxOfChildren(node, data);
    }

    @Override
    public Object visit(ASTReturnStatement node, Object data) {
        return Integer.max(1, (int) node.expr.jjtAccept(this, data));
    }

    @Override
    public Object visit(ASTMainMethod node, Object data) {
        return node.children[2].jjtAccept(this, data);
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
        return getMaxOfChildren(node, data);
    }

    @Override
    public Object visit(ASTAssignment node, Object data) {
        ASTVarReference ref = (ASTVarReference) node.varReference;
        if (ref.desc.getVarType() == VarType.FIELD) {
            return Integer.max(2, 1 + (int) node.value.jjtAccept(this, data));
        } else {
            return Integer.max(1, (int) node.value.jjtAccept(this, data));
        }
    }

    @Override
    public Object visit(ASTBranch node, Object data) {
        int conditionValue = (int) node.condition.jjtAccept(this, data);
        int thenValue = (int) node.thenStatement.jjtAccept(this, data);
        int elseValue = (int) node.elseStatement.jjtAccept(this, data);

        return Integer.max(conditionValue, Integer.max(thenValue, elseValue));
    }

    @Override
    public Object visit(ASTWhileLoop node, Object data) {
        int conditionValue = (int) node.condition.jjtAccept(this, data);
        int bodyValue = (int) node.body.jjtAccept(this, data);

        return Integer.max(conditionValue, bodyValue);
    }

    @Override
    public Object visit(ASTArrayAssignment node, Object data) {
        int indexVal = (int) node.arrayRef.index.jjtAccept(this, data);
        int valueVal = (int) node.value.jjtAccept(this, data);
        return Integer.max(3, Integer.max(1 + indexVal, 2 + valueVal));
    }

    @Override
    public Object visit(ASTExprStatement node, Object data) {
        return node.expression.jjtAccept(this, data);
    }

    @Override
    public Object visit(ASTArrayAccess node, Object data) {
        int arrayRefValue = (int) node.arrayRef.jjtAccept(this, data);
        int indexValue = (int) node.index.jjtAccept(this, data);
        return Integer.max(2, Integer.max(arrayRefValue, 1+indexValue));
    }

    @Override
    public Object visit(ASTIntegerLiteral node, Object data) {
        return 1;
    }

    @Override
    public Object visit(ASTBooleanLiteral node, Object data) {
        return 1;
    }

    @Override
    public Object visit(ASTVarReference node, Object data) {
        int current;
        if (node.desc.getVarType() == VarType.FIELD) {
            current = 1;
        } else {
            current = 0;
        }

        return current;
    }

    @Override
    public Object visit(ASTSelfReference node, Object data) {
        return 1;
    }

    @Override
    public Object visit(ASTArrayCreation node, Object data) {
        return Integer.max(1, (int) node.size.jjtAccept(this, data));
    }

    @Override
    public Object visit(ASTConstructorCall node, Object data) {
        return 2;
    }

    @Override
    public Object visit(ASTNegation node, Object data) {
        if (optimizeBooleanExpressions && Utils.isArithmeticBoolean(node.child)) {
            return node.child.jjtAccept(this, data);
        } else {
            return Integer.max(1, (int) node.child.jjtAccept(this, data));
        }
    }

    @Override
    public Object visit(ASTArrayLength node, Object data) {
        return Integer.max(1, (int) node.arrayRef.jjtAccept(this, data));
    }

    @Override
    public Object visit(ASTFunctionCall node, Object data) {
        int current = 0;

        if (node.desc.isStatic()) {
            current = Integer.max(current, (int) node.arguments.jjtAccept(this, data));
        } else {
            current = Integer.max(current, (int) node.ownerRef.jjtAccept(this, data));
            current = Integer.max(current, 1 + (int) node.arguments.jjtAccept(this, data));
        }

        return current;
    }

    @Override
    public Object visit(ASTArguments node, Object data) {
        int max = 0;
        if (node.children != null) {
            for (int i = 0; i < node.children.length; i++) {
                int num = (int) node.children[i].jjtAccept(this, data) + i;
                if (num > max) {
                    max = num;
                }
            }
        }
        return Integer.max(max, node.jjtGetNumChildren());
    }

    private int BinOpSize(BinOpExpression node, Object data) {
        return Integer.max(2, Integer.max((int) node.left.jjtAccept(this, data), 1 + (int) node.right.jjtAccept(this, data)));
    }

    @Override
    public Object visit(ASTAnd node, Object data) {
        if (optimizeBooleanExpressions) {
            int i = (!Utils.isArithmeticBoolean(node.left) || !Utils.isArithmeticBoolean(node.right)) ? 1 : 0;
            return Integer.max(i, Integer.max((int) node.left.jjtAccept(this, data), (int) node.right.jjtAccept(this, data)));
        } else {
            return Integer.max(1, Integer.max((int) node.left.jjtAccept(this, data), (int) node.right.jjtAccept(this, data)));
        }
    }

    @Override
    public Object visit(ASTLessThan node, Object data) {
        return BinOpSize(node, data);
    }

    @Override
    public Object visit(ASTSum node, Object data) {
        return BinOpSize(node, data);
    }

    @Override
    public Object visit(ASTSub node, Object data) {
        return BinOpSize(node, data);
    }

    @Override
    public Object visit(ASTMul node, Object data) {
        return BinOpSize(node, data);
    }

    @Override
    public Object visit(ASTDiv node, Object data) {
        return BinOpSize(node, data);
    }
}
