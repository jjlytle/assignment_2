#!/bin/zsh

docker container inspect --format '{{ .NetworkSettings.IPAddress}}' myServer | sed 's/$/:1234/' > Nodes
docker container inspect --format '{{ .NetworkSettings.IPAddress}}' myClient | sed 's/$/:1234/' >> Nodes 
