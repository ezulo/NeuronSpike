/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuronspike;
import java.util.List;
import java.util.LinkedList;
import neuronspike.Neuron;

/**
 *
 * @author eddie
 */

class SpikeEvent {
    public Neuron target;
    public long timeStamp;
    public double impulse;
    public SpikeEvent(Neuron t, long time, double i) {
        target = t;
        timeStamp = time;
        impulse = i;
    }
}

public class SpikeEventMgr {
    List<SpikeEvent> eventList;
    public void process(long time) {
        SpikeEvent cur;
        //iterate through eventList
        int i = 0;
        while (i < eventList.size()) {
            cur = eventList.get(i);
            if (cur.timeStamp == time) {
                //send voltage to neuron and remove from list
                cur.target.feedVoltage(time, cur.impulse, this);
                eventList.remove(i);
            }
            else {
                //check next index in list
                i++;
            }
        }
    }
    public void queueSpike(Neuron n, long time, double impulse) {
        SpikeEvent e0 = new SpikeEvent(n, time, impulse);
        eventList.add(e0);
    }
    public SpikeEventMgr() {
        eventList = new LinkedList();
    }
}

/*
current issue:
    spike event list handling:
        how to perform while maintaining good performance
        list implementation requires O(n) per time interval
            issue here: unknown how fast n grows
            possible solution: keep list sorted
                considerably more cost effective with a linked list
*/
