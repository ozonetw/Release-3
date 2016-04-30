%ECHO OFF
%ECHO Cleaning...

cd common/
del *.class

cd ../controllers/
del *.class

cd ../event/
del *.class

cd ../instrumentation/
del *.class

cd ../sensors/
del *.class

cd ../
del *.class

%ECHO Done!
PAUSE