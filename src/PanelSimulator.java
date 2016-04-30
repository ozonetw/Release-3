import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import common.Component;
import common.IOManager;
import controllers.Controller;
import event.Event;
import event.EventManagerInterface;
import event.EventQueue;
import sensors.Sensor;

/**
 * Created by angel on 29/04/16.
 */
public class PanelSimulator extends Sensor implements ActionListener{

    private String evtmgrip;
    private Event evt = null;
    private EventQueue eq = null;
    private EventManagerInterface em = null;
    private boolean isDone = false;
    private int evtId = 0;
    private JFrame mainFrame;
    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JButton breakDoor;
    private JButton fixDoor;
    private JButton detectMove;
    private JButton ignoreMove;
    private JButton breakWindow;
    private JButton fixWindow;
    private JButton startFire;
    private JButton killFire;

    //private JFrame thisFrame;
    //private JTextArea notificationArea; // textually represents the state of the alarm

    //private JLabel indicator; // turns Red if any alarm goes off
    //private JLabel systemState; // indicates
    //private boolean windowFlag = false;
    //private String EvtMgrIP;					// Event Manager IP address
    //private Event Evt = null;					// Event object
    //private EventQueue eq = null;				// Message Queue
    //private int EvtId = 0;						// User specified event ID
    //private EventManagerInterface em = null;	// Interface object to the event manager
    //private boolean WindowState = false;	// Window state: false == not broken, true == broken
    //private int	Delay = 2500;					// The loop delay (2.5 seconds)
    //private boolean Done = false;				// Loop termination flag

    //private JButton breakButton;
    //private JButton fixButton;

    //private JButton fireStartButton;
    //private JButton fireStopButton;

    //private JButton motionDetectStartButton;
    //private JButton motionDetectStopButton;

    //private JButton doorBreakButton;
    //private JButton doorFixButton;


    public static void main(String args[]) {
        PanelSimulator x = new PanelSimulator(args);

    }

    public PanelSimulator(String args[]) {
        initialize();
        System.out.println("Attempting to register on the local machine...");
        try {
            em = new EventManagerInterface();
        } catch (Exception e) {

            System.out.println("Error");

        }
    }

    private void initialize() {
        mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setSize(700, 400);

        mainPanel = new JPanel(new BorderLayout());
        topPanel = new JPanel(new FlowLayout());
        bottomPanel = new JPanel(new FlowLayout());

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        mainFrame.add(mainPanel, BorderLayout.NORTH);

        breakDoor = new JButton("Destroy Door");
        breakDoor.addActionListener(this);

        breakWindow = new JButton("Destroy Window");
        breakWindow.addActionListener(this);

        detectMove = new JButton("Detect movement");
        detectMove.addActionListener(this);

        startFire = new JButton("Start Fire");
        startFire.addActionListener(this);

        fixDoor = new JButton("Fix door");
        fixDoor.addActionListener(this);

        fixWindow = new JButton("Fix Window");
        fixWindow.addActionListener(this);

        ignoreMove = new JButton("Stop Movement");
        ignoreMove.addActionListener(this);

        killFire = new JButton("Kill Fire");
        killFire.addActionListener(this);

        topPanel.add(breakDoor);
        topPanel.add(breakWindow);
        topPanel.add(detectMove);
        topPanel.add(startFire);

        bottomPanel.add(fixDoor);
        bottomPanel.add(fixWindow);
        bottomPanel.add(ignoreMove);
        bottomPanel.add(killFire);

        mainFrame.setVisible(true);

    }

    public void actionPerformed(ActionEvent arg0) {
        JButton button = (JButton) arg0.getSource();

        try {
            eq = em.getEventQueue();

        } // try

        catch (Exception e) {
            System.out.println("Error getting event queue::" + e);

        } // catch


        if (button == breakDoor) {
            //confirmMessage(evtMgrI, DOOR_SENSOR, DOOR_OFF);
            postEvent(evtMgrI, DOOR, 0);
            //PostWindowBreakEvent(evtMgrI, DOOR_SENSOR, DOOR_OFF);
        } else if (button == fixDoor){
            postEvent(evtMgrI, DOOR, 1);
            //confirmMessage(evtMgrI, DOOR_SENSOR, DOOR_ON);
            //PostWindowBreakEvent(evtMgrI, DOOR_SENSOR, DOOR_ON);
        } else if (button == breakWindow) {
            postEvent(evtMgrI, WINDOW, 0);
            //PostWindowBreakEvent(evtMgrI, WINDOW_SENSOR, WINDOW_ON);
        } else if (button == fixWindow) {
            postEvent(evtMgrI, WINDOW, 1);
            //PostWindowBreakEvent(evtMgrI, WINDOW_SENSOR, WINDOW_OFF);
        } else if (button == detectMove) {
            //PostWindowBreakEvent(evtMgrI, MOVE_SENSOR, MOVE_ON);
            postEvent(evtMgrI,MOVE,0);
        } else if (button == ignoreMove) {
            postEvent(evtMgrI,MOVE,1);
            //PostWindowBreakEvent(evtMgrI, MOVE_SENSOR, MOVE_OFF);
        } else if (button == startFire) {
            postEvent(evtMgrI,FIRE, 0);
            //PostWindowBreakEvent(evtMgrI, FIRE_SENSOR, FIRE_ON);
        } else if (button == killFire) {
            postEvent(evtMgrI,FIRE, 1);
            //PostWindowBreakEvent(evtMgrI, FIRE_SENSOR, FIRE_OFF);
        }
    }

    static private void PostWindowBreakEvent(EventManagerInterface ei, int id, String txt) {
        // Here we create the event.

        Event evt = null;
        evt = new Event(id, txt);

        // Here we send the event to the event manager.

        try {
            ei.sendEvent(evt);
            //mw.WriteMessage( "Sent Humidity Event" );

        } // try

        catch (Exception e) {
            System.out.println("Error Posting event:: " + e);

        }

    }


}
