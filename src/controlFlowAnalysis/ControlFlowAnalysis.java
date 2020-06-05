package controlFlowAnalysis;

import symbolTable.descriptor.VarDescriptor;

import java.util.ArrayList;
import java.util.List;

public class ControlFlowAnalysis {

    private static ArrayList<ArrayList<VarDescriptor>> inL = new ArrayList<>();
    private static ArrayList<ArrayList<VarDescriptor>> outL = new ArrayList<>();

    public static void liveness(ControlFlowData controlFlowData) {

        List<ControlFlowNode> graph = controlFlowData.getGraph().getNodeSet();

        /* Initialize solutions */
        //for each node n in CFG
         for(int j = 0; j < graph.size(); j++) {
             //in[n] = ∅;
             inL.add(new ArrayList<>());
             //out[n] = ∅
             outL.add(new ArrayList<>());
         }

        boolean end;

        do {
            end = true;

            //for each node n in CFG in reverse topsort order
            for(int i = graph.size() - 1; i >= 0; i--) {

                ControlFlowNode current_node = graph.get(i);

                //in’[n] = in[n]
                inL.set(i, new ArrayList<>(current_node.getIn()));

                //out’[n] = out[n]
                outL.set(i, new ArrayList<>(current_node.getOut()));

                ArrayList<VarDescriptor> successorsIn = new ArrayList<>();

                //for all successors
                for(int w = 0; i < current_node.getSuccessors().size(); w++) {
                    //get position in the arrayList
                    ControlFlowNode node = current_node.getSuccessors().get(w);

                    //out[n] = ∪ in[s]
                    successorsIn.addAll(node.getIn());
                }

                //out[n] = ∪ in[s]
                current_node.setOut(successorsIn);


                //in[n] = use[n] ∪ (out[n] – def[n])
                ArrayList<VarDescriptor> newSuccessorsIn = new ArrayList<>();

                //add all use
                newSuccessorsIn.addAll(current_node.getUse());

                ArrayList<VarDescriptor> diff = new ArrayList<>(current_node.getOut());

                //(out[n] – def[n])
                diff.removeAll(current_node.getDef());

                newSuccessorsIn.addAll(diff);

                current_node.setIn(newSuccessorsIn);

               //Test for convergence
                if(!current_node.getIn().equals(inL.get(i)) || !current_node.getOut().equals(inL.get(i))) {
                    end = false;
                }
            }

            //until in’[n]=in[n] and out’[n]=out[n] for all n
        } while(!end);
    }
}
