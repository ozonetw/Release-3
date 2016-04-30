/**
 * **************************************************************************************
 * File: EventManagerInterface.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Autonomous University of Zacatecas 
 * Date: November 2015
 * Developer: Ferman Ivan Tovar 
 * Reviewer: Perla Velasco Elizondo
 * **************************************************************************************
 * This class is the interface definition for the event manager services that 
 * are available to participants.
 * **************************************************************************************
 */

package event;

import java.rmi.*;

public interface RMIEventManagerInterface extends Remote {

    /**
     * This interface is used to access the
     * participant registration service on the EventManager
     * 
     * @return long integer registration number
     * @throws java.rmi.RemoteException 
     */
    public long Register() throws java.rmi.RemoteException;

    /**
     * This interface is used to access the
     * participant un-registration service on the EventManager
     * 
     * @param SenderID long integer registration number
     * @throws java.rmi.RemoteException 
     */
    public void UnRegister(long SenderID) throws java.rmi.RemoteException;

    /**
     * This interface is used by participant to
     * access the event sending service on the EventManager
     * @param m Event object (see the class: Event.java)
     * @throws java.rmi.RemoteException 
     */
    public void SendEvent(Event m) throws java.rmi.RemoteException;

    /**
     * This interface is used to allow the
     * participant access the event queue on the EventManager
     * 
     * @param SenderID long integer registration number
     * @return EventQueue object (see the class: EventQueue.java)
     * @throws java.rmi.RemoteException 
     */
    public EventQueue GetEventQueue(long SenderID) throws java.rmi.RemoteException;

} // class
