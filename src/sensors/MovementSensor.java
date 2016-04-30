package sensors;

import common.Component;
import instrumentation.MessageWindow;

/**
 * Created by angel on 23/04/16.
 */
public class MovementSensor extends Sensor implements Runnable {
    private static MovementSensor INSTANCE = new MovementSensor();
    private float currentMoveState = 0;

    private MovementSensor() {
        super();
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (MovementSensor.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MovementSensor();
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
    public static MovementSensor getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    /**
     * Start this sensor
     *
     * @param args IP address of the event manager (on command line).
     *             If blank, it is assumed that the event manager is on the local machine.
     */
    public static void main(String args[]) {
        if (args[0] != null) Component.SERVER_IP = args[0];
        MovementSensor sensor = MovementSensor.getInstance();
        sensor.run();
    }

    @Override
    public void run() {
// Here we check to see if registration worked. If ef is null then the
// event manager interface was not properly created.
        if (evtMgrI != null) {
// We create a message window. Note that we place this panel about 1/2 across
// and 1/3 down the screen
            float winPosX = 0.5f;    //This is the X position of the message window in terms
//of a percentage of the screen height
            float winPosY = 0.3f;    //This is the Y position of the message window in terms
//of a percentage of the screen height
            MessageWindow messageWin = new MessageWindow("Door Sensor", winPosX, winPosY);
            messageWin.writeMessage("Registered with the event manager.");
            try {
                messageWin.writeMessage("   Participant id: " + evtMgrI.getMyId());
                messageWin.writeMessage("   Registration Time: " + evtMgrI.getRegistrationTime());
            } // try
            catch (Exception e) {
                messageWin.writeMessage("Error:: " + e);
            } // catch
            messageWin.writeMessage("\nInitializing Door Simulation::");
            messageWin.writeMessage("   Initial Door Set:: " + currentMoveState);
/**
 * ******************************************************************
 ** Here we start the main simulation loop
 * *******************************************************************
 */
            messageWin.writeMessage("Beginning Simulation... ");
            while (!isDone) {
                if (coinToss()) {
                    currentMoveState = 1;
                } else {
                    currentMoveState = 0;
                }
// Post the current temperature
                postEvent(evtMgrI, MOVE, currentMoveState);
                messageWin.writeMessage("Current Move state is::  " + currentMoveState + " .");
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
                    if (evt.getEventId() == MOVE_SENSOR) {
                        if (evt.getMessage().equalsIgnoreCase(MOVE_ON)) // heater on
                        {
                            currentMoveState = 0;
                        } // if
                        if (evt.getMessage().equalsIgnoreCase(MOVE_OFF)) // heater off
                        {
                            currentMoveState = 1;
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
// Here we wait for a 2.5 seconds before we start the next sample
                try {
                    Thread.sleep(delay);
                } // try
                catch (Exception e) {
                    messageWin.writeMessage("Sleep error:: " + e);
                } // catch
            } // while
        } else {
            System.out.println("Unable to register with the event manager.\n\n");
        } // if
    }
}