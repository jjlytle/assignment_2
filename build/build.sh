#!/bin/zsh

# copy the netBeans projects so i can push them to get hub
yes | cp -R ~/Documents/School/TCSS-558/Assignment_2_558_v1/Client \
~/Desktop/a1_Dockerfiles/assignment_2/netBeans

yes | cp -R ~/Documents/School/TCSS-558/Assignment_2_558_v1/Server \
~/Desktop/a1_Dockerfiles/assignment_2/netBeans

# copy the jars to where i want to use them
yes | cp ~/Documents/School/TCSS-558/Assignment_2_558_v1/Client/GenericNode/dist/GenericNode.jar \
~/Desktop/a1_Dockerfiles/assignment_2/docker_client

yes | cp ~/Documents/School/TCSS-558/Assignment_2_558_v1/Server/GenericNode/dist/GenericNode.jar \
~/Desktop/a1_Dockerfiles/assignment_2/docker_server

cd ~/Desktop/a1_Dockerfiles/assignment_2/docker_client
docker build -t 558_client .

cd ~/Desktop/a1_Dockerfiles/assignment_2/docker_server
docker build -t 558_server .

cd ~/Desktop/a1_Dockerfiles/assignment_2/build
docker run -d --rm -v $(pwd):/testCode --name myClient 558_client
sleep 1
docker run -d --rm -v $(pwd):/testCode --name myServer 558_server
docker container ls

docker exec -it myClient bash
