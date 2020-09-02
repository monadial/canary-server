#!/bin/sh

# Default jvm options
export JAVA_OPTS="$JAVA_OPTS\
    -server\
    -Duser.dir=$(pwd)\
    -Xlog:gc:/tmp/gc.log\
    -XX:+UnlockExperimentalVMOptions\
    -XX:+UnlockDiagnosticVMOptions\
    -XX:MaxMetaspaceExpansion=64M\
    -XX:+ParallelRefProcEnabled"

[ -n "$XMS" ] && JAVA_XMS="$XMS" && echo "Deprecated env variable XMS, use JAVA_XMS instead" >>/dev/stderr
[ -n "$XMX" ] && JAVA_XMX="$XMX" && echo "Deprecated env variable XMX, use JAVA_XMX instead" >>/dev/stderr

[ -n "$JAVA_XMS" ] && JAVA_OPTS="$JAVA_OPTS -Xms$JAVA_XMS"
[ -n "$JAVA_XMX" ] && JAVA_OPTS="$JAVA_OPTS -Xmx$JAVA_XMX"

# Enable JMX?
if [ ! -z "$JMX_PORT" ] && [ ! -z "$RMI_HOST" ]
then
    export JAVA_OPTS="$JAVA_OPTS\
        -Djava.rmi.server.hostname=$RMI_HOST\
        -Dcom.sun.management.jmxremote\
        -Dcom.sun.management.jmxremote.port=$JMX_PORT\
        -Dcom.sun.management.jmxremote.rmi.port=$JMX_PORT\
        -Dcom.sun.management.jmxremote.authenticate=false\
        -Dcom.sun.management.jmxremote.local.only=false\
        -Dcom.sun.management.jmxremote.ssl=false"
fi

# Enable debug ?
if [ ! -z "$DEBUG_PORT" ]
then
    echo "Enabling Java debug on port $DEBUG_PORT"
    export JAVA_OPTS="$JAVA_OPTS\
        -Xdebug\
        -agentlib:jdwp=transport=dt_socket,address=*:$DEBUG_PORT,server=y,suspend=n"
fi

# Print debug logs
env > /tmp/env.log
echo ${JAVA_OPTS} > /tmp/java_opts.log

# Run app
exec java ${JAVA_OPTS} -jar assembly.jar
