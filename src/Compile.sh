#!/bin/bash
echo Compiling files...
javac common/*.java
javac controllers/*.java
javac event/*.java
javac instrumentation/*.java
javac sensors/*.java
javac *.java

rmic EventManager

echo Done. 
echo Press enter to continue...
read