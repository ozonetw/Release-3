/**
 * **************************************************************************************
 * File:HumiditySensor.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Autonomous University of Zacatecas 
 * Date: November 2015
 * Developer: Ferman Ivan Tovar 
 * Reviewer: Perla Velasco Elizondo
 * **************************************************************************************
 * This class simulates a humidity sensor. It polls the event manager for events
 * corresponding to changes in state of the humidifier or dehumidifier and
 * reacts to them by trending the relative humidity up or down. The current
 * relative humidity is posted to the event manager.
 * **************************************************************************************
 */
package sensors;

import common.Component;
import event.EventManagerInterface;
import instrumentation.MessageWindow;

public class HumiditySensor extends Sensor implements Runnable {

    private boolean humidifierState = false;	// Humidifier state: false == off, true == on
    private boolean dehumidifierState = false;	// Dehumidifier state: false == off, true == on
    private float relativeHumidity;		// Current simulated ambient room humidity
    
    private static HumiditySensor INSTANCE = new HumiditySensor();
    
    private HumiditySensor(){
    }

    @Override
    public void run() {
        // Here we check to see if registration worked. If ef is null then the
        // event manager interface was not properly created.
        if (evtMgrI != null) {

            // We create a message window. Note that we place this panel about 1/2 across 
            // and 2/3s down the screen
            float winPosX = 0.5f; 	//This is the X position of the message window in terms 
            //of a percentage of the screen height
            float winPosY = 0.60f;	//This is the Y position of the message window in terms 
            //of a percentage of the screen height 

            MessageWindow messageWin = new MessageWindow("Humidity Sensor", winPosX, winPosY);
            messageWin.writeMessage("Registered with the event manager.");

            try {
                messageWin.writeMessage("   Participant id: " + evtMgrI.getMyId());
                messageWin.writeMessage("   Registration Time: " + evtMgrI.getRegistrationTime());
            } 
            catch (Exception e) {
                messageWin.writeMessage("Error:: " + e);
            } 

            messageWin.writeMessage("\nInitializing Humidity Simulation::");
            relativeHumidity = getRandomNumber() * (float) 100.00;
            if (coinToss()) {
                driftValue = getRandomNumber() * (float) -1.0;
            }
            else {
                driftValue = getRandomNumber();
            } 
            messageWin.writeMessage("   Initial Humidity Set:: " + relativeHumidity);
            messageWin.writeMessage("   Drift Value Set:: " + driftValue);
            
            /**
             * ******************************************************************
             ** Here we start the main simulation loop
             * *******************************************************************
             */
            messageWin.writeMessage("Beginning Simulation... ");
            while (!isDone) {
                // Post the current relative humidity
                postEvent(evtMgrI, HUMIDITY, relativeHumidity);
                messageWin.writeMessage("Current Relative Humidity:: " + relativeHumidity + "%");
                // Get the message queue
                try {
                    queue = evtMgrI.getEventQueue();
                } 
                catch (Exception e) {
                    messageWin.writeMessage("Error getting event queue::" + e);
                } 

                // If there are messages in the queue, we read through them.
                // We are looking for EventIDs = -4, this means the the humidify or
                // dehumidifier has been turned on/off. Note that we get all the messages
                // from the queue at once... there is a 2.5 second delay between samples,..
                // so the assumption is that there should only be a message at most.
                // If there are more, it is the last message that will effect the
                // output of the humidity as it would in reality.
                int qlen = queue.getSize();

                for (int i = 0; i < qlen; i++) {
                    evt = queue.getEvent();
                    if (evt.getEventId() == HUMIDITY_SENSOR) {
                        if (evt.getMessage().equalsIgnoreCase(HUMIDIFIER_ON)) // humidifier on
                        {
                            humidifierState = true;
                        } 

                        if (evt.getMessage().equalsIgnoreCase(HUMIDIFIER_OFF)) // humidifier off
                        {
                            humidifierState = false;
                        } 

                        if (evt.getMessage().equalsIgnoreCase(DEHUMIDIFIER_ON)) // dehumidifier on
                        {
                            dehumidifierState = true;
                        }

                        if (evt.getMessage().equalsIgnoreCase(DEHUMIDIFIER_OFF)) // dehumidifier off
                        {
                            dehumidifierState = false;
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
                    } 
                } 

                // Now we trend the relative humidity according to the status of the
                // humidifier/dehumidifier controller.
                if (humidifierState) {
                    relativeHumidity += getRandomNumber();
                } // if humidifier is on

                if (!humidifierState && !dehumidifierState) {
                    relativeHumidity += driftValue;
                } // if both the humidifier and dehumidifier are off

                if (dehumidifierState) {
                    relativeHumidity -= getRandomNumber();
                } // if dehumidifier is on

                // Here we wait for a 2.5 seconds before we start the next sample
                try {
                    Thread.sleep(delay);
                }
                catch (Exception e) {
                    messageWin.writeMessage("Sleep error:: " + e);
                } 
            } 
        }
        else {
            System.out.println("Unable to register with the event manager.\n\n");
        } 
    }
    
    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (HumiditySensor.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HumiditySensor();
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
    public static HumiditySensor getInstance() {
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
        HumiditySensor sensor = HumiditySensor.getInstance();
        sensor.run();
    }

} 
