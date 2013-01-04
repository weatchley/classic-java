@echo off
REM This Windows script runs ANT to build and deploy the Web Application

REM if %TOMCAT_HOME%a==a goto setupError
if %CATALINA_HOME%a==a goto setupError
REM if %PB_HOST%a==a goto setupError

REM Slurp the command line arguments.  This loop allows for an unlimited number
REM of agruments (up to the command line limit, anyway).
set CMD_LINE_ARGS=
:setupArgs
if %1a==a goto doneArgs
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto setupArgs
:doneArgs

REM ant -Dservletapi.home=%SERVLETAPI_HOME% -Dtomcat.home=%TOMCAT_HOME% -Dpointbase.host=%PB_HOST% %CMD_LINE_ARGS%
ant -Dtomcat.home=%CATALINA_HOME% %CMD_LINE_ARGS%

goto :done

:setupError
echo You must first initialize the build environment
echo The following environment variables must be set:
REM echo TOMCAT_HOME, SERVLETAPI_HOME, and PB_HOST
echo CATALINA_HOME

:done

REM release this variable otherwise, Windows will "run out environment space"
set CMD_LINE_ARGS=
