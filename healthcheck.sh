#!/bin/bash

# Check if analyzer is running

if [[ $(curl http://localhost:8090/api/v1/healthz) == 'OK' ]]; then
  echo "0"
else
  echo "Stopped exiting bot"
  kill -s SIGTERM 1
  kill -s SIGKILL 1
fi