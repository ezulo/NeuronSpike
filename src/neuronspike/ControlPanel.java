package neuronspike;

import java.util.List;
import java.text.ParseException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
import javax.swing.JComponent;
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
    GridLayout layout = new GridLayout(5, 2);
    
    public void actionPerformed(ActionEvent e) {
        if ("A".equals(e.getActionCommand())) {
            layerCursor = (int)Integer.parseInt(layerField.getText());
            neuronCursor = (int)Integer.parseInt(neuronField.getText());
            //perform bounds checking
            if ((layerCursor >= 0) && (layerCursor < subjectMeta.length)) {
                if (((neuronCursor >= 0) && (neuronCursor < subjectMeta[layerCursor]))) {
                    subjectNet.addOscillator(layerCursor, neuronCursor, selectedV);
                }
                else {
                    neuronField.setText("OUT OF BOUNDS");
                }
            }
            else {
                layerField.setText("OUT OF BOUNDS");
            }
            
        }
        if ("C".equals(e.getActionCommand())) {
            subjectNet.clearOscillators();
            neuronField.setText("0");
            layerField.setText("0");
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
        controlPanel.setSize(500, 500);
        //ADD BUTTONS
        JButton b1 = new JButton("Add Oscillator");
        b1.setActionCommand("A");
        JButton b2 = new JButton("Clear Oscillators");
        b2.setActionCommand("C");
        b1.addActionListener(this);
        b2.addActionListener(this);
        controlPanel.add(b1);
        controlPanel.add(b2);
        //ADD FIELDS
        layerField = new JTextField();
        layerField.setHorizontalAlignment(JTextField.CENTER);
        neuronField = new JTextField();
        neuronField.setHorizontalAlignment(JTextField.CENTER);
        controlPanel.add(layerField);
        controlPanel.add(neuronField);
        controlPanel.setVisible(true);
    }
}
