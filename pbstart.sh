#! /bin/bash

. network.init

JAVABIN=`which java`

cd database
$JAVABIN -jar hsqldb.jar --noinput --sql "SHUTDOWN COMPACT;" localhost-billing >/dev/null 2>&1
cd ../
echo
echo "[playBilling] shutdown database server"
sleep 1

kill `cat ./tmp/pid1` >/dev/null 2>&1

cd database
$JAVABIN -cp hsqldb.jar org.hsqldb.Server -database.0 billing -dbname.0 billing >/dev/null 2>&1 &
PID1=$!
echo $PID1 > ../tmp/pid1
cd ../
echo
echo "[playBilling] database started"
sleep 1

echo
echo "[playBilling] rebuild firewall rules"
./web/WEB-INF/shell-scripts/rebuild-firewall.sh >/dev/null 2>&1
sleep 1

kill `cat ./tmp/pid2` >/dev/null 2>&1

CWD=`pwd`
WEBINFLIB=`ls ./web/WEB-INF/lib/*.jar`
JETTYLIB=`ls ./jetty/*.jar`

export CLASSPATH=$JAVA_HOME

for THELIB in $WEBINFLIB
do
	THELIB=`echo $THELIB | sed "s/\.\//\//g"`
	CLASSPATH=$CLASSPATH:$CWD$THELIB
done

for THELIB in $JETTYLIB
do
	THELIB=`echo $THELIB | sed "s/\.\//\//g"`
	CLASSPATH=$CLASSPATH:$CWD$THELIB
done

nohup $JAVABIN -cp $CLASSPATH org.mortbay.jetty.Server jetty/jetty-config.xml >/dev/null 2>&1 &
PID2=$!
echo $PID2 > ./tmp/pid2

echo
echo "[playBilling] application ready (PID: $PID2)"
echo
sleep 1

