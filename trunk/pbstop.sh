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
kill `cat ./tmp/pid2` >/dev/null 2>&1

PID2=`cat ./tmp/pid2`

echo
echo "[playBilling] application shutdown (PID: $PID2)"
echo
sleep 1

