/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package neuronspike;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author ezuloaga
 */
public class NetPlot {
    
    JFreeChart chart;
    int cursor;
    
    public NetPlot(int x, int y) {
        chart = ChartFactory.createScatterPlot(
                "Spiking Neurons",
                "Neuron",
                "Time",
                
    }
    
    public void plotSpikes(List<Neuron> spikeList) {
        trace.addPoint((double)cursor, spikeList.size());
        cursor++;
    }
    
}
