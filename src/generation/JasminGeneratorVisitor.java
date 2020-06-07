package generation;

import jdk.jshell.execution.Util;
import parser.*;
import symbolTable.descriptor.VarDescriptor;
import symbolTable.descriptor.VarType;
import utils.Utils;

import java.io.PrintWriter;

import static utils.Utils.isArithmeticBoolean;

public class JasminGeneratorVisitor implements MyGrammarVisitor {
    private final PrintWriter writer;
    private boolean optimizeBooleanExpressions;
    private boolean loopTemplates;

    public JasminGeneratorVisitor(PrintWriter writer, boolean optimizeBooleanExpressions, boolean loopTemplates) {
        this.writer = writer;
        this.optimizeBooleanExpressions = optimizeBooleanExpressions;
        this.loopTemplates = loopTemplates;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    private String getTypeString(String type) {
        switch (type) {
            case "void":
                return "V";
            case "int":
                return "I";
            case "boolean":
                return "Z";
            case "array":
                return "[I";
            case "String[]":
                return "[Ljava/lang/String;";
            case "String":
                return "Ljava/lang/String;";
            default:
                return String.format("L%s;", type);
        }
    }

    @Override
    public Object visit(SimpleNode node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTDocument node, Object data) {
        ((SimpleNode) node.children[0]).childrenAccept(this, data);
        node.children[1].jjtAccept(this, data);
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
        writer.printf(".class public %s\n", node.identifier);
        String parent;
        if (node.parent == null) {
            parent = "java/lang/Object";
        } else {
            parent = node.parent;
        }
        writer.printf(".super %s\n\n", parent);

        node.children[0].jjtAccept(this, node.identifier); // Field declarations

        writer.println(".method public <init>()V");
        writer.println("aload_0");
        writer.printf("invokenonvirtual %s/<init>()V\n", parent);
        writer.println("return\n.end method\n");

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
        if (data != null) {
            writer.printf(".field '%s' %s\n", node.identifier, getTypeString(node.type));
        }
        return null;
    }

    @Override
    public Object visit(ASTMethod node, Object data) {
        writer.printf(".method public %s(", node.identifier);
        SimpleNode params = (SimpleNode) node.children[0];
        params.childrenAccept(this, data);
        writer.printf(")%s\n", getTypeString(node.type));

        writer.printf(".limit stack %d\n", (int) node.jjtAccept(new StackLimitCalculatorVisitor(optimizeBooleanExpressions), null));
        writer.printf(".limit locals %d\n", getLocalCount(node));

        MethodContext context = new MethodContext(node.getStMethod());
        node.children[2].jjtAccept(this, context); // Generate code for statements

        node.children[3].jjtAccept(this, context); // Generate code for return statement

        writer.println(".end method\n");
        return null;
    }

    private int getLocalCount(ASTMethod node) {
        int locals = 0;
        if (!node.isStatic)
            locals++;

        locals += node.getStMethod().getLocalsCount();
        return locals;
    }

    @Override
    public Object visit(ASTStatementList node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTReturnStatement node, Object data) {
        Expression retExpr = (Expression) node.children[0];
        retExpr.jjtAccept(this, data);
        if (retExpr.type.equals("int") || retExpr.type.equals("boolean")) {
            writer.println("ireturn");
        } else {
            writer.println("areturn");
        }
        return null;
    }

    @Override
    public Object visit(ASTMainMethod node, Object data) {
        writer.println(".method public static main([Ljava/lang/String;)V");

        writer.printf(".limit stack %d\n", (int) node.jjtAccept(new StackLimitCalculatorVisitor(optimizeBooleanExpressions), null));
        writer.printf(".limit locals %d\n", getLocalCount(node));

        MethodContext context = new MethodContext(node.getStMethod());
        node.children[2].jjtAccept(this, context);

        writer.println("return\n.end method\n");
        return null;
    }

    @Override
    public Object visit(ASTParameter node, Object data) {
        writer.print(getTypeString(node.type));
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
        ASTVarReference ref = (ASTVarReference) node.varReference;
        VarDescriptor desc = ref.desc;
        if (desc.getVarType() == VarType.FIELD) {
            writer.println("aload_0");
            node.value.jjtAccept(this, data);
            writer.printf("putfield %s/%s %s\n", desc.getClassName(), desc.getName(), getTypeString(desc.getType()));
        } else {
            if (node.value instanceof ASTSum) {
                ASTSum value = (ASTSum) node.value;
                if (value.left instanceof ASTVarReference && value.right instanceof ASTIntegerLiteral) {
                    ASTVarReference left = (ASTVarReference) value.left;
                    ASTIntegerLiteral right = (ASTIntegerLiteral) value.right;
                    if (left.identifier.equals(ref.identifier) && right.val < 128 && right.val >= 0) {
                        writer.printf("iinc %d %d\n", desc.getStackOffset(), right.val);
                        return null;
                    }
                } else if (value.right instanceof ASTVarReference && value.left instanceof ASTIntegerLiteral) {
                    ASTVarReference right = (ASTVarReference) value.right;
                    ASTIntegerLiteral left = (ASTIntegerLiteral) value.left;
                    if (right.identifier.equals(ref.identifier) && left.val < 128 && left.val >= 0) {
                        writer.printf("iinc %d %d\n", desc.getStackOffset(), left.val);
                        return null;
                    }
                }
            }
            node.value.jjtAccept(this, data);
            if (node.value.type.equals("int") || node.value.type.equals("boolean")) {
                writer.printf("istore %d\n", desc.getStackOffset());
            } else {
                writer.printf("astore %d\n", desc.getStackOffset());
            }
        }
        return null;
    }

    @Override
    public Object visit(ASTBranch node, Object data) {
        MethodContext context = (MethodContext) data;
        String[] labels = context.generateIfLabel();
        String elseLabel = labels[0];
        String endifLabel = labels[1];

        Expression condition = node.condition;
        if (optimizeBooleanExpressions && isArithmeticBoolean(condition)) {
            BooleanExpressionGeneratorVisitor gen = new BooleanExpressionGeneratorVisitor(this);
            condition.jjtAccept(gen, new BooleanGenerationContext(context, null, elseLabel));
        } else {
            if (condition instanceof ASTAnd) {
                ASTAnd expr = (ASTAnd) condition;
                expr.left.jjtAccept(this, data);
                writer.printf("ifle %s\n", elseLabel);
                expr.right.jjtAccept(this, data);
                writer.printf("ifle %s\n", elseLabel);
            } else if (condition instanceof ASTNegation) {
                ASTNegation expr = (ASTNegation) condition;
                expr.child.jjtAccept(this, data);
                writer.printf("ifne %s\n", elseLabel);
            } else if (condition instanceof ASTLessThan) {
                ASTLessThan expr = (ASTLessThan) condition;
                expr.left.jjtAccept(this, data);
                expr.right.jjtAccept(this, data);

                // see if variable is less than the other
                writer.printf("if_icmpge %s\n", elseLabel);
            } else {
                node.condition.jjtAccept(this, data);
                // ifeq makes the jump if the last value on stack is = 0 (= false)
                writer.printf("ifeq %s\n", elseLabel);
            }
        }
        node.thenStatement.jjtAccept(this, data);
        writer.printf("goto %s\n", endifLabel);
        writer.printf("%s:\n", elseLabel);
        node.elseStatement.jjtAccept(this, data);
        writer.printf("%s:\n", endifLabel);
        return null;
    }

    @Override
    public Object visit(ASTWhileLoop node, Object data) {
        MethodContext context = (MethodContext) data;
        if (!loopTemplates) {
            String loopLabel = context.generateLabel();
            String endLabel = context.generateLabel();

            writer.printf("%s:\n", loopLabel);
            Expression condition = node.condition;
            if (optimizeBooleanExpressions && isArithmeticBoolean(condition)) {
                BooleanExpressionGeneratorVisitor gen = new BooleanExpressionGeneratorVisitor(this);
                condition.jjtAccept(gen, new BooleanGenerationContext(context, null, endLabel));
            } else {
                if (condition instanceof ASTAnd) {
                    ASTAnd expr = (ASTAnd) condition;
                    expr.left.jjtAccept(this, data);
                    writer.printf("ifle %s\n", endLabel);
                    expr.right.jjtAccept(this, data);
                    writer.printf("ifle %s\n", endLabel);
                } else if (condition instanceof ASTNegation) {
                    ASTNegation expr = (ASTNegation) condition;
                    expr.child.jjtAccept(this, data);
                    writer.printf("ifne %s\n", endLabel);
                } else if (condition instanceof ASTLessThan) {
                    ASTLessThan expr = (ASTLessThan) condition;
                    expr.left.jjtAccept(this, data);
                    expr.right.jjtAccept(this, data);

                    // see if variable is less than the other
                    writer.printf("if_icmpge %s\n", endLabel);
                } else {
                    node.condition.jjtAccept(this, data);
                    writer.printf("ifeq %s\n", endLabel);
                }
            }
            node.body.jjtAccept(this, data);
            writer.printf("goto %s\n", loopLabel);
            writer.printf("%s:\n", endLabel);
            return null;
        } else {
            
            String beginLabel = context.generateLabel();
            String conditionLabel = context.generateLabel();
            

            writer.printf("goto %s\n",conditionLabel);
            writer.printf("%s:\n", beginLabel);
            node.body.jjtAccept(this, data);
            writer.printf("%s:\n", conditionLabel);
            Expression condition = node.condition;
            if (optimizeBooleanExpressions && isArithmeticBoolean(condition)) {
                String endLabel = context.generateLabel();
                BooleanExpressionGeneratorVisitor gen = new BooleanExpressionGeneratorVisitor(this);
                condition.jjtAccept(gen, new BooleanGenerationContext(context, null, endLabel));
                writer.printf("goto %s\n", beginLabel);
                writer.printf("%s:\n", endLabel);
            } else {
                if (condition instanceof ASTAnd) {
                    String skipLabel = context.generateLabel();
                    ASTAnd expr = (ASTAnd) condition;
                    expr.left.jjtAccept(this, data);
                    writer.printf("ifle %s\n", skipLabel);
                    expr.right.jjtAccept(this, data);
                    writer.printf("ifgt %s\n", beginLabel);
                    writer.printf("%s:\n", skipLabel);
                } else if (condition instanceof ASTNegation) {
                    ASTNegation expr = (ASTNegation) condition;
                    expr.child.jjtAccept(this, data);
                    writer.printf("ifeq %s\n", beginLabel);
                } else if (condition instanceof ASTLessThan) {
                    ASTLessThan expr = (ASTLessThan) condition;
                    expr.left.jjtAccept(this, data);
                    expr.right.jjtAccept(this, data);

                    // see if variable is less than the other
                    writer.printf("if_icmplt %s\n", beginLabel);
                } else {
                    node.condition.jjtAccept(this, data);
                    writer.printf("ifne %s\n", beginLabel);
                }
            }

            return null;
        }
    }

    @Override
    public Object visit(ASTArrayAssignment node, Object data) {
        if (node.arrayRef.type.equals("int")) {
            node.arrayRef.arrayRef.jjtAccept(this, data);
            node.arrayRef.index.jjtAccept(this, data);
            node.value.jjtAccept(this, data);
            writer.printf("iastore\n");
        } else if (node.arrayRef.type.equals("String")) {
            node.arrayRef.arrayRef.jjtAccept(this, data);
            node.arrayRef.index.jjtAccept(this, data);
            node.value.jjtAccept(this, data);
            writer.printf("aastore\n");
        }
        return null;
    }

    @Override
    public Object visit(ASTExprStatement node, Object data) {
        node.expression.jjtAccept(this, data);
        if (!node.expression.type.equals("void")) {
            writer.printf("pop\n");
        }
        return null;
    }

    @Override
    public Object visit(ASTArrayAccess node, Object data) {
        if (node.arrayRef.type.equals("array")) {
            node.arrayRef.jjtAccept(this, data);
            node.index.jjtAccept(this, data);
            writer.printf("iaload\n");
        } else if (node.arrayRef.type.equals("String[]")) {
            node.arrayRef.jjtAccept(this, data);
            node.index.jjtAccept(this, data);
            writer.printf("aaload\n");
        }
        return null;
    }

    @Override
    public Object visit(ASTIntegerLiteral node, Object data) {
        int val = node.val;
        if (val >= 0 && val <= 5) {
            writer.printf("iconst_%d\n", val);
        } else if (val <= 127) {
            writer.printf("bipush %d\n", val);
        } else if (val <= 32767) {
            writer.printf("sipush %d\n", val);
        } else {
            writer.printf("ldc %d\n", node.val);
        }
        return null;
    }

    @Override
    public Object visit(ASTBooleanLiteral node, Object data) {
        writer.printf("iconst_%d\n", node.val ? 1 : 0);
        return null;
    }

    @Override
    public Object visit(ASTVarReference node, Object data) {
        VarDescriptor desc = node.desc;
        if (desc.getVarType() == VarType.FIELD) {
            writer.println("aload_0");
            writer.printf("getfield %s/%s %s\n", desc.getClassName(), desc.getName(), getTypeString(desc.getType()));
        } else {
            int off = desc.getStackOffset();
            if (desc.getType().equals("int") || desc.getType().equals("boolean")) {
                writer.printf("iload%s%d\n", off <= 3 ? "_" : " ", off);
            } else {
                writer.printf("aload%s%d\n", off <= 3 ? "_" : " ", off);
            }
        }

        return null;
    }

    @Override
    public Object visit(ASTSelfReference node, Object data) {
        writer.printf("aload_0\n");
        return null;
    }

    @Override
    public Object visit(ASTArrayCreation node, Object data) {
        node.size.jjtAccept(this, data);
        writer.printf("newarray int\n");
        return null;
    }

    @Override
    public Object visit(ASTConstructorCall node, Object data) {
        writer.printf("new %s\ndup\n", node.identifier);
        writer.printf("invokespecial %s/<init>()V\n", node.identifier);
        return null;
    }

    @Override
    public Object visit(ASTNegation node, Object data) {
        MethodContext context = (MethodContext) data;
        String failLabel = context.generateLabel();
        String endLabel = context.generateLabel();
        if (optimizeBooleanExpressions) {
            BooleanExpressionGeneratorVisitor gen = new BooleanExpressionGeneratorVisitor(this);
            node.jjtAccept(gen, new BooleanGenerationContext(context, null, failLabel));
        } else {
            node.child.jjtAccept(this, data);
            writer.printf("ifgt %s\n", failLabel);
        }
        writer.printf("iconst_1\n");
        writer.printf("goto %s\n", endLabel);
        writer.printf("%s:\n", failLabel);
        writer.printf("iconst_0\n");
        writer.printf("%s:\n", endLabel);
        return null;
    }

    @Override
    public Object visit(ASTArrayLength node, Object data) {
        node.arrayRef.jjtAccept(this, data);
        writer.println("arraylength");
        return null;
    }

    @Override
    public Object visit(ASTFunctionCall node, Object data) {
        if (node.desc.isStatic()) {
            node.arguments.childrenAccept(this, data);
            writer.printf("invokestatic %s/%s(", node.desc.getClassName(), node.desc.getName());
        } else {
            node.ownerRef.jjtAccept(this, data);
            node.arguments.childrenAccept(this, data);
            writer.printf("invokevirtual %s/%s(", node.desc.getClassName(), node.desc.getName());
        }
        for (String par : node.desc.getParameters()) {
            writer.print(getTypeString(par));
        }
        writer.printf(")%s\n", getTypeString(node.desc.getReturnType()));
        return null;
    }

    @Override
    public Object visit(ASTArguments node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTAnd node, Object data) {
        MethodContext context = (MethodContext) data;
        String failLabel = context.generateLabel();
        String endLabel = context.generateLabel();

        if (optimizeBooleanExpressions) {
            BooleanExpressionGeneratorVisitor gen = new BooleanExpressionGeneratorVisitor(this);
            node.jjtAccept(gen, new BooleanGenerationContext(context, null, failLabel));
        } else {
            node.left.jjtAccept(this, data);
            writer.printf("ifle %s\n", failLabel);
            node.right.jjtAccept(this, data);
            writer.printf("ifle %s\n", failLabel);
        }
        writer.printf("iconst_1\n");
        writer.printf("goto %s\n", endLabel);
        writer.printf("%s:\n", failLabel);
        writer.printf("iconst_0\n");
        writer.printf("%s:\n", endLabel);

        return null;
    }

    @Override
    public Object visit(ASTLessThan node, Object data) {
        node.left.jjtAccept(this, data);
        node.right.jjtAccept(this, data);

        MethodContext context = (MethodContext) data;
        String lessLabel = context.generateLabel();
        String endLabel = context.generateLabel();

        // see if variable is less than the other
        writer.printf("if_icmplt %s\n", lessLabel);
        writer.printf("iconst_0\n");
        writer.printf("goto %s\n", endLabel);
        writer.printf("%s:\n", lessLabel);
        writer.printf("iconst_1\n");
        writer.printf("%s:\n", endLabel);
        // finishes with 0 if false and 1 otherwise in the stack
        return null;
    }

    @Override
    public Object visit(ASTSum node, Object data) {
        node.left.jjtAccept(this, data);
        node.right.jjtAccept(this, data);

        writer.println("iadd");
        return null;
    }

    @Override
    public Object visit(ASTSub node, Object data) {
        node.left.jjtAccept(this, data);
        node.right.jjtAccept(this, data);

        writer.println("isub");
        return null;
    }

    @Override
    public Object visit(ASTMul node, Object data) {
        node.left.jjtAccept(this, data);
        node.right.jjtAccept(this, data);

        writer.println("imul");
        return null;
    }

    @Override
    public Object visit(ASTDiv node, Object data) {
        node.left.jjtAccept(this, data);
        node.right.jjtAccept(this, data);

        writer.println("idiv");
        return null;
    }
}
