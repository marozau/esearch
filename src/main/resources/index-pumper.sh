#!/usr/bin/env bash

CFG_FILE="log4j2.xml"

# LOG settings
if [ -z "$LOG_DIR" ]; then
  LOG_DIR="/var/log/index-pumper"
fi

# LOG settings
if [ -z "$LOG_LEVEL" ]; then
  LOG_LEVEL="INFO"
fi

# Memory options
if [ -z "$HEAP_OPTS" ]; then
  HEAP_OPTS="-Xmx128M"
fi

# JVM performance options
if [ -z "$JVM_PERFORMANCE_OPTS" ]; then
  JVM_PERFORMANCE_OPTS="-XX:+UseG1GC "
fi


if [ -z "$MAIN_JAR" ]; then
  MAIN_JAR="esearch-1.0-SNAPSHOT.jar"
fi

if [ -z "$MAIN_CLASS" ]; then
  MAIN_CLASS="today.expresso.es.web.IndexPumper"
fi

OPTIONS="-Dindex.url=http://ru.euronews.com -Dindex.depth=0"

if [ -z "$OPTIONS" ]; then
  echo "ERROR: empty OPTIONS"
  exit 1
fi

run()
{
    exec java \
    ${HEAP_OPTS} \
    ${JVM_PERFORMANCE_OPTS} \
    ${OPTIONS} \
    -Dlog4j.configurationFile=log4j2.xml \
    -classpath libs/*:${MAIN_JAR} ${MAIN_CLASS}
}

while [ ! -f "${CFG_FILE}" ]; do
    sleep 1
done

run
