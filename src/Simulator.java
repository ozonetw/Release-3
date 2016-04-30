import common.Component;
import common.IOManager;
import controllers.Controller;

/**
 * Created by carlos on 28/04/16.
 */
public class Simulator extends Controller implements Runnable {
    private static Simulator INSTANCE = new Simulator();

    public Simulator() {
        super();
    }

    public static void main(String args[]) {
        if (args[0] != null) Component.SERVER_IP = args[0];
        Simulator sSensor = Simulator.getInstance();
        sSensor.run();
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (Simulator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Simulator();
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
    public static Simulator getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    @Override
    public void run() {
        IOManager userInput = new IOManager(); //IOManager IO Object
        boolean isDone = false; //Main loop flag
        int opt; //Menu choice from user
        while (!isDone) {
            // Here, the main thread continues and provides the main menu
            System.out.println("\n\n\n\n");
            System.out.println("Security Event simulation Console \n");
            System.out.println("Select an Option: \n");
            System.out.println("1: Window Break");
            System.out.println("2: Door Break");
            System.out.println("3: Move detection");
            System.out.println("4: Window Fix");
            System.out.println("5: Door Fix");
            System.out.println("6: Move Off\n");
            System.out.println("7: Exit");
            System.out.print("\n>>>> ");
            opt = Integer.parseInt(userInput.keyboardReadString());
            switch (opt) {
                case 1:
                    confirmMessage(evtMgrI, WINDOW_SENSOR, WINDOW_OFF);
                    break;
                case 2:
                    confirmMessage(evtMgrI, DOOR_SENSOR, DOOR_OFF);
                    break;
                case 3:
                    confirmMessage(evtMgrI, MOVE_SENSOR, MOVE_OFF);
                    break;
                case 4:
                    confirmMessage(evtMgrI, WINDOW_SENSOR, WINDOW_ON);
                    break;
                case 5:
                    confirmMessage(evtMgrI, DOOR_SENSOR, DOOR_ON);
                    break;
                case 6:
                    confirmMessage(evtMgrI, MOVE_SENSOR, MOVE_ON);
                    break;
                default:
                    System.out.println("Non supported option.");
                    break;
            }
        }
    }
}