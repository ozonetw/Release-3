import common.Component;
import event.Event;
import event.EventManagerInterface;
import event.EventQueue;
import instrumentation.Indicator;
import instrumentation.MessageWindow;

import static common.Component.*;

/**
 * Created by angel on 25/04/16.
 */
public class AlarmMonitor extends Thread {

    boolean registered = true;                      //When true, the class is registered with an event manager
    MessageWindow messageWin = null;                //The actual message window
    Indicator DoorIndicator;                        //The door indicator
    Indicator windowIndicator;                      //The Window Indicator
    Indicator moveIndicator;                         //The movement Indicator
    Indicator fireIndicator;
    Indicator sprinklerIndicator;
    private EventManagerInterface em = null;        //interface object to the event manager
    private String evtMgrIP = null;                 //Event manager IP address
    private boolean userInput = true;                                //2= neutral
    private boolean tuguefa = false;

    public AlarmMonitor() {
        // event manager is on the local system
        try {
            // Here we create an event manager interface object. This assumes
            // that the event manager is on the local machine
            em = new EventManagerInterface();
        } catch (Exception e) {
            System.out.println("AlarmMonitor::Error instantiating event manager interface: " + e);
            registered = false;
        } // catch // catch

    }//Constructor end

    public AlarmMonitor(String evmIpAddress) {
        // When event manager is not on the local system
        evtMgrIP = evmIpAddress;
        try {
            // Here we create an event manager interface object. This assumes
            // that the event manager is NOT on the local machine
            em = new EventManagerInterface(evtMgrIP);
            Component.SERVER_IP = evtMgrIP;
        } catch (Exception e) {
            System.out.println("AlarmMonitor::Error instantiating event manager interface: " + e);
            registered = false;
        } // End Constructor

    }

    @Override
    public void run() {
        Event evt = null;            // Event object
        EventQueue eq = null;            // Message Queue
//        int evtId = -8;				// User specified event
        float currentDoorState = 0;
        float currentWindowState = 0;          //Current window state reported by the sensor
        float currentMovementState = 0;        //Current movement state reported by the sensor.
        float currentFireState = 0;
        float currentSprinklerState = 0;
        int delay = 1000;            // The loop delay (1 second)
        boolean isDone = false;            // Loop termination flag
        boolean on = true;            // Used to turn on Doors, Windows, heaters, chillers, humidifiers, and dehumidifiers
        boolean off = false;            // Used to turn off Doors, Windows, heaters, chillers, humidifiers, and dehumidifiers

        if (em != null) {
            // Now we create the ECS status and message panel
            // Note that we set up two indicators that are initially yellow. This is
            // because we do not know if the temperature/humidity is high/low.
            // This panel is placed in the upper left hand corner and the status
            // indicators are placed directly to the right, one on top of the other

            messageWin = new MessageWindow("Alarm Monitoring Console", 0, 0);
            DoorIndicator = new Indicator("Door State", messageWin.getX() + messageWin.width(), 0);
            windowIndicator = new Indicator("Window State", messageWin.getX() + messageWin.width(), messageWin.height() / 2, 2);
            moveIndicator = new Indicator("Movement State", messageWin.getX() + messageWin.width() + 200, 0);
            fireIndicator = new Indicator("Fire State", messageWin.getX() + messageWin.width() + 300, 0);
            sprinklerIndicator = new Indicator("Sprinkler State", messageWin.getX() + messageWin.width() + 400, 0);
            //humIndicator = new Indicator("HUMI UNK", messageWin.getX() + messageWin.width(), (int) (messageWin.height() / 2), 2);

            messageWin.writeMessage("Registered with the event managerasdasdasdsda.");

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
            //while(systemState == 0) {
            while (!isDone) {
                // get event queue from event manager
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
//                    evt = new Event(evtId);

                    if (evt.getEventId() == DOOR) { // Door reading
                        try {
                            currentDoorState = Float.valueOf(evt.getMessage());

                        } // try
                        catch (Exception e) {
                            messageWin.writeMessage("Error reading Door state: " + e);
                        } // catch // catch
                    } // if

                    if (evt.getEventId() == WINDOW) { // Window reading
                        try {
                            currentWindowState = Float.valueOf(evt.getMessage());

                        } // try
                        catch (Exception e) {
                            messageWin.writeMessage("Error reading Window state: " + e);
                        } // catch // catch
                    } // if

                    if (evt.getEventId() == MOVE) { // Movement reading
                        try {
                            currentMovementState = Float.valueOf(evt.getMessage());

                        } // try
                        catch (Exception e) {
                            messageWin.writeMessage("Error reading Movement state: " + e);
                        } // catch // catch
                    } // if
                    if (evt.getEventId() == FIRE) { // Movement reading
                        try {
                            currentFireState = Float.valueOf(evt.getMessage());

                        } // try
                        catch (Exception e) {
                            messageWin.writeMessage("Error reading Fire state: " + e);
                        } // catch // catch
                    } // if
                    if (evt.getEventId() == SPRINKLER) { // Movement reading
                        try {
                            currentSprinklerState = Float.valueOf(evt.getMessage());

                        } // try
                        catch (Exception e) {
                            messageWin.writeMessage("Error reading Sprinkler state: " + e);
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
                        } // catch

                        messageWin.writeMessage("\n\nSimulation Stopped. \n");
                        // Get rid of the indicators. The message panel is left for the
                        // user to exit so they can see the last message posted.
                        DoorIndicator.dispose();
                    } // if
                } // for

                messageWin.writeMessage("Door is:: " + currentDoorState);
                messageWin.writeMessage("Window is:: " + currentWindowState);
                messageWin.writeMessage("Move detection is:: " + currentMovementState);
                messageWin.writeMessage("Fire detection is:: " + currentFireState);
                messageWin.writeMessage("Sprinkler detection is:: " + currentSprinklerState);

                //Check door and effect control as necessary
                if (currentDoorState == 0) {
                    DoorIndicator.setLampColorAndMessage("Door Broken", 3);
                    door(on);
                } else {
                    DoorIndicator.setLampColorAndMessage("Door Ok!", 1);
                    door(off);
                }

                //Check Window and effect control as necessary
                if (currentWindowState == 0) {
                    windowIndicator.setLampColorAndMessage("Window Broken", 3);
                    window(on);
                } else {
                    windowIndicator.setLampColorAndMessage("Window Ok", 1);
                    window(off);
                }

                //Check Movement and effect control as necessary
                if (currentMovementState == 0) {
                    moveIndicator.setLampColorAndMessage("No movement detected", 3);
                    move(on);
                } else {
                    moveIndicator.setLampColorAndMessage("Movement Detected!", 1);
                    move(off);
                }

                if (currentFireState == 0) {
                    fireIndicator.setLampColorAndMessage("No Fire detected", 3);
                    fire(on);
                } else {
                    fireIndicator.setLampColorAndMessage("Fire Detected!", 1);
                    fire(off);
                    timer();
                }

                if (currentSprinklerState == 0) {
                    sprinklerIndicator.setLampColorAndMessage("Sprinkler deactivated", 3);
                    sprinkler(on);
                } else {
                    sprinklerIndicator.setLampColorAndMessage("Sprinkler activated!", 1);
                    sprinkler(off);
                }


                // This delay slows down the sample rate to Delay milliseconds
                try {
                    Thread.sleep(delay);
                } // try
                catch (Exception e) {
                    System.out.println("Sleep error:: " + e);
                } // catch
            } // while
            //}
        } else {
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
     * This method posts an event that stops the Alarm monitoring system.
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
        } catch (Exception e) {
            System.out.println("Error sending halt message:: " + e);
        }
    } // halt


    /**
     * Method to set the window state

     public void setWindowState(float windowState) {
     this.windowState = windowState;
     messageWin.writeMessage("**Window state change to::" + this.windowState + "%***");

     }//Sets the window state

     /**
     * Method to set the movement state
     public void setMoveState(float moveState) {
     this.moveState = moveState;
     messageWin.writeMessage("**Move state change to::" + this.moveState + "%***");

     }//Sets the window state
     */
    /**
     * This method posts events that will signal the Alarm controller to
     * turn on/off the Door
     *
     * @param ON indicates whether to turn the Door on or off. Exceptions:
     *           Posting to event manager exception
     */
    private void door(boolean ON) {
        // Here we create the event.
        Event evt;
        if (ON) {
            evt = new Event(Component.ALARMS_CONTROLLER, Component.DOOR_ON);
        } else {
            evt = new Event(Component.ALARMS_CONTROLLER, Component.DOOR_OFF);
        } // if
        // Here we send the event to the event manager.
        try {
            em.sendEvent(evt);
        } // try
        catch (Exception e) {
            System.out.println("Error sending Door control message:: " + e);
        } // catch
    } // Door

    /**
     * This method posts events that will signal the Alarm controller to
     * turn on/off the Door
     *
     * @param ON indicates whether to turn the Door on or off. Exceptions:
     *           Posting to event manager exception
     */
    private void window(boolean ON) {
        // Here we create the event.
        Event evt;
        if (ON) {
            evt = new Event(Component.ALARMS_CONTROLLER, Component.WINDOW_ON);
        } else {
            evt = new Event(Component.ALARMS_CONTROLLER, Component.WINDOW_OFF);
        } // if
        // Here we send the event to the event manager.
        try {
            em.sendEvent(evt);
        } // try
        catch (Exception e) {
            System.out.println("Error sending Door control message:: " + e);
        } // catch
    } // Door

    /**
     * This method posts events that will signal the Alarm controller to
     * turn on/off the Door
     *
     * @param ON indicates whether to turn the Door on or off. Exceptions:
     *           Posting to event manager exception
     */
    private void move(boolean ON) {
        // Here we create the event.
        Event evt;
        if (ON) {
            evt = new Event(Component.ALARMS_CONTROLLER, Component.MOVE_ON);
        } else {

            evt = new Event(Component.ALARMS_CONTROLLER, Component.MOVE_OFF);
        } // if
        // Here we send the event to the event manager.
        try {
            em.sendEvent(evt);
        } // try
        catch (Exception e) {
            System.out.println("Error sending move control message:: " + e);
        } // catch
    } // Door

    private void fire(boolean ON) {
        // Here we create the event.
        Event evt;
        if (ON) {
            evt = new Event(Component.FIRE_CONTROLLER, Component.FIRE_ON);
        } else {

            evt = new Event(Component.FIRE_CONTROLLER, Component.FIRE_OFF);
        } // if
        // Here we send the event to the event manager.
        try {
            em.sendEvent(evt);
        } // try
        catch (Exception e) {
            System.out.println("Error sending fire control message:: " + e);
        } // catch
    } // Door

    private void sprinkler(boolean ON) {
        // Here we create the event.
        Event evt;
        if (ON) {
            evt = new Event(Component.SPRINKLER_CONTROLLER, Component.SPRINKLER_ON);
        } else {
            evt = new Event(Component.SPRINKLER_CONTROLLER, Component.SPRINKLER_OFF);
        } // if
        // Here we send the event to the event manager.
        try {
            em.sendEvent(evt);
        } // try
        catch (Exception e) {
            System.out.println("Error sending sprinkler control message:: " + e);
        } // catch
    } // Door

    public void timer() {
        if (!tuguefa) {
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (userInput) {
                for (int i = 0; i <= 5; i++) {
                    sprinkler(false);
                }
                tuguefa = true;
            }
        }
        userInput = true;
    }

    public void userActivateSprinkler(boolean on) {
        if (tuguefa) {
            if (on) {
                for (int i = 0; i <= 5; i++) {
                    sprinkler(false);
                }
            } else {
                userInput = false;
                tuguefa = false;
                for (int i = 0; i <= 5; i++) {
                    sprinkler(true);
                    fire(true);
                }
                tuguefa = false;
            }
        }
    }

}