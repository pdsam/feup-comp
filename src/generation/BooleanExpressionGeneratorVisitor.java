package generation;

import parser.*;
import utils.Utils;

import java.io.PrintWriter;

public class BooleanExpressionGeneratorVisitor implements MyGrammarVisitor {

    private JasminGeneratorVisitor generator;
    private final PrintWriter writer;

    public BooleanExpressionGeneratorVisitor(JasminGeneratorVisitor generator) {
        this.generator = generator;
        this.writer = generator.getWriter();
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
        BooleanGenerationContext ctx = (BooleanGenerationContext) data;
        return node.jjtAccept(generator, ctx.methodcontext);
    }

    @Override
    public Object visit(ASTIntegerLiteral node, Object data) {
        BooleanGenerationContext ctx = (BooleanGenerationContext) data;
        return node.jjtAccept(generator, ctx.methodcontext);
    }

    @Override
    public Object visit(ASTBooleanLiteral node, Object data) {
        BooleanGenerationContext ctx = (BooleanGenerationContext) data;
        return node.jjtAccept(generator, ctx.methodcontext);
    }

    @Override
    public Object visit(ASTVarReference node, Object data) {
        BooleanGenerationContext ctx = (BooleanGenerationContext) data;
        return node.jjtAccept(generator, ctx.methodcontext);
    }

    @Override
    public Object visit(ASTSelfReference node, Object data) {
        BooleanGenerationContext ctx = (BooleanGenerationContext) data;
        return node.jjtAccept(generator, ctx.methodcontext);
    }

    @Override
    public Object visit(ASTArrayCreation node, Object data) {
        BooleanGenerationContext ctx = (BooleanGenerationContext) data;
        return node.jjtAccept(generator, ctx.methodcontext);
    }

    @Override
    public Object visit(ASTConstructorCall node, Object data) {
        BooleanGenerationContext ctx = (BooleanGenerationContext) data;
        return node.jjtAccept(generator, ctx.methodcontext);
    }

    @Override
    public Object visit(ASTNegation node, Object data) {
        BooleanGenerationContext ctx = (BooleanGenerationContext) data;
        String success = ctx.methodcontext.generateLabel();
        BooleanGenerationContext newCtx = new BooleanGenerationContext(ctx.methodcontext, ctx.failLabel, success);

        node.child.jjtAccept(this, newCtx);
        if (!(Utils.isArithmeticBoolean(node.child))) {
            writer.printf("ifgt %s\n", ctx.failLabel);
        } else {
            writer.printf("goto %s\n", ctx.failLabel);
            writer.printf("%s: \n", success);
        }
        return null;
    }

    @Override
    public Object visit(ASTArrayLength node, Object data) {
        BooleanGenerationContext ctx = (BooleanGenerationContext) data;
        return node.jjtAccept(generator, ctx.methodcontext);
    }

    @Override
    public Object visit(ASTFunctionCall node, Object data) {
        BooleanGenerationContext ctx = (BooleanGenerationContext) data;
        return node.jjtAccept(generator, ctx.methodcontext);
    }

    @Override
    public Object visit(ASTArguments node, Object data) {
        BooleanGenerationContext ctx = (BooleanGenerationContext) data;
        return node.jjtAccept(generator, ctx.methodcontext);
    }

    @Override
    public Object visit(ASTAnd node, Object data) {
        BooleanGenerationContext ctx = (BooleanGenerationContext) data;
        node.left.jjtAccept(this, ctx);
        if (!(Utils.isArithmeticBoolean(node.left))) {
            writer.printf("ifle %s\n", ctx.failLabel);
        }
        node.right.jjtAccept(this, ctx);
        if (!(Utils.isArithmeticBoolean(node.right))) {
            writer.printf("ifle %s\n", ctx.failLabel);
        }
        return null;
    }

    @Override
    public Object visit(ASTLessThan node, Object data) {
        BooleanGenerationContext ctx = (BooleanGenerationContext) data;
        node.left.jjtAccept(this, ctx);
        node.right.jjtAccept(this, ctx);

        writer.printf("if_icmpge %s\n",  ctx.failLabel);
        return null;
    }

    @Override
    public Object visit(ASTSum node, Object data) {
        BooleanGenerationContext ctx = (BooleanGenerationContext) data;
        return node.jjtAccept(generator, ctx.methodcontext);
    }

    @Override
    public Object visit(ASTSub node, Object data) {
        BooleanGenerationContext ctx = (BooleanGenerationContext) data;
        return node.jjtAccept(generator, ctx.methodcontext);
    }

    @Override
    public Object visit(ASTMul node, Object data) {
        BooleanGenerationContext ctx = (BooleanGenerationContext) data;
        return node.jjtAccept(generator, ctx.methodcontext);
    }

    @Override
    public Object visit(ASTDiv node, Object data) {
        BooleanGenerationContext ctx = (BooleanGenerationContext) data;
        return node.jjtAccept(generator, ctx.methodcontext);
    }
}
