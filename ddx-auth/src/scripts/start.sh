#!/bin/bash
cd `dirname $0`
cd ..
DEPLOY_DIR=`pwd`
JVM_OPTS="-XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m -Xms512m -Xmx512m -Xmn128m -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC"
JVM_AGENT_OPTS="-javaagent:${DEPLOY_DIR}/lib/jmx_prometheus_javaagent-0.15.0.jar=3010:${DEPLOY_DIR}/conf/jmx_prometheus_javaagent-0.15.0.yml"
CONF_DIR=$DEPLOY_DIR/conf
LIB_DIR=$DEPLOY_DIR/lib
SERVER_NAME="auth-server"
SERVER_PORT=`cat $CONF_DIR/bootstrap.yml | grep -w "port:" | grep -v "#" | awk  'NR==1{print $2}' | tr -d '\r'`
IS_LISTENED=`netstat -an | grep -w LISTEN | grep -w $SERVER_PORT`
if [ -n "$IS_LISTENED" ]; then
    echo "ERROR: The $SERVER_NAME[$SERVER_PORT] already started!"
    exit 1
fi

LOGS_DIR=$DEPLOY_DIR/log
if [ ! -d $LOGS_DIR ]; then
    mkdir $LOGS_DIR
fi
STDOUT_FILE=$LOGS_DIR/stdout.log

LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`
DEBUG_OPTS="-Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n"

echo -e "Starting $SERVER_NAME ...\c"
$JAVA_HOME/bin/java $JVM_OPTS $JVM_AGENT_OPTS -classpath $CONF_DIR:$LIB_JARS com.ddx.auth.DDXAuthApplication >> $STDOUT_FILE 2>&1 &

COUNT=0
while [ $COUNT -lt 1 ]; do
    echo -e ".\c"
    sleep 3
    IS_LISTENED=`netstat -an | grep -w LISTEN | grep -w $SERVER_PORT`
    if [ -n "$IS_LISTENED" ]; then
        COUNT=1
    fi
done

PIDS=`ps -ef | grep "$LIB_DIR" | awk 'NR==1{print $2}'`
echo "OK!"
echo "PID: $PIDS"
echo "STDOUT: $STDOUT_FILE"
