import common.Component;
import common.IOManager;
import controllers.Controller;

/**
 * Created by angel on 23/04/16.
 */
public class AlarmConsole extends Controller implements Runnable {

    private static AlarmConsole INSTANCE = new AlarmConsole();

    public AlarmConsole() {
        super();
    }

    public static void main(String args[]) {
        if (args[0] != null) Component.SERVER_IP = args[0];
        AlarmConsole sSensor = AlarmConsole.getInstance();
        sSensor.run();
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (AlarmConsole.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AlarmConsole();
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
    public static AlarmConsole getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    @Override
    public void run() {
        IOManager userInput = new IOManager();  //IOManager IO Object
        boolean isDone = false;                 //Main loop flag
        String option;
        AlarmMonitor aMonitor = new AlarmMonitor();
        if (aMonitor.isRegistered()) {
            aMonitor.start();
            while (!isDone) {
                // Here, the main thread continues and provides the main menu
                System.out.println("\n\n\n\n");
                System.out.println("Door And Windows Control System (ECS) Command Console: \n");


                System.out.println("Select an Option: \n");
                System.out.println("1: Deactivate system");
                System.out.println("2: Activate system");
                System.out.println("3: Deactivate Sprinklers");
                System.out.println("4: Activate Sprinklers");
                System.out.println("X: Stop System\n");
                System.out.print("\n>>>> ");
                option = userInput.keyboardReadString();

                //////////// option 1 ////////////
                if (option.equals("1")) {
                    confirmMessage(evtMgrI, ALARMS_CONTROLLER, SECURITY_OFF);

                } // if
                if (option.equals("2")) {
                    confirmMessage(evtMgrI, ALARMS_CONTROLLER, SECURITY_ON);
                } // while

                if (option.equals("3")){
                    confirmMessage(evtMgrI, SPRINKLER_CONTROLLER, SPRINKLER_ON);
                    confirmMessage(evtMgrI, FIRE_CONTROLLER, FIRE_ON);
                    aMonitor.userActivateSprinkler(false);
                }

                if (option.equals("4")){
                    confirmMessage(evtMgrI, SPRINKLER_CONTROLLER, SPRINKLER_OFF);
                    aMonitor.userActivateSprinkler(true);
                }




                //////////// option X ////////////
                if (option.equalsIgnoreCase("X")) {
                    // Here the user is done, so we set the Done flag and halt
                    // the environmental control system. The monitor provides a method
                    // to do this.ts important to have processes release their queues
                    // with the event manager. Ie queues are not released these
                    // become dead queues ad they collect events and will eventually
                    // cause problems for the event manager.
                    aMonitor.halt();
                    isDone = true;
                    aMonitor.halt();
                }
            }
        }
    }
}//Alarm Console
