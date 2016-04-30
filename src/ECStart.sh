echo Opening ECS System
echo ECS Monitoring Console
xterm -T "MUSEUM ENVIRONMENTAL CONTROL SYSTEM CONSOLE" -e "java ECSConsole" &

echo Opening Temperature Controller Console
xterm -T "TEMPERATURE CONTROLLER CONSOLE" -e "java controllers/TemperatureController %1" &
xterm -T "TEMPERATURE SENSOR CONSOLE" -e "java sensors/TemperatureSensor %1" &

echo Opening Humidity Sensor Console
xterm -T "HUMIDITY CONTROLLER CONSOLE" -e "java controllers/HumidityController %1" &
xterm -T "HUMIDITY SENSOR CONSOLE" -e "java sensors/HumiditySensor %1" &

echo Working...
set +v