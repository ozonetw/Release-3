/**
 * **************************************************************************************
 * File:TemperatureSensor.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Autonomous University of Zacatecas 
 * Date: November 2015
 * Developer: Ferman Ivan Tovar 
 * Reviewer: Perla Velasco Elizondo
 * **************************************************************************************
 * This class simulates a temperature sensor. It polls the event manager for
 * events corresponding to changes in state of the heater or chiller and reacts
 * to them by trending the ambient temperature up or down. The current ambient
 * room temperature is posted to the event manager.
 * **************************************************************************************
 */
package sensors;

import common.Component;
import instrumentation.MessageWindow;

public class TemperatureSensor extends Sensor implements Runnable {

    private boolean heaterState = false;	// Heater state: false == off, true == on
    private boolean chillerState = false;	// Chiller state: false == off, true == on
    private float currentTemperature;		// Current simulated ambient room temperature

    private static TemperatureSensor INSTANCE = new TemperatureSensor();

    private TemperatureSensor() {
        super();
    }

    public void run() {
        // Here we check to see if registration worked. If ef is null then the
        // event manager interface was not properly created.
        if (evtMgrI != null) {

            // We create a message window. Note that we place this panel about 1/2 across 
            // and 1/3 down the screen			
            float winPosX = 0.5f; 	//This is the X position of the message window in terms 
            //of a percentage of the screen height
            float winPosY = 0.3f; 	//This is the Y position of the message window in terms 
            //of a percentage of the screen height 

            MessageWindow messageWin = new MessageWindow("Temperature Sensor", winPosX, winPosY);
            messageWin.writeMessage("Registered with the event manager.");

            try {
                messageWin.writeMessage("   Participant id: " + evtMgrI.getMyId());
                messageWin.writeMessage("   Registration Time: " + evtMgrI.getRegistrationTime());
            } // try
            catch (Exception e) {
                messageWin.writeMessage("Error:: " + e);
            } // catch

            messageWin.writeMessage("\nInitializing Temperature Simulation::");
            currentTemperature = (float) 50.00;
            if (coinToss()) {
                driftValue = getRandomNumber() * (float) -1.0;
            }
            else {
                driftValue = getRandomNumber();
            } // if

            messageWin.writeMessage("   Initial Temperature Set:: " + currentTemperature);
            messageWin.writeMessage("   Drift Value Set:: " + driftValue);

            /**
             * ******************************************************************
             ** Here we start the main simulation loop
             * *******************************************************************
             */
            messageWin.writeMessage("Beginning Simulation... ");

            while (!isDone) {
                // Post the current temperature
                postEvent(evtMgrI, TEMPERATURE, currentTemperature);
                messageWin.writeMessage("Current Temperature::  " + currentTemperature + " F");

                // Get the message queue
                try {
                    queue = evtMgrI.getEventQueue();
                } // try
                catch (Exception e) {
                    messageWin.writeMessage("Error getting event queue::" + e);
                } // catch

                // If there are messages in the queue, we read through them.
                // We are looking for EventIDs = -5, this means the the heater
                // or chiller has been turned on/off. Note that we get all the messages
                // at once... there is a 2.5 second delay between samples,.. so
                // the assumption is that there should only be a message at most.
                // If there are more, it is the last message that will effect the
                // output of the temperature as it would in reality.
                int qlen = queue.getSize();

                for (int i = 0; i < qlen; i++) {
                    evt = queue.getEvent();
                    if (evt.getEventId() == TEMPERATURE_SENSOR) {
                        if (evt.getMessage().equalsIgnoreCase(HEATER_ON)) // heater on
                        {
                            heaterState = true;
                        } // if

                        if (evt.getMessage().equalsIgnoreCase(HEATER_OFF)) // heater off
                        {
                            heaterState = false;
                        } // if

                        if (evt.getMessage().equalsIgnoreCase(CHILLER_ON)) // chiller on
                        {
                            chillerState = true;
                        } // if

                        if (evt.getMessage().equalsIgnoreCase(CHILLER_OFF)) // chiller off
                        {
                            chillerState = false;
                        } // if
                    } // if

                    // If the event ID == 99 then this is a signal that the simulation
                    // is to end. At this point, the loop termination flag is set to
                    // true and this process unregisters from the event manager.
                    if (evt.getEventId() == END) {
                        isDone = true;
                        try {
                            evtMgrI.unRegister();
                        } // try
                        catch (Exception e) {
                            messageWin.writeMessage("Error unregistering: " + e);
                        } // catch
                        messageWin.writeMessage("\n\nSimulation Stopped. \n");
                    } // if
                } // for

                // Now we trend the temperature according to the status of the
                // heater/chiller controller.
                if (heaterState) {
                    currentTemperature += getRandomNumber();
                } // if heater is on
                if (!heaterState && !chillerState) {
                    currentTemperature += driftValue;
                } // if both the heater and chiller are off
                if (chillerState) {
                    currentTemperature -= getRandomNumber();
                } // if chiller is on
                // Here we wait for a 2.5 seconds before we start the next sample
                try {
                    Thread.sleep(delay);
                } // try
                catch (Exception e) {
                    messageWin.writeMessage("Sleep error:: " + e);
                } // catch
            } // while
        }
        else {
            System.out.println("Unable to register with the event manager.\n\n");
        } // if
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (TemperatureSensor.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TemperatureSensor();
                }
            }
        }
    }
    
    /**
     * This method calls createInstance method to creates and ensure that 
     * only one instance of this class is created. Singleton design pattern.
     * 
     * @return The instance of this class.
     */
    public static TemperatureSensor getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    /**
     * Start this sensor
     * 
     * @param args IP address of the event manager (on command line). 
     * If blank, it is assumed that the event manager is on the local machine.
     */
    public static void main(String args[]) {
        if(args[0] != null) Component.SERVER_IP = args[0];
        TemperatureSensor sensor = TemperatureSensor.getInstance();
        sensor.run();
    }

}
