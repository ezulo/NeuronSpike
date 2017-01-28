/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuronspike;
import neuronspike.Neuron;

/**
 *
 * @author eddie
 */
public class Event {
    long timeTriggered;
    Neuron triggeredNeuron;
    public Event(long tT, Neuron tN) {
        timeTriggered = tT;
        triggeredNeuron = tN;
    }
}
