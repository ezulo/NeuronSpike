package neuronspike;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author eddie
 */
public class NeuronNet {
    
    static List<List<Neuron>> layerList;
    static List<Neuron> inputLayer;
    static List<Neuron> outputLayer;
    static List<Neuron> oscillators;
    static List<NetPlot> nplots;
    static SpikeEventMgr sEM;
    static long global_time;
    static double global_spike_threshold;
    static long global_delay;
    static double[] global_weight_range;
    static long oscillator_wavelength;
    
    public NeuronNet(
            int numLayers, 
            int[] layerNeuronCount, 
            double[] connectivity,
            double spikeThreshold,
            long delay,
            double[] weightRange,
            int wavelength
    ) {
        //netplot stuff
        layerList = new ArrayList();
        oscillators = new ArrayList();
        global_time = 0;
        global_spike_threshold = spikeThreshold;
        global_delay = delay;
        global_weight_range = weightRange;
        oscillator_wavelength = wavelength;
        
        //generate all layers, pop onto layer list
        for (int i = 0; i < numLayers; i++) {
            List<Neuron> current_layer = new ArrayList();
            //generate neurons
            for (int j = 0; j < layerNeuronCount[i]; j++) {
                current_layer.add(
                        new Neuron(
                            global_time, global_spike_threshold, global_delay, i, j
                    )
                );
            }
            layerList.add(current_layer);
            //make connections to previous layer
            if (i != 0) {
                Random rand = new Random();
                //fetch previous layer
                List<Neuron> previous_layer = layerList.get(i - 1);
                //for every neuron in the previous layer...
                for (int k = 0; k < layerNeuronCount[i - 1]; k++) {
                    //for every neuron in current layer...
                    for (int l = 0; l < layerNeuronCount[i]; l++) {
                       //generate a random number from 0 to 1.0
                       double randFloat = rand.nextFloat();
                       //if number is > connectivity, calculate weight, connect
                       if (randFloat < connectivity[i]) {
                           double total_weight = global_weight_range[0] + 
                                   ((Math.abs(randFloat - 1)) * 
                                   (global_weight_range[1] - 
                                   global_weight_range[0]));
                           Neuron n0 = previous_layer.get(k);
                           Neuron n1 = current_layer.get(l);
                           n0.attachNeuron(
                                   n1, total_weight
                           );
                       }
                    }                   
                }
            }
        }
        
        //create plots
        nplots = new ArrayList();
        NetPlot nplot;
        int x_size, y_size;
        x_size = 50;
        for (int i = 0; i < layerList.size(); i++) {
            List<Neuron> layer = layerList.get(i);
            y_size = layer.size();
            nplot = new NetPlot(
                    "Layer " + i,
                    "Time",
                    "Neuron",
                    x_size, y_size
            );
            nplots.add(nplot);
        }
        
        inputLayer = layerList.get(0);
        outputLayer = layerList.get(layerList.size() - 1);
        Neuron oscillator = new Neuron(global_time, 0.99, 1, -1, 0);
        
        for (int i = 0; i < layerNeuronCount[0]; i++) {
            oscillator.attachNeuron(inputLayer.get(i), 1);
        }
        
        oscillators.add(oscillator);
        
        sEM = SpikeEventMgr.getInstance();
        launchConsole(this);
        
    }
    
    static void launchConsole(NeuronNet self) {
        ControlPanel panel;
        panel = new ControlPanel(self);
    }
    
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
    
    static void printLedger(long currentTime) {
        List<Neuron> ledger = sEM.getLedger();
        System.out.println("Spiked neurons for timestep " + currentTime + " : ");
        System.out.println("[");
        int[] place;
        for (int i = 0; i < ledger.size(); i++) {
            place = ledger.get(i).getLocation();
            System.out.println("Layer " + place[0] + " Neuron " + place[1]);
        }
        System.out.println("]");
        System.out.println("");
    }
    
    static void plotLayers() {
        List<Neuron> ledger = sEM.getLedger();
        List<Neuron> layerLedger;
        NetPlot nplot;
        for (int layerNum = 0; layerNum < layerList.size(); layerNum++) {
            layerLedger = new ArrayList();
            for (int i = 0; i < ledger.size(); i++) {
                Neuron n = ledger.get(i);
                if (n.getLocation()[0] == layerNum) {
                    layerLedger.add(n);
                }
            }
            nplot = nplots.get(layerNum);
            nplot.plotSpikes(layerLedger);
        }
    }
    
    public int[] getLayerMetadata() {
        //returns: array of layer sizes
        int[] d = new int[layerList.size()];
        for (int i = 0; i < d.length; i++) {
            d[i] = layerList.get(i).size();
        }
        return d;
    }
    
    public void addOscillator(int l, int n, double v) {
        Neuron neuron = new Neuron(global_time, 0.99, 1, -1, 0);
        neuron.attachNeuron(getNeuron(l,n),v);
        oscillators.add(neuron);
    }
    
    public void clearOscillators() {
        oscillators.clear();
    }
    
    private Neuron getNeuron(int l, int n) {
        //returns: nth neuron at layer l
        List<Neuron> nlayer = layerList.get(l);
        return nlayer.get(n);
    }
    
    public void incrementTime() {
        //factor out auto-spiking
        //keep neuron exciting outside of the 
        if (global_time % oscillator_wavelength == 0) {
            for (int i = 0; i < oscillators.size(); i++) {
                sEM.queueSpike(oscillators.get(i), global_time, 1.0);
            }
        }
        sEM.process(global_time);
        printState(global_time, outputLayer);
        plotLayers();
        global_time += 1;
    }
    
}