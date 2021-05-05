#!/bin/bash

# Check if analyzer is running
if ps -ef | grep "analyzer" > /dev/null
then
    echo "0"
else
    echo "Stopped exiting bot"
    kill -s SIGTERM 1
    kill -s SIGKILL 1
fi
