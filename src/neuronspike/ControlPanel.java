package neuronspike;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;

/**
 *
 * @author ezuloaga
 */
public class ControlPanel implements ActionListener {
    Neuron selected;
    NeuronNet subjectNet;
    int[] subjectMeta;
    JFrame controlPanel;
    int layerCursor;
    int neuronCursor;
    double selectedV;
    JTextField layerField;
    JTextField neuronField;
    JTextField paramField;
    GridLayout layout = new GridLayout(7, 2);
    
    private boolean boundCheck() {
        if ((layerCursor >= 0) && (layerCursor < subjectMeta.length)) {
            if (((neuronCursor >= 0) && (neuronCursor < subjectMeta[layerCursor]))) {
                return true;
            }
            else {
                neuronField.setText("OUT OF BOUNDS");
                return false;
            }
        }
        else {
            layerField.setText("OUT OF BOUNDS");
            return false;
        }
    }
    
    private void setDefault() {
        layerField.setText("< Layer >");
        neuronField.setText("< Neuron >");
        paramField.setText("< Rate / Prob / Wavelength >");
    }
    
    public void actionPerformed(ActionEvent e) {
        if ("Clear".equals(e.getActionCommand())) {
            subjectNet.clearOscillators();
            setDefault();
            return;
        }
        double param;
        try {
            layerCursor = (int)Integer.parseInt(layerField.getText());
            neuronCursor = (int)Integer.parseInt(neuronField.getText());
            param = (double)Float.parseFloat(paramField.getText());
        }
        catch (Exception exc) {
            setDefault();
            return;
        }
        if ("Add Cont".equals(e.getActionCommand())) {
            if (boundCheck()) {
                subjectNet.addOscillator(
                    layerCursor, 
                    neuronCursor, 
                    selectedV,
                    0,
                    param
                );
            }
        }
        else if ("Add Prob".equals(e.getActionCommand())) {
            if (boundCheck()) {
                subjectNet.addOscillator(
                    layerCursor, 
                    neuronCursor, 
                    selectedV,
                    1,
                    param
                );
            }
        }
        else if ("Add Sine".equals(e.getActionCommand())) {
            if (boundCheck()) {
                subjectNet.addOscillator(
                    layerCursor, 
                    neuronCursor, 
                    selectedV,
                    2,
                    param
                );
            }
        }
    }
    
    public ControlPanel(NeuronNet net) {
        subjectNet = net;
        subjectMeta = subjectNet.getLayerMetadata();
        layerCursor = 0;
        neuronCursor = 0;
        selectedV = 1.0;
        controlPanel = new JFrame();
        controlPanel.setLayout(layout);
        controlPanel.setSize(200, 500);
        //ADD BUTTONS
        JButton b1 = new JButton("Add Osc (Continuous)");
        b1.setActionCommand("Add Cont");
        JButton b2 = new JButton("Add Osc (Probabilistic)");
        b2.setActionCommand("Add Prob");
        JButton b3 = new JButton("Add Osc (Sine)");
        b3.setActionCommand("Add Sine");
        JButton b4 = new JButton("Clear Oscillators");
        b4.setActionCommand("Clear");
        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        b4.addActionListener(this);
        controlPanel.add(b1);
        controlPanel.add(b2);
        controlPanel.add(b3);
        controlPanel.add(b4);
        //ADD FIELDS
        layerField = new JTextField();
        layerField.setHorizontalAlignment(JTextField.CENTER);
        neuronField = new JTextField();
        neuronField.setHorizontalAlignment(JTextField.CENTER);
        paramField = new JTextField();
        paramField.setHorizontalAlignment(JTextField.CENTER);
        controlPanel.add(layerField);
        controlPanel.add(neuronField);
        controlPanel.add(paramField);
        controlPanel.setVisible(true);
    }
}
