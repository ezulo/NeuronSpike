/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package neuronspike;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javafx.scene.chart.NumberAxis;

/**
 *
 * @author ezuloaga
 */
public class NetPlot {
    
    ChartFrame frame;
    JFreeChart chart;
    XYSeries spikes;
    int cursor;
    int cutoffInterval;
    int limit_y;
    int size_x;
    String label;
    String x_label;
    String y_label;
    XYSeriesCollection series;
    
    private JFreeChart createChart() {
    	JFreeChart chart = ChartFactory.createScatterPlot(
                label,x_label,y_label, series,
                PlotOrientation.VERTICAL, true, true, false);
        return chart;
    }
    
    private void addPoint(double x, double y) {
        spikes.add(x, y);
    }

    public NetPlot(String l, String x, String y, int cutoff, int ybound) {
        label = l;
        x_label = x;
        y_label = y;
        limit_y = ybound;
        spikes = new XYSeries("Spike");
        series = new XYSeriesCollection();
        series.addSeries(spikes);
    	chart = createChart();
        frame = new ChartFrame("Neuron spiking", chart);
        frame.setSize(400, 400);
        frame.setVisible(true);
        cutoffInterval = cutoff;
    }
    
    public void plotSpikes(List<GenericNeuron> spikeList) {
    	XYPlot xyPlot = chart.getXYPlot();
    	ValueAxis domain = xyPlot.getDomainAxis();
        domain.setRange(Math.max(cursor-cutoffInterval, 0), Math.max(cursor, cutoffInterval));
    	domain = xyPlot.getRangeAxis();
        domain.setRange(-1, limit_y);
        
        for (int i = 0; i < spikeList.size(); i++) {
            addPoint(cursor, spikeList.get(i).getLocation()[1]);
        }
        cursor++;
        
        // Remove too old points
        while (!spikes.isEmpty() && spikes.getDataItem(0).getX().doubleValue() < cursor - cutoffInterval)
        	spikes.remove(0);
//        if (cursor % cutoffInterval == 0) {
//            spikes.clear();
//        }
    }
    
}
