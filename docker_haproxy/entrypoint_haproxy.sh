#!/bin/bash
# haproxy daemon - runs container continually until haproxy exits
service haproxy start
echo "haproxy daemon up..."
sleep 3
while :
do
 haproxystatus=`ps aux | grep haproxy-systemd | grep cfg`
 if [ -z "$haproxystatus" ]
 then
 #exit
 echo "haproxy down"
 fi
 sleep 10
done
