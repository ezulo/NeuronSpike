/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuronspike;
import neuronspike.*;
import java.util.List;
import java.util.LinkedList;

/**
 *
 * @author eddie
 */

/**
 * What we should do:
 * 1) Initialize all neurons and attachments
 * 2) Every time unit, do the following:
 *      A) process all artifically placed voltage inputs
 *      B) queue spike events if threshold voltage is reached
 *      
 * 
 */

public class NeuronSpike {

    /**
     * @param args the command line arguments
     */
    
    public static SpikeEventMgr sEM;
    
    static long period = 1;
    static long end_time = 20;
    
    public static void main(String[] args) {
        
        long global_time = 0;
        
        // instantiate spike event manager
        sEM = new SpikeEventMgr();
        
        // let's hardcode some neurons
        Neuron A = new Neuron(global_time, 0.6, 0);
        Neuron B = new Neuron(global_time, 0.6, 0);
        
        // ..and any input neurons
        Neuron i0 = new Neuron(global_time, 1.0, 0);
        
        // let's make some attachments
        A.attachNeuron(B, 0.1);
        B.attachNeuron(A, 0.25);
        
        //now the input neurons
        i0.attachNeuron(A, 0.05);
        
        //instantiate inputs list and add input neurons
        List<Neuron> inputs = new LinkedList();
        inputs.add(i0);
        
        //while loop
        //mess with time intervals and such here
        //input neurons will spike at rate of time interval
        while (global_time < end_time) {
            global_time += period;
            //update event manager
            sEM.process(global_time);

            //spike the input neurons (as of right now, spikes every time interval)
            for (int i = 0; i < inputs.size(); i++) {
                sEM.queueSpike(inputs.get(i), global_time);
            }
        }
        
    }
    
}
