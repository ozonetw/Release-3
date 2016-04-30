/**
 * **************************************************************************************
 * File:IOManager.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Autonomous University of Zacatecas 
 * Date: November 2015
 * Developer: Ferman Ivan Tovar 
 * Reviewer: Perla Velasco Elizondo
 * **************************************************************************************
 * This class has methods to manage the input/output from console and 
 * include others to convert strings to numbers.
 * **************************************************************************************
 */
package common;

import java.io.*;

public class IOManager {

    /**
     * This method reads a string from the keyboard
     *
     * @return The String that was read.
     */
    public String keyboardReadString() {
        BufferedReader myReader = new BufferedReader(new InputStreamReader(System.in));
        String stringItem = "";
        try {
            stringItem = myReader.readLine();
        } // try
        catch (IOException IOError) {
            System.out.println("Read Error in Termio.KeyboardReadString method");
        } // catch
        return stringItem;
    } // keyboardReadString

    /**
     * This method reads a single character from the keyboard
     *
     * @return The char that was read
     */
    public char keyboardReadChar() {
        BufferedReader myReader = new BufferedReader(new InputStreamReader(System.in));
        char charItem = ' ';
        try {
            charItem = (char) myReader.read();
        } // try
        catch (IOException IOError) {
            System.out.println("Read Error in Termio.KeyboardReadChar method");
        } // catch
        return charItem;
    } // keyboardReadChar

    /**
     * This method accepts a String Object and determines if the string is
     * representing a number. If an integer is passed (for example 4), then the
     * program will assume that it is a floating point number (for example 4.0).
     * If the string represents a numeric value, then a true is returned to the
     * caller, otherwise a false is returned.
     *
     * @param stringItem The String that probably is a number
     * @return True if is a number, otherwise returns false
     */
    public boolean isNumber(String stringItem) {
        Float floatItem = (float) 0.0;
        try {
            floatItem = Float.valueOf(stringItem);
        } // try
        catch (NumberFormatException IOError) {
            return false;
        } // catch
        return true;
    } //IsNumber

    /**
     * This method accepts a string and if the string represents a number, then
     * it is converted to a float and returned to the caller, otherwise a
     * NumericFormatException is raised and a message is printed for the caller.
     *
     * @param stringItem The String that contains a float number
     * @return The float value
     */
    public float toFloat(String stringItem) {
        Float floatItem = (float) 0.0;
        try {
            floatItem = Float.valueOf(stringItem);
        } // try
        catch (NumberFormatException ioError) {
            System.out.print("Error converting " + stringItem);
            System.out.print(" to a floating point number::");
            System.out.println(" Termio.ToFloat method.");
        } // catch
        return floatItem;
    } //ToFloat

    /**
     * This method accepts a string and if the string represents a number, then
     * it is converted to a double and returned to the caller, otherwise a
     * NumericFormatException is raised and a message is printed for the caller.
     *
     * @param stringItem The String that contains a double number
     * @return The double value
     */
    public double toDouble(String stringItem) {
        Float floatItem = (float) 0.0;
        try {
            floatItem = Float.valueOf(stringItem);
        } // try
        catch (NumberFormatException ioError) {
            System.out.print("Error converting " + stringItem);
            System.out.print(" to a floating point number::");
            System.out.println(" Termio.ToDouble method.");
        } // catch
        return floatItem.doubleValue();
    } //ToDouble

    /**
     * This method accepts a string and if the string represents a number, then
     * it is converted to a integer and returned to the caller, otherwise a
     * NumericFormatException is raised and a message is printed for the caller.
     *
     * @param stringItem The String that contains a double number
     * @return The integer value
     */
    public int toInteger(String stringItem) {
        Integer integerItem = 0;
        try {
            integerItem = Integer.valueOf(stringItem);
        } // try
        catch (NumberFormatException ioError) {
            System.out.print("Error converting " + stringItem);
            System.out.print(" to an integer number::");
            System.out.println(" Termio.ToInteger method.");
        } // catch
        return integerItem;
    } //ToDouble
} //class
