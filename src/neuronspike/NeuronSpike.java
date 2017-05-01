/*

poisson: distribution that models time events on queues, etc. look into this

idea is to form a scatterplot of neuron spikes over time

to do next:
get auto spiking behavior out
collect numbers / logs on neuron firings

feed the network voltage
add oscillators
inject spikes
have a way to select neurons (layer 1, neuron 3, etc)

*/


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuronspike;
import java.util.Timer;
import java.util.TimerTask;

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

class runSim extends TimerTask {
    NeuronNet network;
    public runSim(NeuronNet nn) {
        network = nn;
    }
    public void run() {
        network.incrementTime();
    }
}

public class NeuronSpike {

    /**
     * @param args the command line arguments
     */
    
    public static SpikeEventMgr sEM;
    
    //simulation properties
    static long period = 1;
    static long end_time = 30;
    static int oscillator_wavelength = 1;
    
    //network properties
    //first layer is fed voltage by oscillator
    //last layer is read as output
    static int[] layers = {10,100,100,5};
    static double[] connectivity = {1.0, 0.1, 0.1, 0.1};
    
    //neuron properties
    public static double global_spike_threshold = 0.6;
    public static long global_delay = 1;
    public static double[] global_weight_range = {0, 0.2};
    
    public static void main(String[] args) {
        
        long global_time = 0;
        
        //each network is composed of different neuron layers:
        //an input layer that receives an impulse every timestep
        //n inner layers to propogate the voltage
        //an output layer whose output is read
        
        NeuronNet network = new NeuronNet(
            layers.length, 
            layers, 
            connectivity,
            global_spike_threshold,
            global_delay,
            global_weight_range,
            oscillator_wavelength
        );
        
        TimerTask task = new runSim(network);
        Timer timer = new Timer();
        timer.schedule(task, 1000, 100);

    }
    
}
