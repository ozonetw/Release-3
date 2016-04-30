#!/bin/bash
echo Cleaning classes...

cd common
find . -type f -name '*.class' -delete
cd ..

cd controllers
find . -type f -name '*.class' -delete
cd ..

cd event
find . -type f -name '*.class' -delete
cd ..

cd instrumentation
find . -type f -name '*.class' -delete
cd ..

cd sensors
find . -type f -name '*.class' -delete
cd ..

find . -type f -name '*.class' -delete

echo Done. 
echo Press enter to coninue...
read