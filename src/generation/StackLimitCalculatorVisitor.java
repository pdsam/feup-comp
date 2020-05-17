package generation;

import parser.*;
import symbolTable.descriptor.VarType;

public class StackLimitCalculatorVisitor implements MyGrammarVisitor {

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
        return Integer.max(3, Integer.max(2 + indexVal, 1 + valueVal));
    }

    @Override
    public Object visit(ASTExprStatement node, Object data) {
        return Integer.max(0, (int) node.expression.jjtAccept(this, data));
    }

    @Override
    public Object visit(ASTArrayAccess node, Object data) {
        return Integer.max(2, 1 + (int) node.index.jjtAccept(this, data));
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
        return Integer.max(1, (int) node.child.jjtAccept(this, data));
    }

    @Override
    public Object visit(ASTArrayLength node, Object data) {
        return Integer.max(1, (int) node.arrayRef.jjtAccept(this, data));
    }

    @Override
    public Object visit(ASTFunctionCall node, Object data) {
        int current = 0;

        current = Integer.max(current, (int) node.ownerRef.jjtAccept(this, data));

        current = Integer.max(current, 1 + (int) node.arguments.jjtAccept(this, data));

        if (!node.desc.isStatic()) {
            current++;
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
        return max;
    }

    private int BinOpSize(BinOpExpression node, Object data) {
        return Integer.max(2, 1 + Integer.max((int) node.left.jjtAccept(this, data), (int) node.right.jjtAccept(this, data)));
    }

    @Override
    public Object visit(ASTAnd node, Object data) {
        return BinOpSize(node, data);
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