/**
 * **************************************************************************************
 * File:EventManagerInterface.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Autonomous University of Zacatecas 
 * Date: November 2015
 * Developer: Ferman Ivan Tovar 
 * Reviewer: Perla Velasco Elizondo
 * **************************************************************************************
 * This class provides an interface to the event manager for
 * participants (processes), enabling them to to send and receive events between
 * participants. A participant is any thing (thread, object, process) that
 * instantiates an EventEventManagerInterface object - this automatically
 * attempts to register that entity with the event manager
 * **************************************************************************************
 */
package event;

import java.rmi.*;
import java.net.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class EventManagerInterface {

    private long participantId = -1;			// This processes ID
    private RMIEventManagerInterface rmiEvtMgrI = null;	// Event manager interface object
    private String defaultPort = "1099";		// Default event manager port

    /**
     * *************************************************************************
     * Exceptions::
     ***************************************************************************
     */
    class SendEventException extends Exception {

        SendEventException() {
            super();
        }

        SendEventException(String s) {
            super(s);
        }

    } // Exception

    class GetEventException extends Exception {

        GetEventException() {
            super();
        }

        GetEventException(String s) {
            super(s);
        }

    } // Exception

    class ParticipantAlreadyRegisteredException extends Exception {

        ParticipantAlreadyRegisteredException() {
            super();
        }

        ParticipantAlreadyRegisteredException(String s) {
            super(s);
        }

    } // Exception

    class ParticipantNotRegisteredException extends Exception {

        ParticipantNotRegisteredException() {
            super();
        }

        ParticipantNotRegisteredException(String s) {
            super(s);
        }

    } // Exception

    class LocatingEventManagerException extends Exception {

        LocatingEventManagerException() {
            super();
        }

        LocatingEventManagerException(String s) {
            super(s);
        }

    } // Exception

    class LocalHostIpAddressException extends Exception {

        LocalHostIpAddressException() {
            super();
        }

        LocalHostIpAddressException(String s) {
            super(s);
        }

    } // Exception

    class RegistrationException extends Exception {

        RegistrationException() {
            super();
        }

        RegistrationException(String s) {
            super(s);
        }

    } // Exception

    /**
     * This method registers participants with the event manager. 
     * This instantiation should be used when the event manager is on the local 
     * machine, using the default port.
     * 
     * @throws event.EventManagerInterface.LocatingEventManagerException
     * @throws event.EventManagerInterface.RegistrationException
     * @throws event.EventManagerInterface.ParticipantAlreadyRegisteredException 
     */
    public EventManagerInterface() throws LocatingEventManagerException, RegistrationException, ParticipantAlreadyRegisteredException {
	// First we check to see if the participant is already registered. If not
        // we go on with the registration. If the are, we throw an exception.
        if (participantId == -1) {
            try {
                rmiEvtMgrI = (RMIEventManagerInterface) Naming.lookup("EventManager");
            } // try
            catch (Exception e) {
                throw new LocatingEventManagerException("Event manager not found on local machine at default port (1099)");
            } // catch
            try {
                participantId = rmiEvtMgrI.Register();
            } // try
            catch (Exception e) {
                throw new RegistrationException("Error registering participant " + participantId);
            } // catch
        } else {
            throw new ParticipantAlreadyRegisteredException("Participant already registered " + participantId);
        } // if

    } // EventManagerInterface

    /**
     * This method registers participants with the event manager at a 
     * specified IP address. This instantiation is used when the EventManager 
     * is not on a local machine.
     * 
     * @param serverIpAddress The specified address to the Event Manager Server
     * @throws event.EventManagerInterface.LocatingEventManagerException
     * @throws event.EventManagerInterface.RegistrationException
     * @throws event.EventManagerInterface.ParticipantAlreadyRegisteredException 
     */
    public EventManagerInterface(String serverIpAddress) throws LocatingEventManagerException,
            RegistrationException, ParticipantAlreadyRegisteredException {
	// Assumes that the event manager is on another machine. The user must provide the IP
        // address of the event manager and the port number
        String emServer = "//" + serverIpAddress + ":" + defaultPort + "/EventManager";
        if (participantId == -1) {
            try {
                rmiEvtMgrI = (RMIEventManagerInterface) Naming.lookup(emServer);
            } // try
            catch (Exception e) {
                throw new LocatingEventManagerException("Event manager not found on machine at:" + serverIpAddress + "::" + e);
            } // catch
            try {
                participantId = rmiEvtMgrI.Register();
            } // try
            catch (Exception e) {
                throw new RegistrationException("Error registering participant " + participantId);
            } // catch
        } else {
            throw new ParticipantAlreadyRegisteredException("Participant already registered " + participantId);
        } // if

    } // EventManagerInterface

    /**
     * This method allows participants to get their participant Id.
     * 
     * @return The ID
     * @throws event.EventManagerInterface.ParticipantNotRegisteredException 
     */
    public long getMyId() throws ParticipantNotRegisteredException {
        if (participantId != -1) {
            return participantId;
        } else {
            throw new ParticipantNotRegisteredException("Participant not registered");
        } // if
    } // getMyId

    /**
     * This method allows participants to obtain the time of registration.
     * 
     * @return String time stamp in the format: yyyy MM dd::hh:mm:ss:SSS yyyy =
     * year MM = month dd = day hh = hour mm = minutes ss = seconds SSS =
     * milliseconds
     * @throws event.EventManagerInterface.ParticipantNotRegisteredException 
     */
    public String getRegistrationTime() throws ParticipantNotRegisteredException {
        Calendar TimeStamp = Calendar.getInstance();
        SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy MM dd::hh:mm:ss:SSS");

        if (participantId != -1) {
            TimeStamp.setTimeInMillis(participantId);
            return (TimeStampFormat.format(TimeStamp.getTime()));

        } else {

            throw new ParticipantNotRegisteredException("Participant not registered");

        } // if

    } // getRegistrationTime

    /**
     * This method sends an event to the event manager.
     * 
     * @param evt Event object
     * @throws event.EventManagerInterface.ParticipantNotRegisteredException
     * @throws event.EventManagerInterface.SendEventException 
     */
    public void sendEvent(Event evt) throws ParticipantNotRegisteredException, SendEventException {
        if (participantId != -1) {
            try {
                evt.setSenderId(participantId);
                rmiEvtMgrI.SendEvent(evt);
            } // try
            catch (Exception e) {
                throw new SendEventException("Error sending event" + e);
            } // catch
        } else {
            throw new ParticipantNotRegisteredException("Participant not registered");
        } // if
    } // sendEvent

    /**
     * This method sends an event to the
     * event manager.
     * 
     * @return Event object.
     * @throws event.EventManagerInterface.ParticipantNotRegisteredException
     * @throws event.EventManagerInterface.GetEventException 
     */
    public EventQueue getEventQueue() throws ParticipantNotRegisteredException, GetEventException {
        EventQueue eq = null;
        if (participantId != -1) {
            try {
                eq = rmiEvtMgrI.GetEventQueue(participantId);
            } // try
            catch (Exception e) {
                throw new GetEventException("Error getting event" + e);
            } // catch
        } else {
            throw new ParticipantNotRegisteredException("Participant not registered");
        } // if
        return eq;
    } // getEventQueue

    /**
     * This method is called when the object is no longer used. 
     * Essentially this method unregisters participants from the event manager. 
     * It is important that participants actively unregister with the event manager. 
     * Failure to do so will cause unconnected queues to fill up with messages 
     * over time. This will result in a memory leak and eventual failure of the 
     * event manager.
     * 
     * @throws event.EventManagerInterface.ParticipantNotRegisteredException
     * @throws event.EventManagerInterface.RegistrationException 
     */
    public void unRegister() throws ParticipantNotRegisteredException, RegistrationException {
        if (participantId != -1) {
            try {
                rmiEvtMgrI.UnRegister(participantId);
            } // try
            catch (Exception e) {
                throw new RegistrationException("Error unregistering" + e);
            } // catch
        } else {
            throw new ParticipantNotRegisteredException("Participant not registered");
        } // if
    } // unRegister
} // EventManagerInterface
