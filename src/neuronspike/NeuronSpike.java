/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuronspike;
import neuronspike.*;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;
import java.lang.Math;

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
    
    //simulation properties
    static long period = 1;
    static long end_time = 100;
    static long oscillator_wavelength = 2;
    
    //network properties
    //first layer is fed voltage by oscillator
    //last layer is read as output
    static int[] layers = {10, 100, 100, 5};
    static double[] connectivity = {1.0, 0.8, 0.7, 1.0};
    
    //neuron properties
    static double global_spike_threshold = 0.6;
    static long global_delay = 1;
    static double[] global_weight_range = {0.1, 0.8};
   
    
    static void printState(long currentTime, List<Neuron> neuronList) {
        int i = 0;
        System.out.println("Time step: " + currentTime);
        Neuron cur;
        for (i = 0; i < neuronList.size(); i++) {
            cur = neuronList.get(i);
            //feed 0 V to the neuron to update its voltage
            cur.feedVoltage(currentTime, 0.0, sEM);
            System.out.println(i + " - V: " + cur.volts);
        }
        System.out.println("");
    }
    
    public static void main(String[] args) {
        
        long global_time = 0;
        
        //each network is composed of different neuron layers:
        //an input layer that receives an impulse every timestep
        //n inner layers to propogate the voltage
        //an output layer whose output is read
        
        int numLayers = layers.length;
        
        List<List> global = new LinkedList();
        
        for (int i = 0; i < numLayers; i++) {
            List<Neuron> current_layer = new LinkedList();
            //generate neurons
            for (int j = 0; j < layers[i]; j++) {
                Neuron current_neuron = new Neuron(
                    global_time, global_spike_threshold, global_delay
                );
                current_layer.add(current_neuron);
            }
            //make connections to previous layer
            if (i != 0) {
                Random rand = new Random();
                //fetch previous layer
                List<Neuron> previous_layer = global.get(i - 1);
                //for every neuron in the previous layer...
                for (int k = 0; k < layers[i - 1]; k++) {
                    //for every neuron in current layer...
                    for (int l = 0; l < layers[i]; l++) {
                       //generate a random number from 0 to 1.0
                       double randFloat = rand.nextFloat();
                       //if number is > connectivity, calculate weight, connect
                       if (randFloat < connectivity[i]) {
                           double total_weight = global_weight_range[0] + 
                                   ((Math.abs(randFloat - 1)) * 
                                   (global_weight_range[1] - 
                                   global_weight_range[0]));
                           previous_layer.get(k).attachNeuron(
                                   current_layer.get(l), total_weight
                           );
//                           System.out.print("connection for layer ");
//                           System.out.print(i);
//                           System.out.print(" made: Neurons ");
//                           System.out.print(l);
//                           System.out.print(" -> ");
//                           System.out.print(k);
//                           System.out.print(" weight: ");
//                           System.out.println(total_weight);
                       }
                    }                   
                }
            }
            
            //add neuron layer to global list
            global.add(current_layer);
        }
        
        //create an oscillator (neuron spikes every timestep)
        Neuron oscillator = new Neuron(global_time, 0.99, 0);
        
        //connect the input layer to the oscillator
        List<Neuron> inputLayer = global.get(0);
        for (int i = 0; i < layers[0]; i++) {
            oscillator.attachNeuron(inputLayer.get(i), 0.5);
        }
        
        // instantiate spike event manager
        sEM = new SpikeEventMgr();
        
        
        
        //while loop
        //mess with time intervals and such here
        //input neurons will spike at rate of time interval
        while (global_time < end_time) {

            //this if statement does run
            if (global_time % oscillator_wavelength == 0) {
                sEM.queueSpike(oscillator, global_time, 1.0);
            }
            sEM.process(global_time);
            printState(global_time, global.get(global.size() - 1));
            global_time += period;
        }
        
        System.out.println("Hello world! Simulation finished.");
        
    }
    
}
