%ECHO OFF
%ECHO Starting ACS System
PAUSE
%ECHO ACS Monitoring Console
START "MUSEUM ALARM CONTROL SYSTEM CONSOLE" /NORMAL java ACSConsole %1
%ECHO Starting Alarms Controller Console
START "ALARMS CONTROLLER CONSOLE" /MIN /NORMAL java controllers/AlarmsController %1
START "DOOR SENSOR CONSOLE" /MIN /NORMAL java sensors/DoorSensor %1
START "WINDOW SENSOR CONSOLE" /MIN /NORMAL java sensors/WindowSensor %1
START "MOVEMENT SENSOR CONSOLE" /MIN /NORMAL java sensors/MovementSensor %1
START "Simulator Panel" /MIN /NORMAL java PanelSimulator %1