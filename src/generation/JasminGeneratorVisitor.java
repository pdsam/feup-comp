package generation;

import parser.*;

import java.io.PrintWriter;

public class JasminGeneratorVisitor implements MyGrammarVisitor {
    private PrintWriter writer;

    public JasminGeneratorVisitor(PrintWriter writer) {
        this.writer = writer;
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
            default:
                return String.format("L%s;", type); //TODO get proper class signature
        }
    }

    @Override
    public Object visit(SimpleNode node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTDocument node, Object data) {
        System.out.println("document");
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
        System.out.println("class");
        writer.printf(".class public %s\n", node.identifier);
        String parent;
        if (node.parent == null) {
            parent = "java/lang/Object";
        } else {
            parent = node.parent;
        }
        writer.printf(".super %s\n\n", parent);

        node.children[0].jjtAccept(this, data); //Field declarations

        writer.println(".method public<init>()V");
        writer.println("aload_0");
        writer.printf("invokenonvirtual %s/<init>()V\n", parent);
        writer.println("return\n.end method\n");

        node.children[1].jjtAccept(this, data); //Method declarations
        node.children[2].jjtAccept(this, data); //Main declaration
        node.children[3].jjtAccept(this, data); //Method declarations

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
        System.out.println("method");
        writer.printf(".method public %s(", node.identifier);
        SimpleNode params = (SimpleNode) node.children[0];
        params.childrenAccept(this, data);
        writer.printf(")%s\n", getTypeString(node.type));
        writer.println(".limit stack 99\n.limit locals 99\n");

        node.children[2].jjtAccept(this, data); //Generate code for statements

        node.children[3].jjtAccept(this, data); //Generate code for return statement

        writer.println(".end method\n");
        return null;
    }

    @Override
    public Object visit(ASTStatementList node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTReturnStatement node, Object data) {
        //TODO hope every type is set
        /*Expression retExpr = (Expression) node.children[0];
        retExpr.jjtAccept(this, data);
        if (retExpr.type.equals("int") || retExpr.type.equals("boolean")) {
            writer.println("ireturn");
        } else {
            writer.println("areturn");
        }*/
        return null;
    }

    @Override
    public Object visit(ASTMainMethod node, Object data) {
        writer.println(".method public static main([Ljava/lang/String;)V");
        writer.println(".limit stack 99\n.limit locals 99\n");

        writer.println(".end method\n");
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
        //TODO hope every type is set
        /*
        //TODO instructions to store in class field

        //into local variable
        node.varReference.jjtAccept(this, data);
        node.value.jjtAccept(this, data);
        int temp = 0;
        if (node.value.type.equals("int") || node.value.type.equals("boolean")) {
            writer.printf("istore %d\n", temp);
        } else {
            writer.printf("astore %d\n", temp);
        }*/
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
        node.arrayRef.arrayRef.jjtAccept(this, data);
        node.arrayRef.index.jjtAccept(this, data);
        node.value.jjtAccept(this, data);
        writer.printf("iastore\n");
        return null;
    }

    @Override
    public Object visit(ASTExprStatement node, Object data) {
        node.expression.jjtAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTArrayAccess node, Object data) {
        node.arrayRef.jjtAccept(this, data);
        node.index.jjtAccept(this, data);
        writer.printf("iaload\n");
        return null;
    }

    @Override
    public Object visit(ASTIntegerLiteral node, Object data) {
        writer.printf("ldc %d\n", node.val);

        return null;
    }

    @Override
    public Object visit(ASTBooleanLiteral node, Object data) {
        writer.printf("iconst_%d\n", node.val ? 1 : 0);
        return null;
    }

    @Override
    public Object visit(ASTVarReference node, Object data) {
        //TODO
        return null;
    }

    @Override
    public Object visit(ASTSelfReference node, Object data) {
        //TODO
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
        //TODO get proper class name
        writer.printf("new %s\ndup\n", node.identifier);
        writer.printf("invokespecial %s/<init>()V\n", node.identifier);
        return null;
    }

    @Override
    public Object visit(ASTNegation node, Object data) {
        //TODO
        return null;
    }

    @Override
    public Object visit(ASTArrayLength node, Object data) {
        //TODO
        return null;
    }

    @Override
    public Object visit(ASTFunctionCall node, Object data) {
        //TODO check if static method call
        node.ownerRef.jjtAccept(this, data);
        node.arguments.childrenAccept(this, data);
        /*
        * TODO get descriptor of method to extract parameter info and
        * return type
        */
        writer.printf("invokevirtual %s/%s(", node.ownerRef.type, node.identifier);

        writer.printf(")%s\n", getTypeString("void"));
        return null;
    }

    @Override
    public Object visit(ASTArguments node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTAnd node, Object data) {
        //TODO get left and right into the stack
        //TODO write and instruction
        return null;
    }

    @Override
    public Object visit(ASTLessThan node, Object data) {
        //TODO get left and right into the stack
        //TODO write less than instruction
        return null;
    }

    @Override
    public Object visit(ASTSum node, Object data) {
        node.left.jjtAccept(this, data);
        node.right.jjtAccept(this, data);

        writer.println("isum");
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
