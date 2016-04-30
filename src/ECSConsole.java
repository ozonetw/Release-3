/**
 * **************************************************************************************
 * File:ECSConsole.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Autonomous University of Zacatecas 
 * Date: November 2015
 * Developer: Ferman Ivan Tovar 
 * Reviewer: Perla Velasco Elizondo
 * **************************************************************************************
 * This class is the console for the museum environmental control system. This
 * process consists of two threads. The ECSMonitor object is a thread that is
 * started that is responsible for the monitoring and control of the museum
 * environmental systems. The main thread provides a text interface for the user
 * to change the temperature and humidity ranges, as well as shut down the
 * system.
 * **************************************************************************************
 */

import common.*;

public class ECSConsole {

    public static void main(String args[]) {
        IOManager userInput = new IOManager();	// IOManager IO Object
        boolean isDone = false;			// Main loop flag
        String option;                          // Menu choice from user
        boolean isError;                        // Error flag
        ECSMonitor monitor;                     // The environmental control system monitor
        float tempRangeHigh = (float) 100.0;	// These parameters signify the temperature and humidity ranges in terms
        float tempRangeLow = (float) 0.0;	// of high value and low values. The ECSmonitor will attempt to maintain
        float humiRangeHigh = (float) 100.0;	// this temperature and humidity. Temperatures are in degrees Fahrenheit
        float humiRangeLow = (float) 0.0;	// and humidity is in relative humidity percentage.

        /////////////////////////////////////////////////////////////////////////////////
        // Get the IP address of the event manager
        /////////////////////////////////////////////////////////////////////////////////
        if (args.length != 0) {
            // event manager is not on the local system
            monitor = new ECSMonitor(args[0]);
        }
        else {
            monitor = new ECSMonitor();
        } // if

        // Here we check to see if registration worked. If ef is null then the
        // event manager interface was not properly created.
        if (monitor.isRegistered()) {
            monitor.start(); // Here we start the monitoring and control thread

            while (!isDone) {
                // Here, the main thread continues and provides the main menu
                System.out.println("\n\n\n\n");
                System.out.println("Environmental Control System (ECS) Command Console: \n");

                if (args.length != 0) {
                    System.out.println("Using event manger at: " + args[0] + "\n");
                }
                else {
                    System.out.println("Using local event manger \n");
                }

                System.out.println("Set Temperature Range: " + tempRangeLow + "F - " + tempRangeHigh + "F");
                System.out.println("Set Humidity Range: " + humiRangeLow + "% - " + humiRangeHigh + "%\n");
                System.out.println("Select an Option: \n");
                System.out.println("1: Set temperature ranges");
                System.out.println("2: Set humidity ranges");
                System.out.println("X: Stop System\n");
                System.out.print("\n>>>> ");
                option = userInput.keyboardReadString();

                //////////// option 1 ////////////
                if (option.equals("1")) {
                    // Here we get the temperature ranges
                    isError = true;
                    while (isError) {
                        // Here we get the low temperature range
                        while (isError) {
                            System.out.print("\nEnter the low temperature>>> ");
                            option = userInput.keyboardReadString();
                            if (userInput.isNumber(option)) {
                                isError = false;
                                tempRangeLow = Float.valueOf(option).floatValue();
                            }
                            else {
                                System.out.println("Not a number, please try again...");
                            } // if
                        } // while

                        isError = true;
                        // Here we get the high temperature range
                        while (isError) {
                            System.out.print("\nEnter the high temperature>>> ");
                            option = userInput.keyboardReadString();
                            if (userInput.isNumber(option)) {
                                isError = false;
                                tempRangeHigh = Float.valueOf(option).floatValue();
                            }
                            else {
                                System.out.println("Not a number, please try again...");
                            } // if
                        } // while

                        if (tempRangeLow >= tempRangeHigh) {
                            System.out.println("\nThe low temperature range must be less than the high temperature range...");
                            System.out.println("Please try again...\n");
                            isError = true;
                        }
                        else {
                            monitor.setTemperatureRange(tempRangeLow, tempRangeHigh);
                        } // if
                    } // while
                } // if

                //////////// option 2 ////////////
                if (option.equals("2")) {
                    // Here we get the humidity ranges
                    isError = true;
                    while (isError) {
                        // Here we get the low humidity range
                        while (isError) {
                            System.out.print("\nEnter the low humidity>>> ");
                            option = userInput.keyboardReadString();

                            if (userInput.isNumber(option)) {
                                isError = false;
                                humiRangeLow = Float.valueOf(option).floatValue();
                            }
                            else {
                                System.out.println("Not a number, please try again...");
                            } // if
                        } // while

                        isError = true;
                        // Here we get the high humidity range
                        while (isError) {
                            System.out.print("\nEnter the high humidity>>>  ");
                            option = userInput.keyboardReadString();

                            if (userInput.isNumber(option)) {
                                isError = false;
                                humiRangeHigh = Float.valueOf(option).floatValue();
                            }
                            else {
                                System.out.println("Not a number, please try again...");
                            } // if
                        } // while

                        if (humiRangeLow >= humiRangeHigh) {
                            System.out.println("\nThe low humidity range must be less than the high humidity range...");
                            System.out.println("Please try again...\n");
                            isError = true;
                        }
                        else {
                            monitor.setHumidityRange(humiRangeLow, humiRangeHigh);
                        } // if
                    } // while
                } // if

                //////////// option X ////////////
                if (option.equalsIgnoreCase("X")) {
		// Here the user is done, so we set the Done flag and halt
                    // the environmental control system. The monitor provides a method
                    // to do this. Its important to have processes release their queues
                    // with the event manager. If these queues are not released these
                    // become dead queues and they collect events and will eventually
                    // cause problems for the event manager.

                    monitor.halt();
                    isDone = true;
                    System.out.println("\nConsole Stopped... Exit monitor mindow to return to command prompt.");
                    monitor.halt();
                } // if
            } // while
        }
        else {
            System.out.println("\n\nUnable start the monitor.\n\n");
        } // if
    } // main
} // ECSConsole
