package controlFlowAnalysis;

import symbolTable.descriptor.VarDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ControlFlowAnalysis {

    public static void liveness(ControlFlowGraph graph) {
        List<ControlFlowNode> nodes = graph.getNodeSet();

        boolean end;

        do {
            end = true;

            //for each node n in CFG in reverse topsort order
            for(int i = nodes.size() - 1; i >= 0; i--) {

                ControlFlowNode current_node = nodes.get(i);

                //in’[n] = in[n]
                List<VarDescriptor> inL = new ArrayList<>(current_node.getIn());

                //out’[n] = out[n]
                List<VarDescriptor> outL = new ArrayList<>(current_node.getOut());

                List<VarDescriptor> successorsIn = new ArrayList<>();

                //for all successors
                for(ControlFlowNode successor : current_node.getSuccessors()) {
                    //out[n] = ∪ in[s]
                    successorsIn.removeAll(successor.getIn());
                    successorsIn.addAll(successor.getIn());
                }

                //out[n] = ∪ in[s]
                current_node.setOut(successorsIn);

                //use[n]
                ArrayList<VarDescriptor> newSuccessorsIn = new ArrayList<>(current_node.getUse());

                //(out[n] – def[n])
                ArrayList<VarDescriptor> diff = new ArrayList<>(current_node.getOut());
                diff.removeAll(current_node.getDef());

                //in[n] = use[n] ∪ (out[n] – def[n])
                newSuccessorsIn.removeAll(diff);
                newSuccessorsIn.addAll(diff);
                current_node.setIn(newSuccessorsIn);

                //Test for convergence
                if(!current_node.getIn().equals(inL) || !current_node.getOut().equals(outL)) {
                    end = false;
                }

            }

            //until in’[n]=in[n] and out’[n]=out[n] for all n
        } while(!end);

    }

    public static InterferenceGraph interferenceGraph(ControlFlowGraph graph) {
        List<ControlFlowNode> nodes = graph.getNodeSet();

        InterferenceGraph interferenceGraph = new InterferenceGraph();

        //For each CFG node n that assigns the value to the variable a (ie, a $ \in$ def[n])
        // we add the edges (a, b1), (a, b2),...,(a, bm), where out[n] = {b1, b2,..., bm}
        for(int i = 0; i < nodes.size(); i--) {

            ControlFlowNode current_node = nodes.get(i);
            List<VarDescriptor> current_nodeDef = current_node.getDef();
            List<VarDescriptor> current_nodeOut = current_node.getOut();

            for(int j = 0; j < current_nodeDef.size(); j++) {

                VarDescriptor descriptor = current_nodeDef.get(j);

                for (VarDescriptor outDescriptor : current_nodeOut) {
                    interferenceGraph.addEdge(descriptor, outDescriptor);
                }

            }
        }
        return interferenceGraph;
    }

    public static int simplification(InterferenceGraph graph, int numRegisters, Stack<VarNode> stack) {
        List<VarNode> nodes = graph.getNodes();
        boolean notEnoughColors;
        int neededRegisters = numRegisters;
        int minInterferences = Integer.MAX_VALUE;

        //simplification
        do {
            notEnoughColors = true;
            for(VarNode node : nodes) {
                if (node.numInterferences() < numRegisters) {
                    stack.push(node);

                    for(VarNode interfering : node.getInterferences()) {
                        interfering.removeInterference(node);
                    }
                    notEnoughColors = false;
                } else if (node.numInterferences() < minInterferences) {
                    minInterferences = node.numInterferences();
                }
            }

            if(notEnoughColors) {
                neededRegisters = minInterferences + 1;
            } else {
                nodes.removeAll(stack);
            }
        } while(!nodes.isEmpty());

        return neededRegisters;
    }

    public static void selection(InterferenceGraph interferenceGraph, int neededRegisters, int initialStackOffset, Stack<VarNode> stack) {

        do {
            VarNode node = stack.pop();
            //interference were removes during the simplification, putting them back
            node = interferenceGraph.lookup(node.getDescriptor());

            //checks the color of the other interfering nodes
            for(int i = initialStackOffset; i < neededRegisters; i++) {
                //check if any interfering node has the same color
                for (VarNode interfering : node.getInterferences()) {
                    if(i == interfering.getDescriptor().getStackOffset()){
                        //that color can not be assign
                        continue;
                    }
                }
                //any other interfering node has the same color, attributing the unused color in that context
                node.getDescriptor().setStackOffset(i);
            }

        } while(!stack.empty());
    }
}
