 /**
 * **************************************************************************************
 * File:ECSMonitor.java 
 * Course: Software Architecture 
 * Project: Event Architectures  
 * Institution: Autonomous University of Zacatecas 
 * Date: November 2015
 * Developer: Ferman Ivan Tovar 
 * Reviewer: Perla Velasco Elizondo
 * **************************************************************************************
 * This class monitors the environmental control systems that control museum
 * temperature and humidity. In addition to monitoring the temperature and
 * humidity, the ECSMonitor also allows a user to set the humidity and
 * temperature ranges to be maintained. If temperatures exceed those limits
 * over/under alarm indicators are triggered.
 * **************************************************************************************
 */
import common.Component;
import instrumentation.*;
import event.*;

public class ECSMonitor extends Thread {

    private EventManagerInterface em = null;            // Interface object to the event manager
    private String evtMgrIP = null;			// Event Manager IP address
    private float tempRangeHigh = 100;                  // These parameters signify the temperature and humidity ranges in terms
    private float tempRangeLow = 0;			// of high value and low values. The ECSmonitor will attempt to maintain
    private float humiRangeHigh = 100;                  // this temperature and humidity. Temperatures are in degrees Fahrenheit
    private float humiRangeLow = 0;			// and humidity is in relative humidity percentage.
    boolean registered = true;				// Signifies that this class is registered with an event manager.
    MessageWindow messageWin = null;			// This is the message window
    Indicator tempIndicator;				// Temperature indicator
    Indicator humIndicator;				// Humidity indicator

    public ECSMonitor() {
        // event manager is on the local system
        try {
            // Here we create an event manager interface object. This assumes
            // that the event manager is on the local machine
            em = new EventManagerInterface();
        }
        catch (Exception e) {
            System.out.println("ECSMonitor::Error instantiating event manager interface: " + e);
            registered = false;
        } // catch // catch
    } //Constructor

    public ECSMonitor(String evmIpAddress) {
        // event manager is not on the local system
        evtMgrIP = evmIpAddress;
        try {
            // Here we create an event manager interface object. This assumes
            // that the event manager is NOT on the local machine
            em = new EventManagerInterface(evtMgrIP);
            Component.SERVER_IP = evtMgrIP;
        }
        catch (Exception e) {
            System.out.println("ECSMonitor::Error instantiating event manager interface: " + e);
            registered = false;
        } // catch // catch
    } // Constructor

    @Override
    public void run() {
        Event evt = null;			// Event object
        EventQueue eq = null;			// Message Queue
        int evtId = 0;				// User specified event ID
        float currentTemperature = 0;           // Current temperature as reported by the temperature sensor
        float currentHumidity = 0;		// Current relative humidity as reported by the humidity sensor
        int delay = 1000;			// The loop delay (1 second)
        boolean isDone = false;			// Loop termination flag
        boolean on = true;			// Used to turn on heaters, chillers, humidifiers, and dehumidifiers
        boolean off = false;			// Used to turn off heaters, chillers, humidifiers, and dehumidifiers

        if (em != null) {
            // Now we create the ECS status and message panel
            // Note that we set up two indicators that are initially yellow. This is
            // because we do not know if the temperature/humidity is high/low.
            // This panel is placed in the upper left hand corner and the status 
            // indicators are placed directly to the right, one on top of the other

            messageWin = new MessageWindow("ECS Monitoring Console", 0, 0);
            tempIndicator = new Indicator("TEMP UNK", messageWin.getX() + messageWin.width(), 0);
            humIndicator = new Indicator("HUMI UNK", messageWin.getX() + messageWin.width(), (int) (messageWin.height() / 2), 2);

            messageWin.writeMessage("Registered with the event manager.");

            try {
                messageWin.writeMessage("   Participant id: " + em.getMyId());
                messageWin.writeMessage("   Registration Time: " + em.getRegistrationTime());
            } // try // try
            catch (Exception e) {
                System.out.println("Error:: " + e);
            } // catch

            /**
             * ******************************************************************
             ** Here we start the main simulation loop
             * *******************************************************************
             */
            while (!isDone) {
                // Here we get our event queue from the event manager
                try {
                    eq = em.getEventQueue();
                } // try
                catch (Exception e) {
                    messageWin.writeMessage("Error getting event queue::" + e);
                } // catch // catch

                // If there are messages in the queue, we read through them.
                // We are looking for EventIDs = 1 or 2. Event IDs of 1 are temperature
                // readings from the temperature sensor; event IDs of 2 are humidity sensor
                // readings. Note that we get all the messages at once... there is a 1
                // second delay between samples,.. so the assumption is that there should
                // only be a message at most. If there are more, it is the last message
                // that will effect the status of the temperature and humidity controllers
                // as it would in reality.
                int qlen = eq.getSize();

                for (int i = 0; i < qlen; i++) {
                    evt = eq.getEvent();

                    if (evt.getEventId() == 1) { // Temperature reading
                        try {
                            currentTemperature = Float.valueOf(evt.getMessage()).floatValue();
                        } // try
                        catch (Exception e) {
                            messageWin.writeMessage("Error reading temperature: " + e);
                        } // catch // catch
                    } // if

                    if (evt.getEventId() == 2) { // Humidity reading
                        try {
                            currentHumidity = Float.valueOf(evt.getMessage()).floatValue();
                        } // try
                        catch (Exception e) {
                            messageWin.writeMessage("Error reading humidity: " + e);
                        } // catch // catch
                    } // if

                    // If the event ID == 99 then this is a signal that the simulation
                    // is to end. At this point, the loop termination flag is set to
                    // true and this process unregisters from the event manager.
                    if (evt.getEventId() == 99) {
                        isDone = true;
                        try {
                            em.unRegister();
                        } // try
                        catch (Exception e) {
                            messageWin.writeMessage("Error unregistering: " + e);
                        } // catch // catch

                        messageWin.writeMessage("\n\nSimulation Stopped. \n");
                        // Get rid of the indicators. The message panel is left for the
                        // user to exit so they can see the last message posted.
                        humIndicator.dispose();
                        tempIndicator.dispose();
                    } // if
                } // for

                messageWin.writeMessage("Temperature:: " + currentTemperature + "F  Humidity:: " + currentHumidity);
                // Check temperature and effect control as necessary
                if (currentTemperature < tempRangeLow) { // temperature is below threshhold
                    tempIndicator.setLampColorAndMessage("TEMP LOW", 3);
                    heater(on);
                    chiller(off);
                }
                else {
                    if (currentTemperature > tempRangeHigh) { // temperature is above threshhold
                        tempIndicator.setLampColorAndMessage("TEMP HIGH", 3);
                        heater(off);
                        chiller(on);
                    }
                    else {
                        tempIndicator.setLampColorAndMessage("TEMP OK", 1); // temperature is within threshhold
                        heater(off);
                        chiller(off);
                    } // if
                } // if

                // Check humidity and effect control as necessary
                if (currentHumidity < humiRangeLow) {
                    humIndicator.setLampColorAndMessage("HUMI LOW", 3); // humidity is below threshhold
                    humidifier(on);
                    dehumidifier(off);
                }
                else {
                    if (currentHumidity > humiRangeHigh) { // humidity is above threshhold
                        humIndicator.setLampColorAndMessage("HUMI HIGH", 3);
                        humidifier(off);
                        dehumidifier(on);
                    }
                    else {
                        humIndicator.setLampColorAndMessage("HUMI OK", 1); // humidity is within threshhold
                        humidifier(off);
                        dehumidifier(off);
                    } // if
                } // if

                // This delay slows down the sample rate to Delay milliseconds
                try {
                    Thread.sleep(delay);
                } // try
                catch (Exception e) {
                    System.out.println("Sleep error:: " + e);
                } // catch
            } // while
        }
        else {
            System.out.println("Unable to register with the event manager.\n\n");
        } // if
    } // main

    /**
     * This method returns the registered status
     *
     * @return boolean true if registered, false if not registered
     */
    public boolean isRegistered() {
        return (registered);
    } // setTemperatureRange

    /**
     * This method sets the temperature range
     *
     * @param lowtemp low temperature range
     * @param hightemp high temperature range
     */
    public void setTemperatureRange(float lowtemp, float hightemp) {
        tempRangeHigh = hightemp;
        tempRangeLow = lowtemp;
        messageWin.writeMessage("***Temperature range changed to::" + tempRangeLow + "F - " + tempRangeHigh + "F***");
    } // setTemperatureRange

    /**
     * This method sets the humidity range
     *
     * @param lowhumi low humidity range
     * @param highhumi high humidity range
     */
    public void setHumidityRange(float lowhumi, float highhumi) {
        humiRangeHigh = highhumi;
        humiRangeLow = lowhumi;
        messageWin.writeMessage("***Humidity range changed to::" + humiRangeLow + "% - " + humiRangeHigh + "%***");
    } // setTemperatureRange

    /**
     * This method posts an event that stops the environmental control system.
     * Exceptions: Posting to event manager exception
     */
    public void halt() {
        messageWin.writeMessage("***HALT MESSAGE RECEIVED - SHUTTING DOWN SYSTEM***");
        // Here we create the stop event.
        Event evt;
        evt = new Event(Component.END, "XXX");
        // Here we send the event to the event manager.
        try {
            em.sendEvent(evt);
        }
        catch (Exception e) {
            System.out.println("Error sending halt message:: " + e);
        }
    } // halt

    /**
     * This method posts events that will signal the temperature controller to
     * turn on/off the heater
     *
     * @param ON indicates whether to turn the heater on or off. Exceptions:
     * Posting to event manager exception
     */
    private void heater(boolean ON) {
        // Here we create the event.
        Event evt;
        if (ON) {
            evt = new Event(Component.TEMPERATURE_CONTROLLER, Component.HEATER_ON);
        }
        else {
            evt = new Event(Component.TEMPERATURE_CONTROLLER, Component.HEATER_OFF);
        } // if
        // Here we send the event to the event manager.
        try {
            em.sendEvent(evt);
        } // try
        catch (Exception e) {
            System.out.println("Error sending heater control message:: " + e);
        } // catch
    } // heater

    /**
     * This method posts events that will signal the temperature controller to
     * turn on/off the chiller
     *
     * @param ON indicates whether to turn the chiller on or off. Exceptions:
     * Posting to event manager exception
     */
    private void chiller(boolean ON) {
        // Here we create the event.
        Event evt;
        if (ON) {
            evt = new Event(Component.TEMPERATURE_CONTROLLER, Component.CHILLER_ON);
        }
        else {
            evt = new Event(Component.TEMPERATURE_CONTROLLER, Component.CHILLER_OFF);
        } // if
        // Here we send the event to the event manager.
        try {
            em.sendEvent(evt);
        } // try
        catch (Exception e) {
            System.out.println("Error sending chiller control message:: " + e);
        } // catch
    } // Chiller

    /**
     * This method posts events that will signal the humidity controller to turn
     * on/off the humidifier
     *
     * @param ON indicates whether to turn the humidifier on or off. Exceptions:
     * Posting to event manager exception
     */
    private void humidifier(boolean ON) {
        // Here we create the event.
        Event evt;
        if (ON) {
            evt = new Event(Component.HUMIDITY_CONTROLLER, Component.HUMIDIFIER_ON);
        }
        else {
            evt = new Event(Component.HUMIDITY_CONTROLLER, Component.HUMIDIFIER_OFF);
        } // if
        // Here we send the event to the event manager.
        try {
            em.sendEvent(evt);
        } // try
        catch (Exception e) {
            System.out.println("Error sending humidifier control message::  " + e);
        } // catch
    } // Humidifier

    /**
     * This method posts events that will signal the humidity controller to turn
     * on/off the dehumidifier
     *
     * @param ON indicates whether to turn the dehumidifier on or off.
     * Exceptions: Posting to event manager exception
     */
    private void dehumidifier(boolean ON) {
        // Here we create the event.
        Event evt;
        if (ON) {
            evt = new Event(Component.HUMIDITY_CONTROLLER, Component.DEHUMIDIFIER_ON);
        }
        else {
            evt = new Event(Component.HUMIDITY_CONTROLLER, Component.DEHUMIDIFIER_OFF);
        } // if
        // Here we send the event to the event manager.
        try {
            em.sendEvent(evt);
        } // try
        catch (Exception e) {
            System.out.println("Error sending dehumidifier control message::  " + e);
        } // catch
    } // Dehumidifier
} // ECSMonitor
