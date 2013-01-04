export USER=higashis
#
export ANT_HOME=/usr/local/apache-ant-1.6.5
export SVN_HOME=/usr/local/svn-1.3.2
#
export JAVA_HOME=/usr/local/jdk1.6.0_03
export CATALINA_HOME=/usr/local/apache-tomcat-5.5.25
export CATALINA_BASE=/usr/local/homes/$USER
export CATALINA_PID=$CATALINA_BASE/logs/catalina.pid
export JAVA_OPTS="-Xms512m -Xmx512m -server -XX:MaxPermSize=256m -d64 \
                  -Djava.awt.headless=false \
                  -Dcom.sun.management.jmxremote \
                  -Dcom.sun.management.jmxremote.port=8094 \
                  -Dcom.sun.management.jmxremote.ssl=false \
                  -Dcom.sun.management.jmxremote.authenticate=true \
                  -Dcom.sun.management.jmxremote.password.file=conf/jmxremote.password \
                  -Dcom.sun.management.jmxremote.access.file=conf/jmxremote.access \
                  -XX:+HeapDumpOnOutOfMemoryError \
                  -XX:+UseConcMarkSweepGC"
#
export EDITOR=nano
export SVN_EDITOR=$EDITOR
export PAGER=less
export ProductionStatus=dev
export CSIPS=prod
export
CLASSPATH=$CLASSPATH:/usr/local/homes/higashis/csi.jar:/usr/local/homes/higashis/ojdbc14.jar:
#
export
PATH=$PATH:/usr/local/homes/higashis/bin:$JAVA_HOME/bin:$ANT_HOME/bin:$SVN_HOME/bin
