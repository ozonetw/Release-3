package controllers;

import common.Component;
import instrumentation.Indicator;
import instrumentation.MessageWindow;

/**
 * Created by carlos on 29/04/16.
 */
public class FireController extends Controller implements Runnable {
    private static FireController INSTANCE = new FireController();
    private boolean fireState = false;    // Heater state: false == off, true == on
    private boolean sprinklerState = false;    // Dehumidifier state: false == off, true == on

    public FireController() {
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (FireController.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FireController();
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
    public static FireController getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    /**
     * Start this controller
     *
     * @param args IP address of the event manager (on command line).
     *             If blank, it is assumed that the event manager is on the local machine.
     */
    public static void main(String args[]) {
        if (args[0] != null) Component.SERVER_IP = args[0];
        FireController sensor = FireController.getInstance();
        sensor.run();
    }

    @Override
    public void run() {
// Here we check to see if registration worked. If em is null then the
// event manager interface was not properly created.
        if (evtMgrI != null) {
            System.out.println("Registered with the event manager.");
/* Now we create the alarms control status and message panel
** We put this panel about 2/3s the way down the terminal, aligned to the left
** of the terminal. The status indicators are placed directly under this panel
*/
            float winPosX = 0.0f;    //This is the X position of the message window in terms
//of a percentage of the screen height
            float winPosY = 0.60f;    //This is the Y position of the message window in terms
//of a percentage of the screen height
            MessageWindow messageWin = new MessageWindow("Fire Controller Status Console", winPosX, winPosY);
// Now we put the indicators directly under the humitity status and control panel
            Indicator fireIndicator = new Indicator("Fire Alarm OFF", messageWin.getX(), messageWin.getY() + messageWin.height());
            messageWin.writeMessage("Registered with the event manager.");
            try {
                messageWin.writeMessage("   Participant id: " + evtMgrI.getMyId());
                messageWin.writeMessage("   Registration Time: " + evtMgrI.getRegistrationTime());
            } catch (Exception e) {
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
                } catch (Exception e) {
                    messageWin.writeMessage("Error getting event queue::" + e);
                }
// If there are messages in the queue, we read through them.
// We are looking for EventIDs = 6, this is a request to turn the
// humidifier or dehumidifier on/off. Note that we get all the messages
// at once... there is a 2.5 second delay between samples,.. so
// the assumption is that there should only be a message at most.
// If there are more, it is the last message that will effect the
// output of the humidity as it would in reality.
                int qlen = queue.getSize();
                for (int i = 0; i < qlen; i++) {
                    evt = queue.getEvent();
                        if (evt.getEventId() == FIRE_CONTROLLER) {
                            if (evt.getMessage().equalsIgnoreCase(FIRE_ON)) { // humidifier on
                                fireState = true;
                                messageWin.writeMessage("Received fire  on event");
// Confirm that the message was received and acted on
                                confirmMessage(evtMgrI, FIRE_SENSOR, FIRE_ON);
                            }
                            if (evt.getMessage().equalsIgnoreCase(FIRE_OFF)) { // humidifier off
                                fireState = false;
                                messageWin.writeMessage("Received fire off event");
// Confirm that the message was recieved and acted on
                                confirmMessage(evtMgrI, FIRE_SENSOR, FIRE_OFF);
                            }
                        }
/*                    if (evt.getEventId() == ALARMS_CONTROLLER) {
                        if (evt.getMessage().equalsIgnoreCase(SECURITY_ON)) {
                            systemState = true;
                            messageWin.writeMessage("Security enabled");
                            //confirmMessage(evtMgrI, ALARMS_CONTROLLER, "SECURITY_ON");
                        }
                        if (evt.getMessage().equalsIgnoreCase(SECURITY_OFF)) {
                            systemState = false;
                            messageWin.writeMessage("Security disabled");
                            //confirmMessage(evtMgrI, ALARMS_CONTROLLER, "SECURITY_OFF");
                        }
                    }*/
// If the event ID == 99 then this is a signal that the simulation
// is to end. At this point, the loop termination flag is set to
// true and this process unregisters from the event manager.
                    if (evt.getEventId() == END) {
                        isDone = true;
                        try {
                            evtMgrI.unRegister();
                        } catch (Exception e) {
                            messageWin.writeMessage("Error unregistering: " + e);
                        }
                        messageWin.writeMessage("\n\nSimulation Stopped. \n");
// Get rid of the indicators. The message panel is left for the
// user to exit so they can see the last message posted.
                        fireIndicator.dispose();
                    }
                }
// Update the lamp status
                if (fireState) {
// Set to green, humidifier is on
                    fireIndicator.setLampColorAndMessage("FIRE ALARM ON", 1);
                } else {
// Set to black, humidifier is off
                    fireIndicator.setLampColorAndMessage("FIRE ALARM OFF", 0);
                }
                try {
                    Thread.sleep(delay);
                } catch (Exception e) {
                    System.out.println("Sleep error:: " + e);
                }
            }
        } else {
            System.out.println("Unable to register with the event manager.\n\n");
        }
    }
}
