/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuronspike;
import java.util.Random;
import java.lang.Math;
/**
 *
 * @author ezuloaga
 */
public class OscillatorNeuron extends Neuron {

    //classifications:
    //0: linear
    //1: probabilistic
    //2: sine
    
    //parameter used in following fashion per classification:
    //0: rate of firing (per timestep)
    //1: probability of firing (per timestep)
    //2: lambda (decay and growth rate)
    
    private int nclass;
    private double param;
    private double toyval;
    private Random rand;
    
    public OscillatorNeuron(
        long time, 
        double s_t, 
        long d, 
        int classification,
        double parameter
    ){
        super(time, s_t, d);
        nclass = classification;
        param = parameter;
        if (!(nclass == 2)) {
            if (param < 0.0) param = 0.0;
            if (param > 1.0) param = 1.0;
        }
        toyval = 0.0;
        rand = new Random();
    }
    
    public boolean readyToFire(long timestep) {
        switch (nclass) {
            case 0:
                toyval += param;
                if (toyval >= 1.0) {
                    toyval -= 1.0;
                    return true;
                }
                else {
                    return false;
                }
            case 1:
                toyval = rand.nextFloat();
                if (toyval < param) {
                    return true;
                }
                else {
                    return false;
                }
            case 2:
                toyval += ((Math.sin(2*Math.PI*timestep/param))+1.0)/2.0;
                if (toyval >= 1.0) {
                    toyval -= 1.0;
                    return true;
                }
                else {
                    return false;
                }
            default:
                return false;
        }
    }
    
}
