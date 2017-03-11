/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuronspike;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author eddie
 */
public class Neuron {
   List<Neuron> outputNeurons;
   List<Float> weights;
   double volts;
   double spike_threshold;
   long lastUpdated;
   long delay;
   boolean spiking;
   
   public Neuron(long time, double s_t, long d) {
       outputNeurons = new ArrayList<>();
       weights = new ArrayList<>();
       volts = 0;
       spike_threshold = s_t;
       delay = d;
       lastUpdated = time;
       spiking = false;
   }
   public void attachNeuron(Neuron n, double w) {
       outputNeurons.add(n);
       weights.add(new Float(w));
   }
   private void neuronDecay(long time, double lambda) {
       //should be called when pertinent. e.g., neuron receiving voltage
       //lambda must be < 1.0
       if (time == lastUpdated) {
           return;
       }
       if (volts <= 0.001) {
           volts = 0.0;
           return;
       }
       double deltaT = time - lastUpdated;
       double v1 = volts * Math.pow(lambda, deltaT);
       volts = v1;
       lastUpdated = time;
       return;
   }
   public void feedVoltage(long time, double inVolts, SpikeEventMgr sEM) {
       //simple voltage feed. public but should mainly be used only by event mgr
       neuronDecay(time, 0.8);
       lastUpdated = time;
       volts += inVolts;
       
       //check for spiking behavior
       if (volts >= spike_threshold) {
           volts = 0.0;
           //queue voltage signals to all outputs
           for (int i = 0; i < outputNeurons.size(); i++) {
                sEM.queueSpike(outputNeurons.get(i),
                    time + outputNeurons.get(i).delay,
                    weights.get(i));
           }
       }
       return;
   }

}
