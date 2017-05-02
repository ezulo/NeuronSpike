/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package neuronspike;
import java.util.List;
import org.jfree.data.xy.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author ezuloaga
 */
public class NetPlot {
    
    ChartFrame frame;
    JFreeChart chart;
    XYSeries spikes;
    XYSeries limits;
    XYSeriesCollection spikes_container;
    int cursor;
    int cutoffInterval;
    int limit_y;
    int size_x;
    String label;
    String x_label;
    String y_label;
    
    private JFreeChart createChart() {
        chart = ChartFactory.createScatterPlot(
            label,
            x_label,
            y_label,
            spikes_container
        );
        return chart;
    }
    
    private void createDataset(){
        spikes_container = new XYSeriesCollection();
        spikes_container.addSeries(spikes);
        spikes_container.addSeries(limits);
    }
    
    private void addPoint(double x, double y) {
        spikes.add(x, y);
    }
    
    private void addLimitPoints(int start) {
        limits.add(start, -1);
        limits.add(start, limit_y);
        limits.add(start + cutoffInterval, -1);
        limits.add(start + cutoffInterval, limit_y);
    }
    
    private void updatePlot() {
        addPoint(cursor, 1.0);
        
    }

    public NetPlot(String l, String x, String y, int cutoff, int ybound) {
        label = l;
        x_label = x;
        y_label = y;
        spikes = new XYSeries("Spike");
        limits = new XYSeries("Limit");
        limit_y = ybound;
        createDataset();
        createChart();
        frame = new ChartFrame("Neuron spiking", chart);
        addLimitPoints(0);
        frame.setSize(400, 400);
        frame.setVisible(true);
        cutoffInterval = cutoff;
    }
    
    public void plotSpikes(List<GenericNeuron> spikeList) {
        for (int i = 0; i < spikeList.size(); i++) {
            addPoint(cursor, spikeList.get(i).getLocation()[1]);
        }
        cursor++;
        if (cursor % cutoffInterval == 0) {
            spikes.clear();
            limits.clear();
            addLimitPoints(cursor);
        }
    }
    
}
