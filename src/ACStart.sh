#!/usr/bin/env bash
echo Opening ACS System
echo ACS Monitoring Console
xterm -T "MUSEUM ALARM CONTROL SYSTEM CONSOLE" -e "java AlarmConsole %1" &

echo Opening Alarm Controller Console
xterm -T "ALARM CONTROLLER CONSOLE" -e "java controllers/AlarmsController %1" &
xterm -T "DOOR SENSOR CONSOLE" -e "java sensors/DoorSensor %1" &
xterm -T "WINDOW SENSOR CONSOLE" -e "java sensors/WindowSensor %1" &
xterm -T "MOVEMENT SENSOR CONSOLE" -e "java sensors/MovementSensor %1" &
xterm -T "FIRE CONTROLLER CONSOLE" -e "java controllers/FireController %1" &
xterm -T "SPRINKLER CONTROLLER CONSOLE" -e "java controllers/SprinklerController %1" &
xterm -T "FIRE SENSOR CONSOLE" -e "java sensors/FireSensor %1" &
xterm -T "SPRINKLER SENSOR CONSOLE" -e "java sensors/SprinklerSensor %1" &
xterm -T "SIMULATOR" -e "java PanelSimulator %1" &

echo Opening Simulator Console
#xterm -T "Security Event Simulation Console" -e "java Simulator %1" &

echo Working...
set +v