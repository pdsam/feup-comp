package semantics;

import parser.*;
import symbolTable.*;
import symbolTable.descriptor.*;
import symbolTable.exception.AlreadyDeclaredException;
import symbolTable.exception.UnknownTypeException;

import java.util.ArrayList;
import java.util.List;

public class SemanticVisitor implements MyGrammarVisitor {
    public static int numErrors = 0;

    private void logError(SimpleNode node, String msg) {
        System.err.println("Error at line: "+node.line+", column: " +node.column+". "+ msg);
        this.increment();
    }

    private void logWarning(SimpleNode node, String msg) {
        System.err.println("Warning at line: "+node.line+", column: " +node.column+". "+ msg);
    }

    private void increment(){
        numErrors++;
        if(numErrors > 10){
            System.out.println("Max number of errors reached, Semantic analyser exiting.");
            System.exit(1);
        }
    }

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
        List<String> params = new ArrayList<>();

        if (node.parameters != null) {
            for (String param : node.parameters) {
                if (!param.equals("void"))
                    params.add(param);
            }
        }

        VarDescriptor var = new VarDescriptor(node.className, node.className);
        MethodDescriptor mtd = new MethodDescriptor(node.methodName, node.returnType, params, node.className, node.isStatic);

        // If the import is only of a class then there is no method to register
        //then the visitor stops here
        if (mtd.getName() == null)
            return null;

        // Registering a method
        try {
            st.put(mtd);
        } catch(UnknownTypeException e) {
            logError(node, e.getMessage() + " '" + mtd.getReturnType() + "' as return for method " + mtd.getName());
        } catch (AlreadyDeclaredException e) {
            //When there is a duplicate import only a warning is generated
            logWarning(node, e.getMessage());
        } catch (Exception e) {
            logError(node, e.getMessage());
        }

        try {
            // The following line register a new class as a valid type
            //so no type checking is done whatsoever
            st.put(var);
        } catch(Exception e){
            logError(node, e.getMessage());
        }

        return null;
    }

    @Override
    public Object visit(ASTClass node, Object data) {
        SymbolTableDoc parentST = (SymbolTableDoc) data;
        SymbolTableClass st = node.getStClass();
        parentST.setClassName(node.identifier);
        st.setParent(parentST);

        //Registering this own class, which means there is a new type
        //so no type checking is needed
        VarDescriptor var = new VarDescriptor(node.identifier, node.identifier);
        try {
            parentST.put(var);
        } catch(Exception e){
            logError(node, e.getMessage());
        }

        // Grand children are the methods declared inside the class
        for(Node child : node.children){
            if(child instanceof ASTVarDeclarationsList)
                continue;

            SimpleNode simpleChild = (SimpleNode) child;
            if(simpleChild.children != null) {
                for(Node grandChild : simpleChild.children){
                    registerMethodNode((ASTMethod) grandChild, st);
                }
            }
        }

        // Register methods if this class extends another
        if(node.parent != null) {
            st.setSuperClass(node.parent);
            for(MethodDescriptor mtd : parentST.getClassMethods(node.parent)){
                try {
                    // Every method inside the class is referenced to the class name
                    //and they have already been type checked
                    mtd.setClassName(node.identifier);
                    st.put(mtd);
                } catch (Exception ignore) {
                    //There will be an exception if the method is already declared
                    //which means that the class is overriding the methods of the superclass
                }
            }
        }

        node.childrenAccept(this, st);
        return null;
    }

    private void registerMethodNode(ASTMethod node, SymbolTable classTable) {
        MethodDescriptor mtd = new MethodDescriptor(node.identifier, node.type, classTable.getClassName(), node.isStatic);
        SimpleNode paramList = (SimpleNode) node.jjtGetChild(0); // parameter list is the first child of the method node
        List<String> parameters = new ArrayList<>();

        for(int i = 0; i < paramList.jjtGetNumChildren(); i++){
            ASTParameter param = (ASTParameter) paramList.jjtGetChild(i);
            parameters.add(param.type);
        }

        mtd.setParameters(parameters);
        try {
            classTable.put(mtd);
        }  catch(UnknownTypeException e) {
            logError(node, e.getMessage() + " '" + mtd.getReturnType() + "' as return for method " + mtd.getName());
        } catch (Exception e) {
            logError(node, e.getMessage());
        }
    }

    @Override
    public Object visit(ASTParameter node, Object data) {
        VarDescriptor var = new VarDescriptor(node.identifier, node.type);
        SymbolTable st = (SymbolTable) data;

        try{
            st.put(var);
        } catch(UnknownTypeException e) {
            logError(node, e.getMessage() + " '" + var.getType() + "' as return for parameter " + var.getName());
        } catch(Exception e){
            logError(node, e.getMessage());
        }
        return null;
    }

    @Override
    public Object visit(ASTVar node, Object data) {
        VarDescriptor var = new VarDescriptor(node.identifier, node.type);
        SymbolTable st = (SymbolTable) data;
        try{
            st.put(var);
        } catch(UnknownTypeException e) {
            logError(node, e.getMessage() + " '" + var.getType() + "' as return for variable " + var.getName());
        } catch(Exception e){
            logError(node, e.getMessage());
        }

        return null;
    }

    @Override
    public Object visit(ASTVarReference node, Object data) {
        SymbolTable st = (SymbolTable) data;
        VarDescriptor var = null;

        try {
            var = st.variable_lookup(node.identifier);
        } catch (Exception e) {
            logError(node, e.getMessage());
        }

        if(var == null) {
            node.type = "null";
        } else {
            if (((SymbolTableMethod) st).isStaticContext() && var.isField()) {
                logError(node, String.format("%s is non static variable in static context.", node.identifier));
            }

            node.type = var.getType();
            node.desc = var;
        }
        return null;
    }

    @Override
    public Object visit(ASTFunctionCall node, Object data) {
        node.childrenAccept(this, data);
        SymbolTable st = (SymbolTable) data;
        MethodDescriptor mtd = null;

        try {
            mtd = st.method_lookup(node.identifier, node.arguments.list, node.ownerRef.type);
        } catch (Exception e) {
            logError(node, e.getMessage());
        }

        if(mtd == null) {
            System.out.println(node.identifier + ": null");
            node.type = "null";
        }
        else {
            node.type = mtd.getReturnType();
            node.desc = mtd;

        }
        return null;
    }

    @Override
    public Object visit(ASTArguments node, Object data) {
        node.childrenAccept(this, data);

        if(node.children == null)
            return null;

        for(Node child : node.children) {
            Expression exp = (Expression) child;
            node.list.add(exp.type);
        }

        return null;
    }

    @Override
    public Object visit(ASTMethod node, Object data) {
        SymbolTableMethod st = node.getStMethod();
        st.setParent((SymbolTable)data);
        st.setStaticContext(node.isStatic);
        node.childrenAccept(this, st);
        return null;
    }

    @Override
    public Object visit(ASTMainMethod node, Object data) {
        SymbolTableMethod st = node.getStMethod();
        st.setParent((SymbolTable)data);
        st.setStaticContext(true);
        node.childrenAccept(this, st);
        return null;
    }

    @Override
    public Object visit(ASTMainContainer node, Object data) {
        node.childrenAccept(this,data);
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
        SymbolTable st = (SymbolTable) data;
        ASTVarReference varRef = (ASTVarReference) node.varReference;
        VarDescriptor var = null;

        node.childrenAccept(this, data);

        if(!varRef.type.equals(node.value.type)){
            logError(node, "Types do not match: was expecting '" + varRef.type + "' but got '" + node.value.type + '\'');
        }

        try {
            var = st.variable_lookup(varRef.identifier);
        } catch (Exception e) {
            // All errors are already logged in ASTVarReference visitor
            //so here we just ignore them
        }

        if(var != null)
            var.initialize();

        return null;
    }

    @Override
    public Object visit(ASTBranch node, Object data) {
        node.childrenAccept(this, data);
        if(!node.condition.type.equals("boolean")) {
            logError(node, "If condition must evaluate to boolean");
        }

        //TODO: check if variables are initialized inside one of the statements
        return null;
    }

    @Override
    public Object visit(ASTWhileLoop node, Object data) {
        node.childrenAccept(this, data);

        if(!node.condition.type.equals("boolean")) {
            logError(node, "While condition must evaluate to boolean");
        }

        //TODO: check if variables are initialized inside the statement
        return null;
    }

    @Override
    public Object visit(ASTArrayAssignment node, Object data) {
        //TODO: check if value being assigned is int
        node.childrenAccept(this, data);

        if(node.arrayRef.type.equals("array") && !node.value.type.equals("int")) {
            logError(node,"Types do not match: was expecting 'int' but got '" + node.value.type + '\'');
        } else if (node.arrayRef.type.equals("String[]") && !node.value.type.equals("String")) {
            logError(node,"Types do not match: was expecting 'String' but got '" + node.value.type + '\'');
        }

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
        ASTVarReference arr = (ASTVarReference) node.arrayRef;
        SymbolTable st = (SymbolTable) data;

        // Check if the variable is of type int[] or String[] (for main parameter)
        if(!arr.type.equals("array") && !arr.type.equals("String[]")) {
            logError(node, "Variable '" + arr.identifier + "' is not an array");
        }

        if(!node.index.type.equals("int")) {
            logError(node, "Index expression is not of type int");
        }

        try {
            st.variable_lookup(arr.identifier);
        } catch (Exception e) {
            logError(node, e.getMessage());
        }

        return null;
    }

    @Override
    public Object visit(ASTIntegerLiteral node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTBooleanLiteral node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTSelfReference node, Object data) {
        SymbolTableMethod st = (SymbolTableMethod) data;
        if (st.isStaticContext()) {
            logError(node, "Cannot use non static variable in static context.");
        }

        node.childrenAccept(this, data);

        node.type = st.getClassName();
        return null;
    }

    @Override
    public Object visit(ASTArrayCreation node, Object data) {
        node.childrenAccept(this, data);

        if(!node.size.type.equals("int")){
            logError(node, "Array size must be of type int");
        }

        node.type = "array";
        return null;
    }

    @Override
    public Object visit(ASTConstructorCall node, Object data) {
        SymbolTable st = (SymbolTable) data;

        try {
            st.variable_lookup(node.identifier);
        } catch (Exception e) {
            logError(node, "Unknown class '" + node.identifier + '\'');
        }

        node.type = node.identifier;
        return null;
    }

    @Override
    public Object visit(ASTArrayLength node, Object data) {
        node.childrenAccept(this, data);

        if(!node.arrayRef.type.equals("array") && !node.arrayRef.type.equals("String[]")) {
            logError(node, "Length property does not exist for type " + node.arrayRef.type);
        }

        node.type = "int";
//        node.arrayRef.jjtAccept(this, data);
//        if (!node.arrayRef.type.equals("array")) {
//            logError(node, "Not an array.");
//        }
        return null;
    }

    @Override
    public Object visit(ASTNegation node, Object data) {
        node.childrenAccept(this, data);

        if(node.child.type != "boolean") {
            logError(node, "! operator must be applied to a boolean expression.");
        }

        node.type = "boolean";
        return null;
    }

    private void checkOperandsTypes(BinOpExpression expr, String type) {
        if(!expr.left.type.equals(type)) {
            logError(expr, "Left side of && operator must be of type boolean.");
        } else if(!expr.right.type.equals(type)) {
            logError(expr, "Right side of && operator must be of type boolean.");
        }
    }

    @Override
    public Object visit(ASTAnd node, Object data) {
        node.childrenAccept(this, data);
        checkOperandsTypes(node, "boolean");
        node.type = "boolean";
        return null;
    }

    @Override
    public Object visit(ASTLessThan node, Object data) {
        node.childrenAccept(this, data);
        checkOperandsTypes(node, "int");
        node.type = "boolean";
        return null;
    }

    @Override
    public Object visit(ASTSum node, Object data) {
        node.childrenAccept(this, data);
        checkOperandsTypes(node, "int");
        node.type = "int";
        return null;
    }

    @Override
    public Object visit(ASTSub node, Object data) {
        node.childrenAccept(this, data);
        checkOperandsTypes(node, "int");
        node.type = "int";
        return null;
    }

    @Override
    public Object visit(ASTMul node, Object data) {
        node.childrenAccept(this, data);
        checkOperandsTypes(node, "int");
        node.type = "int";
        return null;
    }

    @Override
    public Object visit(ASTDiv node, Object data) {
        node.childrenAccept(this, data);
        checkOperandsTypes(node, "int");
        node.type = "int";
        return null;
    }
}
