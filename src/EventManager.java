
/**
 * **************************************************************************************
 * File:EventManager.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Autonomous University of Zacatecas 
 * Date: November 2015
 * Developer: Ferman Ivan Tovar 
 * Reviewer: Perla Velasco Elizondo
 * **************************************************************************************
 * This class is the event manager responsible for receiving and distributing
 * events from participants and all associated house keeping chores.
 * Communication with participants is via RMI. There are a number of RMI methods
 * that allow participants to register, post events, get events, etc.
 * **************************************************************************************
 */

import event.RMIEventManagerInterface;
import event.Event;
import event.EventQueue;
import java.net.*;
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;



public class EventManager extends UnicastRemoteObject implements RMIEventManagerInterface {

    static Vector<EventQueue> eventQueueList;           // This is the list of event queues.
    static RequestLogger requestLogger;                 // This is a request logger - Logger is a private inner class

    public EventManager() throws RemoteException {
        super();					// Required by RMI
        requestLogger = new RequestLogger();		// Screen logging object
        eventQueueList = new Vector<>(15, 1);           // Queue for storing events
    } // Constructor

    public static void main(String args[]) {
        try {
            // Here we start up the server. We first must instantiate a class of type PolicyDB
            InetAddress localHostAddress = InetAddress.getLocalHost();
            String eventManagerIpAddress = localHostAddress.getHostAddress();

            EventManager em = new EventManager();
            Naming.bind("EventManager", em);

            // Finally we notify the user that the server is ready.
            requestLogger.displayStatistics("Server IP address::" + eventManagerIpAddress + ". Event manager ready.");
        }
        // Potential exceptions
        catch (Exception e) {
            requestLogger.displayStatistics("Event manager startup error: " + e);
        }
    }

    /**
     * This method registers participants with the event manager.
     *
     * @return The participants id
     * @throws RemoteException
     */
    @Override
    synchronized public long Register() throws RemoteException {
        // Create a new queue and add it to the list of event queues.
        EventQueue queue = new EventQueue();
        eventQueueList.add(queue);
        requestLogger.displayStatistics("Register event. Issued ID = " + queue.getId());
        return queue.getId();
    }

    /**
     * This method unregisters participants with the event manager.
     *
     * @param id The participants id
     * @throws RemoteException
     */
    @Override
    synchronized public void UnRegister(long id) throws RemoteException {
        EventQueue mq;
        boolean found = false;
        // Find the queue for id.
        for (int i = 0; i < eventQueueList.size(); i++) {
            //Get the queue for id and remove it from the list.
            mq = eventQueueList.get(i);
            if (mq.getId() == id) {
                mq = eventQueueList.remove(i);
                found = true;
            }
        }

        if (found) {
            requestLogger.displayStatistics("Unregistered ID::" + id);
        }
        else {
            requestLogger.displayStatistics("Unregister error. ID:" + id + " not found.");
        }
    }

    /**
     * This method allows participants to send events to the event manager.
     *
     * @param m The event to send
     * @throws RemoteException
     */
    synchronized public void SendEvent(Event m) throws RemoteException {
        EventQueue mq;
        // For every queue on the list, add the event.
        for (int i = 0; i < eventQueueList.size(); i++) {
            mq = eventQueueList.get(i);
            mq.addEvent(m);
            eventQueueList.set(i, mq);
        }
        requestLogger.displayStatistics("Incoming event posted from ID: " + m.getSenderId());
    }

    /**
     * Get the event queue for a participant ID.
     *
     * @param id Participant id
     * @return An EventQueue
     * @throws RemoteException
     */
    @Override
    synchronized public EventQueue GetEventQueue(long id) throws RemoteException {
        EventQueue temp = null;
        boolean found = false;

        // Find the queue for id.
        for (EventQueue mq : eventQueueList) {
            // Get each queue off of the list and see if it is id's queue
            // Once the queue is found, then get a copy of the queue, clear the
            // queue, and return the queue back to the participant
            if (mq.getId() == id) {
                temp = mq.getCopy();
                mq.clearEventQueue();
                found = true;
            }
        }

        if (found) {
            requestLogger.displayStatistics("Get event queue request from ID: " + id + ". Event queue returned.");
        }
        else {
            requestLogger.displayStatistics("Get event queue request from ID: " + id + ". ID not found.");
        }

        return temp;

    } // GetEventList

    /**
     * *************************************************************************
     * This class longs requests by displaying them on the server with general
     * statics after each remote call for services.
     * **************************************************************************
     */
    private class RequestLogger {

        int requestsServiced = 0;	// This is the number of requests seviced

        /**
         * This method increments the number of service request from
         * participants, counts the number of active queues (registered
         * participants), and displays this information on the terminal.
         *
         * @param message
         */
        void displayStatistics(String message) {
            requestsServiced++;

            if (message.length() == 0) {
                System.out.println("-------------------------------------------------------------------------------");
                System.out.println("Number of requests: " + requestsServiced);
                System.out.println("Number of registered participants: " + eventQueueList.size());
                System.out.println("-------------------------------------------------------------------------------");
            }
            else {
                System.out.println("-------------------------------------------------------------------------------");
                System.out.println("Message:: " + message);
                System.out.println("Number of requests: " + requestsServiced);
                System.out.println("Number of registered participants: " + eventQueueList.size());
                System.out.println("-------------------------------------------------------------------------------");
            }
        }
    }
}