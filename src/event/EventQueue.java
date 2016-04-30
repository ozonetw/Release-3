/**
 * **************************************************************************************
 * File:EventQueue.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Autonomous University of Zacatecas 
 * Date: November 2015
 * Developer: Ferman Ivan Tovar 
 * Reviewer: Perla Velasco Elizondo
 * **************************************************************************************
 * This class defines event queues which are stored by the
 * EventManger. Each registered participant has an event queue assigned to them.
 * As events are sent by registered participants to the EventManger they are
 * posted in each queue. Queues are removed when participants unregister.
 * **************************************************************************************
 */

package event;

import java.io.Serializable;
import java.util.*;

public class EventQueue implements Serializable {

    private Vector<Event> eventList;            // This is the list of events associated with a participant
    private long queueId;			// This is the participants id
    private int listSize;			// This is the size of the list

    public EventQueue() {
        eventList = new Vector<Event>(15, 1);
        Calendar timeStamp = Calendar.getInstance();
        queueId = timeStamp.getTimeInMillis();
        listSize = 0;

    } // constructor

    /**
     * This method returns the event queue id
     * (which is the participants id).
     * 
     * @return long integer
     */
    public long getId() {
        return queueId;
    } // addEvent

    /**
     * This method returns the size of the queue.
     * 
     * @return The size of the queue
     */
    public int getSize() {
        return eventList.size();
    } // addEvent

    /**
     * This method adds an event to the list arriving events are 
     * appended to the end of the list.
     * 
     * @param m Event from a participant
     */
    public void addEvent(Event m) {
        eventList.add(m);
    } // addEvent

    /**
     * This method gets the event off of the front of the list. 
     * This is the oldest event in the list (arriving events
     * are appended to the list, hence the newest event is at the end of the
     * list). This method removes events from the list.
     * 
     * @return The event
     */
    public Event getEvent() {
        Event m = null;
        if (eventList.size() > 0) {
            m = eventList.get(0);
            eventList.removeElementAt(0);
        } // if
        return m;
    } // getEvent

    /**
     * This method will clears all the events the event queue.
     */
    public void clearEventQueue() {
        eventList.removeAllElements();
    } // clearEventQueue

    /**
     * This method is used to obtain a copy of the event queue. 
     * This method returns a second copy (separate memory)
     * of the queue, not a pointer to the queue.
     * 
     * @return EventQueue. This method returns a second copy (separate memory)
     * of the queue, not a pointer to the queue.
     */
    @SuppressWarnings("unchecked")
    public EventQueue getCopy() {
        EventQueue mq = new EventQueue();
        mq.queueId = queueId;
        mq.eventList = (Vector<Event>) eventList.clone();
        return mq;
    } // getCopy
} // EventQueue class
