/**
 * **************************************************************************************
 * File:TemperatureController.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Autonomous University of Zacatecas 
 * Date: November 2015
 * Developer: Ferman Ivan Tovar 
 * Reviewer: Perla Velasco Elizondo
 * **************************************************************************************
 * This class simulates a device that controls a heater and chiller. 
 * It polls the event manager for event ids = 5 and reacts to them by turning 
 * on or off the heater or chiller. The following command are valid strings for 
 * controlling the heater and chiller.
 * H1 = heater on 
 * H0 = heater off 
 * C1 = chiller on 
 * C0 = chiller off
 * **************************************************************************************
 */

package controllers;

import common.Component;
import instrumentation.Indicator;
import instrumentation.MessageWindow;

public class TemperatureController extends Controller implements Runnable {

    private boolean heaterState = false;	// Heater state: false == off, true == on
    private boolean chillerState = false;	// Chiller state: false == off, true == on
    
    private static TemperatureController INSTANCE = new TemperatureController();
    
    private TemperatureController(){
    }

    @Override
    public void run() {

        // Here we check to see if registration worked. If ef is null then the
        // event manager interface was not properly created.
        if (evtMgrI != null) {
            System.out.println("Registered with the event manager.");

            /* Now we create the temperature control status and message panel
             ** We put this panel about 1/3 the way down the terminal, aligned to the left
             ** of the terminal. The status indicators are placed directly under this panel
             */
            float winPosX = 0.0f; 	//This is the X position of the message window in terms 
            //of a percentage of the screen height
            float winPosY = 0.3f; 	//This is the Y position of the message window in terms 
            //of a percentage of the screen height 

            MessageWindow messageWin = new MessageWindow("Temperature Controller Status Console", winPosX, winPosY);

            // Put the status indicators under the panel...
            Indicator chillIndicator = new Indicator("Chiller OFF", messageWin.getX(), messageWin.getY() + messageWin.height());
            Indicator heatIndicator = new Indicator("Heater OFF", messageWin.getX() + (chillIndicator.width() * 2), messageWin.getY() + messageWin.height());

            messageWin.writeMessage("Registered with the event manager.");

            try {
                messageWin.writeMessage("   Participant id: " + evtMgrI.getMyId());
                messageWin.writeMessage("   Registration Time: " + evtMgrI.getRegistrationTime());
            }
            catch (Exception e) {
                System.out.println("Error:: " + e);
            }

            /**
             * ******************************************************************
             ** Here we start the main simulation loop
             * *******************************************************************
             */
            while (!isDone) {
                try {
                    queue = evtMgrI.getEventQueue();
                }
                catch (Exception e) {
                    messageWin.writeMessage("Error getting event queue::" + e);
                }

                // If there are messages in the queue, we read through them.
                // We are looking for EventIDs = 5, this is a request to turn the
                // heater or chiller on. Note that we get all the messages
                // at once... there is a 2.5 second delay between samples,.. so
                // the assumption is that there should only be a message at most.
                // If there are more, it is the last message that will effect the
                // output of the temperature as it would in reality.
                int qlen = queue.getSize();

                for (int i = 0; i < qlen; i++) {
                    evt = queue.getEvent();
                    if (evt.getEventId() == TEMPERATURE_CONTROLLER) {
                        if (evt.getMessage().equalsIgnoreCase(HEATER_ON)) { // heater on
                            heaterState = true;
                            messageWin.writeMessage("Received heater on event");
                            // Confirm that the message was recieved and acted on
                            confirmMessage(evtMgrI, TEMPERATURE_SENSOR, HEATER_ON);
                        }

                        if (evt.getMessage().equalsIgnoreCase(HEATER_OFF)) { // heater off
                            heaterState = false;
                            messageWin.writeMessage("Received heater off event");
                            // Confirm that the message was recieved and acted on
                            confirmMessage(evtMgrI, TEMPERATURE_SENSOR, HEATER_OFF);
                        }

                        if (evt.getMessage().equalsIgnoreCase(CHILLER_ON)) { // chiller on
                            chillerState = true;
                            messageWin.writeMessage("Received chiller on event");
                            // Confirm that the message was recieved and acted on
                            confirmMessage(evtMgrI, TEMPERATURE_SENSOR, CHILLER_ON);
                        }

                        if (evt.getMessage().equalsIgnoreCase(CHILLER_OFF)) { // chiller off
                            chillerState = false;
                            messageWin.writeMessage("Received chiller off event");
                            // Confirm that the message was recieved and acted on
                            confirmMessage(evtMgrI, TEMPERATURE_SENSOR, CHILLER_OFF);
                        }
                    }
                    // If the event ID == 99 then this is a signal that the simulation
                    // is to end. At this point, the loop termination flag is set to
                    // true and this process unregisters from the event manager.
                    if (evt.getEventId() == END) {
                        isDone = true;
                        try {
                            evtMgrI.unRegister();
                        }
                        catch (Exception e) {
                            messageWin.writeMessage("Error unregistering: " + e);
                        }
                        messageWin.writeMessage("\n\nSimulation Stopped. \n");
                        // Get rid of the indicators. The message panel is left for the
                        // user to exit so they can see the last message posted.
                        heatIndicator.dispose();
                        chillIndicator.dispose();
                    }
                }

                // Update the lamp status
                if (heaterState) {
                    // Set to green, heater is on
                    heatIndicator.setLampColorAndMessage("HEATER ON", 1);
                }
                else {
                    // Set to black, heater is off
                    heatIndicator.setLampColorAndMessage("HEATER OFF", 0);
                }
                if (chillerState) {
                    // Set to green, chiller is on
                    chillIndicator.setLampColorAndMessage("CHILLER ON", 1);
                }
                else {
                    // Set to black, chiller is off
                    chillIndicator.setLampColorAndMessage("CHILLER OFF", 0);
                }
                try {
                    Thread.sleep(delay);
                }
                catch (Exception e) {
                    System.out.println("Sleep error:: " + e);
                }
            }
        }
        else {
            System.out.println("Unable to register with the event manager.\n\n");
        }
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (TemperatureController.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TemperatureController();
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
    public static TemperatureController getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }
    
    /**
     * Start this controller
     * 
     * @param args IP address of the event manager (on command line). 
     * If blank, it is assumed that the event manager is on the local machine.
     */
    public static void main(String args[]) {
        if(args[0] != null) Component.SERVER_IP = args[0];
        TemperatureController sensor = TemperatureController.getInstance();
        sensor.run();
    }

} // TemperatureController
