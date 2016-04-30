#!/usr/bin/env bash
echo Initializing Event Manager...
su root -c "rmiregistry" &
xterm -T "EVENT MANAGER" -e "java EventManager %1" & 
echo Ready.
set +v
