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
        for(int i = 0; i < nodes.size(); i++) {

            ControlFlowNode current_node = nodes.get(i);
            List<VarDescriptor> current_nodeDef = current_node.getDef();
            List<VarDescriptor> current_nodeOut = current_node.getOut();

            for(int j = 0; j < current_nodeDef.size(); j++) {

                VarDescriptor descriptor = current_nodeDef.get(j);
                descriptor.setStackOffset(-1);

                for (VarDescriptor outDescriptor : current_nodeOut) {
                    outDescriptor.setStackOffset(-1);
                    interferenceGraph.addEdge(descriptor, outDescriptor);
                }

            }
        }
        return interferenceGraph;
    }

    public static int coloring(InterferenceGraph graph, int numRegisters, int initialStackOffset) throws AllocationException {
        Stack<VarNode> stack = new Stack<>();
        List<VarNode> nodes = graph.getNodes();

        int localRegisters = simplification(nodes, numRegisters, stack);

        if(localRegisters > numRegisters)
            throw new AllocationException(numRegisters, localRegisters);

        int maxStackOffset = selection(localRegisters, initialStackOffset, stack);

        return maxStackOffset + 1;
    }

    private static int simplification(List<VarNode> nodes, int numRegisters, Stack<VarNode> stack) {
        boolean notEnoughColors;
        int localRegisters = numRegisters;
        int minInterferences = Integer.MAX_VALUE;

        do {
            //System.out.println("====================================");

            notEnoughColors = true;
            for(VarNode node : nodes) {
                //System.out.println("Iteration for node " + node.getDescriptor().getName());
                if (node.numInterferences() < localRegisters) {
                    //System.out.println("\tPushing node " + node.getDescriptor().getName());
                    stack.push(node);

                    //Removing this node interference from the nodes that remain in the graph
                    for(VarNode interfering : node.getInterferences()) {
                        interfering.removeInterference(node);
                    }
                    notEnoughColors = false;
                } else if (node.numInterferences() < minInterferences) {
                    //Saving the minimum number of interferences to allocate more registers
                    minInterferences = node.numInterferences();
                    //System.out.println("\tUpdating minInterferences " + minInterferences);
                }
            }

            //System.out.println("====================================");

            if(notEnoughColors) {
                localRegisters = minInterferences + 1;
                //System.out.println("There were not enough colors.");
                //System.out.println("Increasing the num registers to " + localRegisters);
            } else {
                nodes.removeAll(stack);
            }
        } while(!nodes.isEmpty());

        return localRegisters;
    }

    private static int selection(int localRegisters, int initialStackOffset, Stack<VarNode> stack) {
        int totalRegisters = localRegisters + initialStackOffset;
        int maxStackOffset = 0;
        boolean interferes;

        do {
            VarNode node = stack.pop();
            //System.out.println("====================================");
            //System.out.print("Popped Node: " + node);

            //checks the color of the other interfering nodes
            for(int i = initialStackOffset; i < totalRegisters; i++) {
                interferes = false;
                //System.out.println("For color " + i + ": ");
                //check if any interfering node has the same color
                for (VarNode interfering : node.getInterferences()) {
                    //System.out.println("\tChecking interference with " + interfering.getDescriptor().getName()
                            //+ " with value " + interfering.getDescriptor().getStackOffset());
                    if(i == interfering.getDescriptor().getStackOffset()){
                        //that color can not be assigned
                        interferes = true;
                        break;
                    }
                }
                //no other interfering node has the same color, this color is safe to use
                if(!interferes) {
                    //System.out.println("\tColor chosen!");
                    node.getDescriptor().setStackOffset(i);
                    if(i > maxStackOffset) maxStackOffset = i;
                    break;
                }
            }
        } while(!stack.empty());

        return maxStackOffset;
    }
}
