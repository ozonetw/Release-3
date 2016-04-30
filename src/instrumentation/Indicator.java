/**
 * **************************************************************************************
 * File:Indicator.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Autonomous University of Zacatecas 
 * Date: November 2015
 * Developer: Ferman Ivan Tovar 
 * Reviewer: Perla Velasco Elizondo
 * **************************************************************************************
 * This class is used to create an indicator lamp on the terminal. The indicator
 * lamp is essentially a square box with a round corner rectangle inside that is
 * the indicator lamp. The lamp's color can be turn black, green, yellow, or
 * red. A short message can be displayed below the indicator lamp as well. Both
 * the lamp color and the message can be changed at run time.
 * **************************************************************************************
 */

package instrumentation;

import javax.swing.*;
import java.awt.*;

public class Indicator extends JFrame {

    private int height;
    private int upperLeftX;
    private int upperLeftY;
    private String messageLabel;
    private Color iluminationColor = Color.black;
    private Color textColor = Color.black;
    private JFrame indicatorWindow;

    /**
     * This method sets up a JFrame window and
     * drawing pane with the title specified at the position indicated by the x,
     * y coordinates.
     * 
     * @param label the indicator title
     * @param xPos the vertical position of the indicator on the screen specified 
     * in terms of a percentage of the screen width
     * @param yPos the horizontal position of the indicator on the screen specified 
     * in terms of a percentage of the screen height
     */
    public Indicator(String label, float xPos, float yPos) {
        messageLabel = label;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.lightGray);
        Toolkit aKit = this.getToolkit();
        Dimension screenSize = aKit.getScreenSize();
	// Size up the indicators. They width and height is 1% of the 
        // screen height or width (which ever is larger).
        if (screenSize.width >= screenSize.height) {
            height = (int) (screenSize.height * 0.1);
        } else {
            height = (int) (screenSize.width * 0.1);
        } // if
        
        /* Calculate the X and Y position of the window's upper left
         ** hand corner as a proportion of the screen
         */
        upperLeftX = (int) (screenSize.width * xPos);
        upperLeftY = (int) (screenSize.height * yPos);

        setBounds(upperLeftX, upperLeftY, height, height);
        setVisible(true);
        Graphics g = getGraphics();
        repaint();
    } // constructor

    /**
     * This method sets up a JFrame window and
     * drawing pane with the title specified at the position indicated by the x,
     * y coordinates.
     * 
     * @param label the indicator title
     * @param xPos the vertical position of the indicator on the screen specified 
     * in terms of pixels
     * @param yPos the horizontal position of the indicator on the screen specified 
     * in terms of pixels
     */
    public Indicator(String label, int xPos, int yPos) {
        messageLabel = label;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.lightGray);
        Toolkit aKit = this.getToolkit();
        Dimension screenSize = aKit.getScreenSize();

        // Size up the indicators. They width and height is 1% of the 
        // screen height or width (which ever is larger).		
        if (screenSize.width >= screenSize.height) {
            height = (int) (screenSize.height * 0.1);
        } else {
            height = (int) (screenSize.width * 0.1);
        } // if

        upperLeftX = xPos;
        upperLeftY = yPos;

        setBounds(xPos, yPos, height, height);
        setVisible(true);
        Graphics g = getGraphics();
        repaint();
    } // constructor

     /**
     * This method sets up a JFrame window and
     * drawing pane with the title specified at the position indicated by the x,
     * y coordinates.
     * 
     * @param label the indicator title
     * @param xPos the vertical position of the indicator on the screen specified 
     * in terms of a percentage of the screen width
     * @param yPos the horizontal position of the indicator on the screen specified 
     * in terms of a percentage of the screen height
     * @param initialColor specifies the color that the indicator should be on
     * startup
     */
    public Indicator(String label, float xPos, float yPos, int initialColor) {
        messageLabel = label;

        switch (initialColor) {
            case 0:
                iluminationColor = Color.black;
                break;
            case 1:
                iluminationColor = Color.green;
                break;
            case 2:
                iluminationColor = Color.yellow;
                break;
            case 3:
                iluminationColor = Color.red;
                break;
        } // switch

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.lightGray);
        Toolkit aKit = this.getToolkit();
        Dimension screenSize = aKit.getScreenSize();

        if (screenSize.width >= screenSize.height) {
            height = (int) (screenSize.height * 0.1);
        } else {
            height = (int) (screenSize.width * 0.1);
        } // if

        /* Calculate the X and Y position of the window's upper left
         ** hand corner as a proportion of the screen
         */
        upperLeftX = (int) (screenSize.width * xPos);
        upperLeftY = (int) (screenSize.height * yPos);

        setBounds(upperLeftX, upperLeftY, height, height);

        setVisible(true);
        Graphics g = getGraphics();
        repaint();
    } // constructor

    /**
     * This method sets up a JFrame window and
     * drawing pane with the title specified at the position indicated by the x,
     * y coordinates.
     * 
     * @param label the indicator title
     * @param xPos the vertical position of the indicator on the screen specified 
     * in terms of pixels
     * @param yPos the horizontal position of the indicator on the screen specified 
     * in terms of pixels
     * @param initialColor specifies the color that the indicator should
     * be on startup
     */
    public Indicator(String label, int xPos, int yPos, int initialColor) {
        messageLabel = label;

        switch (initialColor) {
            case 0:
                iluminationColor = Color.black;
                break;
            case 1:
                iluminationColor = Color.green;
                break;
            case 2:
                iluminationColor = Color.yellow;
                break;
            case 3:
                iluminationColor = Color.red;
                break;
        } // switch

        //setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.lightGray);
        Toolkit aKit = this.getToolkit();
        Dimension screenSize = aKit.getScreenSize();

        if (screenSize.width >= screenSize.height) {
            height = (int) (screenSize.height * 0.1);
        } else {
            height = (int) (screenSize.width * 0.1);
        } // if

        upperLeftX = xPos;
        upperLeftY = yPos;

        setBounds(xPos, yPos, height, height);
        setVisible(true);
        Graphics g = getGraphics();

        repaint();
    } // constructor

    /**
     * This method returns the X (vertical)
     * position of the window in pixels
     * 
     * @return integer position in pixels
     */
    public int GetX() {
        return (upperLeftX);
    } // GetX

    /**
     * This method returns the Y (horizontal)
     * position of the window in pixels
     * 
     * @return integer position in pixels
     */
    public int GetY() {
        return (upperLeftY);
    } // GetY	

    /**
     * This method returns the vertical height of the window
     * 
     * @return integer length in pixels
     */
    public int height() {
        return (this.height);
    } // height

    /**
     * This method returns the horizontal length of the window.
     * 
     * @return integer length in pixels
     */
    public int width() {
        return (this.height);

    } // width

    /**
     * This method will change both the indicator lamp color and the 
     * indicator label to the specified color and label. 
     * The display text is always black.
     * 
     * @param s the new indicator label
     * @param c the new indicator color where 
     * 0=black, 1=green, 2=yellow, and 3=red
     */
    public void setLampColorAndMessage(String s, int c) {
        switch (c) {
            case 0:
                iluminationColor = Color.black;
                break;
            case 1:
                iluminationColor = Color.green;
                break;
            case 2:
                iluminationColor = Color.yellow;
                break;
            case 3:
                iluminationColor = Color.red;
                break;
        } // switch
        messageLabel = s;
        repaint();
    } // setLampColor

    /**
     * This method will change the indicator 
     * lamp color to the specified color.
     * 
     * @param c the new indicator color where 
     * 0=black, 1=green, 2=yellow, and 3=red
     */
    public void setLampColor(int c) {
        switch (c) {
            case 0:
                iluminationColor = Color.black;
                break;
            case 1:
                iluminationColor = Color.green;
                break;
            case 2:
                iluminationColor = Color.yellow;
                break;
            case 3:
                iluminationColor = Color.red;
                break;
        } // switch
        repaint();
    } // setLampColor

    /**
     * This method will change both the indicator label 
     * to the specified label string. 
     * The display text is always black.
     * 
     * @param m the new indicator label
     */
    public void setMessage(String m) {
        messageLabel = m;
        repaint();
    } // setMessage

    /**
     * The paint() method is an overridden method
     * inherited from JFrame that draws the indicator on the screen. This method
     * must be overridden so that various JRE services can update the display.
     * If you do not override paint, the indicator will not be consistiently
     * drawn to the screen and may have various graphics disappear at run time.
     * This method is invoked indirectly by the repaint() method.
     * 
     * @param g Graphics g this is the indicator's graphic instance.
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        FontMetrics fm = g.getFontMetrics();
        int xLabelPosition = (int) (height * 0.92) - (int) (fm.stringWidth(messageLabel) * 0.5);
        int yLabelPosition = (int) (height * 0.90);
        g.setColor(iluminationColor);
        g.fillRoundRect((int) (height * 0.15), (int) (height * 0.35), (int) (height * 0.70), (int) (height * 0.40), (int) (height * 0.20), (int) (height * 0.20));
        g.setColor(textColor);
        g.drawString(messageLabel, xLabelPosition, yLabelPosition);
    } // paint
} // Indicator
