%ECHO OFF
START "EVENT MANAGER REGISTRY" /MIN /NORMAL rmiregistry %1
START "EVENT MANAGER" /MIN /NORMAL java EventManager %1