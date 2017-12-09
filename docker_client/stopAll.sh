#!/bin/bash

java -jar GenericNode.jar tc 172.17.0.3 1234 exit
java -jar GenericNode.jar tc 172.17.0.4 1234 exit
java -jar GenericNode.jar tc 172.17.0.5 1234 exit

touch stop
