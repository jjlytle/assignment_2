#!/bin/bash
# TCSS558 Generic Client container - runs container continually 
# Exits container when /stop file is created by external process
echo "myClient container up...  "
while :
do
  if [ -f "/stop" ]
  then
    exit
  fi
  sleep 1
done

