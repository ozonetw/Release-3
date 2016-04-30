/**
 * **************************************************************************************
 * File:Event.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Autonomous University of Zacatecas 
 * Date: November 2015
 * Developer: Ferman Ivan Tovar 
 * Reviewer: Perla Velasco Elizondo
 * **************************************************************************************
 * This class defines events. Events include the sender's ID, the
 * eventID, and a message field for sending small messages between entities
 * participating in the event system.
 * **************************************************************************************
 */
package event;

import java.io.Serializable;

public class Event implements Serializable {

    private String messageText;         // Any string message.
    private int eventId;		// Event Id is defined by the participant.
    private long senderId;		// Id assigned at registration time by the event manager. The ID for every event is
    // set by the EventManagerInterface before the message is sent to the event manager.

    /**
     * @param evtId The ID for the event
     * @param text The content of the event
     */
    public Event(int evtId, String text) {
        messageText = text;
        eventId = evtId;

    } // constructor

    /**
     * @param evtId The ID for the event
     */
    public Event(int evtId) {
        messageText = null;
        eventId = evtId;

    } // constructor

    /**
     * This method returns the ID of the
     * participant that posted this Event.
     * 
     * @return long integer
     */
    public long getSenderId() {
        return senderId;

    } // getSenderId

    /**
     * This method sets the ID of Event
     * to the long value.
     * 
     * @param id long integer
     */
    public void setSenderId(long id) {
        senderId = id;
    } // getSenderId

    /**
     * This method returns the event ID of
     * the posted event. 
     * There is not semantic imposed on event IDs.
     * 
     * @return The integer that represents the ID of this event
     */
    public int getEventId() {
        return eventId;
    } // GetEventIO
    
    /**
     * This method returns the message (if there is one) of the 
     * posted event. There is not semantic imposed on event IDs.
     * 
     * @return The message contained in this event
     */
    public String getMessage() {
        return messageText;
    } // getMessage

} // Event class
