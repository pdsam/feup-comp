package controlFlowAnalysis;

import symbolTable.descriptor.VarDescriptor;

import java.util.ArrayList;
import java.util.List;

public class ControlFlowAnalysis {

    private ArrayList<ArrayList<VarDescriptor>> inL = new ArrayList<>();
    private ArrayList<ArrayList<VarDescriptor>> outL = new ArrayList<>();

    public void algorithm(ArrayList<ControlFlowNode> graph) {

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

                //in’[n] = in[n]
                inL.set(i, new ArrayList<>(graph.get(i).getIn()));

                //out’[n] = out[n]
                outL.set(i, new ArrayList<>(graph.get(i).getOut()));

                ArrayList<VarDescriptor> successorsIn = new ArrayList<>();

                //for all successors
                for(int w = 0; i < graph.get(i).getSuccessors().size(); w++) {
                    //get position in the arrayList
                    ControlFlowNode node = graph.get(i).getSuccessors().get(w);

                    //out[n] = ∪ in[s]
                    successorsIn.addAll(node.getIn());
                }

                //out[n] = ∪ in[s]
                graph.get(i).setOut(successorsIn);


                //in[n] = use[n] ∪ (out[n] – def[n])
                ArrayList<VarDescriptor> newSuccessorsIn = new ArrayList<>();

                //add all use
                newSuccessorsIn.addAll(graph.get(i).getUse());

                ArrayList<VarDescriptor> diff = new ArrayList<>(graph.get(i).getOut());

                //(out[n] – def[n])
                diff.removeAll(graph.get(i).getDef());

                newSuccessorsIn.addAll(diff);

                graph.get(i).setIn(newSuccessorsIn);

               //Test for convergence
                if(!graph.get(i).getIn().equals(inL.get(i)) || !graph.get(i).getOut().equals(inL.get(i))) {
                    end = false;
                }
            }

            //until in’[n]=in[n] and out’[n]=out[n] for all n
        } while(!end);
    }
}
