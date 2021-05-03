#!/bin/bash
set -xo pipefail

docker login cryptobotregistrybsn.azurecr.io --username $2 --password $3

docker rm $(docker stop $(docker ps -a -q --filter ancestor=signal-catcher:latest --format="{{.ID}}"))
docker rmi $(docker images |grep 'signal-catcher')

docker pull cryptobotregistrybsn.azurecr.io/signal-catcher:$1
docker tag cryptobotregistrybsn.azurecr.io/signal-catcher:$1 signal-catcher:latest

docker run -d --network="host"  -e environment=$4 -e AWS_ACCESS_KEY_ID=$5 -e AWS_SECRET_ACCESS_KEY=$6 -e AWS_DEFAULT_REGION=$7 -e signalqueue=$8 --name="signal-catcher" signal-catcher:latest