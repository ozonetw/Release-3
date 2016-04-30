/**
 * **************************************************************************************
 * File:Sensor.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Autonomous University of Zacatecas 
 * Date: November 2015
 * Developer: Ferman Ivan Tovar 
 * Reviewer: Perla Velasco Elizondo
 * **************************************************************************************
 * This class contains the necessary to build a sensor, in order to every 
 * sensor extends from this.
 * **************************************************************************************
 */
package sensors;

import common.Component;
import event.Event;
import event.EventManagerInterface;
import java.util.Random;

public class Sensor extends Component {

    protected int delay = 2500;				// The loop delay (2.5 seconds)
    protected boolean isDone = false;			// Loop termination flag
    protected float driftValue;				// The amount of temperature gained or lost

    
    protected Sensor() {}

    /**
     * This method provides the simulation with random floating point 
     * temperature values between 0.1 and 0.9.
     * 
     * @return A random number
     */
    protected float getRandomNumber() {
        Random r = new Random();
        Float val;
        val = Float.valueOf((float) -1.0);
        while (val < 0.1) {
            val = r.nextFloat();
        }
        return (val.floatValue());
    } // GetRandomNumber

    /**
     * This method provides a random true or
     * false value used for determining the positiveness or negativeness of the
     * drift value.
     * 
     * @return A random boolean value
     */
    protected boolean coinToss() {
        Random r = new Random();
        return (r.nextBoolean());
    } // CoinToss

    /**
     * This method posts the specified temperature value to the specified event
     * manager.
     *
     * @param ei This is the eventmanger interface where the event will be
     * posted.
     * @param eventId This is the ID to identify the type of event
     * @param value Is the value to publish in the event queue
     */
    protected void postEvent(EventManagerInterface ei, int eventId, float value) {
        // Create the event.
        Event evt = new Event(eventId, String.valueOf(value));
        // Send the event to the event manager.
        try {
            ei.sendEvent(evt);
        }
        catch (Exception e) {
            System.out.println("Error Posting Temperature:: " + e);
        }
    }
}
