/**
 * **************************************************************************************
 * File:MessageWindow.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Autonomous University of Zacatecas 
 * Date: November 2015
 * Developer: Ferman Ivan Tovar 
 * Reviewer: Perla Velasco Elizondo
 * **************************************************************************************
 * This class is used to create a message window in for the A3 assignment.
 * Messages posted are displayed with a timestamp and followed by a newline
 * character. The message window will also auto scroll as messages fill the
 * window.
 * **************************************************************************************
 */

package instrumentation;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import javax.swing.*;
import java.awt.*;

public class MessageWindow {

    private int windowWidth; 		// width of the window in pixels
    private int windowHeight;		// height of the window in pixels
    private int screenWidth;		// width of the screen in pixels
    private int screenHeight;		// height of the screen in pixels 
    private int upperLeftX;		// The default window's upper left hand corner's X position
    private int upperLeftY;		// The default window's upper left hand corner's Y position
    private JFrame messageWindow;       // Message window frame
    private JTextArea messageArea; 	// This is the message area widget

    /**
     * This method sets up the JFrame
     * window with the title specified at the position indicated by the x, y
     * coordinates
     * 
     * @param title the window title
     * @param xPos the vertical position of the window on the 
     * screen specified in percentage of the total screen width
     * @param yPos the horizontal position of the window on the
     * screen specified in percentage of the total screen width
     */
    public MessageWindow(String title, float xPos, float yPos) {
        messageWindow = new JFrame(title);
        JPanel messagePanel = new JPanel();

        messageWindow.getContentPane().setBackground(Color.blue);
        Toolkit aKit = messageWindow.getToolkit();
        Dimension windowSize = aKit.getScreenSize();

        /* Make window height 25% of the screen height and 
         ** the window width 50% of the screen width
         */
        screenHeight = windowSize.height;
        screenWidth = windowSize.width;

        windowHeight = (int) (screenHeight * 0.20);
        windowWidth = (int) (screenWidth * 0.5);

        /* Calculate the X and Y position of the window's upper left
         ** hand corner as a proportion of the screen
         */
        upperLeftX = (int) (screenWidth * xPos);
        upperLeftY = (int) (screenHeight * yPos);
        messageWindow.setBounds(upperLeftX, upperLeftY, windowWidth, windowHeight);
        messageWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        /* This sets up the size of the message area within the window
         */
        messageArea = new JTextArea((int) (windowHeight / 20), (int) (windowWidth / 12));
        messageArea.setLineWrap(true);

        JScrollPane messageAreaScrollPane = new JScrollPane(messageArea);

        messageWindow.add(messagePanel);
        messagePanel.add(messageAreaScrollPane);

        messageWindow.setVisible(true);
    } // constructor

    /**
     * This method sets up the JFrame
     * window with the title specified at the position indicated by the x, y
     * coordinates
     * 
     * @param title the window title
     * @param xPos the vertical position of the window on the 
     * screen specified in pixels
     * @param yPos the horizontal position of the window on the
     * screen specified in pixels
     */
    public MessageWindow(String title, int xPos, int yPos) {
        messageWindow = new JFrame(title);
        JPanel messagePanel = new JPanel();

        messageWindow.getContentPane().setBackground(Color.blue);
        Toolkit aKit = messageWindow.getToolkit();
        Dimension windowSize = aKit.getScreenSize();

        /* Make window height 25% of the screen height and 
         ** the window width 50% of the screen width
         */
        screenHeight = windowSize.height;
        screenWidth = windowSize.width;

        windowHeight = (int) (screenHeight * 0.25);
        windowWidth = (int) (screenWidth * 0.5);

        upperLeftX = xPos;
        upperLeftY = yPos;
        messageWindow.setBounds(xPos, yPos, windowWidth, windowHeight);
        messageWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        /* This sets up the size of the message area within the window
         */
        messageArea = new JTextArea((int) (windowHeight / 20), (int) (windowWidth / 12));
        messageArea.setLineWrap(true);

        JScrollPane messageAreaScrollPane = new JScrollPane(messageArea);

        messageWindow.add(messagePanel);
        messagePanel.add(messageAreaScrollPane);

        messageWindow.setVisible(true);
    } // constructor

    /**
     * This method returns the X (vertical)
     * position of the window in pixels.
     * 
     * @return integer position in pixels
     */
    public int getX() {
        return (upperLeftX);
    } // getX

    /**
     * This method returns the Y (horizontal)
     * position of the window in pixels.
     * 
     * @return integer position in pixels
     */
    public int getY() {
        return (upperLeftY);
    } // getY		

    /**
     * This method returns the vertical height
     * of the window.
     * 
     * @return integer length in pixels
     */
    public int height() {
        return (windowHeight);
    } // writeMessage

    /**
     * This method returns the horizontal length
     * of the window.
     * 
     * @return integer length in pixels
     */
    public int width() {
        return (windowWidth);
    } // writeMessage

    /**
     * This method returns the vertical
     * height of the display device.
     * 
     * @return integer length in pixels
     */
    public int termHeight() {
        return (screenHeight);
    } // writeMessage

    /**
     * This method returns the horizontal
     * length of the display device.
     * 
     * @return integer length in pixels
     */
    public int termWidth() {
        return (screenWidth);
    } // writeMessage

    /**
     * This method writes a message to the text area. 
     * All messages are written with a date stamp and all strings
     * are followed by a newline.
     * 
     * @param message The string that will be showed in the window
     */
    public void writeMessage(String message) {
        Calendar timeStamp = Calendar.getInstance();
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy MM dd::hh:mm:ss:SSS");

        String timeString = timeStampFormat.format(timeStamp.getTime());
        messageArea.append(timeString + ":: " + message + "\n");
        messageArea.setCaretPosition(messageArea.getDocument().getLength());
    } // writeMessage
} // MessageWindow
