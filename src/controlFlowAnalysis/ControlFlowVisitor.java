package controlFlowAnalysis;

import parser.*;
import symbolTable.SymbolTable;
import symbolTable.descriptor.VarDescriptor;
import symbolTable.descriptor.VarType;
import symbolTable.exception.SemanticException;

import java.util.LinkedList;
import java.util.List;

public class ControlFlowVisitor implements MyGrammarVisitor {
    private List<ControlFlowGraph> graphList = new LinkedList<>();

    @Override
    public Object visit(SimpleNode node, Object data) {
        return null;
    }

    @Override
    public List<ControlFlowGraph> visit(ASTDocument node, Object data) {
        node.childrenAccept(this, data);

        return graphList;
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
        // Getting the ParametersList children (the parameters)
        int numParams = node.jjtGetChild(0).jjtGetNumChildren();
        if(!node.isStatic) numParams++;

        ControlFlowData cfdata = new ControlFlowData(st, node.descriptor, numParams);
        Statement lastStatement = null;

        //All nodes of the CFG will be filled with succ[], pred[], use[] and def[]
        for(int i = 0; i < node.jjtGetNumChildren(); i++) {
            Node child = node.jjtGetChild(i);
            child.jjtAccept(this, cfdata);

            //If there is a StatementList, then we save the last statement node
            //in order to register the ReturnStatement as its successor
            if(child instanceof ASTStatementList && child.jjtGetNumChildren() > 0) {
                lastStatement = (Statement) child.jjtGetChild(child.jjtGetNumChildren() - 1);
            }
        }

        //If there was a StatementList, then we register the last statement as the
        //Return's predecessor
        if(lastStatement != null){
            if(lastStatement instanceof ASTBranch)
                addIfSuccessor((ASTBranch) lastStatement, cfdata.getNode(), cfdata.getGraph());
            else
                cfdata.getGraph().addSuccessor(lastStatement.cfNode, cfdata.getNode());
        }

        graphList.add(cfdata.getGraph());

        return null;
    }

    @Override
    public Object visit(ASTMainMethod node, Object data) {
        SymbolTable st = node.getStMethod();
        //The initial stack offset is 1 due to the args parameter
        ControlFlowData cfdata = new ControlFlowData(st, node.descriptor, 1);

        //All nodes of the CFG will be filled with succ[], pred[], use[] and def[]
        node.childrenAccept(this, cfdata);

        graphList.add(cfdata.getGraph());

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

            if(current instanceof ASTBranch)
                addIfSuccessor((ASTBranch) current, next.cfNode, cfg);
            else
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

            if(current instanceof ASTBranch)
                addIfSuccessor((ASTBranch) current, next.cfNode, cfg);
            else
                cfg.addSuccessor(current.cfNode, next.cfNode);
        }

        return null;
    }

    private void addIfSuccessor(ASTBranch node, ControlFlowNode next, ControlFlowGraph cfg) {
        Statement thenStmt;
        Statement elseStmt;

        if(node.thenStatement instanceof ASTScopedStatementList && node.thenStatement.jjtGetNumChildren() > 0) {
            thenStmt = (Statement) node.thenStatement.jjtGetChild(node.thenStatement.jjtGetNumChildren() - 1);
        } else {
            thenStmt = node.thenStatement;
        }

        if(node.elseStatement instanceof ASTScopedStatementList && node.elseStatement.jjtGetNumChildren() > 0) {
            elseStmt = (Statement) node.elseStatement.jjtGetChild(node.elseStatement.jjtGetNumChildren() - 1);
        } else {
            elseStmt = node.elseStatement;
        }

        //Adding the next statement (after the IF clause) as the branches' successors
        //but only if they are not empty
        if(thenStmt.cfNode != null) {
            if(thenStmt instanceof ASTBranch)
                addIfSuccessor((ASTBranch) thenStmt, next, cfg);
            else
                cfg.addSuccessor(thenStmt.cfNode, next);
        }
        if(elseStmt.cfNode != null) {
            if(elseStmt instanceof ASTBranch)
                addIfSuccessor((ASTBranch) elseStmt, next, cfg);
            else
                cfg.addSuccessor(elseStmt.cfNode, next);
        }

        //If at least one of the branches is empty, then the IF statement can directly jump to
        //the following statement in the list
        if(thenStmt.cfNode == null || elseStmt.cfNode == null) {
            cfg.addSuccessor(node.cfNode, next);
        }
    }

    @Override
    public Object visit(ASTReturnStatement node, Object data) {
        ControlFlowData cfdata = (ControlFlowData) data;
        ControlFlowNode thisNode = new ControlFlowNode();
        thisNode.setNode(node);
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
        thisNode.setNode(node);
        cfdata.setNode(thisNode);

        //This will fill use[] and def[] properties of this node
        node.condition.jjtAccept(this, cfdata);
        //Adding the constructed node to the graph
        cfg.addNode(thisNode);
        node.cfNode = thisNode;

        //These will create the successors nodes
        node.thenStatement.jjtAccept(this, cfdata);
        node.elseStatement.jjtAccept(this, cfdata);

        //This node has the first statement of the thenStatement and the elseStatement as its successors
        //The first statement may either be the first on a ScopedStatementList
        //or the only Statement in the each statement branch
        Statement thenStmt;
        Statement elseStmt;
        if(node.thenStatement instanceof ASTScopedStatementList && node.thenStatement.jjtGetNumChildren() > 0) {
            thenStmt = (Statement) node.thenStatement.jjtGetChild(0);
        } else {
            thenStmt = node.thenStatement;
        }

        if(node.elseStatement instanceof ASTScopedStatementList && node.elseStatement.jjtGetNumChildren() > 0) {
            elseStmt = (Statement) node.elseStatement.jjtGetChild(0);
        } else {
            elseStmt = node.elseStatement;
        }

        //Adding the first ThenStatement and first ElseStatement as this node's successors
        //but only if they are not empty
        if(thenStmt.cfNode != null) {
            cfg.addSuccessor(thisNode, thenStmt.cfNode);
        }
        if(elseStmt.cfNode != null) {
            cfg.addSuccessor(thisNode, elseStmt.cfNode);
        }

        return null;
    }

    @Override
    public Object visit(ASTWhileLoop node, Object data) {
        ControlFlowData cfdata = (ControlFlowData) data;
        ControlFlowGraph cfg = cfdata.getGraph();
        ControlFlowNode thisNode = new ControlFlowNode();
        thisNode.setNode(node);
        cfdata.setNode(thisNode);

        //This will fill use[] and def[] properties of this node
        node.condition.jjtAccept(this, cfdata);
        //Adding the constructed node to the graph
        cfg.addNode(thisNode);
        node.cfNode = thisNode;

        //This will create the successor node
        node.body.jjtAccept(this, cfdata);

        //This node has the first statement of the body as its successor
        //The first statement may either be the first on a ScopedStatementList
        //or the only Statement in the body
        Statement firstBodyStmt;
        Statement lastBodyStmt;
        if(node.body instanceof ASTScopedStatementList && node.body.jjtGetNumChildren() > 0) {
            firstBodyStmt = (Statement) node.body.jjtGetChild(0);
            lastBodyStmt = (Statement) node.body.jjtGetChild(node.body.jjtGetNumChildren() - 1);
        } else {
            firstBodyStmt = node.body;
            lastBodyStmt = firstBodyStmt;
        }

        if(firstBodyStmt.cfNode != null)
            cfg.addSuccessor(thisNode, firstBodyStmt.cfNode);

        //The last statement in the while body must have the while statement as its successor
        if(lastBodyStmt.cfNode != null) {
            if(lastBodyStmt instanceof ASTBranch)
                addIfSuccessor((ASTBranch) lastBodyStmt, thisNode, cfg);
            else
                cfg.addSuccessor(lastBodyStmt.cfNode, thisNode);
        }

        return null;
    }

    @Override
    public Object visit(ASTExprStatement node, Object data) {
        ControlFlowData cfdata = (ControlFlowData) data;
        ControlFlowNode thisNode = new ControlFlowNode();
        thisNode.setNode(node);
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
            if(varDescriptor.getVarType() == VarType.LOCAL)
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
        thisNode.setNode(node);
        cfdata.setNode(thisNode);

        //This will fill def[] properties of this node
        ASTVarReference var = (ASTVarReference) node.arrayRef.arrayRef;
        varDef(var.identifier, cfdata);
        //This will fill use[] properties of this node
        node.childrenAccept(this, cfdata);
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
        thisNode.setNode(node);
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
            //We are only interested in variables that are allocated to registers inside
            //the current method (i.e. Locals)
            if(varDescriptor.getVarType() == VarType.LOCAL)
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
        node.childrenAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTParameter node, Object data) {
        return null;
    }

    @Override
    public Object visit(ASTSelfReference node, Object data) {
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
