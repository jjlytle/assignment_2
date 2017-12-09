#!/bin/zsh

docker container inspect --format '{{ .NetworkSettings.IPAddress}}' myServer1 | sed 's/$/:1234/' > Nodes
docker container inspect --format '{{ .NetworkSettings.IPAddress}}' myServer2 | sed 's/$/:1234/' >> Nodes
docker container inspect --format '{{ .NetworkSettings.IPAddress}}' myServer3 | sed 's/$/:1234/' >> Nodes
# docker container inspect --format '{{ .NetworkSettings.IPAddress}}' myClient | sed 's/$/:1234/' >> Nodes 
