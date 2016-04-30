/**
 * **************************************************************************************
 * File:Controller.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Autonomous University of Zacatecas 
 * Date: November 2015
 * Developer: Ferman Ivan Tovar 
 * Reviewer: Perla Velasco Elizondo
 * **************************************************************************************
 * This class contains the necessary to build a controller, in order to every 
 * controller extends from this.
 * **************************************************************************************
 */
package controllers;

import common.Component;
import event.Event;
import event.EventManagerInterface;

public class Controller extends Component {
    protected int delay = 2500;				// The loop delay (2.5 seconds)
    protected boolean isDone = false;			// Loop termination flag
    
    protected Controller() {
        super();
    }
    
    /**
     * This method posts the specified message to the specified event manager
     * 
     * @param ei This is the eventmanger interface where the event will be posted.
     * @param evtId This is the ID to identify the type of event
     * @param m This is the received command.
     */
    protected void confirmMessage(EventManagerInterface ei, int evtId, String m) {
        // Here we create the event.
        Event evt = new Event(evtId, m);
	// Here we send the event to the event manager.
        try {
            ei.sendEvent(evt);
        } // try
        catch (Exception e) {
            System.out.println("Error Confirming Message:: " + e);
        } // catch
    } // PostMessage
}
