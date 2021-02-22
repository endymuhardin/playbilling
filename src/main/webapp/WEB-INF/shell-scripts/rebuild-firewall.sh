#!/bin/sh

# run at start

. network.init

# custom rule for playBilling
iptables -D FORWARD -s $CLIENT_NETWORK -d ! $CLIENT_NETWORK -j DROP >/dev/null 2>&1
iptables -I FORWARD -s $CLIENT_NETWORK -d ! $CLIENT_NETWORK -j DROP >/dev/null 2>&1
iptables -t nat -D PREROUTING -s $CLIENT_NETWORK -d ! $CLIENT_NETWORK -p tcp --dport 80 -j DNAT --to-destination $LOGIN_ADDRESS >/dev/null 2>&1
iptables -t nat -I PREROUTING -s $CLIENT_NETWORK -d ! $CLIENT_NETWORK -p tcp --dport 80 -j DNAT --to-destination $LOGIN_ADDRESS >/dev/null 2>&1

# get online clients
cd database
ONLINE=`java -jar hsqldb.jar --noinput --sql "SELECT DISTINCT ip_address FROM billing_session WHERE session_status='open';" localhost-billing`
cd ../

for CLIENT in $ONLINE
do
    CHECK=`echo $CLIENT|grep $CLIENT_NETID`
    if [ ! -z $CHECK ]; then
	iptables -D FORWARD -s $CLIENT -d ! $CLIENT -j ACCEPT >/dev/null 2>&1
	iptables -I FORWARD -s $CLIENT -d ! $CLIENT -j ACCEPT >/dev/null 2>&1
	iptables -t nat -D PREROUTING -s $CLIENT -d ! $CLIENT -p tcp -j ACCEPT >/dev/null 2>&1
	iptables -t nat -I PREROUTING -s $CLIENT -d ! $CLIENT -p tcp -j ACCEPT >/dev/null 2>&1
    fi
done
