package controlFlowAnalysis;

import parser.*;
import symbolTable.SymbolTable;
import symbolTable.descriptor.VarDescriptor;
import symbolTable.exception.SemanticException;

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
        ControlFlowAnalysis.liveness(cfdata);

        return null;
    }

    @Override
    public Object visit(ASTMainMethod node, Object data) {
        SymbolTable st = node.getStMethod();
        ControlFlowData cfdata = new ControlFlowData(st);

        //All nodes of the CFG will be filled with succ[], pred[], use[] and def[]
        node.childrenAccept(this, cfdata);

        //Afterwards, we can run the Liveness algorithm
        ControlFlowAnalysis.liveness(cfdata);

        return null;
    }

    @Override
    public Object visit(ASTStatementList node, Object data) {
        node.childrenAccept(this, data);
        ControlFlowData cfdata = (ControlFlowData) data;
        ControlFlowGraph cfg = cfdata.getGraph();

        for (int i = 0; i < node.jjtGetNumChildren() - 1; i++) {
            Statement current = (Statement) node.jjtGetChild(i);
            Statement next = (Statement) node.jjtGetChild(i+1);

            cfg.addSuccessor(current.cfNode, next.cfNode);
        }

        return null;
    }

    @Override
    public Object visit(ASTScopedStatementList node, Object data) {
        node.childrenAccept(this, data);
        ControlFlowData cfdata = (ControlFlowData) data;
        ControlFlowGraph cfg = cfdata.getGraph();

        for (int i = 0; i < node.jjtGetNumChildren() - 1; i++) {
            Statement current = (Statement) node.jjtGetChild(i);
            Statement next = (Statement) node.jjtGetChild(i+1);

            cfg.addSuccessor(current.cfNode, next.cfNode);
        }

        return null;
    }

    @Override
    public Object visit(ASTReturnStatement node, Object data) {
        ControlFlowData cfdata = (ControlFlowData) data;
        ControlFlowNode thisNode = new ControlFlowNode();
        cfdata.setNode(thisNode);

        //This will fill use[] and def[] properties of this node
        node.childrenAccept(this, cfdata);
        //Adding the constructed node to the graph
        cfdata.getGraph().addNode(thisNode);
        node.cfNode = thisNode;
        return null;
    }

    @Override
    public Object visit(ASTBranch node, Object data) {
        ControlFlowData cfdata = (ControlFlowData) data;
        ControlFlowGraph cfg = cfdata.getGraph();
        ControlFlowNode thisNode = new ControlFlowNode();
        cfdata.setNode(thisNode);

        //This will fill use[] and def[] properties of this node
        node.condition.jjtAccept(this, cfdata);
        //Adding the constructed node to the graph
        cfg.addNode(thisNode);
        node.cfNode = thisNode;

        //These will create the successors nodes
        node.thenStatement.jjtAccept(this, cfdata);
        ControlFlowNode thenNode = cfdata.getNode();
        node.elseStatement.jjtAccept(this, cfdata);
        ControlFlowNode elseNode = cfdata.getNode();

        cfg.addSuccessor(thisNode, thenNode);
        cfg.addSuccessor(thisNode, elseNode);

        return null;
    }

    @Override
    public Object visit(ASTWhileLoop node, Object data) {
        ControlFlowData cfdata = (ControlFlowData) data;
        ControlFlowGraph cfg = cfdata.getGraph();
        ControlFlowNode thisNode = new ControlFlowNode();
        cfdata.setNode(thisNode);

        //This will fill use[] and def[] properties of this node
        node.condition.jjtAccept(this, cfdata);
        //Adding the constructed node to the graph
        cfg.addNode(thisNode);
        node.cfNode = thisNode;

        //This will create the successor node
        node.body.jjtAccept(this, cfdata);
        cfg.addSuccessor(thisNode, cfdata.getNode());

        return null;
    }

    @Override
    public Object visit(ASTExprStatement node, Object data) {
        ControlFlowData cfdata = (ControlFlowData) data;
        ControlFlowNode thisNode = new ControlFlowNode();
        cfdata.setNode(thisNode);

        //This will fill use[] and def[] properties of this node
        node.childrenAccept(this, data);
        //Adding the constructed node to the graph
        cfdata.getGraph().addNode(thisNode);
        node.cfNode = thisNode;
        return null;
    }

    private void varDef(String varName, ControlFlowData cfdata) {
        ControlFlowNode thisNode = cfdata.getNode();

        try {
            VarDescriptor varDescriptor = cfdata.getSymbolTable().variable_lookup(varName);
            thisNode.addDef(varDescriptor);
        } catch (SemanticException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object visit(ASTArrayAssignment node, Object data) {
        ControlFlowData cfdata = (ControlFlowData) data;
        ControlFlowGraph cfg = cfdata.getGraph();
        ControlFlowNode thisNode = new ControlFlowNode();
        cfdata.setNode(thisNode);

        //This will fill def[] properties of this node
        ASTVarReference var = (ASTVarReference) node.arrayRef.arrayRef;
        varDef(var.identifier, cfdata);
        //This will fill use[] properties of this node
        node.value.jjtAccept(this, cfdata);
        //Adding the constructed node to the graph
        cfg.addNode(thisNode);
        node.cfNode = thisNode;
        return null;
    }

    @Override
    public Object visit(ASTAssignment node, Object data) {
        ControlFlowData cfdata = (ControlFlowData) data;
        ControlFlowGraph cfg = cfdata.getGraph();
        ControlFlowNode thisNode = new ControlFlowNode();
        cfdata.setNode(thisNode);

        //This will fill def[] properties of this node
        ASTVarReference var = (ASTVarReference) node.varReference;
        varDef(var.identifier, cfdata);
        //This will fill use[] properties of this node
        node.value.jjtAccept(this, cfdata);
        //Adding the constructed node to the graph
        cfg.addNode(thisNode);
        node.cfNode = thisNode;
        return null;
    }

    private void varUse(String varName, ControlFlowData cfdata) {
        ControlFlowNode thisNode = cfdata.getNode();

        try {
            VarDescriptor varDescriptor = cfdata.getSymbolTable().variable_lookup(varName);
            thisNode.addUse(varDescriptor);
        } catch (SemanticException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object visit(ASTVarReference node, Object data) {
        varUse(node.identifier, (ControlFlowData) data);

        return null;
    }

    @Override
    public Object visit(ASTParameterList node, Object data) {
        //TODO: check later if we can optimize parameters alocation
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTParameter node, Object data) {
        //varDef(node.identifier, (ControlFlowData) data);
        //TODO: check later if we can optimize parameters alocation
        //def var
        return null;
    }

    @Override
    public Object visit(ASTSelfReference node, Object data) {
        //TODO: check later if we can optimize 'this' alocation
        //use var
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
