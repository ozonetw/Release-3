/**
 * **************************************************************************************
 * File:EventUtility.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Autonomous University of Zacatecas 
 * Date: November 2015
 * Developer: Ferman Ivan Tovar 
 * Reviewer: Perla Velasco Elizondo
 * **************************************************************************************
 * This class illustrates how to utilize the event manager and provides a few
 * basic utilities that can help developers test and debug their systems.
 * **************************************************************************************
 */

import common.IOManager;
import event.Event;
import event.EventManagerInterface;
import event.EventQueue;

public class EventUtility {

    public static void main(String args[]) {
        IOManager input = new IOManager();      // IOManager IO Object
        String evtMgrIP;			// Event Manager IP address
        boolean isDone = false;			// Main loop flag
        String option;				// Menu choice from user
        Event evt = null;			// Event object
        boolean isError;			// Error flag
        EventQueue eq = null;			// Message Queue
        int EvtId = 0;				// User specified event ID
        EventManagerInterface ef = null;        // Interface object to the event manager

        /////////////////////////////////////////////////////////////////////////////////
        // Get the IP address of the event manager
        /////////////////////////////////////////////////////////////////////////////////
        System.out.println("\n\n\n\n");

        System.out.println("Enter IP address of event manager or...");
        System.out.print("press enter if on local machine: ");
        evtMgrIP = input.keyboardReadString();

        System.out.println("\n\n\n\n");

        System.out.print("\n\nAttempting to register...");

        if (evtMgrIP.length() == 0) {
            try {
                // Here we create an event manager interface object. This assumes
                // that the event manager is on the local machine
                ef = new EventManagerInterface();
            }

            catch (Exception e) {
                System.out.println("Error instantiating event manager interface: " + e);
            } // catch

        }
        else {

            if (evtMgrIP.length() != 0) {
                try {
                    // Here we create an event manager interface object. This assumes
                    // that the event manager is NOT on the local machine
                    ef = new EventManagerInterface(evtMgrIP);
                }

                catch (Exception e) {
                    System.out.println("Error instantiating event manager interface: " + e);
                } // catch

            }// if

        }// if

        // Here we check to see if registration worked. If ef is null then the
        // event manager interface was not properly created.
        if (ef != null) {
            System.out.println("Registered with the event manager.\n\n");

            while (!isDone) {
                // Here is the main menu
                System.out.println("Select an Option: \n");
                System.out.println("1: What is my ID?");
                System.out.println("2: What was my registration time? ");
                System.out.println("3: Send Event Message.");
                System.out.println("4: Get Event List.");
                System.out.println("X: Exit \n");
                System.out.print("\n>>>> ");
                option = input.keyboardReadString();
		//////////// option 1 ////////////
                // Here we print out the participants ID number. This is established when
                // the event interface is instantiated. If the participant is not
                // registered an exception is thrown.
                if (option.equals("1")) {
                    try {
                        long MyParticipantID = ef.getMyId();
                        System.out.println("\nMy participant id = " + MyParticipantID);
                    } // try
                    catch (Exception e) {
                        System.out.println("Error:: " + e);
                    } // catch

                }
		//////////// option 2 ////////////
                // Here we print out the participants registrations time. This is actual time
                // that the event interface was instantiated. If the participant is not
                // registered an exception is thrown.
                if (option.equals("2")) {
                    try {
                        String registrationTime = ef.getRegistrationTime();
                        System.out.println("\nMy participant id = " + registrationTime);
                    } // try
                    catch (Exception e) {
                        System.out.println("Error:: " + e);
                    } // catch
                }
                //////////// option 3 ////////////
                if (option.equals("3")) {
                    // Here we get an event ID from the user,... it has to be an integer,
                    // so the input is verified. If the input is not an integer, we chastise
                    // the user and ask them to try again...

                    isError = true;

                    while (isError) {
                        System.out.println("\nEnter an integer event ID: ");
                        option = input.keyboardReadString();

                        if (input.isNumber(option)) {
                            isError = false;
                            EvtId = Integer.valueOf(option).intValue();
                        }
                        else {
                            System.out.println("Please enter an integer value... try again...");
                        } // if

                    } // while
                    // Here ask the user to provide a string message to post with the event.
                    System.out.println("\nEnter a string to post with the event, or enter to continue: ");
                    option = input.keyboardReadString();
                    // Here we create the event.
                    evt = new Event(EvtId, option);
                    // Here we send the event to the event manager.
                    try {
                        ef.sendEvent(evt);
                        System.out.println("Message posted.");
                    } // try
                    catch (Exception e) {
                        System.out.println("Error:: " + e);
                    } // catch

                } // if
                //////////// option 4 ////////////
                if (option.equals("4")) {
                    // Here we get the event queue for this event interface from the event manager.
                    isError = true;
                    try {
                        eq = ef.getEventQueue();

                        System.out.println(eq.getSize() + " messages received...");
                        System.out.println("=========================");

                        int qlen = eq.getSize();

                        for (int i = 0; i < qlen; i++) {
                            evt = eq.getEvent();
                            System.out.print((i + 1) + "::Sender ID: " + evt.getSenderId());
                            System.out.print(":: Event ID:: " + evt.getEventId());
                            System.out.println("::" + evt.getMessage());
                        } // for
                        System.out.println("\n");
                    } // try
                    catch (Exception e) {
                        System.out.println("Error getting event list: " + e);
                    } // catch
                } // if

                //////////// option X ////////////
                if (option.equalsIgnoreCase("X")) {
                    // Here the user is done, so we set the Done flag and unregister
                    // the event interface from the event manager. If you fail to
                    // unregister, the event manager doesn't know to remove queues.
                    // These become dead queues and they collect events and will eventually
                    // cause problems for the event manager.
                    isDone = true;

                    try {
                        ef.unRegister();
                    } // try
                    catch (Exception e) {
                        System.out.println("Error unregistering: " + e);
                    } // catch
                } // if
            } // while
        }
        else {
            System.out.println("Unable to register with the event manager.\n\n");
        } // if
    } // main
} // EventTest
