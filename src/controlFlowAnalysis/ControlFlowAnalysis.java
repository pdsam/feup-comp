package controlFlowAnalysis;

import symbolTable.descriptor.VarDescriptor;

import java.util.ArrayList;
import java.util.List;

public class ControlFlowAnalysis {
    public static void liveness(ControlFlowData controlFlowData) {
        List<ControlFlowNode> graph = controlFlowData.getGraph().getNodeSet();
        boolean end;

        do {
            end = true;

            //for each node n in CFG in reverse topsort order
            for(int i = graph.size() - 1; i >= 0; i--) {

                ControlFlowNode current_node = graph.get(i);

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
}
