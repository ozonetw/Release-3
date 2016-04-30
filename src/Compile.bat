%ECHO OFF
%ECHO Compiling files ...

javac common/*.java
javac controllers/*.java
javac event/*.java
javac instrumentation/*.java
javac sensors/*.java
javac *.java

rmic EventManager

%ECHO Done!
PAUSE

