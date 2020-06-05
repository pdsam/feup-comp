package controlFlowAnalysis;

import java.util.ArrayList;
import java.util.Collections;

public class DataFlowAnalysis {

    

    public void algorithm(ArrayList<DataFlowNode> graph) {



        //for each node n in CFG
         for(int j = 0; j < graph.size(); j++) {
             //in[n] = ∅;
             graph.get(j).setIn(new ArrayList<>());
             //out[n] = ∅
             graph.get(j).setOut(new ArrayList<>());
         }
/*
        boolean end = false;

        do {
            //for each node n in CFG in reverse topsort order
            for(int i = graph.size() - 1; i >= 0; i--) {


                //in’[n] = in[n]
                graph.get(i).setInL(new ArrayList<>(graph.get(i).getIn()));

                //out’[n] = out[n]
                graph.get(i).setOutL(new ArrayList<>(graph.get(i).getOut()));

                //out[n] = ∪ in[s]
                //in[n] = use[n] ∪ (out[n] – def[n])
            }

            end = true;

            //until in’[n]=in[n] and out’[n]=out[n] for all n
            for(int j = 0; j < graph.size(); j++) {
                Collections.sort(graph.get(j).getIn());
                Collections.sort(graph.get(j).getInL());
                Collections.sort(graph.get(j).getOut());
                Collections.sort(graph.get(j).getOutL());

                if(!graph.get(j).getIn().equals(graph.get(j).getInL()) || !graph.get(j).getOut().equals(graph.get(j).getOutL())) {
                    end = false;
                }
            }

        } while(!end);
*/
    }
}
